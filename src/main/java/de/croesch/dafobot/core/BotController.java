package de.croesch.dafobot.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.work.api.ChangeVerifierIF;
import de.croesch.dafobot.work.api.EditorIF;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageEditabilityCheckerIF;
import de.croesch.dafobot.work.api.PageNeedsQAException;
import de.croesch.dafobot.work.api.PagePoolIF;
import de.croesch.dafobot.work.api.VerificationResult;

/**
 * The assembled <em>logic</em> of the bot. It receives the main parts of the bot and manages the interaction and order
 * of execution.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class BotController {
  private static final Logger LOG = LoggerFactory.getLogger(BotController.class);

  private final MediaWikiBot bot;

  private final PagePoolIF pages;

  private final PageEditabilityCheckerIF editabilityChecker;

  private final EditorIF editor;

  private final ChangeVerifierIF verifier;

  private final Connection connection;

  public BotController(final MediaWikiBot bot,
                       final Connection connection,
                       final PagePoolIF pages,
                       final PageEditabilityCheckerIF editabilityChecker,
                       final EditorIF editor,
                       final ChangeVerifierIF verifier) {
    this.bot = bot;
    this.connection = connection;
    this.pages = pages;
    this.editabilityChecker = editabilityChecker;
    this.editor = editor;
    this.verifier = verifier;
  }

  public void run() {
    LOG.info("beginning..");
    boolean canContinue = true;
    while (canContinue && this.pages.hasNext()) {
      canContinue = editNext();
    }
  }

  private boolean editNext() {
    final String next = this.pages.next();
    LOG.debug("next: " + next);

    final SimpleArticle article = this.bot.readData(next);
    final SimpleArticle oldArticle = new SimpleArticle(article);
    insertArticle(article.getTitle(), false);
    if (this.editabilityChecker.canEdit(article)) {
      try {
        LOG.debug("can edit.");
        this.editor.edit(article, this.connection);
        LOG.debug("edited.");
        final VerificationResult result = this.verifier.verify(oldArticle, article);
        LOG.debug("verified: " + result);
        switch (result) {
          case GOOD:
            this.bot.writeContent(article);
            insertArticle(article.getTitle(), true);
            break;
          case FATAL:
            LOG.error("FATAL error during editing " + next);
            return false;
          case BAD:
            LOG.info("bad edit for " + next);
            break;
        }
      } catch (final NoEditNeededException e) {
        LOG.info("no edit needed for '" + article.getTitle() + "'");
        insertProblematicArticle(article.getTitle(), null, false);
      } catch (final PageNeedsQAException e) {
        LOG.warn("QA needed for '" + article.getTitle() + "' (" + e.getMessage() + ")");
        insertProblematicArticle(article.getTitle(), e.getMessage(), true);
      }
    } else {
      LOG.warn("Cannot edit " + next);
    }
    return true;
  }

  private void insertProblematicArticle(final String title, final String reason, final boolean corrupt) {
    final String statementString = "INSERT INTO `no_edit_pages` SET page=?, reason=?, corrupt=?";

    try (final PreparedStatement statement = this.connection.prepareStatement(statementString);) {
      statement.setString(1, title);
      statement.setString(2, reason);
      statement.setBoolean(3, corrupt);
      statement.executeUpdate();
    } catch (final SQLException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
  }

  private void insertArticle(final String title, final boolean edit) {
    String statementString;
    if (edit) {
      statementString = "INSERT INTO `pages` SET page=?,lastedit=now() ON DUPLICATE KEY UPDATE lastedit=now()";
    } else {
      statementString = "INSERT INTO `pages` SET page=? ON DUPLICATE KEY UPDATE lastchecked=now()";
    }

    try (final PreparedStatement statement = this.connection.prepareStatement(statementString);) {
      statement.setString(1, title);
      statement.executeUpdate();
    } catch (final SQLException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
  }
}

package de.croesch.dafobot.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

  private final String maintenanceArticleName;

  public BotController(final MediaWikiBot bot,
                       final Connection connection,
                       final PagePoolIF pages,
                       final PageEditabilityCheckerIF editabilityChecker,
                       final EditorIF editor,
                       final ChangeVerifierIF verifier) {
    this(bot, connection, pages, editabilityChecker, editor, verifier, "");
  }

  public BotController(final MediaWikiBot bot,
                       final Connection connection,
                       final PagePoolIF pages,
                       final PageEditabilityCheckerIF editabilityChecker,
                       final EditorIF editor,
                       final ChangeVerifierIF verifier,
                       final String maintenance) {
    this.bot = bot;
    this.connection = connection;
    this.pages = pages;
    this.editabilityChecker = editabilityChecker;
    this.editor = editor;
    this.verifier = verifier;
    this.maintenanceArticleName = maintenance;
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
      insertProblematicArticle(article.getTitle(), "in use", false);
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

    if (countCorruptStatements() > 10) {
      writeCorruptStatementsToSpecialArticle();
    }
  }

  private void writeCorruptStatementsToSpecialArticle() {
    if (this.maintenanceArticleName == null || this.maintenanceArticleName.isEmpty()) {
      return;
    }
    final String statementString = "SELECT page, reason, at FROM `no_edit_pages` WHERE corrupt=true";
    final List<String[]> articles = new ArrayList<String[]>();
    try (Statement statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                               ResultSet.CONCUR_UPDATABLE);) {
      final ResultSet result = statement.executeQuery(statementString);
      while (result.next()) {
        final String[] article = new String[3];
        article[0] = result.getString(1);
        article[1] = result.getString(2);
        article[2] = result.getTimestamp(3).toString();
        articles.add(article);
        result.deleteRow();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
    final SimpleArticle maintenanceArticle = this.bot.readData(this.maintenanceArticleName);
    String newRows = "";
    for (final String[] article : articles) {
      newRows += "|-\n";
      newRows += "| [[" + article[0] + "]] || <nowiki>" + article[1] + "</nowiki> || " + article[2] + "\n";
    }
    final String marker = "<!-- dafobot -->";
    maintenanceArticle.setText(maintenanceArticle.getText().replaceFirst(marker + "\n?", newRows + marker + "\n"));
    maintenanceArticle.setEditSummary("Update");
    this.bot.writeContent(maintenanceArticle);
  }

  private int countCorruptStatements() {
    final String statementString = "SELECT COUNT(*) FROM `no_edit_pages` WHERE corrupt=true";

    try (final PreparedStatement statement = this.connection.prepareStatement(statementString);) {
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new IllegalStateException();
      }
      return resultSet.getInt(1);
    } catch (final SQLException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
    return 0;
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

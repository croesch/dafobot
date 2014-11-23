package de.croesch.dafobot.core;

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

  public BotController(final MediaWikiBot bot,
                       final PagePoolIF pages,
                       final PageEditabilityCheckerIF editabilityChecker,
                       final EditorIF editor,
                       final ChangeVerifierIF verifier) {
    this.bot = bot;
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
    if (this.editabilityChecker.canEdit(article)) {
      try {
        LOG.debug("can edit.");
        this.editor.edit(article);
        LOG.debug("edited.");
        final VerificationResult result = this.verifier.verify(oldArticle, article);
        LOG.debug("verified: " + result);
        switch (result) {
          case GOOD:
            this.bot.writeContent(article);
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
      } catch (final PageNeedsQAException e) {
        LOG.warn("QA needed for '" + article.getTitle() + "' (" + e.getMessage() + ")");
      }
    } else {
      LOG.warn("Cannot edit " + next);
    }
    return true;
  }
}

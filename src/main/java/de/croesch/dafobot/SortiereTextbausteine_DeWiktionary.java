package de.croesch.dafobot;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.core.BotController;
import de.croesch.dafobot.jwbf.mediawiki.bots.WiktionaryDeBot;
import de.croesch.dafobot.work.VisualManualVerifier;
import de.croesch.dafobot.work.api.ChangeVerifierIF;
import de.croesch.dafobot.work.api.EditorIF;
import de.croesch.dafobot.work.api.PageEditabilityCheckerIF;
import de.croesch.dafobot.work.api.PagePoolIF;
import de.croesch.dafobot.work.sort_text_components.Editor;
import de.croesch.dafobot.work.sort_text_components.PageEditabilityChecker;
import de.croesch.dafobot.work.sort_text_components.PagePool;

/**
 * Main class for sorting of the text components.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class SortiereTextbausteine_DeWiktionary {
  private static final Logger LOG = LoggerFactory.getLogger(SortiereTextbausteine_DeWiktionary.class);

  public static void main(final String[] args) throws IOException {
    final Properties configuration = new Properties();
    configuration.load(MediaWikiBot.class.getResourceAsStream("/bot.conf"));

    try (final Connection connection = DriverManager.getConnection(configuration.getProperty("db.url"),
                                                                   configuration.getProperty("db.user"),
                                                                   configuration.getProperty("db.password"));) {
      LOG.debug("starting.");
      final MediaWikiBot bot = new WiktionaryDeBot(configuration);
      final PagePoolIF pages = new PagePool(bot, connection);
      final PageEditabilityCheckerIF editabilityChecker = new PageEditabilityChecker();
      final EditorIF editor = new Editor();
      final ChangeVerifierIF verifier = new VisualManualVerifier();

      final BotController controller = new BotController(bot, connection, pages, editabilityChecker, editor, verifier);
      controller.run();
    } catch (final SQLException e) {
      LOG.error(e.getMessage());
    }

    LOG.info("finished.");
  }
}

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
import de.croesch.dafobot.jwbf.mediawiki.bots.WikipediaDeBot;
import de.croesch.dafobot.work.AutomaticVerifier;
import de.croesch.dafobot.work.GeneralPageEditabilityChecker;
import de.croesch.dafobot.work.api.ChangeVerifierIF;
import de.croesch.dafobot.work.api.EditorIF;
import de.croesch.dafobot.work.api.PageEditabilityCheckerIF;
import de.croesch.dafobot.work.api.PagePoolIF;
import de.croesch.dafobot.work.sk_ort_code.Editor;
import de.croesch.dafobot.work.sk_ort_code.PagePool;

/**
 * Main class for updating buergermeister in slowakei (2014).
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class SlowakeiCode_DeWikipedia {
  private static final Logger LOG = LoggerFactory.getLogger(SlowakeiCode_DeWikipedia.class);

  public static void main(final String[] args) throws IOException {
    final Properties configuration = new Properties();
    configuration.load(MediaWikiBot.class.getResourceAsStream("/bot.conf"));

    try (final Connection connection = DriverManager.getConnection(configuration.getProperty("db.url.sk_ort_code"),
                                                                   configuration.getProperty("db.user"),
                                                                   configuration.getProperty("db.password"));) {
      LOG.debug("starting.");
      final MediaWikiBot bot = new WikipediaDeBot(configuration);
      final PagePoolIF pages = new PagePool(bot);
      final PageEditabilityCheckerIF editabilityChecker = new GeneralPageEditabilityChecker();
      final EditorIF editor = new Editor();
      final ChangeVerifierIF verifier = new AutomaticVerifier();

      final BotController controller = new BotController(bot, connection, pages, editabilityChecker, editor, verifier);
      controller.run();
    } catch (final SQLException e) {
      LOG.error(e.getMessage());
    }

    LOG.info("finished.");
  }
}

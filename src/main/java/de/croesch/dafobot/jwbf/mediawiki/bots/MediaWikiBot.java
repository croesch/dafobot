package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.util.Properties;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter for {@link net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot}.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class MediaWikiBot extends net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot {
  private static final Logger LOG = LoggerFactory.getLogger(MediaWikiBot.class);

  private long lastWrite = -1;

  /**
   * Creates the bot and automatically logs it in with the credentials found in <code>bot.conf</code>.
   *
   * @since Date: Nov 19, 2014
   * @param prop the configuration for the bot
   * @param site the site where the bot should take action
   */
  public MediaWikiBot(final Properties prop, final String site) {
    super(site);

    if (!isLoggedIn()) {
      login(prop.getProperty("user.name"), prop.getProperty("user.password"));
    }
  }

  @Override
  public final synchronized void writeContent(final SimpleArticle simpleArticle) {
    final long sleepTime = this.lastWrite + 12000 - System.currentTimeMillis();
    if (sleepTime > 0) {
      LOG.info("Sleeping for " + sleepTime + " ms");
      try {
        Thread.sleep(sleepTime);
      } catch (final InterruptedException e) {
        LOG.warn(e.getMessage());
      }
    }
    this.lastWrite = System.currentTimeMillis();
    super.writeContent(simpleArticle);
  }
}

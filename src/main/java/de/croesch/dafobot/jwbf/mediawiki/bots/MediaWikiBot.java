package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.io.IOException;
import java.util.Properties;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * Adapter for {@link net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot}.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class MediaWikiBot extends net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot {
  /**
   * Creates the bot and automatically logs it in with the credentials found in <code>bot.conf</code>.
   *
   * @since Date: Nov 19, 2014
   * @param site the site where the bot should take action
   * @throws IOException if log in fails
   */
  public MediaWikiBot(final String site) throws IOException {
    super(site);
    final Properties prop = new Properties();
    prop.load(MediaWikiBot.class.getResourceAsStream("/bot.conf"));

    if (!isLoggedIn()) {
      login(prop.getProperty("user.name"), prop.getProperty("user.password"));
    }
  }

  @Override
  public final synchronized void writeContent(final SimpleArticle simpleArticle) {
    super.writeContent(simpleArticle);
  }
}

package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.io.IOException;
import java.util.Properties;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class MediaWikiBot extends net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot {
  public MediaWikiBot(final String site) throws IOException {
    super(site);
    final Properties prop = new Properties();
    prop.load(MediaWikiBot.class.getResourceAsStream("/bot.config"));

    if (!isLoggedIn()) {
      login(prop.getProperty("user.name"), prop.getProperty("user.password"));
    }
  }

  @Override
  public final synchronized void writeContent(final SimpleArticle simpleArticle) {
    // ignore
  }
}

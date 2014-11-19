package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.io.IOException;

/**
 * Bot for German wikipedia.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class WikipediaDeBot extends MediaWikiBot {
  /**
   * Creates a dewiki-bot and automatically logs it in with the credentials found in <code>bot.conf</code>.
   *
   * @since Date: Nov 19, 2014
   * @throws IOException if log in fails
   */
  public WikipediaDeBot() throws IOException {
    super("https://de.wikipedia.org/w/");
  }
}

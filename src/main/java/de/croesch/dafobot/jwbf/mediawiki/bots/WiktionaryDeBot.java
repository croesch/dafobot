package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.io.IOException;

/**
 * Bot for German wiktionary.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class WiktionaryDeBot extends MediaWikiBot {
  /**
   * Creates a dewikt-bot and automatically logs it in with the credentials found in <code>bot.conf</code>.
   *
   * @since Date: Nov 19, 2014
   * @throws IOException if log in fails
   */
  public WiktionaryDeBot() throws IOException {
    super("https://de.wiktionary.org/w/");
  }
}

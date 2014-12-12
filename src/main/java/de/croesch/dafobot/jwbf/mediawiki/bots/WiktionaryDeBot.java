package de.croesch.dafobot.jwbf.mediawiki.bots;

import java.util.Properties;

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
   * @param props the configuration of the bot
   */
  public WiktionaryDeBot(final Properties props) {
    super(props, "https://de.wiktionary.org/w/");
  }
}

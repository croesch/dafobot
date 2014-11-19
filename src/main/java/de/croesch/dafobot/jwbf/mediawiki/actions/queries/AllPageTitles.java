package de.croesch.dafobot.jwbf.mediawiki.actions.queries;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class AllPageTitles extends net.sourceforge.jwbf.mediawiki.actions.queries.AllPageTitles {

  public AllPageTitles(final MediaWikiBot bot, final String nextPage, final int ... namespaces) {
    super(bot, namespaces);
    setNextPageInfo(nextPage);
  }

  public AllPageTitles(final MediaWikiBot bot, final int ... namespaces) {
    super(bot, namespaces);
  }
}

package de.croesch.dafobot.work.sort_text_components;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import de.croesch.dafobot.jwbf.mediawiki.actions.queries.AllPageTitles;
import de.croesch.dafobot.work.api.PagePoolIF;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class PagePool extends AllPageTitles implements PagePoolIF {
  public PagePool(final MediaWikiBot bot) {
    super(bot, 0);
  }
}

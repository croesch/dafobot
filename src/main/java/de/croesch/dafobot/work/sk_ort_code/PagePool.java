package de.croesch.dafobot.work.sk_ort_code;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import de.croesch.dafobot.jwbf.mediawiki.actions.queries.TemplateUserTitles;
import de.croesch.dafobot.work.api.PagePoolIF;

/**
 * All pages with 'Infobox Ort in der Slowakei'.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class PagePool extends TemplateUserTitles implements PagePoolIF {
  public PagePool(final MediaWikiBot bot) {
    super(bot, "Vorlage:Infobox Ort in der Slowakei", 0);
  }
}

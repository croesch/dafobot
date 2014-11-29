package de.croesch.dafobot.work.buergermeister_slowakei_2014;

import java.sql.Connection;

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
  public PagePool(final MediaWikiBot bot, final Connection connection) {
    super(bot, "Vorlage:Infobox Ort in der Slowakei", 0);
  }
}

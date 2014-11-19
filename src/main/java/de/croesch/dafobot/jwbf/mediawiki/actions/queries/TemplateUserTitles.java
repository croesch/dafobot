package de.croesch.dafobot.jwbf.mediawiki.actions.queries;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class TemplateUserTitles extends net.sourceforge.jwbf.mediawiki.actions.queries.TemplateUserTitles {

  public TemplateUserTitles(final MediaWikiBot bot,
                            final String templateName,
                            final String nextPage,
                            final int ... namespaces) {
    super(bot, templateName, namespaces);
    setNextPageInfo(nextPage);
  }

  public TemplateUserTitles(final MediaWikiBot bot, final String templateName, final int ... namespaces) {
    super(bot, templateName, namespaces);
  }
}

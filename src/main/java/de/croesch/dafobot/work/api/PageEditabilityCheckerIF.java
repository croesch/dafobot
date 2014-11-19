package de.croesch.dafobot.work.api;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public interface PageEditabilityCheckerIF {

  boolean canEdit(SimpleArticle article);

}

package de.croesch.dafobot.work.api;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * Checker for the editability of a page.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public interface PageEditabilityCheckerIF {

  /**
   * Checks whether the user currently can edit the given article.
   *
   * @since Date: Nov 19, 2014
   * @param article the article to check if it can be edited.
   * @return <code>true</code> if the user has the right and the ability to edit the article.
   */
  boolean canEdit(SimpleArticle article);
}

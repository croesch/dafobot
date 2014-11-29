package de.croesch.dafobot.work.api;

import java.sql.Connection;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * The actual brain of the bot. This is where the action happens. It edits articles.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public interface EditorIF {

  void edit(SimpleArticle article, Connection connection) throws NoEditNeededException, PageNeedsQAException;
}

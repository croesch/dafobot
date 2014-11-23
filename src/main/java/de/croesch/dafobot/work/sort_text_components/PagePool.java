package de.croesch.dafobot.work.sort_text_components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.jwbf.mediawiki.actions.queries.AllPageTitles;
import de.croesch.dafobot.work.api.PagePoolIF;

/**
 * All pages in the default namespace.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class PagePool extends AllPageTitles implements PagePoolIF {
  private static final Logger LOG = LoggerFactory.getLogger(PagePool.class);

  public PagePool(final MediaWikiBot bot, final Connection connection) {
    super(bot, 0);

    try (final PreparedStatement statement = connection.prepareStatement("SELECT page FROM `pages` ORDER BY lastchecked desc limit 1");) {
      final ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        setNextPageInfo(rs.getString(1));
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
  }
}

package de.croesch.dafobot.work;

import java.util.regex.Pattern;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.work.api.PageEditabilityCheckerIF;

/**
 * Checks editability rules that should apply for every page this bot edits.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class GeneralPageEditabilityChecker implements PageEditabilityCheckerIF {
  public static final Pattern INUSE = Pattern.compile("\\{\\{Inuse(\\}\\}|\\|)");

  @Override
  public final boolean canEdit(final SimpleArticle article) {
    return !INUSE.matcher(article.getText()).find();
  }
}

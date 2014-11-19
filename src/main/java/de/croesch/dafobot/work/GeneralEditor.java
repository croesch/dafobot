package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.work.api.EditorIF;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public abstract class GeneralEditor implements EditorIF {

  @Override
  public final void edit(final SimpleArticle article) {
    article.setMinorEdit(false);
    article.setEditSummary("Bot: " + getEditSummary());
    doSpecialEdit(article);
  }

  protected abstract String getEditSummary();

  protected abstract void doSpecialEdit(final SimpleArticle article);
}

package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.work.api.EditorIF;

/**
 * Performs things that should be done in every single edit made by the bot.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public abstract class GeneralEditor implements EditorIF {

  @Override
  public final void edit(final SimpleArticle article) {
    article.setMinorEdit(false);
    article.setEditSummary("Bot: " + getEditSummary());
    article.setText(doSpecialEdit(article.getTitle(), article.getText()));
  }

  protected abstract String getEditSummary();

  protected abstract String doSpecialEdit(String title, String text);
}

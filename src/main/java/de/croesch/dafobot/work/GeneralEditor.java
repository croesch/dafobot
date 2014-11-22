package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.EditorIF;
import de.croesch.dafobot.work.api.NoEditNeededException;

/**
 * Performs things that should be done in every single edit made by the bot.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public abstract class GeneralEditor implements EditorIF {

  @Override
  public final void edit(final SimpleArticle article) throws NoEditNeededException {
    article.setMinorEdit(false);
    article.setEditSummary("Bot: " + getEditSummary());
    final Text text = new Text(article.getText());
    article.setText(doSpecialEdit(article.getTitle(), text).toPlainString());
  }

  protected abstract String getEditSummary();

  protected abstract Text doSpecialEdit(String title, Text text) throws NoEditNeededException;
}

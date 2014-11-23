package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.core.EditedText;
import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.EditorIF;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Performs things that should be done in every single edit made by the bot.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public abstract class GeneralEditor implements EditorIF {

  @Override
  public final void edit(final SimpleArticle article) throws NoEditNeededException, PageNeedsQAException {
    article.setMinorEdit(false);
    article.setEditSummary("Bot: " + getEditSummary());
    final Text text = new Text(article.getText());
    final EditedText editedText = doSpecialEdit(article.getTitle(), text);
    if (editedText.getCommentAppendum() != null && !editedText.getCommentAppendum().isEmpty()) {
      article.setEditSummary(article.getEditSummary() + editedText.getCommentAppendum());
    }
    article.setText(editedText.getText().toPlainString());
  }

  protected abstract String getEditSummary();

  protected abstract EditedText doSpecialEdit(String title, Text text) throws NoEditNeededException,
                                                                      PageNeedsQAException;
}

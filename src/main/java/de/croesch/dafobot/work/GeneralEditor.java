package de.croesch.dafobot.work;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
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
  public final void edit(final SimpleArticle article, final Connection connection) throws NoEditNeededException,
                                                                                  PageNeedsQAException {
    article.setMinorEdit(false);
    article.setEditSummary("Bot: " + getEditSummary());

    final Text text = new Text(article.getText());
    final Collection<String> additionalActions = new HashSet<>();
    article.setText(doSpecialEdit(article.getTitle(), text, connection, additionalActions).toPlainString());

    for (final String additionalAction : additionalActions) {
      article.setEditSummary(article.getEditSummary() + ", " + additionalAction);
    }
  }

  protected abstract String getEditSummary();

  protected abstract Text doSpecialEdit(String title,
                                        Text text,
                                        Connection connection,
                                        Collection<String> additionalActions) throws NoEditNeededException,
                                                                             PageNeedsQAException;
}

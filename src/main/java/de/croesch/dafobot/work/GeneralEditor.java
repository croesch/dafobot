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

  private final boolean minor;

  public GeneralEditor() {
    this(false);
  }

  public GeneralEditor(final boolean minorEdits) {
    this.minor = minorEdits;
  }

  @Override
  public final void edit(final SimpleArticle article, final Connection connection) throws NoEditNeededException,
                                                                                  PageNeedsQAException {
    article.setMinorEdit(this.minor);
    article.setEditSummary("Bot: " + getEditSummary());

    final Text text = new Text(article.getText());
    final Collection<String> additionalActions = new HashSet<>();
    article.setText(doSpecialEdit(article.getTitle(), text, connection, additionalActions).toPlainString());

    article.setEditSummary(article.getEditSummary() + getEditSummaryAppendum(additionalActions));
  }

  protected String getEditSummaryAppendum(final Collection<String> additionalActions) {
    final StringBuilder sb = new StringBuilder();
    for (final String additionalAction : additionalActions) {
      sb.append(", ").append(additionalAction);
    }
    return sb.toString();
  }

  protected abstract String getEditSummary();

  protected abstract Text doSpecialEdit(String title,
                                        Text text,
                                        Connection connection,
                                        Collection<String> additionalActions) throws NoEditNeededException,
                                                                             PageNeedsQAException;
}

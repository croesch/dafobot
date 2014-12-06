package de.croesch.dafobot.work;

import java.sql.Connection;
import java.util.Collection;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Editor for the wiktionary.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public abstract class WiktionaryEditor extends GeneralEditor {

  @Override
  public final Text doSpecialEdit(final String title,
                                  final Text text,
                                  final Connection connection,
                                  final Collection<String> additionalActions) throws NoEditNeededException,
                                                                             PageNeedsQAException {
    Text subResult = edit(title, text, connection, additionalActions);
    subResult = new EmptyTemplateRemover().edit(subResult, additionalActions);
    subResult = new DuplicateEmptyLineRemover().edit(subResult, additionalActions);
    return subResult;
  }

  protected abstract Text edit(String title, Text text, Connection connection, Collection<String> additionalActions)
                                                                                                                    throws NoEditNeededException,
                                                                                                                    PageNeedsQAException;
}

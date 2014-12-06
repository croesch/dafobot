package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.Collection;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Simple interface for a sorter.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public interface Sorter {
  public Text sort(final Text text, Collection<String> additionalActions) throws PageNeedsQAException,
                                                                         NoEditNeededException;
}

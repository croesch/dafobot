package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.core.TextBuilder;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Default sorter - sorts something that occurs multiple times in a {@link Text}. There might be a prefix and a suffix.
 * It splits the text, in occurrences, sorts them and performs sub sorting on each occurrence (except prefix and
 * suffix).
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
abstract class DefaultSorter extends AbstractSorter {

  private final Sorter subSorter;

  public DefaultSorter(final Sorter sorter) {
    this.subSorter = sorter;
  }

  @Override
  public final Text sort(final Text text, final Collection<String> additionalActions) throws NoEditNeededException,
                                                                                     PageNeedsQAException {
    final TextBuilder tb = new TextBuilder();
    final Matcher matcher = pattern().matcher(text.toString());

    int lastStart = 0;
    boolean found;
    boolean editNeeded = false;
    do {
      found = matcher.find();
      Text partText = text.substring(lastStart, found ? matcher.start() : text.length());
      if (found) {
        lastStart = matcher.start();
      }
      try {
        partText = edit(partText, additionalActions);
        editNeeded = true;
      } catch (final NoEditNeededException e) {
        // ignore
      }
      tb.append(partText);
    } while (found);

    if (!editNeeded) {
      throw new NoEditNeededException();
    }

    return tb.toText();
  }

  private List<Text> split(final Text text) {
    return null;
  }

  private List<Text> sort(final List<Text> texts) {
    return texts;
  }

  private Text edit(final Text text, final Collection<String> additionalActions) throws PageNeedsQAException,
                                                                                NoEditNeededException {
    return this.subSorter.sort(text, additionalActions);
  }

  protected abstract Pattern pattern();
}

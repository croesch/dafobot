package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.core.TextBuilder;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;
import de.croesch.dafobot.work.sort_text_components.Occurrence;
import de.croesch.dafobot.work.sort_text_components.Range;

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

    final int beginOfSuffix = findSuffix(text);
    final Text textWithoutSuffix = text.substring(0, beginOfSuffix);

    final Matcher matcher = pattern().matcher(textWithoutSuffix.toString());

    final List<Occurrence> occurrences = findOccurrences(matcher);
    fillRange(occurrences);

    List<Text> result = split(textWithoutSuffix, occurrences);
    result = sort(result);

    appendPrefix(tb, textWithoutSuffix, occurrences);
    appendBody(additionalActions, tb, result);
    appendSuffix(tb, text, beginOfSuffix);

    return tb.toText();
  }

  protected int findSuffix(final Text text) {
    return text.length();
  }

  private void appendPrefix(final TextBuilder tb, final Text text, final List<Occurrence> occurrences) {
    if (occurrences.isEmpty()) {
      tb.append(text);
    } else {
      tb.append(text.substring(0, occurrences.get(0).where().getFrom()));
    }
  }

  private void appendSuffix(final TextBuilder tb, final Text text, final int beginOfSuffix) {
    tb.append(text.substring(beginOfSuffix));
  }

  private void appendBody(final Collection<String> additionalActions, final TextBuilder tb, final List<Text> result)
                                                                                                                    throws PageNeedsQAException,
                                                                                                                    NoEditNeededException {
    boolean editNeeded = false;
    for (Text t : result) {
      try {
        t = edit(t, additionalActions);
        editNeeded = true;
      } catch (final NoEditNeededException e) {
        // ignore
      }
      tb.append(t);
    }

    if (!editNeeded) {
      throw new NoEditNeededException();
    }
  }

  private List<Occurrence> findOccurrences(final Matcher matcher) {
    final List<Occurrence> occurrences = new ArrayList<>();

    while (matcher.find()) {
      occurrences.add(new Occurrence(new Range(matcher.start())));
    }
    return occurrences;
  }

  private List<Text> split(final Text text, final List<Occurrence> occurrences) {
    final List<Text> texts = new ArrayList<>();
    for (final Occurrence occurrence : occurrences) {
      if (occurrence.where().getTo() < 0) {
        texts.add(text.substring(occurrence.where().getFrom()));
      } else {
        texts.add(text.substring(occurrence.where().getFrom(), occurrence.where().getTo()));
      }
    }
    return texts;
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

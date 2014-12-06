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

    final List<Text> result = split(textWithoutSuffix, occurrences);
    final List<Text> sortedResult = sort(result);

    boolean editNeeded = areDifferent(result, sortedResult);

    appendPrefix(tb, textWithoutSuffix, occurrences);
    editNeeded |= appendBody(additionalActions, tb, sortedResult);
    appendSuffix(tb, text, beginOfSuffix);

    if (!editNeeded) {
      throw new NoEditNeededException();
    }

    return tb.toText();
  }

  private boolean areDifferent(final List<Text> result, final List<Text> sortedResult) {
    return !result.equals(sortedResult);
  }

  protected abstract int findSuffix(final Text text);

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

  private boolean appendBody(final Collection<String> additionalActions, final TextBuilder tb, final List<Text> result)
                                                                                                                       throws PageNeedsQAException,
                                                                                                                       NoEditNeededException {
    boolean editNeeded = false;
    for (int i = 0; i < result.size(); ++i) {
      Text t = result.get(i);
      try {
        t = edit(t, additionalActions);
        editNeeded = true;
      } catch (final NoEditNeededException e) {
        // ignore
      }
      tb.append(t);
      // each result is trimmed so append 'divider' (except for last one)
      if (i + 1 < result.size()) {
        tb.append(new Text("\n\n"));
      }
    }

    return editNeeded;
  }

  private List<Occurrence> findOccurrences(final Matcher matcher) {
    final List<Occurrence> occurrences = new ArrayList<>();

    while (matcher.find()) {
      int offset = 0;
      // don't let new line be part of the occurrence
      if (matcher.group().startsWith("\n")) {
        ++offset;
      }
      occurrences.add(new Occurrence(new Range(matcher.start() + offset)));
    }
    return occurrences;
  }

  private List<Text> split(final Text text, final List<Occurrence> occurrences) {
    final List<Text> texts = new ArrayList<>();
    for (final Occurrence occurrence : occurrences) {
      Text toAdd;
      if (occurrence.where().getTo() < 0) {
        toAdd = text.substring(occurrence.where().getFrom());
      } else {
        toAdd = text.substring(occurrence.where().getFrom(), occurrence.where().getTo());
      }
      // add it trimmed to simplify sorting
      texts.add(new Text(toAdd.toPlainString().trim()));
    }
    return texts;
  }

  protected abstract List<Text> sort(final List<Text> texts) throws PageNeedsQAException;

  private Text edit(final Text text, final Collection<String> additionalActions) throws PageNeedsQAException,
                                                                                NoEditNeededException {
    return this.subSorter.sort(text, additionalActions);
  }

  protected abstract Pattern pattern();
}

package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.croesch.dafobot.work.sort_text_components.Occurrence;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
abstract class AbstractSorter implements Sorter {

  protected void fillRange(final List<Occurrence> occurrences) {
    final int[] starts = new int[occurrences.size()];
    for (int i = 0; i < occurrences.size(); ++i) {
      starts[i] = occurrences.get(i).where().getFrom();
    }
    Arrays.sort(starts);
    final Map<Integer, Integer> ends = new HashMap<>();
    for (int i = 0; i < starts.length - 1; ++i) {
      ends.put(starts[i], starts[i + 1]);
    }

    for (int i = 0; i < occurrences.size(); ++i) {
      if (ends.containsKey(occurrences.get(i).where().getFrom())) {
        occurrences.get(i).where().setTo(ends.get(occurrences.get(i).where().getFrom()));
      }
    }
  }
}

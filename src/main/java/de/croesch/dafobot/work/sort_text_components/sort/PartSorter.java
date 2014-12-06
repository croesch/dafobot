package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.regex.Pattern;

/**
 * Sorts the (language) parts of an article.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class PartSorter extends DefaultSorter {

  public PartSorter(final Sorter subPartSorter) {
    super(subPartSorter);
  }

  @Override
  protected Pattern pattern() {
    return Pattern.compile("(\n|^)==[^=]");
  }
}

package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.regex.Pattern;

/**
 * Sorts the sub parts of a language part in an article.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class SubPartSorter extends DefaultSorter {

  public SubPartSorter(final Sorter componentSorter) {
    super(componentSorter);
  }

  @Override
  protected Pattern pattern() {
    return Pattern.compile("\n===[^=]");
  }
}

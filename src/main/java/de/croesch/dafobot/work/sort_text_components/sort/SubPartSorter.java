package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.List;
import java.util.regex.Pattern;

import de.croesch.dafobot.core.Text;

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

  @Override
  protected List<Text> sort(final List<Text> texts) {
    return texts;
  }

  @Override
  protected int findSuffix(final Text text) {
    return text.length();
  }
}

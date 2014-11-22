package de.croesch.dafobot.work.sort_text_components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A component (special template) that result in creating a section (generally speaking).
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public class Component implements ComponentIF {
  private static String WHITESPACE_POSSIBLE_PATTERN = "[\\s_]*";

  private static String WHITESPACE_NECESSARY_PATTERN = "[\\s_]+";

  private final String name;

  private final String namePattern;

  public Component(final String ... nameParts) {
    final StringBuilder np = new StringBuilder(WHITESPACE_POSSIBLE_PATTERN);
    final StringBuilder n = new StringBuilder();
    for (int i = 0; i < nameParts.length; ++i) {
      final String part = nameParts[i];
      np.append(part);
      n.append(part);
      if (i + 1 < nameParts.length) {
        np.append(WHITESPACE_NECESSARY_PATTERN);
        n.append(" ");
      }
    }
    this.name = n.toString();
    this.namePattern = np.toString() + WHITESPACE_POSSIBLE_PATTERN;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Matcher getMatcher(final String text) {
    return Pattern.compile("\\{\\{" + this.namePattern + "\\}\\}").matcher(text);
  }
}

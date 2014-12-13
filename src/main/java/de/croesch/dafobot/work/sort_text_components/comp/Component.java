package de.croesch.dafobot.work.sort_text_components.comp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A component (special template) that result in creating a section (generally speaking). It's always of form {{...}}.
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public class Component extends AComponent {

  public Component(final String ... nameParts) {
    super(nameParts);
  }

  @Override
  public Matcher getMatcher(final String text) {
    final String anchor = "(\\{\\{" + WHITESPACE_POSSIBLE_PATTERN + "Anker[^}]*\\}\\}" + WHITESPACE_POSSIBLE_PATTERN
                          + ")?";
    return Pattern.compile(anchor + "\\{\\{" + WHITESPACE_POSSIBLE_PATTERN + getNamePattern()
                           + WHITESPACE_POSSIBLE_PATTERN + "\\}\\}").matcher(text);
  }
}

package de.croesch.dafobot.work.sort_text_components.comp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A template where only the beginning is important.
 *
 * @author dafo
 * @since Date: Nov 25, 2014
 */
public class BeginningTemplate extends AComponent {

  public BeginningTemplate(final String ... nameParts) {
    super(nameParts);
  }

  @Override
  public Matcher getMatcher(final String text) {
    return Pattern.compile("\\{\\{" + WHITESPACE_POSSIBLE_PATTERN + getNamePattern()).matcher(text);
  }
}

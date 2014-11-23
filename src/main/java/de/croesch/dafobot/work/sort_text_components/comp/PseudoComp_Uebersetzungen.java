package de.croesch.dafobot.work.sort_text_components.comp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special component for the first component not to sort.
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public class PseudoComp_Uebersetzungen extends AComponent {
  public PseudoComp_Uebersetzungen() {
    super("Übersetzungen");
  }

  @Override
  public String getName() {
    return "==== Übersetzungen ====";
  }

  @Override
  public Matcher getMatcher(final String text) {
    return Pattern.compile("={1,4}" + WHITESPACE_POSSIBLE_PATTERN + getNamePattern() + WHITESPACE_POSSIBLE_PATTERN
                                   + "={1,4}").matcher(text);
  }
}

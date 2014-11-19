package de.croesch.dafobot.work.sort_text_components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special component for the first component not to sort.
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public class PseudoComp_Uebersetzungen implements ComponentIF {

  @Override
  public String getName() {
    return "==== Übersetzungen ====";
  }

  @Override
  public Matcher getMatcher(final String text) {
    return Pattern.compile("={1,4}\\s*Übersetzungen\\s*={1,4}").matcher(text);
  }
}

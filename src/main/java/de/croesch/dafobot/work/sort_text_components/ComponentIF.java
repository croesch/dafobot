package de.croesch.dafobot.work.sort_text_components;

import java.util.regex.Matcher;

/**
 * A component (special template) that result in creating a section (generally speaking).
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public interface ComponentIF {

  /** @return the printable name of that component. */
  String getName();

  /** @return {@link Matcher} for finding this component inside the given text. */
  Matcher getMatcher(String text);
}

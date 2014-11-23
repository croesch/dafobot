package de.croesch.dafobot.work.sort_text_components.comp;

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

  /** @return <code>true</code> if this component is allowed inside the given text. */
  boolean availableFor(String text);

  /** @return {@link Matcher} for finding this component inside the given text. */
  Matcher getMatcher(String text);
}

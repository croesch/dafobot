package de.croesch.dafobot.work.sort_text_components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public class Component implements ComponentIF {
  private final String name;

  public Component(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public Matcher getMatcher(final String text) {
    return Pattern.compile("\\{\\{" + getName() + "\\}\\}").matcher(text);
  }
}

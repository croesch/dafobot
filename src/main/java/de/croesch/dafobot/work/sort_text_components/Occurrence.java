package de.croesch.dafobot.work.sort_text_components;

/**
 * Represents a specific occurrence of a component in a text.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Occurrence {

  private final ComponentIF component;

  private final Range where;

  public Occurrence(final ComponentIF comp, final Range r) {
    this.component = comp;
    this.where = r;
  }

  public ComponentIF component() {
    return this.component;
  }

  public Range where() {
    return this.where;
  }
}

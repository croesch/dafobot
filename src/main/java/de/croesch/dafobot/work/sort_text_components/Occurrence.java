package de.croesch.dafobot.work.sort_text_components;


/**
 * Represents a specific occurrence of a text in a text.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Occurrence {

  private final Range where;

  public Occurrence(final Range r) {
    this.where = r;
  }

  public Range where() {
    return this.where;
  }
}

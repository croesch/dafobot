package de.croesch.dafobot.work.sort_text_components;

/**
 * The range of an occurrence containing the information where to find it in a text.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Range {

  private final int from;

  private int to;

  public Range(final int f) {
    this(f, -1);
  }

  public Range(final int f, final int t) {
    this.from = f;
    this.to = t;
  }

  public int getFrom() {
    return this.from;
  }

  public int getTo() {
    return this.to;
  }

  public void setTo(final int to) {
    this.to = to;
  }
}

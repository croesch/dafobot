package de.croesch.dafobot.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Something like a {@link StringBuilder} for {@link Text}s.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class TextBuilder {

  private final List<Text> texts = new ArrayList<>();

  public void append(final Text text) {
    this.texts.add(text);
  }

  public Text toText() {
    final StringBuilder sb = new StringBuilder();
    for (final Text text : this.texts) {
      sb.append(text.toPlainString());
    }
    return new Text(sb.toString().replaceAll("\n\n+", "\n\n"));
  }
}

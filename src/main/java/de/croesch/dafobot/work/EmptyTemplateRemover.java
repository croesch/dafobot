package de.croesch.dafobot.work;

import java.util.Collection;
import java.util.regex.Matcher;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.sort_text_components.comp.Component;

/**
 * Removes empty templates from a given text.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class EmptyTemplateRemover {

  private static final Component EMPTY1 = new Component("----");

  private static final Component EMPTY2 = new Component("2x----");

  public Text edit(Text text, final Collection<String> additionalActions) {
    Matcher emptyMatcher1 = EMPTY1.getMatcher(text.toString());
    while (emptyMatcher1.find()) {
      additionalActions.add("Entferne {{----}}");
      text = remove(text, emptyMatcher1.start(), emptyMatcher1.end());
      emptyMatcher1 = EMPTY1.getMatcher(text.toString());
    }

    Matcher emptyMatcher2 = EMPTY2.getMatcher(text.toString());
    while (emptyMatcher2.find()) {
      additionalActions.add("Entferne {{2x----}}");
      text = remove(text, emptyMatcher2.start(), emptyMatcher2.end());
      emptyMatcher2 = EMPTY2.getMatcher(text.toString());
    }

    return text;
  }

  private Text remove(final Text text, final int from, final int to) {
    String plainResult = text.substring(0, from).toPlainString();
    if (to < text.length()) {
      plainResult += text.substring(to).toPlainString();
    }
    return new Text(plainResult);
  }
}

package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Sorts the (language) parts of an article.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class PartSorter extends DefaultSorter {
  private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^\\s*==[^={]*\\{\\{[^}]+\\|([^}]+)\\}\\}");

  private static final Pattern END_PATTERN = Pattern.compile("(\n+\\[\\[[^\n\\]]+\\]\\]\\s*)+$");

  public PartSorter(final Sorter subPartSorter) {
    super(subPartSorter);
  }

  @Override
  protected Pattern pattern() {
    return Pattern.compile("(\n|^)==[^=]");
  }

  @Override
  protected int findSuffix(final Text text) {
    final Matcher matcher = END_PATTERN.matcher(text.toString());
    if (matcher.find()) {
      return matcher.start();
    }
    return text.length();
  }

  @Override
  protected List<Text> sort(final List<Text> texts) throws PageNeedsQAException {
    final Map<String, Text> parts = parseLanguages(texts);

    return order(parts);
  }

  private List<Text> order(final Map<String, Text> parts) {
    final List<Text> ordered = new ArrayList<>();

    final List<String> languages = new ArrayList<>(parts.keySet());
    addLanguageIfExists(ordered, parts, languages, "deutsch");
    addLanguageIfExists(ordered, parts, languages, "international");
    Collections.sort(languages);

    for (final String language : languages) {
      ordered.add(parts.get(language));
    }

    return ordered;
  }

  private void addLanguageIfExists(final List<Text> ordered,
                                   final Map<String, Text> parts,
                                   final List<String> languages,
                                   final String language) {
    if (languages.contains(language)) {
      ordered.add(parts.get(language));
      languages.remove(language);
    }
  }

  private Map<String, Text> parseLanguages(final List<Text> texts) throws PageNeedsQAException {
    final Map<String, Text> parts = new HashMap<>();
    for (final Text text : texts) {
      final String language = parseLanguage(text);
      if (parts.containsKey(language)) {
        throw new PageNeedsQAException("Duplicate language part for lang=" + language);
      }
      parts.put(language, text);
    }
    return parts;
  }

  private String parseLanguage(final Text text) throws PageNeedsQAException {
    final Matcher matcher = LANGUAGE_PATTERN.matcher(text.toString());

    if (matcher.find()) {
      return matcher.group(1).trim().toLowerCase();
    }

    throw new PageNeedsQAException("Cannot determine language for part.");
  }
}

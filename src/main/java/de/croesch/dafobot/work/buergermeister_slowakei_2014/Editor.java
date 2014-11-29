package de.croesch.dafobot.work.buergermeister_slowakei_2014;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.core.TextBuilder;
import de.croesch.dafobot.work.GeneralEditor;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Sorts text components.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class Editor extends GeneralEditor {
  private static final String INFOBOX_TXT = "\\{\\{Infobox Ort in der Slowakei";

  private static final String STAND_VERWALTUNG_TXT = "STAND_VERWALTUNG";

  private static final String BUERGERMEISTER_TXT = "BÜRGERMEISTER";

  private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

  private static final Pattern INFOBOX = Pattern.compile(INFOBOX_TXT);

  @Override
  protected Text doSpecialEdit(final String title, Text text, final Collection<String> additionalActions)
                                                                                                         throws NoEditNeededException,
                                                                                                         PageNeedsQAException {
    LOG.info("Begin editing " + title);

    if (!INFOBOX.matcher(text.toString()).find()) {
      throw new PageNeedsQAException("infobox not found!");
    }

    text = update(text, BUERGERMEISTER_TXT, "neuer Bürgermeister", "Bgm. aktualisiert", additionalActions);
    text = update(text, STAND_VERWALTUNG_TXT, "November 2014", "Dat. aktualisiert", additionalActions);

    LOG.info("End editing " + title);
    return text;
  }

  private Text update(final Text text,
                      final String attr,
                      final String newValue,
                      final String editNote,
                      final Collection<String> additionalActions) throws PageNeedsQAException {
    final Matcher attrMatcher = Pattern.compile(INFOBOX_TXT + ".*(" + attr + "(\\s*=\\s*)([^|\n]*)).*?\n\\}\\}",
                                                Pattern.DOTALL).matcher(text.toString());

    if (!attrMatcher.find()) {
      throw new PageNeedsQAException("Attribute '" + attr + "' not found");
    }

    if (attrMatcher.group(3).equals(newValue)) {
      return text;
    }

    final TextBuilder tb = new TextBuilder();
    tb.append(text.substring(0, attrMatcher.end(2)));
    tb.append(new Text(newValue));
    tb.append(text.substring(attrMatcher.end(1)));

    additionalActions.add(editNote);

    return tb.toText();
  }

  @Override
  protected String getEditSummary() {
    return "Aufgrund einer [[Benutzer:DafoBot/Infobox_Ort_in_der_Slowakei_2014|Anfrage]] habe ich ";
  }
}

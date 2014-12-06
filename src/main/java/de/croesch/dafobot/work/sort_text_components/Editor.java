package de.croesch.dafobot.work.sort_text_components;

import java.sql.Connection;
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
import de.croesch.dafobot.work.sort_text_components.sort.ComponentSorter;

/**
 * Sorts the text of an article.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class Editor extends GeneralEditor {
  private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

  @Override
  protected Text doSpecialEdit(final String title,
                               final Text text,
                               final Connection connection,
                               final Collection<String> additionalActions) throws NoEditNeededException,
                                                                          PageNeedsQAException {
    LOG.info("Begin editing " + title);

    final TextBuilder tb = new TextBuilder();
    final Matcher matcher = Pattern.compile("\n===?[^=]").matcher(text.toString());
    final ComponentSorter sorter = new ComponentSorter();

    int lastStart = 0;
    boolean found;
    boolean editNeeded = false;
    do {
      found = matcher.find();
      Text partText = text.substring(lastStart, found ? matcher.start() : text.length());
      if (found) {
        lastStart = matcher.start();
      }
      try {
        partText = sorter.sort(partText, additionalActions);
        editNeeded = true;
      } catch (final NoEditNeededException e) {
        // ignore
      }
      tb.append(partText);
    } while (found);

    if (!editNeeded) {
      throw new NoEditNeededException();
    }

    LOG.info("End editing " + title);
    return tb.toText();
  }

  @Override
  protected String getEditSummary() {
    return "Sortiere Textbausteine nach [[Hilfe:Formatvorlage]] ([[Benutzer:DafoBot/Sortierung der Textbausteine|Informationen]] | [[Benutzer Diskussion:DafoBot/Sortierung der Textbausteine|Diskussion]])";
  }
}

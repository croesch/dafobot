package de.croesch.dafobot.work.sort_text_components;

import java.sql.Connection;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.GeneralEditor;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;
import de.croesch.dafobot.work.sort_text_components.sort.ComponentSorter;
import de.croesch.dafobot.work.sort_text_components.sort.PartSorter;
import de.croesch.dafobot.work.sort_text_components.sort.SubPartSorter;

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

    final PartSorter sorter = new PartSorter(new SubPartSorter(new ComponentSorter()));

    final Text result = sorter.sort(text, additionalActions);

    LOG.info("End editing " + title);
    return result;
  }

  @Override
  protected String getEditSummary() {
    return "Sortiere Textbausteine nach [[Hilfe:Formatvorlage]] ([[Benutzer:DafoBot/Sortierung der Textbausteine|Informationen]] | [[Benutzer Diskussion:DafoBot/Sortierung der Textbausteine|Diskussion]])";
  }
}

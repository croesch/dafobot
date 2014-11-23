package de.croesch.dafobot.work.sort_text_components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.core.TextBuilder;
import de.croesch.dafobot.work.GeneralEditor;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.sort_text_components.comp.AvailableIfOtherComponentExists;
import de.croesch.dafobot.work.sort_text_components.comp.Component;
import de.croesch.dafobot.work.sort_text_components.comp.ComponentIF;
import de.croesch.dafobot.work.sort_text_components.comp.NotAvailableIfOtherComponentExists;
import de.croesch.dafobot.work.sort_text_components.comp.PseudoComp_Uebersetzungen;

/**
 * Sorts text components.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class Editor extends GeneralEditor {
  private static final String[] CONDITION_NAME_ARTICLE = new String[] { "Wortart", "\\|", "(Vor|Nach)name" };

  private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

  private static ComponentIF[] COMPONENTS = { new Component("Lesungen"),
                                             new Component("Anmerkung(\\s+|\\|)Steigerung"),
                                             new Component("Artikel", "Nachname"),
                                             new Component("Artikel", "Toponym"),
                                             new Component("Steigerbarkeit", "Adjektiv"),
                                             new Component("Anmerkung"),
                                             new Component("Anmerkung\\|zum Genus"),
                                             new NotAvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                    new String[] { "Alternative",
                                                                                                  "Schreibweisen" }),
                                             new Component("Veraltete", "Schreibweisen"),
                                             new Component("Nebenformen"),
                                             new Component("Worttrennung"),
                                             new Component("in", "arabischer", "Schrift"),
                                             new Component("in", "kyrillischer", "Schrift"),
                                             new Component("in", "lateinischer", "Schrift"),
                                             new Component("Strichreihenfolge"),
                                             new Component("Vokalisierung"),
                                             new Component("Umschrift"),
                                             new Component("Aussprache"),
                                             new Component("Grammatische", "Merkmale"),
                                             new Component("Bedeutungen"),
                                             new Component("Abkürzungen"),
                                             new Component("Symbole"),
                                             new Component("Herkunft"),
                                             new Component("Synonyme"),
                                             new Component("Sinnverwandte", "(Wörter|Zeichen|Redewendungen)"),
                                             new Component("Gegenwörter"),
                                             new Component("Weibliche", "Wortformen"),
                                             new Component("Männliche", "Wortformen"),
                                             new Component("Verkleinerungsformen"),
                                             new Component("Vergrößerungsformen"),
                                             new Component("Oberbegriffe"),
                                             new Component("Unterbegriffe"),
                                             new Component("Kurzformen"),
                                             new Component("Koseformen"),
                                             new AvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                 new String[] { "Alternative",
                                                                                               "Schreibweisen" }),
                                             new Component("Namensvarianten"),
                                             new NotAvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                    new String[] { "Weibliche",
                                                                                                  "Namensvarianten" }),
                                             new Component("Männliche", "Namensvarianten"),
                                             new AvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                 new String[] { "Weibliche",
                                                                                               "Namensvarianten" }),
                                             new Component("Bekannte", "Namensträger"),
                                             new Component("Beispiele"),
                                             new Component("Redewendungen"),
                                             new Component("Sprichwörter"),
                                             new Component("Charakteristische", "Wortkombinationen"),
                                             new Component("Wortbildungen"),
                                             new Component("Entlehnungen") };

  private static ComponentIF[] END_COMPONENTS = { new PseudoComp_Uebersetzungen(),
                                                 new Component("Dialektausdrücke"),
                                                 new Component("Lemmaverweis[^}]+"),
                                                 new Component("Referenzen"),
                                                 // mehrfach?
                                                 new Component("Navigationsleiste[^}]+"),
                                                 new Component("Quellen"),
                                                 new Component("Ähnlichkeiten") };

  @Override
  protected Text doSpecialEdit(final String title, final Text text) throws NoEditNeededException {
    LOG.info("Begin editing " + title);

    final TextBuilder tb = new TextBuilder();
    final Matcher matcher = Pattern.compile("\n==[^=]").matcher(text.toString());

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
        partText = editPart(partText);
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

  private Text editPart(final Text text) throws NoEditNeededException {
    final TextBuilder tb = new TextBuilder();
    final List<Occurrence> whereEnd = findComponents(text, END_COMPONENTS, new ArrayList<ComponentIF>());
    final int beginEnd = min(whereEnd);

    final Text textWithoutEnd = whereEnd.isEmpty() ? text : text.substring(0, beginEnd);
    final List<Occurrence> whereComponents = findComponents(textWithoutEnd, COMPONENTS, new ArrayList<ComponentIF>());

    if (areAlreadyOrderedCorrectly(whereComponents)) {
      // nothing to sort
      throw new NoEditNeededException();
    }

    tb.append(begin(textWithoutEnd, whereComponents));
    for (final Occurrence occurrence : whereComponents) {
      if (occurrence.where().getTo() < 0) {
        tb.append(textWithoutEnd.substring(occurrence.where().getFrom()));
      } else {
        tb.append(textWithoutEnd.substring(occurrence.where().getFrom(), occurrence.where().getTo()));
      }
    }
    if (!whereEnd.isEmpty()) {
      tb.append(text.substring(beginEnd));
    }

    return tb.toText();
  }

  private boolean areAlreadyOrderedCorrectly(final List<Occurrence> occurrences) {
    if (occurrences.isEmpty()) {
      return true;
    }
    int lastStart = -1;
    for (final Occurrence occurrence : occurrences) {
      if (occurrence.where().getFrom() < lastStart) {
        return false;
      }
      lastStart = occurrence.where().getFrom();
    }
    return true;
  }

  private List<Occurrence> findComponents(final Text text,
                                          final ComponentIF[] components,
                                          final List<ComponentIF> duplicates) {
    final List<Occurrence> occurrences = new ArrayList<>();

    for (final ComponentIF component : components) {
      if (component.availableFor(text.toString())) {
        final Matcher matcher = component.getMatcher(text.toString());
        final boolean found = matcher.find();
        if (found) {
          occurrences.add(new Occurrence(component, new Range(matcher.start())));
          if (matcher.find()) {
            duplicates.add(component);
          }
        }
      }
    }

    fillRange(occurrences);
    return occurrences;
  }

  private void fillRange(final List<Occurrence> occurrences) {
    final int[] starts = new int[occurrences.size()];
    for (int i = 0; i < occurrences.size(); ++i) {
      starts[i] = occurrences.get(i).where().getFrom();
    }
    Arrays.sort(starts);
    final Map<Integer, Integer> ends = new HashMap<>();
    for (int i = 0; i < starts.length - 1; ++i) {
      ends.put(starts[i], starts[i + 1]);
    }

    for (int i = 0; i < occurrences.size(); ++i) {
      if (ends.containsKey(occurrences.get(i).where().getFrom())) {
        occurrences.get(i).where().setTo(ends.get(occurrences.get(i).where().getFrom()));
      }
    }
  }

  private Text begin(final Text text, final List<Occurrence> where) {
    final int min = min(where);
    return text.substring(0, min);
  }

  private int min(final List<Occurrence> where) {
    int min = Integer.MAX_VALUE;
    for (final Occurrence value : where) {
      min = Math.min(min, value.where().getFrom());
    }
    return min;
  }

  @Override
  protected String getEditSummary() {
    return "Sortiere Textbausteine";
  }
}

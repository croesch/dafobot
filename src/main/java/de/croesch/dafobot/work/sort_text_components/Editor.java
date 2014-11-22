package de.croesch.dafobot.work.sort_text_components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.work.GeneralEditor;
import de.croesch.dafobot.work.api.NoEditNeededException;

/**
 * Sorts text components.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class Editor extends GeneralEditor {
  private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

  private static ComponentIF[] COMPONENTS = { new Component("Lesungen"),
                                             new Component("Anmerkung(\\s+|\\|)Steigerung"),
                                             new Component("Artikel\\s+Nachname"),
                                             new Component("Artikel\\s+Toponym"),
                                             new Component("Anmerkung"),
                                             new Component("Anmerkung\\|zum Genus"),
                                             new Component("Alternative\\s+Schreibweisen"),
                                             new Component("Veraltete\\s+Schreibweisen"),
                                             new Component("Nebenformen"),
                                             new Component("Worttrennung"),
                                             new Component("in arabischer Schrift"),
                                             new Component("in kyrillischer Schrift"),
                                             new Component("in lateinischer Schrift"),
                                             new Component("Strichreihenfolge"),
                                             new Component("Vokalisierung"),
                                             new Component("Umschrift"),
                                             new Component("Aussprache"),
                                             new Component("Grammatische Merkmale"),
                                             new Component("Bedeutungen"),
                                             new Component("Abkürzungen"),
                                             new Component("Symbole"),
                                             new Component("Herkunft"),
                                             new Component("Synonyme"),
                                             new Component("Sinnverwandte (Wörter|Zeichen|Redewendungen)"),
                                             new Component("Gegenwörter"),
                                             new Component("Weibliche Wortformen"),
                                             new Component("Männliche Wortformen"),
                                             new Component("Verkleinerungsformen"),
                                             new Component("Vergrößerungsformen"),
                                             new Component("Oberbegriffe"),
                                             new Component("Unterbegriffe"),
                                             new Component("Kurzformen"),
                                             new Component("Koseformen"),
                                             new Component("Namensvarianten"),
                                             new Component("Weibliche Namensvarianten"),
                                             new Component("Männliche Namensvarianten"),
                                             new Component("Bekannte Namensträger"),
                                             new Component("Beispiele"),
                                             new Component("Redewendungen"),
                                             new Component("Sprichwörter"),
                                             new Component("Charakteristische Wortkombinationen"),
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
  protected String doSpecialEdit(final String title, final String text) throws NoEditNeededException {
    LOG.info("Begin editing " + title);

    final StringBuilder sb = new StringBuilder();
    final List<Occurrence> whereEnd = findComponents(text, END_COMPONENTS, new ArrayList<ComponentIF>());
    final int beginEnd = min(whereEnd);

    final String textWithoutEnd = whereEnd.isEmpty() ? text : text.substring(0, beginEnd);
    final List<Occurrence> whereComponents = findComponents(textWithoutEnd, COMPONENTS, new ArrayList<ComponentIF>());

    if (whereComponents.isEmpty()) {
      // nothing to sort
      throw new NoEditNeededException();
    }

    sb.append(begin(textWithoutEnd, whereComponents));
    for (final Occurrence occurrence : whereComponents) {
      if (occurrence.where().getTo() < 0) {
        sb.append(textWithoutEnd.substring(occurrence.where().getFrom()));
      } else {
        sb.append(textWithoutEnd.substring(occurrence.where().getFrom(), occurrence.where().getTo()));
      }
    }
    if (whereEnd.isEmpty()) {
      sb.append(text.substring(beginEnd));
    }

    LOG.info("End editing " + title);
    return sb.toString();
  }

  private List<Occurrence> findComponents(final String text,
                                          final ComponentIF[] components,
                                          final List<ComponentIF> duplicates) {
    final List<Occurrence> occurrences = new ArrayList<>();

    for (final ComponentIF component : components) {
      final Matcher matcher = component.getMatcher(text);
      final boolean found = matcher.find();
      if (found) {
        occurrences.add(new Occurrence(component, new Range(matcher.start())));
        if (matcher.find()) {
          duplicates.add(component);
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

  private String begin(final String text, final List<Occurrence> where) {
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

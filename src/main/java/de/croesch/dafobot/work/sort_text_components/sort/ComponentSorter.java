package de.croesch.dafobot.work.sort_text_components.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.core.TextBuilder;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;
import de.croesch.dafobot.work.sort_text_components.Occurrence;
import de.croesch.dafobot.work.sort_text_components.Range;
import de.croesch.dafobot.work.sort_text_components.comp.AvailableIfOtherComponentExists;
import de.croesch.dafobot.work.sort_text_components.comp.BeginningTemplate;
import de.croesch.dafobot.work.sort_text_components.comp.Component;
import de.croesch.dafobot.work.sort_text_components.comp.ComponentIF;
import de.croesch.dafobot.work.sort_text_components.comp.MultiComponent;
import de.croesch.dafobot.work.sort_text_components.comp.NotAvailableIfOtherComponentExists;
import de.croesch.dafobot.work.sort_text_components.comp.PseudoComp_Uebersetzungen;

/**
 * Sorts the text components of an sub part of an article.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class ComponentSorter extends AbstractSorter {

  private static final ComponentIF NAME = new BeginningTemplate("Wortart", "|", "(Vor|Nach)name");

  private static final Component MALE = new Component("m");

  private static final Component FEMALE = new Component("f");

  private static final String[] CONDITION_NAME_ARTICLE = new String[] {"Wortart", "|", "(Vor|Nach)name"};

  private static final Component MALE_OLD_VARIANT = new Component("Männliche", "Wortformen");

  private static final Component FEMALE_OLD_VARIANT = new Component("Weibliche", "Wortformen");

  private static ComponentIF[] COMPONENTS = {new Component("Lesungen"),
                                             new Component("Anmerkung", "Steigerung"),
                                             new Component("Artikel", "Nachname"),
                                             new Component("Artikel", "Toponym"),
                                             new Component("Steigerbarkeit", "Adjektiv"),
                                             new Component("Anmerkung(en)?"),
                                             new MultiComponent("Anmerkung(en)?", "|", "[^}]*"),
                                             new NotAvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                    new String[] {"Alternative",
                                                                                                  "Schreibweisen"}),
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
                                             new Component("Grundformverweis(",
                                                           "(Dekl|Konj|Partizipform))?(",
                                                           "|",
                                                           "[^}]*)?"),
                                             new Component("Bedeutungen"),
                                             new Component("Abkürzungen"),
                                             new Component("Symbole"),
                                             new Component("(QS", "Herkunft", "|", "fehlt|Herkunft)"),
                                             new Component("Synonyme"),
                                             new Component("Sinnverwandte", "(Wörter|Zeichen|Redewendungen)"),
                                             new Component("Gegenwörter"),
                                             FEMALE_OLD_VARIANT,
                                             MALE_OLD_VARIANT,
                                             new Component("Verkleinerungsformen"),
                                             new Component("Vergrößerungsformen"),
                                             new Component("Oberbegriffe"),
                                             new Component("Unterbegriffe"),
                                             new Component("Kurzformen"),
                                             new Component("Koseformen"),
                                             new AvailableIfOtherComponentExists(CONDITION_NAME_ARTICLE,
                                                                                 new String[] {"Alternative",
                                                                                               "Schreibweisen"}),
                                             new Component("Namensvarianten"),
                                             new Component("Weibliche", "Namensvarianten"),
                                             new Component("Männliche", "Namensvarianten"),
                                             new Component("Bekannte", "Namensträger"),
                                             new Component("Beispiele"),
                                             new Component("Redewendungen"),
                                             new Component("Sprichwörter"),
                                             new Component("Charakteristische", "Wortkombinationen"),
                                             new Component("Wortbildungen"),
                                             new Component("Entlehnungen")};

  private static ComponentIF[] END_COMPONENTS = {new PseudoComp_Uebersetzungen(),
                                                 new Component("Dialektausdrücke"),
                                                 new Component("Lemmaverweis[^}]+"),
                                                 new Component("Referenzen"),
                                                 // mehrfach?
                                                 new Component("Navigationsleiste[^}]+"),
                                                 new Component("Quellen"),
                                                 new Component("Ähnlichkeiten")};

  @Override
  public Text sort(final Text text, final Collection<String> additionalActions) throws PageNeedsQAException,
                                                                               NoEditNeededException {
    final TextBuilder tb = new TextBuilder();
    final List<Occurrence> whereEnd = findComponents(text, END_COMPONENTS, new ArrayList<ComponentIF>());
    final int beginEnd = min(whereEnd);

    Text textWithoutEnd = whereEnd.isEmpty() ? text : text.substring(0, beginEnd);
    textWithoutEnd = replaceOldNameVariants(textWithoutEnd, additionalActions);
    final ArrayList<ComponentIF> duplicateComponents = new ArrayList<ComponentIF>();
    final List<Occurrence> whereComponents = findComponents(textWithoutEnd, COMPONENTS, duplicateComponents);

    if (!duplicateComponents.isEmpty()) {
      throw new PageNeedsQAException("duplicate components " + duplicateComponents);
    }
    if (areAlreadyOrderedCorrectly(whereComponents)) {
      if (additionalActions.isEmpty()) {
        // nothing to sort
        throw new NoEditNeededException();
      }
      tb.append(textWithoutEnd);
      if (!whereEnd.isEmpty()) {
        tb.append(text.substring(beginEnd));
      }
      return tb.toText();
    }

    tb.append(begin(textWithoutEnd, whereComponents));
    for (int i = 0; i < whereComponents.size(); ++i) {
      final Occurrence occurrence = whereComponents.get(i);
      Text toAppend;
      if (occurrence.where().getTo() < 0) {
        toAppend = textWithoutEnd.substring(occurrence.where().getFrom());
      } else {
        toAppend = textWithoutEnd.substring(occurrence.where().getFrom(), occurrence.where().getTo());
      }
      tb.append(new Text(toAppend.toPlainString().trim()));
      // each result is trimmed so append 'divider' (except for last one)
      // if (i + 1 < whereComponents.size()) {
      tb.append(new Text("\n\n"));
      // }
    }
    if (!whereEnd.isEmpty()) {
      tb.append(text.substring(beginEnd));
    }

    return tb.toText();
  }

  private Text replaceOldNameVariants(Text text, final Collection<String> additionalActions) {
    final String editString = "Wortformen -> Namensvarianten";
    if (NAME.getMatcher(text.toString()).find()) {
      final Matcher femaleOldMatcher = FEMALE_OLD_VARIANT.getMatcher(text.toString());
      if (femaleOldMatcher.find()) {
        String replacement;
        if (FEMALE.getMatcher(text.toString()).find()) {
          replacement = "{{Namensvarianten}}";
        } else {
          replacement = "{{Weibliche Namensvarianten}}";
        }
        additionalActions.add(editString);
        text = new Text(text.substring(0, femaleOldMatcher.start()).toPlainString() + replacement
                        + text.substring(femaleOldMatcher.end()).toPlainString());
      }
      final Matcher maleOldMatcher = MALE_OLD_VARIANT.getMatcher(text.toString());
      if (maleOldMatcher.find()) {
        String replacement;
        if (MALE.getMatcher(text.toString()).find()) {
          replacement = "{{Namensvarianten}}";
        } else {
          replacement = "{{Männliche Namensvarianten}}";
        }
        additionalActions.add(editString);
        text = new Text(text.substring(0, maleOldMatcher.start()).toPlainString() + replacement
                        + text.substring(maleOldMatcher.end()).toPlainString());
      }
    }
    return text;
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
          occurrences.add(new Occurrence(new Range(matcher.start())));
          if (component.isAllowedMultipleTimes()) {
            while (matcher.find()) {
              occurrences.add(new Occurrence(new Range(matcher.start())));
            }
          } else if (matcher.find()) {
            duplicates.add(component);
          }
        }
      }
    }

    fillRange(occurrences);
    return occurrences;
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
}

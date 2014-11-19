package de.croesch.dafobot.work.sort_text_components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.work.GeneralEditor;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class Editor extends GeneralEditor {

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
  protected void doSpecialEdit(final SimpleArticle article) {
    final StringBuilder sb = new StringBuilder();
    final Map<Integer, ComponentIF> whereEnd = findComponents(article.getText(), END_COMPONENTS,
                                                              new ArrayList<ComponentIF>());
    final int beginEnd = min(whereEnd);

    final String textWithoutEnd = article.getText().substring(0, beginEnd);
    final Map<Integer, ComponentIF> whereComponents = findComponents(textWithoutEnd, COMPONENTS,
                                                                     new ArrayList<ComponentIF>());
    sb.append("============").append("\n");
    sb.append(" BEGIN").append("\n");
    sb.append("============").append("\n");
    sb.append(begin(textWithoutEnd, whereComponents));

    sb.append("============").append("\n");
    sb.append(" END").append("\n");
    sb.append("============").append("\n");
    sb.append(article.getText().substring(beginEnd));

    article.setText(sb.toString());
  }

  private Map<Integer, ComponentIF> findComponents(final String text,
                                                   final ComponentIF[] components,
                                                   final List<ComponentIF> duplicates) {
    final Map<Integer, ComponentIF> where = new HashMap<Integer, ComponentIF>();

    for (final ComponentIF component : components) {
      final Matcher matcher = component.getMatcher(text);
      final boolean found = matcher.find();
      if (found) {
        where.put(matcher.start(), component);
        if (matcher.find()) {
          duplicates.add(component);
        }
      }

      //      sb.append(component.getName()).append(": ").append(found).append("\n");
    }
    return where;
  }

  private String begin(final String text, final Map<Integer, ComponentIF> where) {
    final int min = min(where);
    return text.substring(0, min);
  }

  private int min(final Map<Integer, ComponentIF> where) {
    int min = Integer.MAX_VALUE;
    for (final Integer value : where.keySet()) {
      min = Math.min(min, value);
    }
    return min;
  }

  @Override
  protected String getEditSummary() {
    return "Sortiere Textbausteine";
  }
}

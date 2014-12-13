package de.croesch.dafobot.work;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.croesch.dafobot.core.Text;

/**
 * If there are multiple lines behind each other this removes it.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class KosmetikEditor {

  public Text edit(final Text subResult, final Collection<String> additionalActions) {
    String string = subResult.toPlainString();

    // remove bad comments
    string = string
      .replaceAll("<!-- für weitere Sprachkürzel siehe den Link (rechts )?unterhalb des Editierfensters -->", "");
    // remove unnecessary empty lines
    string = string.replaceAll("\n\n\n+", "\n\n");
    // remove trailing spaces
    string = string.replaceAll(" +\n", "\n");
    // remove space after one/two colons at line beginning
    string = string.replaceAll("\n(:{1,2}) +([^\n])", "\n$1$2");
    // remove unnecessary spaces
    string = string.replaceAll("  +", " ");
    // replace dash by comma if two numbers are following up
    string = replaceDashByComma(string);
    // use correct dash
    string = string.replaceAll("(\n:\\[\\d)-(\\d\\])", "$1–$2");
    string = string.replaceAll("(\n:\\[\\d)-(\\d, \\d\\])", "$1–$2");
    // add missing space
    string = string.replaceAll("(\n:\\[\\d,)(\\d\\])", "$1 $2");
    // remove spaces between " and <ref>
    string = string.replaceAll("(\\{\\{Beispiele\\}\\}.*?“) +(<ref>)", "$1$2");
    // trim inside <ref>..</ref>
    string = string.replaceAll("<ref>[ \t]+", "<ref>");
    string = string.replaceAll("[ \t]+</ref>", "</ref>");
    // remove empty template
    string = string.replaceAll("\\{\\{Ähnlichkeiten\\}\\}\n\n", "");
    // remove {{PAGENAME}}
    string = string.replaceAll("\\{\\{PAGENAME\\}\\}", "{{subst:PAGENAME}}");

    if (!string.equals(subResult.toPlainString())) {
      additionalActions.add("Kosmetik");
    }
    return new Text(string);
  }

  private String replaceDashByComma(final String string) {
    final Matcher m = Pattern.compile("\n:\\[(\\d)[-—–]+(\\d)\\]").matcher(string);
    final StringBuffer sb = new StringBuffer(string.length());
    while (m.find()) {
      try {
        final Integer one = Integer.valueOf(m.group(1));
        final Integer two = Integer.valueOf(m.group(2));
        if (one + 1 == two) {
          m.appendReplacement(sb, "\n:[" + m.group(1) + ", " + m.group(2) + "]");
        }
      } catch (final NumberFormatException e) {
        // continue
      }
    }
    m.appendTail(sb);
    return sb.toString();
  }
}

package de.croesch.dafobot.work.buergermeister_slowakei_2014;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  protected Text doSpecialEdit(final String title,
                               Text text,
                               final Connection connection,
                               final Collection<String> additionalActions) throws NoEditNeededException,
                                                                          PageNeedsQAException {
    LOG.info("Begin editing " + title);

    if (!INFOBOX.matcher(text.toString()).find()) {
      throw new PageNeedsQAException("infobox not found!");
    }

    final String newMayor = getMayor(text, connection);

    text = update(text, BUERGERMEISTER_TXT, newMayor, "Bürgermeister aktualisiert", additionalActions);
    text = update(text, STAND_VERWALTUNG_TXT, "November 2014", "Datum aktualisiert", additionalActions);

    if (additionalActions.isEmpty()) {
      throw new NoEditNeededException();
    }

    LOG.info("End editing " + title);
    return text;
  }

  private String getMayor(final Text text, final Connection connection) throws PageNeedsQAException {
    final Matcher okresMatcher = Pattern.compile(INFOBOX_TXT + ".*OKRES\\s*=\\s*([^|\n]*).*?\n\\}\\}", Pattern.DOTALL)
                                        .matcher(text.toString());
    final Matcher cityMatcher = Pattern.compile(INFOBOX_TXT + ".*NAME\\s*=\\s*([^|\n]*).*?\n\\}\\}", Pattern.DOTALL)
                                       .matcher(text.toString());

    if (!okresMatcher.find()) {
      throw new PageNeedsQAException("Cannot extract okres");
    }
    if (!cityMatcher.find()) {
      throw new PageNeedsQAException("Cannot extract city");
    }

    final String okres = okresMatcher.group(1);
    final String city = cityMatcher.group(1);

    try (final PreparedStatement statement = connection.prepareStatement("SELECT buergermeister FROM `election` WHERE okres=? AND ort=?");) {
      statement.setString(1, okres);
      statement.setString(2, city);

      final ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        final String mayor = rs.getString(1);
        if (rs.next()) {
          throw new PageNeedsQAException("more than one mayor found");
        }
        return mayor;
      } else {
        throw new PageNeedsQAException("new mayor not found");
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new PageNeedsQAException(e.getMessage());
    }
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
    return "Aufgrund von Neuwahlen ([[Benutzer:DafoBot/Infobox_Ort_in_der_Slowakei_2014|weitere Infos]]) ";
  }

  @Override
  protected String getEditSummaryAppendum(final Collection<String> additionalActions) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (final String additionalAction : additionalActions) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      sb.append(additionalAction);
    }
    return sb.toString();
  }
}

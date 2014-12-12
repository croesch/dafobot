package de.croesch.dafobot.work.sk_ort_code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.core.Text;
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

  private static final Logger LOG = LoggerFactory.getLogger(Editor.class);

  private static final Pattern INFOBOX = Pattern.compile(INFOBOX_TXT);

  public Editor() {
    super(true);
  }

  @Override
  protected Text doSpecialEdit(final String title,
                               final Text text,
                               final Connection connection,
                               final Collection<String> additionalActions) throws NoEditNeededException,
                                                                          PageNeedsQAException {
    LOG.info("Begin editing " + title);

    if (!INFOBOX.matcher(text.toString()).find()) {
      throw new PageNeedsQAException("infobox not found!");
    }

    final Matcher codeMatcher = Pattern.compile(INFOBOX_TXT + ".*?CODE\\s*=\\s*([0-9]+).*?\\}\\}", Pattern.DOTALL)
            .matcher(text.toString());

    if (codeMatcher.find()) {
      final Integer code = Integer.valueOf(codeMatcher.group(1));

      final String statementString = "UPDATE `pages` SET code=? WHERE page=?";

      try (final PreparedStatement statement = connection.prepareStatement(statementString);) {
        statement.setInt(1, code.intValue());
        statement.setString(2, title);
        statement.executeUpdate();
      } catch (final SQLException e) {
        e.printStackTrace();
        throw new PageNeedsQAException(e.getMessage());
      }
    } else {
      throw new PageNeedsQAException("infobox not found!");
    }

    throw new NoEditNeededException();
  }

  @Override
  protected String getEditSummary() {
    return "";
  }
}

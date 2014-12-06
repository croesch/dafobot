package de.croesch.dafobot.work;

import java.util.Collection;

import de.croesch.dafobot.core.Text;

/**
 * If there are multiple lines behind each other this removes it.
 *
 * @author dafo
 * @since Date: Dec 6, 2014
 */
public class DuplicateEmptyLineRemover {

  public Text edit(final Text subResult, final Collection<String> additionalActions) {
    return new Text(subResult.toPlainString().replaceAll("\n\n\n+", "\n\n"));
  }
}

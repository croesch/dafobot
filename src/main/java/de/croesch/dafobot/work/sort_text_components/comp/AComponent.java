package de.croesch.dafobot.work.sort_text_components.comp;

/**
 * A component. The subclasses define how it exactly looks like. This just handles creation of a pattern to find the
 * components and handling whitespace occurrence.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
abstract class AComponent implements ComponentIF {
  protected static String WHITESPACE_POSSIBLE_PATTERN = "[\\s_]*";

  protected static String WHITESPACE_NECESSARY_PATTERN = "[\\s_]+";

  private final String name;

  private final String namePattern;

  public AComponent(final String ... nameParts) {
    final StringBuilder np = new StringBuilder();
    final StringBuilder n = new StringBuilder();
    for (int i = 0; i < nameParts.length; ++i) {
      final String part = nameParts[i];
      np.append(part);
      n.append(part);
      if (i + 1 < nameParts.length) {
        np.append(WHITESPACE_NECESSARY_PATTERN);
        n.append(" ");
      }
    }
    this.name = n.toString();
    this.namePattern = np.toString();
  }

  @Override
  public String getName() {
    return this.name;
  }

  protected String getNamePattern() {
    return this.namePattern;
  }

  @Override
  public boolean availableFor(final String text) {
    return true;
  }
}

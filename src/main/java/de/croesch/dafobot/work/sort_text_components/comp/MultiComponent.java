package de.croesch.dafobot.work.sort_text_components.comp;

/**
 * Component that is allowed to appear multiple times.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class MultiComponent extends Component {

  public MultiComponent(final String ... nameParts) {
    super(nameParts);
  }

  @Override
  public boolean isAllowedMultipleTimes() {
    return true;
  }
}

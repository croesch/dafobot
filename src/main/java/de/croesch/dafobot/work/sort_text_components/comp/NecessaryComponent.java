package de.croesch.dafobot.work.sort_text_components.comp;

/**
 * This component has to be in the text.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class NecessaryComponent extends Component {

  private final String niceVariant;

  private final ComponentIF component;

  private final String commentAddition;

  public NecessaryComponent(final String niceVariant, final String commentAddition, final String ... nameParts) {
    this(niceVariant, commentAddition, null, nameParts);
  }

  /** Necessary if the other component doesn't exist. */
  public NecessaryComponent(final String niceVariant,
                            final String commentAddition,
                            final ComponentIF component,
                            final String ... nameParts) {
    super(nameParts);
    this.niceVariant = niceVariant;
    this.commentAddition = commentAddition;
    this.component = component;
  }

  public String getNiceVariant() {
    return this.niceVariant;
  }

  @Override
  public boolean isNecessary(final String text) {
    if (this.component != null) {
      return !this.component.getMatcher(text).find();
    }
    return true;
  }

  public String getCommentAddition() {
    return this.commentAddition;
  }
}

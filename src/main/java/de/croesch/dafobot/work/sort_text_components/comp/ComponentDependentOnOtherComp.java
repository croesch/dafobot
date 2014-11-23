package de.croesch.dafobot.work.sort_text_components.comp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * This component's availability is dependent on another component. This is used to handle that the order of components
 * is dependent on the existence of some components.
 * </p>
 * <p>
 * <em>Example:</em> Let's say we have components A, B, C and D. If D is available the order has to be ABC, if D is not
 * available, the order is ACB.
 * </p>
 * <p>
 * So we construct the order ABCB where the first B is available if D is available, and the second B only if D is not
 * available. And we've already managed the order being dependent on the existence of D.
 * </p>
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public abstract class ComponentDependentOnOtherComp extends Component {
  private final ComponentIF condition;

  public ComponentDependentOnOtherComp(final String[] conditionParts, final String[] component) {
    super(component);
    this.condition = new AComponent(conditionParts) {
      @Override
      public Matcher getMatcher(final String text) {
        return Pattern.compile("\\{\\{" + WHITESPACE_POSSIBLE_PATTERN + getNamePattern()).matcher(text);
      }
    };
  }

  @Override
  public abstract boolean availableFor(String text);

  protected boolean otherComponentIsAvailable(final String text) {
    return this.condition.getMatcher(text).find();
  }
}

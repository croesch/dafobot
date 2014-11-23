package de.croesch.dafobot.work.sort_text_components.comp;

/**
 * This component is only available if another one is existent. This is used to handle that the order of components is
 * dependent on the existence of some components. See {@link ComponentDependentOnOtherComp} for details.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class AvailableIfOtherComponentExists extends ComponentDependentOnOtherComp {

  public AvailableIfOtherComponentExists(final String[] conditionParts, final String[] component) {
    super(conditionParts, component);
  }

  @Override
  public boolean availableFor(final String text) {
    return otherComponentIsAvailable(text);
  }
}

package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import de.croesch.dafobot.work.api.ChangeVerifierIF;
import de.croesch.dafobot.work.api.VerificationResult;

/**
 * {@link ChangeVerifierIF} for manual verification of a change.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class AutomaticVerifier implements ChangeVerifierIF {
  @Override
  public VerificationResult verify(final SimpleArticle oldArticle, final SimpleArticle article) {
    return VerificationResult.GOOD;
  }
}

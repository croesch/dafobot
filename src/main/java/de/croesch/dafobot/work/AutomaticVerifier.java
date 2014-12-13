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
  private final int maximum;

  private int count = 0;

  public AutomaticVerifier(final int maximum) {
    this.maximum = maximum;
  }

  public AutomaticVerifier() {
    this(-1);
  }

  @Override
  public VerificationResult verify(final SimpleArticle oldArticle, final SimpleArticle article) {
    ++this.count;
    if (this.maximum < 0 || this.count <= this.maximum) {
      return VerificationResult.GOOD;
    }
    return VerificationResult.FATAL;
  }
}

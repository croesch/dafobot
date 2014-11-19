package de.croesch.dafobot.work;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.work.api.ChangeVerifierIF;
import de.croesch.dafobot.work.api.VerificationResult;
import de.croesch.dafobot.work.visual_verification.VerificationFrame;

/**
 * {@link ChangeVerifierIF} for manual verification of a change.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public class VisualManualVerifier implements ChangeVerifierIF {
  private static final Logger LOG = LoggerFactory.getLogger(VisualManualVerifier.class);

  @Override
  public VerificationResult verify(final SimpleArticle oldArticle, final SimpleArticle article) {
    final VerificationFrame verificationFrame = new VerificationFrame(oldArticle, article);
    while (verificationFrame.isOpen()) {
      try {
        Thread.sleep(1000);
      } catch (final InterruptedException e) {
        LOG.error(e.getMessage(), e);
      }
    }
    return verificationFrame.getResult();
  }
}

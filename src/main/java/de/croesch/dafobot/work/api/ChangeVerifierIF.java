package de.croesch.dafobot.work.api;

import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

/**
 * Verifies the changes and decides whether a change should be made.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public interface ChangeVerifierIF {

  /**
   * Verify the change from <code>oldArticle</code> to <code>article</code>.
   *
   * @since Date: Nov 19, 2014
   * @param oldArticle the original version of the article
   * @param article the new version of the article
   * @return the {@link VerificationResult} stating if the change can be done
   */
  VerificationResult verify(SimpleArticle oldArticle, SimpleArticle article);
}

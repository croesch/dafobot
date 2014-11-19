package de.croesch.dafobot.work.api;

/**
 * Verification result of a change.
 *
 * @author dafo
 * @since Date: Nov 16, 2014
 */
public enum VerificationResult {
  /** change can be done. */
  GOOD,
  /** change should not be done. */
  BAD,
  /** change should not be done. And bot should be stopped! */
  FATAL;
}

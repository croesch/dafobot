package de.croesch.dafobot.work.api;

/**
 * TODO Comment here ...
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

package de.croesch.dafobot.work.api;

import java.io.IOException;

/**
 * Normally each page the {@link PagePoolIF} delivers can be edited. This exception is used to signal that this isn't
 * the case with the current page because its source is somehow corrupt.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class PageNeedsQAException extends IOException {

  /** generated serial version UID */
  private static final long serialVersionUID = -3390086626749487606L;

  public PageNeedsQAException(final String reason) {
    super(reason);
  }
}

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.plugin.logging.Log;

/**
 * Abstract base class for objects that write to the maven log.
 */
public abstract class MavenLogged implements MavenLoggable {
  /** The maven log. */
  private final Log mLog;

  /**
   * Creates a new <code>MavenLogged</code> instance.
   *
   * @param log The maven log.
   */
  protected MavenLogged(Log log) {
    mLog = log;
  }

  /** {@inheritDoc} */
  @Override
  public Log getLog() {
    return mLog;
  }
}

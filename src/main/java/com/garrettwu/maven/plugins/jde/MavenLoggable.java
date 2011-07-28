// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.plugin.logging.Log;

/**
 * Components implementing MavenLoggable are capable of logging messages a maven log.
 */
public interface MavenLoggable {
  /**
   * Gets the logger for communicating to the maven user.
   *
   * @return The maven logger.
   */
  Log getLog();
}

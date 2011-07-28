// (c) Copyright 2011 Garrett Wu

/**
 * A maven plugin for integrating with the Emacs JDE package.
 *
 * <p>The Java Development Environment for Emacs (JDE) looks for a file (usually named
 * prj.el) in the parent directories of an opened <code>.java</code> file.  If found, it
 * is used to configure custom JDE variables that tell Emacs where to find the jars,
 * classes, java sources, and javadoc html files related to the code for the project.</p>
 *
 * <p>This plugin has a goal <code>jde</code> to generate the <code>prj.el</code> file in
 * the root maven directory so Emacs+JDE users can start working with Java code efficiently.</p>
 */
package com.garrettwu.maven.plugins.jde;

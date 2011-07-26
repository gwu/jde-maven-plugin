// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.util.Collection;

/**
 * Builds JDE Project files (prj.el files).
 */
public class JdeProjectFileBuilder {
  /** The maven project dependencies. */
  private Collection<ProjectDependency> mDependencies;

  /**
   * Creates a new <code>JdeProjectFileBuilder</code> instance.
   */
  public JdeProjectFileBuilder() {
    mDependencies = null;
  }

  /**
   * Sets the maven dependencies for the project.
   *
   * @return This builder instance so you can chain configuration method calls.
   */
  public JdeProjectFileBuilder withDependencies(Collection<ProjectDependency> dependencies) {
    // TODO
    return this;
  }

  /**
   * Builds a JDE project file.
   *
   * @return A new JDE project file.
   */
  public JdeProjectFile build() {
    // TODO
    return null;
  }
}

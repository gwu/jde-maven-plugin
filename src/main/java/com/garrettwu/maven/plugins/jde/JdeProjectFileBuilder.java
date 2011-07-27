// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * Builds JDE Project files (prj.el files).
 */
public class JdeProjectFileBuilder {
  /** The version of maven project files this build can create. */
  private static final String VERSION = "1.0";

  /** The maven project. */
  private MavenProject mMavenProject;

  /** The maven project dependencies. */
  private Collection<ProjectDependency> mDependencies;

  /**
   * Creates a new <code>JdeProjectFileBuilder</code> instance.
   */
  public JdeProjectFileBuilder() {
    mMavenProject = null;
    mDependencies = null;
  }

  /**
   * Sets the maven project.
   *
   * @param mavenProject The maven project.
   * @return This builder instance so you can chain configuration method calls.
   */
  public JdeProjectFileBuilder withMavenProject(MavenProject mavenProject) {
    mMavenProject = mavenProject;
    return this;
  }

  /**
   * Sets the maven dependencies for the project.
   *
   * @return This builder instance so you can chain configuration method calls.
   */
  public JdeProjectFileBuilder withDependencies(Collection<ProjectDependency> dependencies) {
    mDependencies = dependencies;
    return this;
  }

  /**
   * Builds a JDE project file.
   *
   * @return A new JDE project file.
   */
  public JdeProjectFile build() {
    if (null == mMavenProject) {
      throw new RuntimeException("Must set maven project");
    }
    if (null == mDependencies) {
      throw new RuntimeException("Must set dependencies");
    }

    List<String> sourcePaths = new ArrayList<String>();
    List<String> classPaths = new ArrayList<String>();
    List<String> javadocPaths = new ArrayList<String>();

    // Add paths for local project.
    sourcePaths.add(mMavenProject.getBuild().getSourceDirectory());
    sourcePaths.add(mMavenProject.getBuild().getTestSourceDirectory());
    classPaths.add(mMavenProject.getBuild().getOutputDirectory());
    classPaths.add(mMavenProject.getBuild().getTestOutputDirectory());

    // Add paths for project dependencies.
    for (ProjectDependency dependency : mDependencies) {
      // Add source path.
      if (dependency.isSourceAvailable()) {
        sourcePaths.add(dependency.getSourcePath());
      }

      // Add class path.
      classPaths.add(dependency.getClassPath());

      // Add javadoc path.
      if (dependency.isJavadocAvailable()) {
        javadocPaths.add(dependency.getJavadocPath());
      }
    }
    return new JdeProjectFile(VERSION, sourcePaths, classPaths, javadocPaths);
  }
}

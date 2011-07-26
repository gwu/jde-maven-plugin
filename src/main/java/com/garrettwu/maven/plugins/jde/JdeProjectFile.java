// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.util.List;

/**
 * A JDE project file (prj.el file).
 */
public class JdeProjectFile {
  /** The jde project file version. */
  private final String mVersion;

  /** The list of paths to source used by the maven project. */
  private final List<String> mSourcePaths;

  /** The list of paths to classes and jars used by the maven project. */
  private final List<String> mClassPaths;

  /** The list of paths/urls to javadoc describing code used by the maven project. */
  private final List<String> mJavadocPaths;

  /**
   * Creates a new <code>JdeProjectFile</code> instance.
   *
   * @param version The version of the prj.el file.
   * @param sourcePaths A list of paths to source code.
   * @param classPaths A list of paths to classes/jars.
   * @param javadocPaths A list of paths or urls to javadoc.
   */
  public JdeProjectFile(String version,
      List<String> sourcePaths, List<String> classPaths, List<String> javadocPaths) {
    mVersion = version;
    mSourcePaths = sourcePaths;
    mClassPaths = classPaths;
    mJavadocPaths = javadocPaths;
  }

  /**
   * Gets the jde project file version.
   *
   * @return The jde project file version.
   */
  public String getVersion() {
    return mVersion;
  }

  /**
   * Gets a list of paths to the source code used by the maven project.
   *
   * @return A list of paths.
   */
  public List<String> getSourcePaths() {
    return mSourcePaths;
  }

  /**
   * Gets a list of paths to the classes and jars used by the maven project.
   *
   * @return A list of paths.
   */
  public List<String> getClassPaths() {
    return mClassPaths;
  }

  /**
   * Gets a list of paths to the javadoc used by the maven project (may be urls).
   *
   * @return A list of paths and/or urls.
   */
  public List<String> getJavadocPaths() {
    return mJavadocPaths;
  }
}

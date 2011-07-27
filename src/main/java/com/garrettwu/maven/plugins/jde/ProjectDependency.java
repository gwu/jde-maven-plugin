// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.Artifact;

/**
 * A project dependency declared as part of a maven project.
 */
public class ProjectDependency {
  /** The groupId of the dependency. */
  private final String mGroupId;

  /** The artifactId of the dependency. */
  private final String mArtifactId;

  /** The version of the dependency. */
  private final String mVersion;

  /** The path to the source, or null if not known. */
  private final String mSourcePath;

  /** The path to the classes or jar. */
  private final String mClassPath;

  /** The path or url to the javadocs, or null if not known. */
  private final String mJavadocPath;

  /**
   * Creates a new <code>ProjectDependency</code> instance.
   *
   * @param groupId The dependency's groupId.
   * @param artifactId The dependency's artifactId.
   * @param version The dependency's version.
   * @param sourcePath Path to the source, or null if not available.
   * @param classPath Path to the classes or jar.
   * @param javadocPath Path or URL to the javadoc, or null if not available.
   */
  public ProjectDependency(String groupId, String artifactId, String version,
      String sourcePath, String classPath, String javadocPath) {
    mGroupId = groupId;
    mArtifactId = artifactId;
    mVersion = version;
    mSourcePath = sourcePath;
    mClassPath = classPath;
    mJavadocPath = javadocPath;
  }

  /**
   * Gets the groupId of the dependency.
   *
   * @return The groupId of the dependency.
   */
  public String getGroupId() {
    return mGroupId;
  }

  /**
   * Gets the artifactId of the dependency.
   *
   * @return The artifactId of the dependency.
   */
  public String getArtifactId() {
    return mArtifactId;
  }

  /**
   * Gets the version of the dependency.
   *
   * @return The version of the dependency.
   */
  public String getVersion() {
    return mVersion;
  }

  /**
   * Determines whether the path to the source for this dependency is available.
   *
   * @return Whether the source path is available.
   */
  public boolean isSourceAvailable() {
    return null != mSourcePath;
  }

  /**
   * Gets the path to the source code for this dependency.
   *
   * @return The path to the source.
   */
  public String getSourcePath() {
    return mSourcePath;
  }

  /**
   * Gets the path to the classes or the jar for this dependency.
   *
   * @return The path to the directory of classes or the path to the jar.
   */
  public String getClassPath() {
    return mClassPath;
  }

  /**
   * Determines whether the javadoc location is known.
   *
   * @return Whether the javadoc location is known.
   */
  public boolean isJavadocAvailable() {
    return null != mJavadocPath;
  }

  /**
   * Gets the location of the javadoc for this dependency.
   *
   * @return A path or a url to the javadoc.
   */
  public String getJavadocPath() {
    return mJavadocPath;
  }

  @Override
  public String toString() {
    return mGroupId + ":" + mArtifactId + ":" + mVersion;
  }
}

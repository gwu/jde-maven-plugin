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

  /**
   * Creates a new <code>ProjectDependency</code> instance.
   *
   * @param groupId The dependency's groupId.
   * @param artifactId The dependency's artifactId.
   * @param version The dependency's version.
   */
  public ProjectDependency(String groupId, String artifactId, String version) {
    mGroupId = groupId;
    mArtifactId = artifactId;
    mVersion = version;
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
}

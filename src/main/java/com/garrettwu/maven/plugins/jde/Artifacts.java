// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.Artifact;

/**
 * Utility methods for working with maven artifacts.
 */
public final class Artifacts {
  /** No constructor available for a utility class. */
  private Artifacts() {}

  /**
   * The canonical name of the artifact, consisting of just the "groupId:artifactId:version".
   *
   * @param artifact An artifact.
   * @return Its canonical name.
   */
  public static String getName(Artifact artifact) {
    return new StringBuilder()
        .append(artifact.getGroupId())
        .append(":")
        .append(artifact.getArtifactId())
        .append(":")
        .append(artifact.getVersion())
        .toString();
  }
}

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.Artifact;

/**
 * A factory capable of creating project dependencies from a maven project.
 */
public class ProjectDependencyFactory {
  /**
   * Creates a new <code>ProjectDependencyFactory</code> instance.
   */
  public ProjectDependencyFactory() {
  }

  /**
   * Creates a project dependency out of a maven artifact.
   *
   * @param artifact A maven artifact.
   * @return A project dependency.
   */
  public ProjectDependency createFromArtifact(Artifact artifact) {
    return new ProjectDependency(artifact.getGroupId(), artifact.getId(), artifact.getVersion());
  }
}

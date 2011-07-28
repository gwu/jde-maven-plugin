// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Reads dependencies from a maven project.
 */
public class ProjectDependencyReader extends MavenClient {
  /** A factory for creating project dependencies from artifacts. */
  private final ProjectDependencyFactory mProjectDependencyFactory;

  /**
   * Creates a new <code>ProjectDependencyReader</code> instance.
   *
   * @param mavenEnvironment The maven environment.
   * @param projectDependencyFactory A factory for creating project dependencies from artifacts.
   */
  public ProjectDependencyReader(MavenEnvironment mavenEnvironment,
      ProjectDependencyFactory projectDependencyFactory) {
    super(mavenEnvironment);
    mProjectDependencyFactory = projectDependencyFactory;
  }

  /**
   * Gets the transitive dependencies for the project.
   *
   * @return The collection of transitive dependencies for the project.
   * @throws MojoExecutionException If there is an error while getting dependencies.
   */
  public Collection<ProjectDependency> getDependencies() throws MojoExecutionException {
    // The following method is lazy when getting artifacts.  It will only return
    // dependency artifacts for scopes that have already been run.  For example, if you've
    // only run 'mvn compile', this method won't return test-scope dependencies.
    //
    // TODO: Should we explicitly run the compile phase before we call this method?
    Set<?> dependencyArtifacts = getCurrentProject().getDependencyArtifacts();

    // Convert the artifacts to project dependencies.
    Collection<ProjectDependency> dependencies
        = new ArrayList<ProjectDependency>(dependencyArtifacts.size());
    for (Object dependencyArtifact : dependencyArtifacts) {
      if (!(dependencyArtifact instanceof Artifact)) {
        throw new MojoExecutionException("Found unexpected type "
            + dependencyArtifact.getClass().getName()
            + " in result from MavenProject.getArtifacts().");
      }
      ProjectDependency dependency
          = mProjectDependencyFactory.createFromArtifact((Artifact) dependencyArtifact);
      if (null != dependency) {
        dependencies.add(dependency);
      }
    }
    return dependencies;
  }
}

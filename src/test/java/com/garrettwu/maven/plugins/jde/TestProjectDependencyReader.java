// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class TestProjectDependencyReader {
  @Test
  public void testGetDependencies() throws MojoExecutionException {
    // Create mocks.
    MockMavenEnvironment mavenEnvironment = new MockMavenEnvironment();
    Artifact artifact1 = createMock(Artifact.class);
    Artifact artifact2 = createMock(Artifact.class);
    ProjectDependencyFactory projectDependencyFactory = createMock(ProjectDependencyFactory.class);

    // Set mock expectations.
    Set<Artifact> directArtifacts = new HashSet<Artifact>();
    Set<Artifact> transitiveArtifacts = new HashSet<Artifact>();
    directArtifacts.add(artifact1);
    transitiveArtifacts.add(artifact1);
    transitiveArtifacts.add(artifact2);
    expect(mavenEnvironment.getCurrentProject().getDependencyArtifacts())
        .andReturn(directArtifacts);
    expect(mavenEnvironment.getCurrentProject().getArtifacts())
        .andReturn(transitiveArtifacts);
    ProjectDependency dependency1 = new ProjectDependency("a", "b", "c", "x", "y", "z");
    ProjectDependency dependency2 = new ProjectDependency("d", "e", "f", "u", "v", "w");
    expect(projectDependencyFactory.createFromArtifact(artifact1))
        .andReturn(dependency1)
        .times(2);
    expect(projectDependencyFactory.createFromArtifact(artifact2))
        .andReturn(dependency2);

    mavenEnvironment.replay();
    replay(artifact1);
    replay(artifact2);
    replay(projectDependencyFactory);

    // Get the dependencies.
    ProjectDependencyReader dependencyReader
        = new ProjectDependencyReader(mavenEnvironment, projectDependencyFactory);
    Collection<ProjectDependency> directDependencies = dependencyReader.getDependencies(false);
    Collection<ProjectDependency> transitiveDependencies = dependencyReader.getDependencies(true);

    mavenEnvironment.verify();
    verify(artifact1);
    verify(artifact2);
    verify(projectDependencyFactory);

    // Verify the dependencies.
    assertEquals(1, directDependencies.size());
    assertTrue(directDependencies.contains(dependency1));

    assertEquals(2, transitiveDependencies.size());
    assertTrue(transitiveDependencies.contains(dependency1));
    assertTrue(transitiveDependencies.contains(dependency2));
  }
}

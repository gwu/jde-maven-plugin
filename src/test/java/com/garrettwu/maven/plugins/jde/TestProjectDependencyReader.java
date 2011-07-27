// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class TestProjectDependencyReader {
  @Test
  public void testGetDependencies() throws MojoExecutionException {
    // Create mocks.
    Log log = createMock(Log.class);
    MavenProject mavenProject = createMock(MavenProject.class);
    Artifact artifact1 = createMock(Artifact.class);
    Artifact artifact2 = createMock(Artifact.class);
    ProjectDependencyFactory projectDependencyFactory = createMock(ProjectDependencyFactory.class);

    // Set mock expectations.
    Set<Artifact> cannedArtifacts = new HashSet<Artifact>();
    cannedArtifacts.add(artifact1);
    cannedArtifacts.add(artifact2);
    expect(mavenProject.getArtifacts()).andReturn(cannedArtifacts).anyTimes();
    ProjectDependency dependency1 = new ProjectDependency("a", "b", "c", "x", "y", "z");
    ProjectDependency dependency2 = new ProjectDependency("d", "e", "f", "u", "v", "w");
    expect(projectDependencyFactory.createFromArtifact(artifact1)).andReturn(dependency1);
    expect(projectDependencyFactory.createFromArtifact(artifact2)).andReturn(dependency2);

    replay(mavenProject);
    replay(artifact1);
    replay(artifact2);
    replay(projectDependencyFactory);
    replay(log);

    // Get the dependencies.
    ProjectDependencyReader dependencyReader
        = new ProjectDependencyReader(log, mavenProject, projectDependencyFactory);
    Collection<ProjectDependency> dependencies = dependencyReader.getDependencies();

    verify(mavenProject);
    verify(artifact1);
    verify(artifact2);
    verify(projectDependencyFactory);
    verify(log);

    // Verify the dependencies.
    assertEquals(2, dependencies.size());
    assertTrue(dependencies.contains(dependency1));
    assertTrue(dependencies.contains(dependency2));
  }
}

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class TestProjectDependencyFactory {
  @Test
  public void testCreateFromArtifact() {
    Log log = createMock(Log.class);
    ArtifactFactory artifactFactory = createMock(ArtifactFactory.class);
    ArtifactResolver artifactResolver = createMock(ArtifactResolver.class);
    ArtifactRepository localArtifactRepository = createMock(ArtifactRepository.class);
    MavenProject mavenProject = createMock(MavenProject.class);
    Artifact artifact = createMock(Artifact.class);

    // Set mock expectations.
    expect(artifact.getGroupId()).andReturn("groupId").anyTimes();
    expect(artifact.getId()).andReturn("artifactId").anyTimes();
    expect(artifact.getVersion()).andReturn("version").anyTimes();

    replay(log);
    replay(artifactFactory);
    replay(artifactResolver);
    replay(localArtifactRepository);
    replay(mavenProject);
    replay(artifact);
    ProjectDependencyFactory factory = new ProjectDependencyFactory(
        log, artifactFactory, artifactResolver, localArtifactRepository, mavenProject);
    ProjectDependency dependency = factory.createFromArtifact(artifact);
    verify(log);
    verify(artifactFactory);
    verify(artifactResolver);
    verify(localArtifactRepository);
    verify(mavenProject);
    verify(artifact);

    assertEquals("groupId", dependency.getGroupId());
    assertEquals("artifactId", dependency.getArtifactId());
    assertEquals("version", dependency.getVersion());
  }
}

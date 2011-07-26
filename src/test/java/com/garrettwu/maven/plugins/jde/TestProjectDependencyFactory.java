// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.maven.artifact.Artifact;
import org.junit.Test;

public class TestProjectDependencyFactory {
  @Test
  public void testCreateFromArtifact() {
    Artifact artifact = createMock(Artifact.class);

    // Set mock expectations.
    expect(artifact.getGroupId()).andReturn("groupId").anyTimes();
    expect(artifact.getId()).andReturn("artifactId").anyTimes();
    expect(artifact.getVersion()).andReturn("version").anyTimes();

    replay(artifact);
    ProjectDependencyFactory factory = new ProjectDependencyFactory();
    ProjectDependency dependency = factory.createFromArtifact(artifact);
    verify(artifact);

    assertEquals("groupId", dependency.getGroupId());
    assertEquals("artifactId", dependency.getArtifactId());
    assertEquals("version", dependency.getVersion());
  }
}

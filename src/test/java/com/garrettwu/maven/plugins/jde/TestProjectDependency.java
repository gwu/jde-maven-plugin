// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.maven.artifact.Artifact;
import org.junit.Test;

public class TestProjectDependency {
  @Test
  public void testConstructor() {
    ProjectDependency dependency = new ProjectDependency(
        "groupId", "artifactId", "version", "source", "jar", "doc");
    assertEquals("groupId", dependency.getGroupId());
    assertEquals("artifactId", dependency.getArtifactId());
    assertEquals("version", dependency.getVersion());
    assertTrue(dependency.isSourceAvailable());
    assertEquals("source", dependency.getSourcePath());
    assertEquals("jar", dependency.getClassPath());
    assertTrue(dependency.isJavadocAvailable());
    assertEquals("doc", dependency.getJavadocPath());
  }
}

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.maven.artifact.Artifact;
import org.junit.Test;

public class TestProjectDependency {
  @Test
  public void testConstructor() {
    ProjectDependency dependency = new ProjectDependency("groupId", "artifactId", "version");
    assertEquals("groupId", dependency.getGroupId());
    assertEquals("artifactId", dependency.getArtifactId());
    assertEquals("version", dependency.getVersion());
  }
}

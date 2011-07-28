// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class TestJdeProjectFileBuilder {
  @Test
  public void testBuild() {
    // Construct mocks.
    MavenProject mavenProject = createMock(MavenProject.class);
    Build projectBuild = createMock(Build.class);

    // Set mock expectations.
    expect(mavenProject.getBuild()).andReturn(projectBuild).anyTimes();
    expect(projectBuild.getSourceDirectory()).andReturn("src/main/java");
    expect(projectBuild.getTestSourceDirectory()).andReturn("src/test/java");
    expect(projectBuild.getOutputDirectory()).andReturn("target/classes");
    expect(projectBuild.getTestOutputDirectory()).andReturn("target/test-classes");

    // Construct the builder.
    JdeProjectFileBuilder builder = new JdeProjectFileBuilder();

    replay(mavenProject);
    replay(projectBuild);

    // Configure the builder.
    builder.withMavenProject(mavenProject);
    builder.withDependencies(Arrays.asList(new ProjectDependency("a", "b", "b", "d", "e", "f")));

    // Build a project file.
    JdeProjectFile projectFile = builder.build();

    verify(mavenProject);
    verify(projectBuild);

    // Verify project file.
    assertEquals(JdeProjectFileBuilder.VERSION, projectFile.getVersion());
    assertEquals(3, projectFile.getSourcePaths().size());
    assertTrue(projectFile.getSourcePaths().contains("src/main/java"));
    assertTrue(projectFile.getSourcePaths().contains("src/test/java"));
    assertTrue(projectFile.getSourcePaths().contains("d"));
    assertEquals(3, projectFile.getClassPaths().size());
    assertTrue(projectFile.getClassPaths().contains("target/classes"));
    assertTrue(projectFile.getClassPaths().contains("target/test-classes"));
    assertTrue(projectFile.getClassPaths().contains("e"));
    assertEquals(1, projectFile.getJavadocPaths().size());
    assertTrue(projectFile.getJavadocPaths().contains("f"));
  }
}

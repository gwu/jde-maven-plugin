// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class TestJdeProjectFileBuilder {
  @Test
  public void testBuild() {
    // Construct the builder.
    JdeProjectFileBuilder builder = new JdeProjectFileBuilder();

    // Configure the builder.
    Collection<ProjectDependency> dependencies = new ArrayList<ProjectDependency>();
    dependencies.add(new ProjectDependency("a", "b", "c"));
    builder.withDependencies(dependencies);

    // Build a project file.
    JdeProjectFile projectFile = builder.build();

    // TODO
  }
}

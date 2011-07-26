// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestJdeProjectFile {
  @Test
  public void testConstructor() {
    JdeProjectFile projectFile = new JdeProjectFile(
        "version", Arrays.asList("source"), Arrays.asList("class"), Arrays.asList("doc"));

    assertEquals("version", projectFile.getVersion());
    assertEquals("source", projectFile.getSourcePaths().get(0));
    assertEquals("class", projectFile.getClassPaths().get(0));
    assertEquals("doc", projectFile.getJavadocPaths().get(0));
  }
}

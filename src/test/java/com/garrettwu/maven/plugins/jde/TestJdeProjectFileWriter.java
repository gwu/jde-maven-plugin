// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestJdeProjectFileWriter {
  /** Path to an expected project file output. */
  private static final String EXPECTED_PROJECT_FILE
    = "com/garrettwu/maven/plugins/jde/prj.el.expected";

  @Test
  public void testWrite() throws IOException {
    JdeProjectFile projectFile = createMock(JdeProjectFile.class);

    // Set mock expectations.
    expect(projectFile.getVersion())
        .andReturn("1.0")
        .anyTimes();
    expect(projectFile.getSourcePaths())
        .andReturn(Arrays.asList("/my/source", "/your/source"))
        .anyTimes();
    expect(projectFile.getClassPaths())
        .andReturn(Arrays.asList("/my/classes", "/my/jar"))
        .anyTimes();
    expect(projectFile.getJavadocPaths())
        .andReturn(Arrays.asList("/a/path", "http://a.url"))
        .anyTimes();

    replay(projectFile);

    // Write a project file.
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JdeProjectFileWriter writer = new JdeProjectFileWriter(outputStream);
    writer.write(projectFile);
    writer.close();

    verify(projectFile);

    // Verify the written file.
    String actualFileOutput = outputStream.toString("UTF-8");
    String expectedFileOutput = getExpectedProjectFileOutput();
    assertEquals(expectedFileOutput, actualFileOutput);
  }

  /**
   * Reads the expected project file output from the test resource file.
   */
  private String getExpectedProjectFileOutput() throws IOException {
    try {
      return FileUtils.readFileToString(
          new File(getClass().getClassLoader().getResource(EXPECTED_PROJECT_FILE).toURI()));
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }
}

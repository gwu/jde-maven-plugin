// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TestUserPathMapping {
  /** An example properties file. */
  public static final String PROPERTIES
      = "org.foo\\:foo\\:1.0 = /path/to/foo\n"
      + "org.bar\\:bar\\:2.0 = /path/to/bar\n";

  @Test
  public void testLoad() throws IOException {
    UserPathMapping mapping = new UserPathMapping();
    assertFalse(mapping.contains("org.foo:foo:1.0"));

    InputStream propertiesStream = new ByteArrayInputStream(PROPERTIES.getBytes("UTF-8"));
    mapping.load(propertiesStream);

    assertTrue(mapping.contains("org.foo:foo:1.0"));
    assertEquals("/path/to/foo", mapping.get("org.foo:foo:1.0"));

    assertTrue(mapping.contains("org.bar:bar:2.0"));
    assertEquals("/path/to/bar", mapping.get("org.bar:bar:2.0"));
  }
}

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Defines a mapping between artifact names and paths specified by the user.
 */
public class UserPathMapping {
  /** A mapping from artifact name to user defined path. */
  private final Map<String, String> mArtifactToPathMap;

  /**
   * Creates a new <code>UserPathMapping</code> instance with no mappings.
   */
  public UserPathMapping() {
    mArtifactToPathMap = new HashMap<String, String>();
  }

  /**
   * Loads this mapping from artifacts to paths from an input stream.
   *
   * <p>The format of the contents of the stream should be a java properties file where
   * the key is an artifact name and the value is a path.</p>
   *
   * @param inputStream The contents of a java properties file.
   * @throws IOException If there is an error reading from the stream or the contents has
   *     an invalid format.
   */
  public void load(InputStream inputStream) throws IOException {
    Properties properties = new Properties();
    properties.load(inputStream);
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      mArtifactToPathMap.put(entry.getKey().toString(), entry.getValue().toString());
    }
  }

  /**
   * Determines whether an artifact has a user path mapping.
   *
   * @param artifactName The name of the artifact.
   * @return Whether it has a user path mapping.
   */
  public boolean contains(String artifactName) {
    return mArtifactToPathMap.containsKey(artifactName);
  }

  /**
   * Returns the path mapped by the artifact.
   *
   * @param artifactName The name of the artifact.
   * @return The user path mapped by the artifact, or null if there is no mapping.
   */
  public String get(String artifactName) {
    return mArtifactToPathMap.get(artifactName);
  }
}

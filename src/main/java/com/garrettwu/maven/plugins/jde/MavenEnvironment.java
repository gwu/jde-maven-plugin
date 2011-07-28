// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Encapsulates the maven environment in which a plugin is being run.
 */
public interface MavenEnvironment {
  /**
   * Provides access to the logger that reports to the maven user.
   *
   * @return The maven logger.
   */
  Log getLog();

  /**
   * Provides access to the logger that reports to the maven user.
   *
   * @return The current maven project.
   */
  MavenProject getCurrentProject();

  /**
   * Provides access to the maven artifact factory.
   *
   * @return The artifact factory.
   */
  ArtifactFactory getArtifactFactory();

  /**
   * Provides access to the maven utility that resolves artifacts (downloads them to the
   * local repository if necessary).
   *
   * @return The artifact resolver.
   */
  ArtifactResolver getArtifactResolver();

  /**
   * Provides access to the local maven repository.
   *
   * @return The local maven repository for artifacts.
   */
  ArtifactRepository getLocalArtifactRepository();
}

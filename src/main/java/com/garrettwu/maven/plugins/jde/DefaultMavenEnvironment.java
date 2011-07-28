// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * The default implementation of a maven environment.
 */
public class DefaultMavenEnvironment implements MavenEnvironment {
  /** The maven logger. */
  private final Log mLog;

  /** The current maven project. */
  private final MavenProject mCurrentProject;

  /** The maven artifact factory. */
  private final ArtifactFactory mArtifactFactory;

  /** The maven artifact resolver. */
  private final ArtifactResolver mArtifactResolver;

  /** The local maven repository of artifacts. */
  private final ArtifactRepository mLocalArtifactRepository;

  /**
   * Creates a new <code>DefaultMavenEnvironment</code> instance.
   *
   * @param log The maven logger.
   * @param currentProject The current maven project.
   * @param artifactFactory The maven artifact factory.
   * @param artifactResolver The maven artifact resolver.
   * @param localArtifactRepository The local maven repository of artifacts.
   */
  public DefaultMavenEnvironment(Log log, MavenProject currentProject,
      ArtifactFactory artifactFactory, ArtifactResolver artifactResolver,
      ArtifactRepository localArtifactRepository) {
    mLog = log;
    mCurrentProject = currentProject;
    mArtifactFactory = artifactFactory;
    mArtifactResolver = artifactResolver;
    mLocalArtifactRepository = localArtifactRepository;
  }

  /** {@inheritDoc} */
  @Override
  public Log getLog() {
    return mLog;
  }

  /** {@inheritDoc} */
  @Override
  public MavenProject getCurrentProject() {
    return mCurrentProject;
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactFactory getArtifactFactory() {
    return mArtifactFactory;
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactResolver getArtifactResolver() {
    return mArtifactResolver;
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactRepository getLocalArtifactRepository() {
    return mLocalArtifactRepository;
  }
}

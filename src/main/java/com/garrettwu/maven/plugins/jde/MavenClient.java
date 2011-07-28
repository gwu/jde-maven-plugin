// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;

/**
 * Abstract base class for objects that run inside a maven environment.
 */
public abstract class MavenClient extends MavenLogged implements MavenEnvironment {
  /** The maven environment. */
  private final MavenEnvironment mMavenEnvironment;

  /**
   * Creates a new <code>MavenClient</code> instance.
   *
   * @param mavenEnvironment The maven environment.
   */
  protected MavenClient(MavenEnvironment mavenEnvironment) {
    super(mavenEnvironment.getLog());
    mMavenEnvironment = mavenEnvironment;
  }

  /**
   * Gets the maven environment.
   *
   * @return The maven environment.
   */
  protected MavenEnvironment getMavenEnvironment() {
    return mMavenEnvironment;
  }

  /** {@inheritDoc} */
  @Override
  public MavenProject getCurrentProject() {
    return getMavenEnvironment().getCurrentProject();
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactFactory getArtifactFactory() {
    return getMavenEnvironment().getArtifactFactory();
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactResolver getArtifactResolver() {
    return getMavenEnvironment().getArtifactResolver();
  }

  /** {@inheritDoc} */
  @Override
  public ArtifactRepository getLocalArtifactRepository() {
    return getMavenEnvironment().getLocalArtifactRepository();
  }
}

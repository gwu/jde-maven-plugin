// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.easymock.EasyMock;

/**
 * A mock implementation of a maven environment using EasyMock.
 *
 * <p>Each of the components of the maven environment (the log, current project, etc.) are
 * implemented as EasyMock objects.</p>
 */
public class MockMavenEnvironment extends DefaultMavenEnvironment {
  /**
   * Creates a new <code>MockMavenEnvironment</code> instance.
   */
  public MockMavenEnvironment() {
    super(EasyMock.createMock(Log.class), EasyMock.createMock(MavenProject.class),
        EasyMock.createMock(ArtifactFactory.class), EasyMock.createMock(ArtifactResolver.class),
        EasyMock.createMock(ArtifactRepository.class));
  }

  /**
   * Prepares the mocks for expected calls.
   */
  public void replay() {
    EasyMock.replay(getLog());
    EasyMock.replay(getCurrentProject());
    EasyMock.replay(getArtifactFactory());
    EasyMock.replay(getArtifactResolver());
    EasyMock.replay(getLocalArtifactRepository());
  }

  /**
   * Verifies that all expected calls were made to the mocks.
   */
  public void verify() {
    EasyMock.verify(getLog());
    EasyMock.verify(getCurrentProject());
    EasyMock.verify(getArtifactFactory());
    EasyMock.verify(getArtifactResolver());
    EasyMock.verify(getLocalArtifactRepository());
  }
}

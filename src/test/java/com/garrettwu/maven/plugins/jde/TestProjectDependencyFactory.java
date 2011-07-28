// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

public class TestProjectDependencyFactory {
  /** A temporary directory to store test files. */
  private File mTempDir;

  @Before
  public void setup() throws IOException {
    mTempDir = Files.createTempDir();
  }

  @After
  public void teardown() throws IOException {
    Files.deleteRecursively(mTempDir);
  }

  @Test
  public void testCreateFromArtifact() throws AbstractArtifactResolutionException {
    // Construct mocks.
    MockMavenEnvironment mavenEnvironment = new MockMavenEnvironment();
    Artifact artifact = createMock(Artifact.class);
    Artifact sourceArtifact = createMock(Artifact.class);
    Artifact javadocArtifact = createMock(Artifact.class);
    ArtifactRepository remoteArtifactRepository = createMock(ArtifactRepository.class);
    List<ArtifactRepository> remoteArtifactRepositories = Arrays.asList(remoteArtifactRepository);

    // Set mock expectations.
    expect(artifact.getGroupId()).andReturn("groupId").anyTimes();
    expect(artifact.getArtifactId()).andReturn("artifactId").anyTimes();
    expect(artifact.getVersion()).andReturn("version").anyTimes();
    expect(mavenEnvironment.getCurrentProject().getRemoteArtifactRepositories())
        .andReturn(remoteArtifactRepositories)
        .anyTimes();

    // Source artifact expectations.
    expect(mavenEnvironment.getArtifactFactory().createArtifactWithClassifier(
            "groupId", "artifactId", "version", "java-source", "sources"))
        .andReturn(sourceArtifact);
    mavenEnvironment.getArtifactResolver().resolve(sourceArtifact,
        remoteArtifactRepositories, mavenEnvironment.getLocalArtifactRepository());
    expect(sourceArtifact.getFile()).andReturn(new File("/to/some/sources.jar"));

    // Class artifact expectations.
    expect(artifact.getFile()).andReturn(new File("/to/some/classes.jar"));

    // Javadoc artifact expectations.
    expect(mavenEnvironment.getArtifactFactory().createArtifactWithClassifier(
            "groupId", "artifactId", "version", "java-source", "javadoc"))
        .andReturn(javadocArtifact);
    mavenEnvironment.getArtifactResolver().resolve(javadocArtifact,
        remoteArtifactRepositories, mavenEnvironment.getLocalArtifactRepository());
    expect(javadocArtifact.getFile()).andReturn(null);
    mavenEnvironment.getLog().info(
        "No javadoc jar file found for artifact: " + artifact.toString());

    mavenEnvironment.replay();
    replay(artifact);
    replay(sourceArtifact);
    replay(javadocArtifact);
    replay(remoteArtifactRepository);

    // Create the project dependency.
    File javadocDir = new File(mTempDir, "javadocs");
    ProjectDependencyFactory factory = new ProjectDependencyFactory(
        mavenEnvironment, javadocDir, new UserPathMapping());
    ProjectDependency dependency = factory.createFromArtifact(artifact);

    mavenEnvironment.verify();
    verify(artifact);
    verify(sourceArtifact);
    verify(javadocArtifact);
    verify(remoteArtifactRepository);

    // Verify the constructed project dependency.
    assertEquals("groupId", dependency.getGroupId());
    assertEquals("artifactId", dependency.getArtifactId());
    assertEquals("version", dependency.getVersion());
    assertEquals("/to/some/sources.jar", dependency.getSourcePath());
    assertEquals("/to/some/classes.jar", dependency.getClassPath());
  }
}

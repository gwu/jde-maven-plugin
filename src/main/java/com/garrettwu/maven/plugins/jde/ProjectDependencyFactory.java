// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * A factory capable of creating project dependencies from a maven project.
 */
public class ProjectDependencyFactory {
  /** The maven logger. */
  private final Log mLog;

  /** A factory for maven artifacts. */
  private final ArtifactFactory mArtifactFactory;

  /** A utility that resolves artifacts (possibly downloading into the local repository). */
  private final ArtifactResolver mArtifactResolver;

  /** The local maven repository. */
  private final ArtifactRepository mLocalArtifactRepository;

  /** The maven project. */
  private final MavenProject mMavenProject;

  /**
   * Creates a new <code>ProjectDependencyFactory</code> instance.
   *
   * @param log The maven logger.
   * @param artifactFactory A maven artifact factory.
   * @param artifactResolver A maven artifact resolver.
   */
  public ProjectDependencyFactory(Log log, ArtifactFactory artifactFactory,
      ArtifactResolver artifactResolver, ArtifactRepository localArtifactRepository,
      MavenProject mavenProject) {
    mLog = log;
    mArtifactFactory = artifactFactory;
    mArtifactResolver = artifactResolver;
    mLocalArtifactRepository = localArtifactRepository;
    mMavenProject = mavenProject;
  }

  /**
   * Creates a project dependency out of a maven artifact.
   *
   * @param artifact A maven artifact.
   * @return A project dependency.
   */
  public ProjectDependency createFromArtifact(Artifact artifact) {
    String groupId = artifact.getGroupId();
    String artifactId = artifact.getId();
    String version = artifact.getVersion();
    String classPath = getClassPath(artifact);
    if (null == groupId || null == artifactId || null == version || null == classPath) {
      return null;
    }
    return new ProjectDependency(groupId, artifactId, version,
        getSourcePath(artifact), classPath, getJavadocPath(artifact));
  }

  /**
   * Gets the path to the source code for an artifact.
   *
   * @param artifact An artifact.
   * @return The path to the source for the artifact, or null if unknown.
   */
  private String getSourcePath(Artifact artifact) {
    // Get the sources jar artifact.
    Artifact sourcesJarArtifact = mArtifactFactory.createArtifactWithClassifier(
        artifact.getGroupId(), artifact.getId(), artifact.getVersion(),
        "jar", "sources");
    // Resolve it.
    try {
      mArtifactResolver.resolve(sourcesJarArtifact,
          mMavenProject.getRemoteArtifactRepositories(),
          mLocalArtifactRepository);
    } catch (AbstractArtifactResolutionException e) {
      mLog.info("Unable to find sources for artifact: " + artifact.toString());
    }
    File file = sourcesJarArtifact.getFile();
    if (null == file) {
      mLog.info("No sources jar file found for artifact: " + artifact.toString());
      return null;
    }
    return file.getPath();
  }

  /**
   * Gets the path to the classes or jar for an artifact.
   *
   * @param artifact An artifact.
   * @return The path to the jar or the directory containing the class files.
   */
  private static String getClassPath(Artifact artifact) {
    File file = artifact.getFile();
    if (null == file) {
      return null;
    }
    return file.getPath();
  }

  /**
   * Gets the path or url to the javadoc for an artifact.
   *
   * @param artifact An artifact.
   * @return A url or path to the javadoc (or null if unknown).
   */
  private static String getJavadocPath(Artifact artifact) {
    // TODO
    return null;
  }
}

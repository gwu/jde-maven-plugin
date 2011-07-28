// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;

/**
 * A factory capable of creating project dependencies from a maven project.
 */
public class ProjectDependencyFactory extends MavenClient {
  /** The directory where javadoc should be unpacked. */
  private final File mJavadocDir;

  /**
   * Creates a new <code>ProjectDependencyFactory</code> instance.
   *
   * @param mavenEnvironment The maven environment.
   * @param javadocDir The directory to unpack javadoc contents to.
   */
  public ProjectDependencyFactory(MavenEnvironment mavenEnvironment, File javadocDir) {
    super(mavenEnvironment);
    mJavadocDir = javadocDir;
  }

  /**
   * Creates a project dependency out of a maven artifact.
   *
   * @param artifact A maven artifact.
   * @return A project dependency.
   */
  public ProjectDependency createFromArtifact(Artifact artifact) {
    String groupId = artifact.getGroupId();
    String artifactId = artifact.getArtifactId();
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
    Artifact sourcesJarArtifact = getArtifactFactory().createArtifactWithClassifier(
        artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
        "java-source", "sources");
    // Resolve it.
    try {
      getArtifactResolver().resolve(sourcesJarArtifact,
          getCurrentProject().getRemoteArtifactRepositories(),
          getLocalArtifactRepository());
    } catch (AbstractArtifactResolutionException e) {
      getLog().info("Unable to find sources for artifact: " + artifact.toString());
    }
    File file = sourcesJarArtifact.getFile();
    if (null == file) {
      getLog().info("No sources jar file found for artifact: " + artifact.toString());
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
  private String getJavadocPath(Artifact artifact) {
    // Get the javadoc jar artifact.
    Artifact javadocJarArtifact = getArtifactFactory().createArtifactWithClassifier(
        artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
        "java-source", "javadoc");
    // Resolve it.
    try {
      getArtifactResolver().resolve(javadocJarArtifact,
          getCurrentProject().getRemoteArtifactRepositories(),
          getLocalArtifactRepository());
    } catch (AbstractArtifactResolutionException e) {
      getLog().info("Unable to find javadoc for artifact: " + artifact.toString());
    }
    File file = javadocJarArtifact.getFile();
    if (null == file || !file.exists()) {
      getLog().info("No javadoc jar file found for artifact: " + artifact.toString());
      return null;
    }

    File artifactJavadocDir = new File(mJavadocDir, artifact.getArtifactId());
    try {
      unpackJar(file, artifactJavadocDir);
    } catch (IOException e) {
      getLog().error("Unable to unpack javadoc jar for artifact: " + artifact.toString(), e);
    }

    return artifactJavadocDir.getPath();
  }

  /**
   * Unpacks a jar file into a target directory.
   *
   * @param jarFile The jar to unpack.
   * @param targetDirectory The directory to put the unpacked contents in.
   * @throws IOException If there is an error.
   */
  private void unpackJar(File jarFile, File targetDirectory) throws IOException {
    if (!targetDirectory.mkdirs()) {
      throw new IOException("Could not create directory: " + targetDirectory.getPath());
    }
    getLog().info("Unpacking javadoc jar " + jarFile.getPath()
        + " into " + targetDirectory.getPath());
    JarFile jar = new JarFile(jarFile);
    for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
      JarEntry entry = entries.nextElement();
      File outputFile = new File(targetDirectory, entry.getName());
      if (entry.isDirectory()) {
        if (!outputFile.mkdirs()) {
          throw new IOException("Could not make directory: " + outputFile.getPath());
        }
      } else {
        FileUtils.copyInputStreamToFile(jar.getInputStream(entry), outputFile);
      }
    }
    jar.close();
  }
}

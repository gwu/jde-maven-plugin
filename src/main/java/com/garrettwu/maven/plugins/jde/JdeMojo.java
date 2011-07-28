// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which generates a prj.el file used by the Emacs JDE package.
 *
 * @goal jde
 * @requiresDependencyResolution compile
 */
public class JdeMojo extends AbstractMojo {
  /**
   * A factory for maven artifacts.
   *
   * @component
   */
  private ArtifactFactory mArtifactFactory;

  /**
   * A utility that resolves artifacts (possibly downloading into the local repository).
   *
   * @component
   */
  private ArtifactResolver mArtifactResolver;

  /**
   * The local maven repository.
   *
   * @parameter default-value="${localRepository}"
   * @required
   * @readonly
   */
  private ArtifactRepository mLocalArtifactRepository;

  /**
   * The maven project this mojo is working on.
   *
   * @parameter property="mavenProject" default-value="${project}"
   * @required
   * @readonly
   */
  private MavenProject mMavenProject;

  /**
   * Location of the prj.el file to generate.
   *
   * @parameter property="projectFile" expression="${jde.project.file}" default-value="${basedir}/prj.el"
   * @required
   */
  private File mProjectFile;

  /**
   * Directory where javadocs for dependencies should unpacked.
   *
   * @parameter property="javadocDir" expression="${jde.javadoc.dir}" default-value="${basedir}/.jde/javadoc"
   * @required
   */
  private File mJavadocDir;

  /**
   * Sets the maven project this mojo works over.
   *
   * <p>The plugin framework will call this method with the correct maven project.</p>
   *
   * @param mavenProject The maven project.
   */
  public void setMavenProject(MavenProject mavenProject) {
    mMavenProject = mavenProject;
  }

  /**
   * Sets the output prj.el file this plugin should write.
   *
   * <p>The plugin framework will call this method with the setting from the project's
   * configuration.</p>
   *
   * @param projectFile The project file to write.
   */
  public void setProjectFile(File projectFile) {
    mProjectFile = projectFile;
  }

  /**
   * The directory used to unpack the javadoc for project dependencies.
   *
   * @param javadocDir The directory to unpack javadoc into.
   */
  public void setJavadocDir(File javadocDir) {
    mJavadocDir = javadocDir;
  }

  /** {@inheritDoc} */
  @Override
  public void execute() throws MojoExecutionException {
    // Make sure we know where to generate the jde project file.
    if (null == mProjectFile) {
      throw new MojoExecutionException("Required property ${jde.project.file} was not set.");
    }
    // Make sure we know where to unpack javadoc files.
    if (null == mJavadocDir) {
      throw new MojoExecutionException("Required property ${jde.javadoc.dir} was not set.");
    }

    getLog().debug("Reading local maven environment...");
    MavenEnvironment mavenEnvironment = new DefaultMavenEnvironment(
        getLog(), mMavenProject, mArtifactFactory, mArtifactResolver, mLocalArtifactRepository);

    getLog().debug("Resolving project dependencies...");
    ProjectDependencyFactory dependencyFactory
        = new ProjectDependencyFactory(mavenEnvironment, mJavadocDir);
    ProjectDependencyReader dependencyReader
        = new ProjectDependencyReader(mavenEnvironment, dependencyFactory);
    Collection<ProjectDependency> dependencies = dependencyReader.getDependencies();

    getLog().debug("Building a JDE project file...");
    JdeProjectFileBuilder jdeProjectFileBuilder = new JdeProjectFileBuilder()
        .withMavenProject(mMavenProject)
        .withDependencies(dependencies);
    JdeProjectFile jdeProjectFile = jdeProjectFileBuilder.build();

    getLog().debug("Writing the JDE project file...");
    try {
      writeProjectFile(jdeProjectFile, mProjectFile);
    } catch (IOException e) {
      throw new MojoExecutionException(
          "Error writing project file to " + mProjectFile.getPath(), e);
    }
  }

  /**
   * Writes a JDE project file to the filesystem at a specified location.
   *
   * @param projectFile The JDE project file to write.
   * @param targetFile The target file in the filesystem.
   * @throws IOException If there is an error.
   */
  private static void writeProjectFile(JdeProjectFile projectFile, File targetFile)
      throws IOException {
    JdeProjectFileWriter writer = null;
    try {
      writer = new JdeProjectFileWriter(new FileOutputStream(targetFile));
      writer.write(projectFile);
    } finally {
      if (null != writer) {
        writer.close();
      }
    }
  }
}

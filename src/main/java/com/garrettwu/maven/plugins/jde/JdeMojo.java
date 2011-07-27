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
   * @readonly
   */
  private File mProjectFile;

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

  /** {@inheritDoc} */
  @Override
  public void execute() throws MojoExecutionException {
    // Make sure we know where to generate the jde project file.
    if (null == mProjectFile) {
      throw new MojoExecutionException("Required property ${jde.project.file} was not set.");
    }

    // Get the list of dependencies for the project.
    ProjectDependencyFactory projectDependencyFactory = new ProjectDependencyFactory(
        getLog(), mArtifactFactory, mArtifactResolver, mLocalArtifactRepository, mMavenProject);
    ProjectDependencyReader dependencyReader
        = new ProjectDependencyReader(getLog(), mMavenProject, projectDependencyFactory);
    Collection<ProjectDependency> dependencies = dependencyReader.getDependencies();

    // Resolve the dependencies' sources.
    getLog().debug("Dependencies:");
    for (ProjectDependency dependency : dependencies) {
      getLog().debug(dependency.toString());
    }


    // Build a project file.
    JdeProjectFileBuilder projectFileBuilder = new JdeProjectFileBuilder()
        .withMavenProject(mMavenProject)
        .withDependencies(dependencies);
    JdeProjectFile projectFile = projectFileBuilder.build();

    // Write the project file.
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(mProjectFile);
      JdeProjectFileWriter projectFileWriter = new JdeProjectFileWriter(outputStream);
      projectFileWriter.write(projectFile);
      projectFileWriter.close();
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to write project file", e);
    } finally {
      if (null != outputStream) {
        try {
          outputStream.close();
        } catch (IOException e) {
          throw new MojoExecutionException("Unable to close project file", e);
        }
      }
    }
  }
}

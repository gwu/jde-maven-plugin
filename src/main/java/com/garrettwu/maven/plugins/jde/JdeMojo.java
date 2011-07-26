// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which generates a prj.el file used by the Emacs JDE package.
 *
 * <p>The prj.el file is like an eclipse .project file that contains metadata about the project,
 * such as the classpath, where to find source code and javadocs for dependencies, etc.</p>
 *
 * @goal jde
 */
public class JdeMojo extends AbstractMojo {
  /**
   * The maven project this mojo is working on.
   *
   * @parameter property="mavenProject" default-value="${project}"
   */
  private MavenProject mMavenProject;

  /**
   * Location of the prj.el file to generate.
   *
   * @parameter property="projectFile" expression="jde.project.file" default-value="${basedir}/prj.el"
   * @required
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
      throw new MojoExecutionException("Required property jde.project.file was not set.");
    }

    // Create parent directories as needed.
    if (!mProjectFile.exists()) {
      mProjectFile.mkdirs();
    }

    // Get the list of dependencies for the project.
    ProjectDependencyReader dependencyReader = new ProjectDependencyReader(mMavenProject);
    Collection<ProjectDependency> dependencies = dependencyReader.getDependencies();

    // Build a project file.
    JdeProjectFileBuilder projectFileBuilder = new JdeProjectFileBuilder()
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

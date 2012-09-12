// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which generates a prj.el file used by the Emacs JDE package.
 *
 * @goal jde
 * @requiresDependencyResolution test
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
   * A java properties file containing a mapping from groupId:artifactId:version to its javadoc.
   *
   * @parameter property="javadocPathsFile" expression="${jde.javadoc.paths}" default-value="${env.HOME}/.jde/javadoc.properties"
   * @required
   */
  private File mJavadocPathsFile;

  /**
   * Include transitive dependencies in generated prj.el.
   *
   * @parameter property="transitiveMode" expression="${jde.transitive.mode}" default-value="false"
   * @required
   */
  private boolean mTransitiveMode;

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

  /**
   * The java properties file containing a mapping from artifact name
   * (groupId:artifactId:version) to its javadoc location.
   *
   * <p>The javadoc location may be a filesystem path or a URL.</p>
   *
   * @param javadocPathsFile a <code>File</code> value
   */
  public void setJavadocPathsFile(File javadocPathsFile) {
    mJavadocPathsFile = javadocPathsFile;
  }

  /**
   * Determines whether to include transitive dependencies in generated prj.el
   *
   * @param transitiveMode whether transitive mode should be enabled
   */
  public void setTransitiveMode(boolean transitiveMode) {
    mTransitiveMode = transitiveMode;
  }

  /**
   * Executes the plugin's goal to generate a JDE project file.
   *
   * @throws MojoExecutionException If there is a fatal error during execution of the plugin.
   */
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

    UserPathMapping javadocUserPathMapping = new UserPathMapping();
    if (null != mJavadocPathsFile && mJavadocPathsFile.exists()) {
      getLog().info("Loading user path mapping for javadocs: " + mJavadocPathsFile.getPath());
      try {
        loadUserPathMapping(javadocUserPathMapping, mJavadocPathsFile);
      } catch (IOException e) {
        throw new MojoExecutionException(
            "Unable to load javadoc paths from " + mJavadocPathsFile.getPath(), e);
      }
    } else {
      getLog().debug("No user path mapping for javadocs specified.");
    }

    getLog().info("Resolving project dependencies...");
    ProjectDependencyFactory dependencyFactory
        = new ProjectDependencyFactory(mavenEnvironment, mJavadocDir, javadocUserPathMapping);
    ProjectDependencyReader dependencyReader
        = new ProjectDependencyReader(mavenEnvironment, dependencyFactory);
    Collection<ProjectDependency> dependencies =
      dependencyReader.getDependencies(mTransitiveMode);

    getLog().info("Building a JDE project file...");
    JdeProjectFileBuilder jdeProjectFileBuilder = new JdeProjectFileBuilder()
        .withMavenProject(mMavenProject)
        .withDependencies(dependencies);
    JdeProjectFile jdeProjectFile = jdeProjectFileBuilder.build();

    getLog().info("Writing the JDE project file...");
    try {
      writeProjectFile(jdeProjectFile, mProjectFile);
    } catch (IOException e) {
      throw new MojoExecutionException(
          "Error writing project file to " + mProjectFile.getPath(), e);
    }
  }

  /**
   * Loads a user path file.
   *
   * @param userPathMapping The target mapping for the loaded file.
   * @param userPathFile The file containing the mapping to load.
   * @return The user path mapping loaded from the file.
   * @throws IOException If there is an error.
   */
  private static void loadUserPathMapping(UserPathMapping userPathMapping, File userPathFile)
      throws IOException {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(userPathFile);
      userPathMapping.load(fileInputStream);
    } finally {
      if (null != fileInputStream) {
        fileInputStream.close();
      }
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

// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import com.garrettwu.lisp.LispElement;
import com.garrettwu.lisp.LispIdentifier;
import com.garrettwu.lisp.LispList;
import com.garrettwu.lisp.LispQuotedElement;
import com.garrettwu.lisp.LispString;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Writes JDE project files (prj.el files) to an output stream.
 */
public class JdeProjectFileWriter implements Closeable {
  /** The print writer connected to the output stream. */
  private final PrintWriter mPrintWriter;

  /**
   * Creates a project file writer that sends output to a given <code>outputStream</code>.
   *
   * @param outputStream The output stream to write the project file to.
   */
  public JdeProjectFileWriter(OutputStream outputStream) {
    mPrintWriter = new PrintWriter(outputStream);
  }

  /**
   * Writes a project file to the writer's output stream.
   *
   * @param projectFile The project file to write.
   * @throws IOException If there is an error.
   */
  public void write(JdeProjectFile projectFile) throws IOException {
    // Print the version element.
    LispElement versionElement = new LispList(
        Arrays.<LispElement>asList(
            new LispIdentifier("jde-project-file-version"),
            new LispString("1.0")));
    mPrintWriter.println(versionElement.toString());

    // Set variables.
    LispElement setVariablesElement = new LispList(
        Arrays.<LispElement>asList(
            new LispIdentifier("jde-set-variables"),

            // Print the source paths.
            new LispQuotedElement(
                new LispList(
                    Arrays.<LispElement>asList(
                        new LispIdentifier("jde-sourcepath"),
                        new LispList(
                            Arrays.<LispElement>asList(
                                new LispIdentifier("append"),
                                new LispIdentifier("jde-sourcepath"),
                                new LispQuotedElement(
                                    new LispList(
                                        Lists.transform(projectFile.getSourcePaths(),
                                            new StringToLispTransform())))))))),

            // Print the class paths.
            new LispQuotedElement(
                new LispList(
                    Arrays.<LispElement>asList(
                        new LispIdentifier("jde-global-classpath"),
                        new LispList(
                            Arrays.<LispElement>asList(
                                new LispIdentifier("append"),
                                new LispIdentifier("jde-global-classpath"),
                                new LispQuotedElement(
                                    new LispList(
                                        Lists.transform(projectFile.getClassPaths(),
                                            new StringToLispTransform())))))))),

            // Print the javadoc paths.
            new LispQuotedElement(
                new LispList(
                    Arrays.<LispElement>asList(
                        new LispIdentifier("jde-help-docsets"),
                        new LispList(
                            Arrays.<LispElement>asList(
                                new LispIdentifier("append"),
                                new LispIdentifier("jde-help-docsets"),
                                new LispQuotedElement(
                                    new LispList(
                                        Lists.transform(projectFile.getJavadocPaths(),
                                            new JdeJavadocLispTransform()))))))))));

    // Print out the variables
    mPrintWriter.println(setVariablesElement.toString());
  }

  /**
   * Closes the writer and all resources associated with it (will close the underlying
   * output stream).
   *
   * @throws IOException If there is an error.
   */
  @Override
  public void close() throws IOException {
    mPrintWriter.close();
  }

  /** {@inheritDoc} */
  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }

  /**
   * A function that transforms a String into a Lisp string.
   */
  private static class StringToLispTransform implements Function<String, LispElement> {
    /** {@inheritDoc} */
    @Override
    public LispElement apply(String from) {
      return new LispString(from);
    }
  }

  /**
   * A function that transforms a path to a javadoc directory into a Lisp element
   * suitable for the JDE javadoc variable setting in emacs.
   */
  private static class JdeJavadocLispTransform implements Function<String, LispElement> {
    /**
     * Each javadoc path string must be transformed into an lisp list for JDE.
     *
     * <pre>
     * ("User (javadoc)" "/path/to/javadoc" nil)
     * </pre>
     *
     * @param javadocDirPath A path to a directory of javadoc files.
     * @return A Lisp element that the JDE emacs package can read.
     */
    @Override
    public LispElement apply(String javadocDirPath) {
      return new LispList(
          Arrays.<LispElement>asList(
              new LispString("User (javadoc)"),
              new LispString(javadocDirPath),
              new LispIdentifier("nil")));
    }
  }
}

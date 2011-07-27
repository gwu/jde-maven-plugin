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
                                            new Function<String, LispElement>() {
                                              @Override
                                              public LispElement apply(String from) {
                                                return new LispString(from);
                                              }
                                            })))))))),

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
                                            new Function<String, LispElement>() {
                                              @Override
                                              public LispElement apply(String from) {
                                                return new LispString(from);
                                              }
                                            })))))))),

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
                                            new Function<String, LispElement>() {
                                              @Override
                                              public LispElement apply(String from) {
                                                return new LispString(from);
                                              }
                                            }))))))))));

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
  public void finalize() throws Throwable {
    close();
    super.finalize();
  }
}

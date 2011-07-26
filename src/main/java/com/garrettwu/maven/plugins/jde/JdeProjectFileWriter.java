// (c) Copyright 2011 Garrett Wu

package com.garrettwu.maven.plugins.jde;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes JDE project files (prj.el files) to an output stream.
 */
public class JdeProjectFileWriter implements Closeable {
  /** The output stream to write project files to. */
  private final OutputStream mOutputStream;

  /**
   * Creates a project file writer that sends output to a given <code>outputStream</code>.
   *
   * @param outputStream The output stream to write the project file to.
   */
  public JdeProjectFileWriter(OutputStream outputStream) {
    mOutputStream = outputStream;
  }

  /**
   * Writes a project file to the writer's output stream.
   *
   * @param projectFile The project file to write.
   * @throws IOException If there is an error.
   */
  public void write(JdeProjectFile projectFile) throws IOException {
    // TODO
  }

  /** {@inheritDoc} */
  @Override
  public void close() throws IOException {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public void finalize() throws Throwable {
    close();
    super.finalize();
  }
}

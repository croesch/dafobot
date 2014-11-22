package de.croesch.dafobot.work.sort_text_components;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.croesch.dafobot.core.Text;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Editor_TestCase {

  protected Text textOf(final Path path) throws IOException {
    return new Text(new String(Files.readAllBytes(path)));
  }

  protected List<Path> allPathFiles(final String folder) throws IOException {
    final List<Path> allPaths = new ArrayList<>();

    for (final String pathString : System.getProperty("java.class.path").split(File.pathSeparator)) {
      final Path path = Paths.get(pathString);
      allPaths.addAll(allPathFiles(path, folder));
    }

    return allPaths;
  }

  private Collection<? extends Path> allPathFiles(final Path path, final String folder) throws IOException {
    final List<Path> allPaths = new ArrayList<>();

    if (Files.isDirectory(path)) {
      try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
        for (final Path file : directoryStream) {
          allPaths.addAll(allPathFiles(file, folder));
        }
      }
    } else if (Files.isRegularFile(path) && path.toString().contains(folder) && /*
                                                                                 * seems like a bug: endsWith does not
                                                                                 * work
                                                                                 */path.toString().endsWith(".before")) {
      allPaths.add(path);
    }
    return allPaths;
  }

}

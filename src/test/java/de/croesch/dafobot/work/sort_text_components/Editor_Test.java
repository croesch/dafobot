package de.croesch.dafobot.work.sort_text_components;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.croesch.dafobot.work.api.NoEditNeededException;

/**
 * Provides black box tests for {@link Editor}.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Editor_Test extends Editor_TestCase {

  @Test
  public void should_Edit_Page_So_That_Result_Equals_Result_File() throws IOException {
    final List<Path> testFiles = allPathFiles("/sort_text_components/beforeafter/");
    for (final Path path : testFiles) {
      final String result = new Editor().doSpecialEdit(path.toString(), stringOf(path));
      assertThat(result).as(path.toString()).isEqualTo(stringOf(afterPath(path)));
    }
    System.out.println("SUCCES testing " + testFiles.size() + " test files.");
  }

  @Test
  public void should_Throw_NoEditNeededException_For_These_Articles() throws IOException {
    final List<Path> testFiles = allPathFiles("/sort_text_components/noeditneeded/");
    for (final Path path : testFiles) {
      try {
        new Editor().doSpecialEdit(path.toString(), stringOf(path));
        Assertions.fail("Didn't throw exception for: " + path.toString());
      } catch (final NoEditNeededException exception) {
        // good!
      }
    }
    System.out.println("SUCCES testing " + testFiles.size() + " test files.");
  }

  private Path afterPath(final Path path) {
    return Paths.get(path.toString().substring(0, path.toString().length() - 7) + ".after");
  }
}

package de.croesch.dafobot.work.sort_text_components;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    for (final Path path : allPathFiles("/sort_text_components/beforeafter/")) {
      final String result = new Editor().doSpecialEdit(path.toString(), stringOf(path));
      assertThat(result).as(path.toString()).isEqualTo(stringOf(afterPath(path)));
    }
  }

  @Test
  public void should_Throw_NoEditNeededException_For_These_Articles() throws IOException {
    for (final Path path : allPathFiles("/sort_text_components/noeditneeded/")) {
      try {
        new Editor().doSpecialEdit(path.toString(), stringOf(path));
        Assertions.fail("Didn't throw exception for: " + path.toString());
      } catch (final NoEditNeededException exception) {
        // good!
      }
    }
  }

  private Path afterPath(final Path path) {
    return Paths.get(path.toString().substring(0, path.toString().length() - 7) + ".after");
  }
}

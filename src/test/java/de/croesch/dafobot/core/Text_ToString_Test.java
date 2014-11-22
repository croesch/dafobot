package de.croesch.dafobot.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Contains tests for {@link Text#toString()}.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class Text_ToString_Test {

  @Test
  public void should_Return_Text_Without_Comment() {
    assertThat(new Text("").toString()).isEqualTo("");
    assertThat(new Text("<!-->").toString()).isEqualTo("");
    assertThat(new Text("<!--->").toString()).isEqualTo("");
    assertThat(new Text("<!---->").toString()).isEqualTo("");
    assertThat(new Text("A<!---->B").toString()).isEqualTo("AB");
    assertThat(new Text("A<!-- -->B").toString()).isEqualTo("AB");
    assertThat(new Text("A<!-- B -->C").toString()).isEqualTo("AC");
    assertThat(new Text("<!--").toString()).isEqualTo("");
    assertThat(new Text("-->").toString()).isEqualTo("-->");
    assertThat(new Text("<!--<!-- -->").toString()).isEqualTo("");
    assertThat(new Text("A").toString()).isEqualTo("A");
    assertThat(new Text("ABC").toString()).isEqualTo("ABC");
    assertThat(
               new Text("{{<!-- bla -->G<!-- bla -->e<!-- bla -->g<!-- bla -->e<!-- bla -->n<!-- bla -->w<!-- bla -->ö<!-- bla -->r<!-- bla -->t<!-- bla -->e<!-- bla -->r<!-- bla -->}}").toString()).isEqualTo("{{Gegenwörter}}");
  }
}

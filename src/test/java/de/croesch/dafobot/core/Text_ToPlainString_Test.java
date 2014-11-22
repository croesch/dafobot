package de.croesch.dafobot.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Contains tests for {@link Text#toPlainString()}.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class Text_ToPlainString_Test {

  @Test
  public void should_Return_Text_Set_Via_Constructor() {
    assertThat(new Text("").toPlainString()).isEqualTo("");
    assertThat(new Text("<!-->").toPlainString()).isEqualTo("<!-->");
    assertThat(new Text("<!--->").toPlainString()).isEqualTo("<!--->");
    assertThat(new Text("<!---->").toPlainString()).isEqualTo("<!---->");
    assertThat(new Text("A<!---->B").toPlainString()).isEqualTo("A<!---->B");
    assertThat(new Text("A<!-- -->B").toPlainString()).isEqualTo("A<!-- -->B");
    assertThat(new Text("A<!-- B -->C").toPlainString()).isEqualTo("A<!-- B -->C");
    assertThat(new Text("<!--").toPlainString()).isEqualTo("<!--");
    assertThat(new Text("-->").toPlainString()).isEqualTo("-->");
    assertThat(new Text("<!--<!-- -->").toPlainString()).isEqualTo("<!--<!-- -->");
    assertThat(new Text("A").toPlainString()).isEqualTo("A");
    assertThat(new Text("ABC").toPlainString()).isEqualTo("ABC");
  }
}

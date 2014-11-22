package de.croesch.dafobot.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Contains tests for {@link Text#substring(int, int)}.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class Text_Substring_Int_Int_Test {

  @Test(expected = IndexOutOfBoundsException.class)
  public void should_Throw_Error_If_From_Index_Is_Less_Than_Zero() {
    new Text("abc").substring(-1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void should_Throw_Error_If_From_Index_Is_Higher_Than_To_Index() {
    new Text("abc").substring(1, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void should_Throw_Error_If_To_Index_Is_Higher_Than_Length() {
    new Text("abc").substring(0, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void should_Throw_Error_If_To_Index_Is_Higher_Than_Length_Using_A_Comment() {
    new Text("<!-- -->").substring(0, 1);
  }

  @Test
  public void should_Return_Substring_Based_On_Commentless_String() {
    Text sut = new Text("");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("");
    sut = new Text("<!-->");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("<!-->");
    sut = new Text("<!--->");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("<!--->");
    sut = new Text("<!---->");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("<!---->");
    sut = new Text("A<!---->B");
    assertThat(new Text("A<!---->B").toString()).isEqualTo("AB");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("");
    assertThat(sut.substring(0, 1).toPlainString()).isEqualTo("A<!---->");
    assertThat(sut.substring(0, 2).toPlainString()).isEqualTo("A<!---->B");
    assertThat(sut.substring(2, 2).toPlainString()).isEqualTo("");
    assertThat(sut.substring(1, 2).toPlainString()).isEqualTo("B");
    sut = new Text("<!--");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("<!--");
    sut = new Text("-->");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("");
    assertThat(sut.substring(0, 1).toPlainString()).isEqualTo("-");
    assertThat(sut.substring(0, 2).toPlainString()).isEqualTo("--");
    assertThat(sut.substring(0, 3).toPlainString()).isEqualTo("-->");
    assertThat(sut.substring(1, 1).toPlainString()).isEqualTo("");
    assertThat(sut.substring(1, 2).toPlainString()).isEqualTo("-");
    assertThat(sut.substring(1, 3).toPlainString()).isEqualTo("->");
    assertThat(sut.substring(2, 2).toPlainString()).isEqualTo("");
    assertThat(sut.substring(2, 3).toPlainString()).isEqualTo(">");
    assertThat(sut.substring(3, 3).toPlainString()).isEqualTo("");
    sut = new Text("<!--<!-- -->");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("<!--<!-- -->");
    sut = new Text("A");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("");
    assertThat(sut.substring(0, 1).toPlainString()).isEqualTo("A");
    assertThat(sut.substring(1, 1).toPlainString()).isEqualTo("");
    sut = new Text("ABC");
    assertThat(sut.substring(0, 0).toPlainString()).isEqualTo("");
    assertThat(sut.substring(0, 1).toPlainString()).isEqualTo("A");
    assertThat(sut.substring(0, 2).toPlainString()).isEqualTo("AB");
    assertThat(sut.substring(0, 3).toPlainString()).isEqualTo("ABC");
    assertThat(sut.substring(1, 1).toPlainString()).isEqualTo("");
    assertThat(sut.substring(1, 2).toPlainString()).isEqualTo("B");
    assertThat(sut.substring(1, 3).toPlainString()).isEqualTo("BC");
    assertThat(sut.substring(2, 2).toPlainString()).isEqualTo("");
    assertThat(sut.substring(2, 3).toPlainString()).isEqualTo("C");
    assertThat(sut.substring(3, 3).toPlainString()).isEqualTo("");
  }
}

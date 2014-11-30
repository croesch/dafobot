package de.croesch.dafobot.work;

import static org.assertj.core.api.Assertions.assertThat;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

import org.junit.Test;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 30, 2014
 */
public class GeneralPageEditabilityChecker_Test {

  @Test
  public void should_Return_False_If_inuse_Is_Contained() {
    assertThat(canEdit("abc{{inuse}}abc")).isFalse();
    assertThat(canEdit("abc{{inuse}}")).isFalse();
    assertThat(canEdit("{{inuse}}abc")).isFalse();
    assertThat(canEdit("abc {{inuse}} abc")).isFalse();
    assertThat(canEdit("{{inuse}}")).isFalse();
  }

  @Test
  public void should_Return_False_If_Inuse_Is_Contained() {
    assertThat(canEdit("abc{{Inuse}}abc")).isFalse();
    assertThat(canEdit("abc{{Inuse}}")).isFalse();
    assertThat(canEdit("{{Inuse}}abc")).isFalse();
    assertThat(canEdit("abc {{Inuse}} abc")).isFalse();
    assertThat(canEdit("{{Inuse}}")).isFalse();
  }

  @Test
  public void should_Return_False_If_inuseBot_Is_Contained() {
    assertThat(canEdit("abc{{inuseBot}}abc")).isFalse();
    assertThat(canEdit("abc{{inuseBot}}")).isFalse();
    assertThat(canEdit("{{inuseBot}}abc")).isFalse();
    assertThat(canEdit("abc {{inuseBot}} abc")).isFalse();
    assertThat(canEdit("{{inuseBot}}")).isFalse();
  }

  @Test
  public void should_Return_False_If_InuseBot_Is_Contained() {
    assertThat(canEdit("abc{{InuseBot}}abc")).isFalse();
    assertThat(canEdit("abc{{InuseBot}}")).isFalse();
    assertThat(canEdit("{{InuseBot}}abc")).isFalse();
    assertThat(canEdit("abc {{InuseBot}} abc")).isFalse();
    assertThat(canEdit("{{InuseBot}}")).isFalse();
  }

  @Test
  public void should_Return_False_If_in_Bearbeitung_Is_Contained() {
    assertThat(canEdit("abc{{in Bearbeitung}}abc")).isFalse();
    assertThat(canEdit("abc{{in Bearbeitung}}")).isFalse();
    assertThat(canEdit("{{in Bearbeitung}}abc")).isFalse();
    assertThat(canEdit("abc {{in Bearbeitung}} abc")).isFalse();
    assertThat(canEdit("{{in Bearbeitung}}")).isFalse();
  }

  @Test
  public void should_Return_False_If_In_Bearbeitung_Is_Contained() {
    assertThat(canEdit("abc{{In Bearbeitung}}abc")).isFalse();
    assertThat(canEdit("abc{{In Bearbeitung}}")).isFalse();
    assertThat(canEdit("{{In Bearbeitung}}abc")).isFalse();
    assertThat(canEdit("abc {{In Bearbeitung}} abc")).isFalse();
    assertThat(canEdit("{{In Bearbeitung}}")).isFalse();
  }

  private boolean canEdit(final String string) {
    final SimpleArticle article = new SimpleArticle("Test");
    article.setText(string);
    return new GeneralPageEditabilityChecker().canEdit(article);
  }
}

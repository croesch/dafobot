package de.croesch.dafobot.work.sort_text_components.sort;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

public class Test_PartSorter_Sort {

  private static final String SPANISCH = "== abdico ({{Sprache|Spanisch}}) ==";

  private static final String DEUTSCH = "== abdico ({{Sprache|Deutsch}}) ==";

  private static final String LATEIN = "== abdīcō ({{Sprache|Latein}}) ==";

  private static final String INTERNATIONAL = "== abdicō ({{Sprache|International}}) ==";

  private static final String KATALANISCH = "== abdico ({{Sprache|Katalanisch}}) ==";

  private static final String ALBANISCH = "== abdico ({{Sprache|Albanisch}}) ==";

  private static final String CONTENT1 = ".. content 1..";

  private static final String CONTENT2 = ".. content 2..";

  private static final String CONTENT3 = ".. content 3..";

  private static final String CONTENT4 = ".. content 4..";

  private static final String CONTENT5 = ".. content 5..";

  private static final String CONTENT6 = ".. content 6..";

  private static final String NN = "\n\n";

  private final PartSorter sut = new PartSorter(new SubPartSorter(new ComponentSorter()));

  private final Collection<String> additionalActions = new ArrayList<String>();

  @Test
  public void should_Sort_Germany_Then_International_Than_Alphabetical_At_Top() throws NoEditNeededException,
                                                                               PageNeedsQAException {
    final Text text = new Text(ALBANISCH + NN + KATALANISCH + NN + INTERNATIONAL + NN + DEUTSCH + NN + LATEIN + NN
                               + SPANISCH + NN);
    final Text result = this.sut.sort(text, this.additionalActions);

    assertThat(result.toPlainString()).isEqualTo(DEUTSCH + NN + INTERNATIONAL + NN + ALBANISCH + NN + KATALANISCH + NN
                                                 + LATEIN + NN + SPANISCH);
  }

  @Test
  public void should_Sort_Content_With_Headers_At_Top() throws NoEditNeededException, PageNeedsQAException {
    final Text text = new Text(KATALANISCH + NN + CONTENT2 + NN + SPANISCH + NN + CONTENT6 + NN + LATEIN + NN
                               + CONTENT5 + NN + ALBANISCH + NN + CONTENT1 + NN);
    final Text result = this.sut.sort(text, this.additionalActions);

    assertThat(result.toPlainString()).isEqualTo(ALBANISCH + NN + CONTENT1 + NN + KATALANISCH + NN + CONTENT2 + NN
                                                 + LATEIN + NN + CONTENT5 + NN + SPANISCH + NN + CONTENT6);
  }

  @Test
  public void should_Ignore_Trailing_And_Leading_Whitespaces() throws NoEditNeededException, PageNeedsQAException {
    final String header1 = "== abdico ({{Sprache|    Spanisch}}) ==";
    final String header2 = "== abdico ({{Sprache| Türkisch  }}) ==";
    final String header3 = "== abdico ({{Sprache|   Deutsch            }}) ==";
    final Text text = new Text(header1 + NN + CONTENT1 + NN + header2 + NN + CONTENT2 + NN + header3 + NN + CONTENT3
                               + NN);
    final Text result = this.sut.sort(text, this.additionalActions);

    assertThat(result.toPlainString()).isEqualTo(header3 + NN + CONTENT3 + NN + header1 + NN + CONTENT1 + NN + header2
                                                 + NN + CONTENT2);
  }

  @Test
  public void should_Accept_Whitespaces_In_Language() throws NoEditNeededException, PageNeedsQAException {
    final String header1 = "== abdico ({{Sprache|Die gute Sprache}}) ==";
    final String header2 = "== abdico ({{Sprache|Die schlechte Sprache}}) ==";
    final String header3 = "== abdico ({{Sprache|Die neue Sprache}}) ==";
    final Text text = new Text(header1 + NN + CONTENT1 + NN + header2 + NN + CONTENT2 + NN + header3 + NN + CONTENT3
                               + NN);
    final Text result = this.sut.sort(text, this.additionalActions);

    assertThat(result.toPlainString()).isEqualTo(header1 + NN + CONTENT1 + NN + header3 + NN + CONTENT3 + NN + header2
                                                 + NN + CONTENT2);
  }
}

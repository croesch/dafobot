package de.croesch.dafobot.work.sort_text_components;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import de.croesch.dafobot.core.Text;
import de.croesch.dafobot.work.api.NoEditNeededException;
import de.croesch.dafobot.work.api.PageNeedsQAException;

/**
 * Provides black box tests for {@link Editor}.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Editor_Test extends Editor_TestCase {

  private static final String BEFORE_AFTER_DIR = "/sort_text_components/beforeafter/";

  private static final String NO_EDIT_NEEDED_DIR = "/sort_text_components/noeditneeded/";

  private static final String QA_NEEDED_DIR = "/sort_text_components/qaneeded/";

  @Test
  public void should_Order_Vorname_Article_Correctly() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "vorname");
  }

  @Test
  public void should_Order_Nachname_Article_Correctly() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "nachname");
  }

  @Test
  public void should_Order_Text_Article_Correctly() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "text");
  }

  @Test
  public void should_Order_Complex_Text_Article_Correctly() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "text-complex");
  }

  @Test
  public void should_Order_Components_Article_In_Both_Parts() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "two-parts");
  }

  @Test
  public void should_Order_Components_Article_In_Both_SubParts() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "two-subparts");
  }

  @Test
  public void should_Order_Simple_Article_1() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "simple1");
  }

  @Test
  public void should_Order_Simple_Article_2() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "simple2");
  }

  @Test
  public void should_Remove_Empty_Template_1() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "empty-template-1");
  }

  @Test
  public void should_Remove_Empty_Template_2() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "empty-template-2");
  }

  @Test
  public void should_Accept_Duplicate_Anmerkungen_And_Insert_It_In_Original_Order_1() throws IOException,
                                                                                     URISyntaxException {
    compare(BEFORE_AFTER_DIR + "duplicate-anmerkung-1");
  }

  @Test
  public void should_Accept_Duplicate_Anmerkungen_And_Insert_It_In_Original_Order_2() throws IOException,
                                                                                     URISyntaxException {
    compare(BEFORE_AFTER_DIR + "duplicate-anmerkung-2");
  }

  @Test
  public void should_Replace_Old_Name_Variant_Templates_In_Male_Article() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "vorname-m-old");
  }

  @Test
  public void should_Replace_Old_Name_Variant_Templates_In_Female_Article() throws IOException, URISyntaxException {
    compare(BEFORE_AFTER_DIR + "vorname-f-old");
  }

  @Test(expected = NoEditNeededException.class)
  public void should_Throw_NoEditNeededException_If_No_Components_Available() throws IOException, URISyntaxException {
    edit(NO_EDIT_NEEDED_DIR + "no-components");
  }

  @Test(expected = NoEditNeededException.class)
  public void should_Throw_NoEditNeededException_If_Components_Are_Already_Ordered_Correctly() throws IOException,
                                                                                              URISyntaxException {
    edit(NO_EDIT_NEEDED_DIR + "correct-order");
  }

  @Test(expected = PageNeedsQAException.class)
  public void should_Throw_PageNeedsQAException_If_Component_Comes_Twice_Consecutive() throws IOException,
                                                                                      URISyntaxException {
    edit(QA_NEEDED_DIR + "duplicate-component-1");
  }

  @Test(expected = PageNeedsQAException.class)
  public void should_Throw_PageNeedsQAException_If_Components_Exists_Twice_At_Different_Positions() throws IOException,
                                                                                                   URISyntaxException {
    edit(QA_NEEDED_DIR + "duplicate-component-2");
  }

  private void compare(final String resource) throws IOException, URISyntaxException {
    final Text result = edit(resource);
    assertThat(result.toPlainString()).as(resource)
                                      .isEqualTo(textOf(pathOfClasspath(resource + ".after")).toPlainString());
  }

  private Text edit(final String resource) throws URISyntaxException, NoEditNeededException, IOException {
    final Path path = pathOfClasspath(resource + ".before");
    return new Editor().doSpecialEdit(path.toString(), textOf(path), new ArrayList<String>());
  }

  private Path pathOfClasspath(final String resource) throws URISyntaxException {
    final URL url = getClass().getResource(resource);
    final Path path = Paths.get(url.toURI());
    return path;
  }
}

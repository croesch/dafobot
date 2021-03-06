package de.croesch.dafobot.work;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.croesch.dafobot.core.Text;

public class Test_KosmetikEditor_Edit {
  private final List<String> additionalActions = new ArrayList<String>();

  @Test
  public void should_Remove_Spaces_Inside_Ref_Tag() {
    final Text text = new Text("<ref> {{Wikipedia|Dreiständerbau}}  </ref>");
    final Text result = new KosmetikEditor().edit(text, this.additionalActions);

    assertThat(result.toPlainString()).isEqualTo("<ref>{{Wikipedia|Dreiständerbau}}</ref>");
    assertThat(this.additionalActions).containsOnly("Kosmetik");
  }

  @Test
  public void should_Remove_Comment_For_Adding_More_Components() {
    final Text text = new Text("<!-- Hier kannst du weitere Bausteine einsetzen, zum Beispiel für Synonyme oder ähnliche Wörter. Eine Auswahl solcher Bausteine findest du über diesem Bearbeitungsfenster! -->");
    final Text result = new KosmetikEditor().edit(text, this.additionalActions);

    assertThat(result.toPlainString()).isEmpty();
    assertThat(this.additionalActions).containsOnly("Kosmetik");
  }
}

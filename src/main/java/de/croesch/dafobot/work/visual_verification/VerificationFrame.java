package de.croesch.dafobot.work.visual_verification;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.jwbf.core.actions.util.ActionException;
import net.sourceforge.jwbf.core.actions.util.ProcessException;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.croesch.dafobot.work.api.VerificationResult;

/**
 * Frame for manual verification.
 * 
 * @author dafo
 * @since Date: Oct 15, 2010
 */
public class VerificationFrame extends JFrame {
  /** generated serial version UID. */
  private static final long serialVersionUID = 6640164611324700043L;

  private static final Logger LOG = LoggerFactory.getLogger(VerificationFrame.class);

  private JPanel contentPane;

  private final Action cancelAction = new CancelAction();

  private final Action okAction = new OkAction();

  private final SimpleArticle sa;

  private final JEditorPane editorPaneOld = new JEditorPane();

  private final JEditorPane editorPaneNew = new JEditorPane();

  private final JEditorPane editorPaneDiff = new JEditorPane();

  private final JEditorPane logArea = new JEditorPane();

  private boolean isOpen = true;

  private VerificationResult result = VerificationResult.FATAL;

  public VerificationFrame(final SimpleArticle oldArticle, final SimpleArticle newArticle) {
    super(newArticle.getTitle());

    this.sa = newArticle;

    final ArrayList<String> vgl = Differ.vergleiche(oldArticle.getText(), this.sa.getText());
    final StringBuffer dif = new StringBuffer();
    for (final String ln : vgl) {
      dif.append(ln);
    }

    final Font f = new Font("Courier New", Font.PLAIN, 11);
    this.editorPaneNew.setFont(f);
    this.editorPaneOld.setFont(f);
    this.editorPaneDiff.setFont(f);
    this.logArea.setFont(f);

    this.editorPaneNew.setText(this.sa.getText());
    this.editorPaneOld.setText(oldArticle.getText());
    this.editorPaneDiff.setText(dif.toString());
    this.logArea.setText("edit summary: " + this.sa.getEditSummary());

    createFrame();

    this.setVisible(true);
  }

  private void createFrame() {
    addWindowListener(new WindowListener() {

      @Override
      public void windowOpened(final WindowEvent e) {
        VerificationFrame.this.isOpen = true;
      }

      @Override
      public void windowIconified(final WindowEvent e) {}

      @Override
      public void windowDeiconified(final WindowEvent e) {}

      @Override
      public void windowDeactivated(final WindowEvent e) {}

      @Override
      public void windowClosing(final WindowEvent e) {
        VerificationFrame.this.isOpen = false;
      }

      @Override
      public void windowClosed(final WindowEvent e) {
        VerificationFrame.this.isOpen = false;
      }

      @Override
      public void windowActivated(final WindowEvent e) {}
    });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(50, 100, 1150, 500);

    final JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    this.contentPane = new JPanel();
    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(this.contentPane);
    this.contentPane.setLayout(new MigLayout("", "[][grow][grow][grow]", "[][grow][100:100:100,grow][bottom]"));

    final JSplitPane splitPaneLeft = new JSplitPane();
    splitPaneLeft.setResizeWeight(0.33);
    final JSplitPane splitPaneRight = new JSplitPane();
    splitPaneRight.setResizeWeight(0.5);
    final JScrollPane scrollPaneOld = new JScrollPane();
    final JScrollPane scrollPaneNew = new JScrollPane();
    final JScrollPane scrollPaneDiff = new JScrollPane();
    final JLabel lblAlteVersion = new JLabel("Alte Version");
    final JLabel lblNeueVersion = new JLabel("Neue Version");
    final JLabel lblUnterschiede = new JLabel("Unterschiede");

    lblAlteVersion.setHorizontalAlignment(SwingConstants.CENTER);
    lblNeueVersion.setHorizontalAlignment(SwingConstants.CENTER);
    lblUnterschiede.setHorizontalAlignment(SwingConstants.CENTER);

    scrollPaneNew.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPaneDiff.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPaneOld.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    scrollPaneOld.setColumnHeaderView(lblAlteVersion);
    scrollPaneNew.setColumnHeaderView(lblNeueVersion);
    scrollPaneDiff.setColumnHeaderView(lblUnterschiede);

    scrollPaneOld.setViewportView(this.editorPaneOld);
    scrollPaneNew.setViewportView(this.editorPaneNew);
    scrollPaneDiff.setViewportView(this.editorPaneDiff);

    splitPaneLeft.setLeftComponent(scrollPaneOld);
    splitPaneRight.setLeftComponent(scrollPaneNew);
    splitPaneRight.setRightComponent(scrollPaneDiff);
    splitPaneLeft.setRightComponent(splitPaneRight);
    this.contentPane.add(splitPaneLeft, "cell 1 1 3 1,grow");

    final JScrollPane scrollPaneLog = new JScrollPane();
    scrollPaneLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.contentPane.add(scrollPaneLog, "cell 1 2 3 1,grow");

    scrollPaneLog.setViewportView(this.logArea);

    final JPanel panel = new JPanel();
    this.contentPane.add(panel, "cell 1 3 3 1,grow");

    final JButton btnOk = new JButton("OK");
    btnOk.setAction(this.okAction);
    btnOk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {}
    });
    panel.add(btnOk);

    final JButton btnCancel = new JButton("Cancel");
    btnCancel.setAction(this.cancelAction);
    panel.add(btnCancel);
  }

  public boolean isOpen() {
    return this.isOpen;
  }

  public VerificationResult getResult() {
    return this.result;
  }

  private class CancelAction extends AbstractAction {

    private static final long serialVersionUID = 1681574523824759005L;

    public CancelAction() {
      putValue(NAME, "Nein");
      putValue(SHORT_DESCRIPTION, "Bricht die Bearbeitung ab ohne Speichern.");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      VerificationFrame.this.result = VerificationResult.BAD;
      LOG.info("cancel writing");
      VerificationFrame.this.dispose();
    }
  }

  private class OkAction extends AbstractAction {

    private static final long serialVersionUID = 1135002732434724415L;

    public OkAction() {
      putValue(NAME, "Ja");
      putValue(SHORT_DESCRIPTION, "Speichert die Bearbeitung.");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      try {
        VerificationFrame.this.result = VerificationResult.GOOD;
        VerificationFrame.this.dispose();
      } catch (final ActionException ex) {
        VerificationFrame.this.logArea.setText(VerificationFrame.this.logArea.getText() + ex.getStackTrace().toString());
      } catch (final ProcessException ex) {
        VerificationFrame.this.logArea.setText(VerificationFrame.this.logArea.getText() + ex.getStackTrace().toString());
      }
    }
  }
}

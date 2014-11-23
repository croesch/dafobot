package de.croesch.dafobot.core;

/**
 * Return type of an edit.
 *
 * @author dafo
 * @since Date: Nov 23, 2014
 */
public class EditedText {

  private final Text text;

  private final String commentAppendum;

  public EditedText(final Text text, final String commentAppendum) {
    this.text = text;
    this.commentAppendum = commentAppendum;
  }

  public Text getText() {
    return this.text;
  }

  public String getCommentAppendum() {
    return this.commentAppendum;
  }
}

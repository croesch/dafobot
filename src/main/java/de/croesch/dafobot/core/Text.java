package de.croesch.dafobot.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A custom string-like data container. It's used because wiki-texts can contain comments everywhere which makes parsing
 * really hard. To avoid deletion of those comments (even if on a wrong place) and synchronisation between cleaned and
 * original source, this contains the full text but returns only the commentless text.
 *
 * @author dafo
 * @since Date: Nov 22, 2014
 */
public class Text {

  /** the full (with comments) content. */
  private final String content;

  /** lazy initialised character array of the full content. */
  private char[] chars;

  public Text(final String t) {
    this.content = t;
  }

  @Override
  public String toString() {
    /* Build the content for parsing (aka remove the effing comments). Use own construct because regex is too greedy. */
    final StringBuilder sb = new StringBuilder();
    final Matcher openMatcher = Pattern.compile("<!--").matcher(this.content);
    final Matcher closeMatcher = Pattern.compile("-->").matcher(this.content);

    int begin = 0;
    while (openMatcher.find(begin)) {
      sb.append(this.content.substring(begin, openMatcher.start()));
      if (!closeMatcher.find(openMatcher.start() + 4)) {
        return sb.toString();
      }
      begin = closeMatcher.start() + 3;
    }
    return sb.toString() + this.content.substring(begin);
  }

  public String toPlainString() {
    return this.content;
  }

  public int length() {
    return toString().length();
  }

  public Text substring(final int from) {
    if (from < 0) {
      throw new IndexOutOfBoundsException();
    }
    final int start = findPosition(from, false);
    return new Text(this.content.substring(start));
  }

  public Text substring(final int from, final int to) {
    if (from < 0 || to < from) {
      throw new IndexOutOfBoundsException();
    }
    final int start = findPosition(from, false);
    final int end = findPosition(to, true);
    return new Text(this.content.substring(start, end));
  }

  private int findPosition(final int pos, final boolean end) {
    if (pos == 0 && !end) {
      return 0;
    }
    final char[] chars = getChars();
    boolean inComment = false;
    int realPosition = 0;
    for (int outPosition = 0; outPosition <= pos || (end && (inComment || beginsCommentAt(chars, realPosition))); ++realPosition) {
      if (beginsCommentAt(chars, realPosition)) {
        inComment = true;
        realPosition += 3;
      } else if (inComment) {
        if (endsCommentAt(chars, realPosition)) {
          inComment = false;
          realPosition += 2;
        } else if (realPosition == chars.length) {
          inComment = false;
          if (outPosition == pos) {
            break;
          }
        }
      } else {
        if (realPosition > chars.length) {
          throw new IndexOutOfBoundsException();
        }
        if (outPosition == pos) {
          break;
        }
        ++outPosition;
      }
    }
    return realPosition;
  }

  private char[] getChars() {
    if (this.chars == null) {
      this.chars = this.content.toCharArray();
    }
    return this.chars;
  }

  private boolean endsCommentAt(final char[] chars, final int realPosition) {
    return chars.length > realPosition + 2 && chars[realPosition] == '-' && chars[realPosition + 1] == '-'
           && chars[realPosition + 2] == '>';
  }

  private boolean beginsCommentAt(final char[] chars, final int realPosition) {
    return chars.length > realPosition + 3 && chars[realPosition] == '<' && chars[realPosition + 1] == '!'
           && chars[realPosition + 2] == '-' && chars[realPosition + 3] == '-';
  }
}

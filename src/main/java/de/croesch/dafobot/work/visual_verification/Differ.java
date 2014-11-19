package de.croesch.dafobot.work.visual_verification;

import java.util.ArrayList;

/**
 * Provides a really really really simple diff of the change for manual verification.
 * 
 * @author dafo
 * @since Date: Oct 15, 2010
 */
public class Differ {
  public static ArrayList<String> vergleiche(final String oldFile, final String newFile) {
    final String[] oldLines = oldFile.split("\n");
    final ArrayList<String> diffOldLines = new ArrayList<String>();
    for (final String ln : oldLines) {
      diffOldLines.add(ln);
    }

    final String[] newLines = newFile.split("\n");
    final ArrayList<String> diffNewLines = new ArrayList<String>();
    for (final String ln : newLines) {
      diffNewLines.add(ln);
    }

    for (final String oldLn : oldLines) {
      for (final String newLn : newLines) {
        if (oldLn.equals(newLn)) {
          diffOldLines.remove(oldLn);
          diffNewLines.remove(newLn);
        }
      }
    }

    final ArrayList<String> diff = new ArrayList<String>();
    for (final String ln : diffOldLines) {
      diff.add("old: " + ln + "\n");
    }
    for (final String ln : diffNewLines) {
      diff.add("new: " + ln + "\n");
    }
    return diff;
  }
}

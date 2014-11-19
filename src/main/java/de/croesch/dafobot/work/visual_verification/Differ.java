package de.croesch.dafobot.work.visual_verification;

import java.util.ArrayList;

/**
 * TODO Comment here ???
 * 
 * @author $Author: Christian $
 * @version $Revision: 1.1 $ ($Date: 15.10.2010 11:53:18 $)
 */
public class Differ {
  /** Version number. */
  public static final String VER = "$Revision: 1.1 $";

  public static ArrayList<String> vergleiche(String oldFile, String newFile) {
    String[] oldLines = oldFile.split("\n");
    ArrayList<String> diffOldLines = new ArrayList<String>();
    for (String ln : oldLines) {
      diffOldLines.add(ln);
    }

    String[] newLines = newFile.split("\n");
    ArrayList<String> diffNewLines = new ArrayList<String>();
    for (String ln : newLines) {
      diffNewLines.add(ln);
    }

    for (String oldLn : oldLines) {
      for (String newLn : newLines) {
        if (oldLn.equals(newLn)) {
          diffOldLines.remove(oldLn);
          diffNewLines.remove(newLn);
        }
      }
    }

    ArrayList<String> diff = new ArrayList<String>();
    for (String ln : diffOldLines) {
      diff.add("old: " + ln + "\n");
    }
    for (String ln : diffNewLines) {
      diff.add("new: " + ln + "\n");
    }
    return diff;
  }
}

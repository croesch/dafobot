package de.croesch.dafobot.work.sort_text_components;

import java.util.regex.Matcher;

/**
 * TODO Comment here ...
 *
 * @author dafo
 * @since Date: Nov 19, 2014
 */
public interface ComponentIF {

  String getName();

  Matcher getMatcher(String text);

}

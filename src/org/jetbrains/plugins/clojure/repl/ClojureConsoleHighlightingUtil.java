package org.jetbrains.plugins.clojure.repl;

import java.util.regex.Pattern;

import org.intellij.lang.annotations.Language;

/**
 * @author ilyas
 */
public class ClojureConsoleHighlightingUtil {

  @Language("RegExp")
  private static final String OTHER_TOKEN = "_|-|\\*|\\.|\\+|=|&|<|>|\\$|/|\\?|!";
  @Language("RegExp")
  private static final String LETTER = "([a-zA-Z]" + "|" + OTHER_TOKEN + ")";
  @Language("RegExp")
  private static final String CLOJURE_IDENTIFIER = LETTER + "(" + LETTER + "|[0-9]" + ")*";
  private static final String PROMPT_ARROW = "=>";

  public static final String LINE_WITH_PROMPT = CLOJURE_IDENTIFIER + PROMPT_ARROW + ".*";

  public static final Pattern CLOJURE_PROMPT_PATTERN = Pattern.compile(CLOJURE_IDENTIFIER + PROMPT_ARROW);
  public static final Pattern LINE_WITH_PROMPT_PATTERN = Pattern.compile(LINE_WITH_PROMPT);

}

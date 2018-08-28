package org.jetbrains.plugins.clojure.repl;

import java.util.regex.Pattern;

import org.intellij.lang.annotations.Language;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.util.Key;

/**
 * @author ilyas
 */
public class ClojureConsoleHighlightingUtil
{

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

	/**
	 * Print highlighted output to the console
	 *
	 * @param console
	 * @param text
	 */
	public static void processOutput(LanguageConsoleImpl console, String text, Key attributes)
	{
		final ConsoleViewContentType outputType = ConsoleViewContentType.NORMAL_OUTPUT;
		// todo implement multiple cases for error etc.
		final ConsoleViewImpl consoleView = console.getHistoryViewer().getUserData(ConsoleViewImpl.CONSOLE_VIEW_IN_EDITOR_VIEW);
		if(consoleView != null)
		{
			consoleView.print(text, outputType);
		}
	}
}
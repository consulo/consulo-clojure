package org.jetbrains.plugins.clojure.repl;

import java.util.regex.Matcher;

import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;

/**
 * @author ilyas
 */
public class ClojureConsoleProcessHandler extends ColoredProcessHandler
{

	private final LanguageConsoleImpl myLanguageConsole;

	public ClojureConsoleProcessHandler(Process process, String commandLine, LanguageConsoleImpl console)
	{
		super(process, commandLine, CharsetToolkit.UTF8_CHARSET);
		myLanguageConsole = console;
	}

	@Override
	public void coloredTextAvailable(String text, Key attributes)
	{
		ConsoleViewContentType consoleViewType = ConsoleViewContentType.getConsoleViewType(attributes);
		final String string = processPrompts(myLanguageConsole, StringUtil.convertLineSeparators(text));

		myLanguageConsole.printToHistory(string, consoleViewType.getAttributes());
	}

	private static String processPrompts(final LanguageConsoleImpl console, String text)
	{
		if(text != null && text.matches(ClojureConsoleHighlightingUtil.LINE_WITH_PROMPT))
		{
			final Matcher matcher = ClojureConsoleHighlightingUtil.CLOJURE_PROMPT_PATTERN.matcher(text);
			matcher.find();
			final String prefix = matcher.group();
			final String trimmed = StringUtil.trimStart(text, prefix).trim();
			console.setPrompt(prefix + " ");
			return trimmed;
		}
		return text;
	}

	public LanguageConsoleImpl getLanguageConsole()
	{
		return myLanguageConsole;
	}

}

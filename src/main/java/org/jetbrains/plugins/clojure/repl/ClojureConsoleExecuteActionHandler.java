package org.jetbrains.plugins.clojure.repl;

import java.io.IOException;
import java.io.OutputStream;

import org.jetbrains.plugins.clojure.file.ClojureFileType;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiUtil;
import com.intellij.execution.console.ConsoleHistoryController;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.lang.FileASTNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.impl.source.codeStyle.IndentHelper;
import com.intellij.psi.impl.source.codeStyle.IndentHelperImpl;

/**
 * @author ilyas
 */
public class ClojureConsoleExecuteActionHandler
{

	private final ProcessHandler myProcessHandler;
	private final Project myProject;
	private final IndentHelper myIndentHelper;
	private boolean myPreserveMarkup;


	public ClojureConsoleExecuteActionHandler(ProcessHandler processHandler, Project project, boolean preserveMarkup)
	{
		myProcessHandler = processHandler;
		myProject = project;
		myPreserveMarkup = preserveMarkup;
		myIndentHelper = IndentHelper.getInstance();
	}

	public void processLine(String line)
	{
		//final Charset charset = myProcessHandler.getCharset();
		final OutputStream outputStream = myProcessHandler.getProcessInput();
		try
		{
			//byte[] bytes = (line + "\n").getBytes(charset.name());
			byte[] bytes = (line + "\n").getBytes("UTF-8");
			outputStream.write(bytes);
			outputStream.flush();
		}
		catch(IOException e)
		{
			// ignore
		}
	}

	public void runExecuteAction(final ClojureConsole console, boolean executeImmediately)
	{

		final ConsoleHistoryController consoleHistoryModel = console.getHistoryController();
		if(executeImmediately)
		{
			execute(console, consoleHistoryModel);
			return;
		}

		// Process input and add to history
		final Editor editor = console.getCurrentEditor();
		final Document document = editor.getDocument();
		final CaretModel caretModel = editor.getCaretModel();
		final int offset = caretModel.getOffset();
		final String text = document.getText();

		if(!"".equals(text.substring(offset).trim()))
		{
			final String before = text.substring(0, offset);
			final String after = text.substring(offset);
			final FileASTNode node = console.getFile().getNode();
			final Project project = editor.getProject();
			final int indent = myIndentHelper.getIndent(project, ClojureFileType.CLOJURE_FILE_TYPE, node);
			final String spaces = IndentHelperImpl.fillIndent(project, ClojureFileType.CLOJURE_FILE_TYPE, indent);
			final String newText = before + "\n" + spaces + after;

			new WriteCommandAction(myProject)
			{
				@Override
				protected void run(Result result) throws Throwable
				{
					console.setInputText(newText);
					caretModel.moveToOffset(offset + indent + 1);
				}
			}.execute();

			return;
		}

		final String candidate = text.trim();

		// S-expression contains no syntax errors
		if(ClojurePsiUtil.isValidClojureExpression(candidate, myProject) || "".equals(candidate))
		{
			execute(console, consoleHistoryModel);
		}
		else
		{
			console.setInputText(text + "\n");
		}
	}

	private void execute(ClojureConsole languageConsole, ConsoleHistoryController consoleHistoryController)
	{

		// Process input and add to history
		final Document document = languageConsole.getCurrentEditor().getDocument();
		String text = document.getText();
		final TextRange range = new TextRange(0, document.getTextLength());

		languageConsole.getCurrentEditor().getSelectionModel().setSelection(range.getStartOffset(), range.getEndOffset());
		languageConsole.addToHistory(range, languageConsole.getConsoleEditor(), myPreserveMarkup);
		languageConsole.setInputText("");
		if(!StringUtil.isEmptyOrSpaces(text))
		{
			consoleHistoryController.addToHistory(text);
		}
		// Send to interpreter / server
		if(languageConsole.getNReplHost() != null)
		{
			text = "(do (use '[clojure.tools.nrepl :as repl])" +
					" (with-open [conn (repl/connect :host \"" + languageConsole.getNReplHost() +
					"\" :port " + languageConsole.getNReplPort() + ")]\n" +
					"  (-> (repl/client conn 1000)\n" +
					"    (repl/message {:op :eval :code \"" + text + "\"})\n" +
					"    repl/response-values)))";
		}
		processLine(text);
	}


	private void scrollDown(final Editor currentEditor)
	{
		ApplicationManager.getApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				currentEditor.getCaretModel().moveToOffset(currentEditor.getDocument().getTextLength());
			}
		});
	}
}
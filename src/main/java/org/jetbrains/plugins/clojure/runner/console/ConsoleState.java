package org.jetbrains.plugins.clojure.runner.console;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ExecutionBundle;
import com.intellij.openapi.util.Key;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.jetbrains.plugins.clojure.ClojureBundle;

/**
 * @author ilyas
 */
public abstract class ConsoleState {
  public static final ConsoleState NOT_STARTED = new ConsoleState(){
    public ConsoleState attachTo(final ConsoleView console, final ProcessHandler processHandler) {
      return new RunningState(console, processHandler);
    }
  };

  public ConsoleState dispose() {
    return NOT_STARTED;
  }

  public boolean isFinished() {
    return false;
  }

  public boolean isRunning() {
    return false;
  }

  public void sendUserInput(final String input) throws IOException {}

  public abstract ConsoleState attachTo(ConsoleView console, ProcessHandler processHandler);

  private static class RunningState extends ConsoleState {
    private final ConsoleView myConsole;
    private final ProcessAdapter myProcessListener = new ProcessAdapter() {
      public void onTextAvailable(final ProcessEvent event, final Key outputType) {
            myConsole.print(event.getText(), ConsoleViewContentType.getConsoleViewType(outputType));
      }
    };
    private final ProcessHandler myProcessHandler;
    private final Writer myUserInputWriter;

    public RunningState(final ConsoleView console, final ProcessHandler processHandler) {
      myConsole = console;
      myProcessHandler = processHandler;
      processHandler.addProcessListener(myProcessListener);
      final OutputStream processInput = myProcessHandler.getProcessInput();
      myUserInputWriter = processInput != null ? new OutputStreamWriter(processInput) : null;
    }

    public ConsoleState dispose() {
      if (myProcessHandler != null) {
        myProcessHandler.removeProcessListener(myProcessListener);
      }
      return NOT_STARTED;
    }

    public boolean isFinished() {
      return myProcessHandler == null || myProcessHandler.isProcessTerminated();
    }

    public boolean isRunning() {
      return myProcessHandler != null && !myProcessHandler.isProcessTerminated();
    }

    public void sendUserInput(final String input) throws IOException {
      if (myUserInputWriter == null)
        throw new IOException(ClojureBundle.message("no.user.process.input.error.message"));
      myUserInputWriter.write(input);
      myUserInputWriter.flush();
    }

    public ConsoleState attachTo(final ConsoleView console, final ProcessHandler processHandler) {
      return dispose().attachTo(console, processHandler);
    }
  }
}
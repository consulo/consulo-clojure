package org.jetbrains.plugins.clojure.repl.actions;

import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.ClojureIcons;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * @author ilyas
 */
public class RunSelectedTextAction extends ClojureConsoleActionBase {
  public RunSelectedTextAction(){
    getTemplatePresentation().setIcon(ClojureIcons.REPL_EVAL);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    final Editor editor = e.getData(LangDataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    final SelectionModel selectionModel = editor.getSelectionModel();
    String selectedText = selectionModel.getSelectedText();
    if (selectedText == null || selectedText.trim().length() == 0) {
      return;
    }
    final String text = selectedText.trim();
    final Project project = editor.getProject();

    final String msg = ClojurePsiFactory.getInstance(project).getErrorMessage(text);
    if (msg != null) {
      Messages.showErrorDialog(project,
          ClojureBundle.message("evaluate.incorrect.form", msg),
          ClojureBundle.message("evaluate.incorrect.cannot.evaluate"));
      return;
    }

    executeCommand(project, text);
  }
}

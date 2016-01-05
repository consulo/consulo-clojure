package org.jetbrains.plugins.clojure.repl.actions;

import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.ClojureIcons;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiFactory;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;

/**
 * @author ilyas
 */
public final class RunTopSExprAction extends ClojureConsoleActionBase {

  public RunTopSExprAction() {
    getTemplatePresentation().setIcon(ClojureIcons.REPL_EVAL);
  }

  public void actionPerformed(AnActionEvent event) {
    Editor editor = event.getData(LangDataKeys.EDITOR);
    if (editor == null) {
      return;
    }

    Project project = editor.getProject();
    if (project == null) {
      return;
    }

    PsiElement sexp = ClojurePsiUtil.findTopSexpAroundCaret(editor);
    if (sexp == null) {
      return;
    }

    String text = sexp.getText();
    if (ClojurePsiFactory.getInstance(project).hasSyntacticalErrors(text)) {
      Messages.showErrorDialog(project,
          ClojureBundle.message("evaluate.incorrect.sexp"),
          ClojureBundle.message("evaluate.incorrect.cannot.evaluate"));
      return;
    }

    executeCommand(project, text);

  }

}
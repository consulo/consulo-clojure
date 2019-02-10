package org.jetbrains.plugins.clojure.annotator.intentions.imports;

import javax.annotation.Nonnull;
import org.jetbrains.plugins.clojure.ClojureBundle;
import com.intellij.codeInspection.HintAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;

/**
 * @author ilyas
 */
public class ClojureImportClassFix implements HintAction {
  private final PsiNamedElement[] myElements;

  public ClojureImportClassFix(PsiNamedElement[] elements) {
    myElements = elements;
  }

  public boolean showHint(Editor editor) {
    return false;
  }

  @Nonnull
  public String getText() {
    return ClojureBundle.message("import.named", myElements[0].getName());
  }

  @Nonnull
  public String getFamilyName() {
    return ClojureBundle.message("import.symbol");
  }

  public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
    return false;
  }

  public void invoke(@Nonnull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {

  }

  public boolean startInWriteAction() {
    return false;
  }
}

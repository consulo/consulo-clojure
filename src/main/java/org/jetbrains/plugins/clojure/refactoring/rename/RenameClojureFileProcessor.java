package org.jetbrains.plugins.clojure.refactoring.rename;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import javax.annotation.Nonnull;

/**
 * @author ilyas
 */
public class RenameClojureFileProcessor extends RenamePsiElementProcessor {
  @Override
  public boolean canProcessElement(@Nonnull PsiElement element) {
    return false; //todo: ?
  }
}

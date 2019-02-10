package org.jetbrains.plugins.clojure.psi.api;

import com.intellij.psi.PsiElement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.plugins.clojure.psi.ClojurePsiElement;

/**
 * @author ilyas
 */
public interface ClBraced extends ClojurePsiElement {
  @Nonnull
  PsiElement getFirstBrace();

  @Nullable
  PsiElement getLastBrace();
}

package org.jetbrains.plugins.clojure.psi;

import com.intellij.psi.PsiElement;
import javax.annotation.Nullable;

/**
 * @author ilyas
 */
public interface ClojurePsiElement extends PsiElement {

  @Nullable
  PsiElement getFirstNonLeafElement();

  @Nullable
  PsiElement getLastNonLeafElement();

  @Nullable
  PsiElement getNonLeafElement(int k);

}

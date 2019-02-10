package org.jetbrains.plugins.clojure.psi.api.ns;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.clojure.psi.api.ClList;
import org.jetbrains.plugins.clojure.psi.api.ClListLike;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import javax.annotation.Nullable;
import com.intellij.psi.PsiNamedElement;

/**
 * @author ilyas
 */
public interface ClNs extends ClList, PsiNamedElement {
  @Nullable
  ClSymbol getNameSymbol();

  String getDefinedName();

  @Nullable
  ClList findImportClause(@Nullable PsiElement place);

  @Nullable
  ClList findImportClause();

  @Nonnull
  ClList findOrCreateImportClause(@Nullable PsiElement place);

  @Nonnull
  ClList findOrCreateImportClause();

  @Nullable
  ClListLike addImportForClass(PsiElement place, PsiClass clazz);

}

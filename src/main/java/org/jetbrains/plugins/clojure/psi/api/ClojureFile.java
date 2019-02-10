package org.jetbrains.plugins.clojure.psi.api;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiFileWithStubSupport;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.plugins.clojure.psi.ClojurePsiElement;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.ns.ClNs;

import java.util.List;

/**
 * @author ilyas
 */
public interface ClojureFile extends PsiFile, ClojurePsiElement, PsiFileWithStubSupport {

  List<ClDef> getFileDefinitions();

  boolean isClassDefiningFile();

  @Nullable
  String getNamespace();

  @Nullable
  ClNs getNamespaceElement();

  @Nonnull
  ClNs findOrCreateNamespaceElement() throws IncorrectOperationException;

  @Nonnull
  String getPackageName();

  @Nullable
  String getClassName();

  PsiElement setClassName(@NonNls String s);

  @Nullable
  PsiClass getDefinedClass();

  void setNamespace(String newNs);

  @Nullable
  String getNamespacePrefix();

  @Nullable
  String getNamespaceSuffix();
}

package org.jetbrains.plugins.clojure.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.clojure.psi.ClStubElementType;
import org.jetbrains.plugins.clojure.psi.ClojureBaseElementImpl;
import org.jetbrains.plugins.clojure.psi.api.ClKeyword;
import org.jetbrains.plugins.clojure.psi.stubs.api.ClKeywordStub;
import org.jetbrains.plugins.clojure.psi.stubs.index.ClojureKeywordIndex;

import java.util.Collection;

/**
 * @author ilyas
*/
public class ClKeywordImpl extends ClojureBaseElementImpl<ClKeywordStub> implements ClKeyword, StubBasedPsiElement<ClKeywordStub> {
  public ClKeywordImpl(ASTNode node) {
    super(node);
  }

  public ClKeywordImpl(ClKeywordStub stub, @Nonnull ClStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "ClKeyword";
  }

  @Override
  @Nonnull
  public String getName() {
    ClKeywordStub stub = getStub();
    if (stub != null) {
      return stub.getName();
    }

    return getText();
  }

  public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException {
    throw new IncorrectOperationException("Name changing for the keyword");
  }

  @Override
  public PsiReference getReference() {
    return this;
  }

  public PsiElement getElement() {
    return this;
  }

  public TextRange getRangeInElement() {
    return new TextRange(0, getTextLength());
  }

  public PsiElement resolve() {
    return null;
  }

  @Nonnull
  public String getCanonicalText() {
    return getText();
  }

  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return null;
  }

  public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException {
    return null;
  }

  public boolean isReferenceTo(PsiElement element) {
    return false;
  }

  public boolean isSoft() {
    return false;
  }

  @Nonnull
  public Object[] getVariants() {
    final Project project = getProject();
    final Collection<String> keys = StubIndex.getInstance().getAllKeys(ClojureKeywordIndex.KEY, project);
    return keys.toArray(new String[keys.size()]);
  }
}

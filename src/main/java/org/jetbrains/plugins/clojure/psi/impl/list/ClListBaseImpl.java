package org.jetbrains.plugins.clojure.psi.impl.list;

import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.plugins.clojure.psi.ClojureBaseElementImpl;
import org.jetbrains.plugins.clojure.psi.api.ClList;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import org.jetbrains.plugins.clojure.lexer.ClojureTokenTypes;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.lang.ASTNode;

/**
 * @author ilyas
 */
public abstract class ClListBaseImpl<T extends StubElement> extends ClojureBaseElementImpl<T> implements ClList, StubBasedPsiElement<T> {

  public ClListBaseImpl(T stub, @Nonnull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public ClListBaseImpl(ASTNode node) {
    super(node);
  }

  @Nullable
  public String getPresentableText() {
    final ClSymbol first = findChildByClass(ClSymbol.class);
    if (first == null) return null;
    final String text1 = getHeadText();
    PsiElement next = PsiTreeUtil.getNextSiblingOfType(first, ClSymbol.class);
    if (next == null) return text1;
    else return text1 + " " + next.getText();
  }

  @Nullable
  public String getHeadText() {
    PsiElement first = getFirstNonLeafElement();
    if (first == null) return null;
    return first.getText();
  }

  @Nullable
  public ClSymbol getFirstSymbol() {
    PsiElement child = getFirstChild();
    while (child instanceof LeafPsiElement) {
      child = child.getNextSibling();
    }
    if (child instanceof ClSymbol) {
      return (ClSymbol) child;
    }
    return null;
  }

  @Nonnull
  public PsiElement getFirstBrace() {
    PsiElement element = findChildByType(ClojureTokenTypes.LEFT_PAREN);
    assert element != null;
    return element;
  }

  public PsiElement getSecondNonLeafElement() {
    PsiElement first = getFirstChild();
    while (first != null && isWrongElement(first)) {
      first = first.getNextSibling();
    }
    if (first == null) {
      return null;
    }
    PsiElement second = first.getNextSibling();
    while (second != null && isWrongElement(second)) {
      second = second.getNextSibling();
    }
    return second;
  }

  public PsiElement getLastBrace() {
    return findChildByType(ClojureTokenTypes.RIGHT_PAREN);
  }

}

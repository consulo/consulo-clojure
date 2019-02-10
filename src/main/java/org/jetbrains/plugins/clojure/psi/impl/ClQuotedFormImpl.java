package org.jetbrains.plugins.clojure.psi.impl;

import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import org.jetbrains.plugins.clojure.psi.ClojurePsiElementImpl;
import org.jetbrains.plugins.clojure.psi.ClojurePsiElement;
import org.jetbrains.plugins.clojure.psi.api.ClQuotedForm;

/**
 * @author ilyas
 */
public class ClQuotedFormImpl extends ClojurePsiElementImpl implements ClQuotedForm {
  public ClQuotedFormImpl(ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "ClQuotedForm";
  }

  @Nullable
  public ClojurePsiElement getQuotedElement() {
    return findChildByClass(ClojurePsiElement.class);
  }


}

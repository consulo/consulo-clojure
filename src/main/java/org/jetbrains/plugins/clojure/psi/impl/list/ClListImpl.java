package org.jetbrains.plugins.clojure.psi.impl.list;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.EmptyStub;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.PsiElement;

/**
 * @author ilyas
*/
public class ClListImpl extends ClListBaseImpl<EmptyStub> {

  public ClListImpl(ASTNode node) {
    super(node);
  }

  public ClListImpl(EmptyStub stub, @Nonnull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "ClList"; 
  }

  @Override
  public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement place) {
    return ListDeclarations.get(processor, state, lastParent, place, this, getHeadText());
  }
}

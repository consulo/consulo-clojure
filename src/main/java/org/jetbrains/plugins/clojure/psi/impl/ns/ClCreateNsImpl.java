package org.jetbrains.plugins.clojure.psi.impl.ns;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.clojure.psi.stubs.api.ClNsStub;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.lang.ASTNode;

/**
 * @author ilyas
 */
public class ClCreateNsImpl extends ClNsImpl{
  public ClCreateNsImpl(ClNsStub stub, @Nonnull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public ClCreateNsImpl(ASTNode node) {
    super(node);
  }
}

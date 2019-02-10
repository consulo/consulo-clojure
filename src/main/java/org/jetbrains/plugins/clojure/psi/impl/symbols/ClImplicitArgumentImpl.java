package org.jetbrains.plugins.clojure.psi.impl.symbols;

import com.intellij.lang.ASTNode;
import javax.annotation.Nonnull;

/**
 * @author ilyas
 */
public class ClImplicitArgumentImpl extends ClSymbolImpl {
  public ClImplicitArgumentImpl(ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "ClImplicitArgument";
  }

  @Nonnull
  public String getNameString() {
    return getText();
  }
}

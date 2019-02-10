package org.jetbrains.plugins.clojure.psi.api;

import javax.annotation.Nullable;
import org.jetbrains.plugins.clojure.psi.ClojurePsiElement;

/**
 * @author ilyas
 */
public interface ClQuotedForm {
  @Nullable
  ClojurePsiElement getQuotedElement();
}

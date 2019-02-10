package org.jetbrains.plugins.clojure.psi.stubs.api;

import javax.annotation.Nonnull;

import com.intellij.psi.stubs.NamedStub;
import org.jetbrains.plugins.clojure.psi.api.ClKeyword;

/**
 * @author ilyas
 */
public interface ClKeywordStub extends NamedStub<ClKeyword>  {
  @Nonnull
  String getName();
}

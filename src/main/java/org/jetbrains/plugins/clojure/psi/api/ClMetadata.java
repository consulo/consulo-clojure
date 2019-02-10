package org.jetbrains.plugins.clojure.psi.api;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.clojure.psi.ClojurePsiElement;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ilyas
 */
public interface ClMetadata extends ClojurePsiElement {

  @Nonnull
  List<ClKeyword> getKeys();

  @Nullable
  ClojurePsiElement getValue(String key);

}

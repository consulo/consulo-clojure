package org.jetbrains.plugins.clojure.config;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.clojure.utils.ClojureUtils;

/**
 * @author ilyas
 */
public class ClojureModuleSettings {

  @Nonnull
  public String myReplClass = ClojureUtils.CLOJURE_MAIN;

  @Nonnull
  public String myJvmOpts = ClojureUtils.CLOJURE_DEFAULT_JVM_PARAMS;

  @Nonnull
  public String myReplOpts = "";

}

package org.jetbrains.plugins.clojure.module.extension;

import com.intellij.openapi.module.Module;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.ClojureIcons;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 14:38/12.06.13
 */
public class ClojureModuleExtensionProvider implements ModuleExtensionProvider<ClojureModuleExtension, ClojureMutableModuleExtension> {
  @Nullable
  public Icon getIcon() {
    return ClojureIcons.CLOJURE_ICON_16x16;
  }

  @NotNull
  public String getName() {
    return "Clojure";
  }

  @NotNull
  public Class<ClojureModuleExtension> getImmutableClass() {
    return ClojureModuleExtension.class;
  }

  @NotNull
  public ClojureModuleExtension createImmutable(@NotNull String s, @NotNull Module module) {
    return new ClojureModuleExtension(s, module);
  }

  @NotNull
  public ClojureMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull ClojureModuleExtension clojureModuleExtension) {
    return new ClojureMutableModuleExtension(s, module, clojureModuleExtension);
  }
}

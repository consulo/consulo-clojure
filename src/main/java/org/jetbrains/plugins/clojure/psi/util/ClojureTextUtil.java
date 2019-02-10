package org.jetbrains.plugins.clojure.psi.util;

import javax.annotation.Nonnull;

/**
 * @author ilyas
 */
public abstract class ClojureTextUtil {
  
  public  static  String getLastSymbolAtom(@Nonnull String sym, @Nonnull String sep) {
    int index = sym.lastIndexOf(sep);
    return index > 0 && index < sym.length() - 1 ? sym.substring(index + 1) : sym;
  }

  public  static  String getSymbolPrefix(@Nonnull String sym, @Nonnull String sep) {
    int index = sym.lastIndexOf(sep);
    return index > 0 && index < sym.length() - 1 ? sym.substring(0, index) : "";
  }

  public  static  String getLastSymbolAtom(@Nonnull String sym) {
    return getLastSymbolAtom(sym, ".");
  }

  public  static  String getSymbolPrefix(@Nonnull String sym) {
    return getSymbolPrefix(sym, ".");
  }
}

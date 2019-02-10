package org.jetbrains.plugins.clojure.findUsages;

import javax.annotation.Nonnull;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import javax.annotation.Nullable;

import org.jetbrains.plugins.clojure.lexer.ClojureFlexLexer;
import org.jetbrains.plugins.clojure.lexer.ClojureTokenTypes;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;

/**
 * @author ilyas
 */
public class ClojureFindUsagesProvider implements FindUsagesProvider{

  @Nullable
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(new ClojureFlexLexer(),
            ClojureTokenTypes.IDENTIFIERS, ClojureTokenTypes.COMMENTS, ClojureTokenTypes.STRINGS);
  }

  public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
    return psiElement instanceof ClDef || psiElement instanceof ClSymbol;
  }

  public String getHelpId(@Nonnull PsiElement psiElement) {
    return null;
  }

  @Nonnull
  public String getType(@Nonnull PsiElement element) {
    if (element instanceof ClSymbol) return "symbol";
    if (element instanceof ClDef) return "definition";
    return "entity";
  }

  @Nonnull
  public String getDescriptiveName(@Nonnull PsiElement element) {
    if (element instanceof ClSymbol) {
      ClSymbol symbol = (ClSymbol) element;
      final String name = symbol.getText();
      return name == null ? symbol.getText() : name;
    }
    if (element instanceof ClDef) {
      ClDef def = (ClDef) element;
      return def.getPresentationText();
    }
    if (element instanceof PsiNamedElement) {
      final String s = ((PsiNamedElement) element).getName();
      return s != null ? s : element.getText();
    }
    return element.getText();
  }

  @Nonnull
  public String getNodeText(@Nonnull PsiElement element, boolean useFullName) {
    if (element instanceof ClSymbol) {
      ClSymbol symbol = (ClSymbol) element;
      final String name = symbol.getReferenceName();
      return name == null ? symbol.getText() : name;
    }
    if (element instanceof ClDef) {
      ClDef def = (ClDef) element;
      return def.getDefinedName();
    }
    return element.getText();
  }
}

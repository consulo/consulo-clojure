package org.jetbrains.plugins.clojure.psi.resolve.completion;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiFactory;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.JavaPsiClassReferenceElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author Alefas
 * @since 15.01.13
 */
public class ClojureLookupItem extends LookupItem<PsiElement> {
  @Nonnull
  private final PsiElement myElement;
  private boolean isInImport = false;
  private boolean isClassName = false;

  public boolean isInImport() {
    return isInImport;
  }

  public void setInImport(boolean inImport) {
    isInImport = inImport;
  }

  public boolean isClassName() {
    return isClassName;
  }

  public void setClassName(boolean className) {
    isClassName = className;
  }

  @NonNls @Nonnull
  private static String getLookupString(@Nonnull PsiElement element) {
    if (element instanceof PsiNamedElement) {
      return ((PsiNamedElement) element).getName();
    } else {
      return element.getText();
    }
  }

  public ClojureLookupItem(@Nonnull PsiElement element) {
    super(element, getLookupString(element));
    myElement = element;
  }

  @Override
  public void renderElement(LookupElementPresentation presentation) {
    super.renderElement(presentation);
    if (myElement instanceof ClDef) {
      ClDef def = (ClDef) myElement;
      presentation.setTailText(" " + def.getParameterString());
      presentation.setTypeText(def.getContainingFile().getName());
    } else if (myElement instanceof PsiClass) {
      PsiClass clazz = (PsiClass) myElement;
      String location = clazz.getPresentation().getLocationString();
      JavaPsiClassReferenceElement.renderClassItem(presentation, this, clazz, false, StringUtil.notNullize(location), PsiSubstitutor.EMPTY);
    }
  }

  @Override
  public void handleInsert(InsertionContext context) {
    if (isClassName()) {
      PsiDocumentManager.getInstance(context.getProject()).commitDocument(context.getDocument());
      final int startOffset = context.getStartOffset();
      final ClSymbol symbol = PsiTreeUtil.findElementOfClassAtOffset(context.getFile(), startOffset, ClSymbol.class, false);
      if (symbol == null) return;
      if (myElement instanceof PsiClass) {
        PsiClass clazz = (PsiClass) myElement;
        if (symbol.getQualifierSymbol() != null) return;
        if (isInImport()) {
          ClSymbol newSymbol = ClojurePsiFactory.getInstance(context.getProject()).
              createSymbolNodeFromText(clazz.getQualifiedName()).getPsi(ClSymbol.class);
          symbol.replace(newSymbol);
        } else {
          symbol.bindToElement(clazz);
        }
      }
    } else {
      super.handleInsert(context);
    }
  }
}

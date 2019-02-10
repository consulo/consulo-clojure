package org.jetbrains.plugins.clojure.psi.impl.synthetic;

import java.util.List;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.clojure.file.ClojureFileType;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.synthetic.ClSyntheticClass;
import org.jetbrains.plugins.clojure.psi.api.synthetic.ClSyntheticMethod;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiSuperMethodImplUtil;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.IncorrectOperationException;

/**
 * @author ilyas
 */
public class ClSynteticMethodImpl extends LightElement implements ClSyntheticMethod {
  public static final Logger LOG = Logger.getInstance("org.jetbrains.plugins.clojure.psi.impl.synthetic.ClSynteticMethodImpl");

  private final ClDef myDef;
  private final ClSyntheticClass myClass;
  private PsiMethod myCodeBehindMethod;

  protected ClSynteticMethodImpl(ClDef def, ClSyntheticClass clazz) {
    super(def.getManager(), ClojureFileType.CLOJURE_LANGUAGE);
    myDef = def;
    myClass = clazz;
    PsiElementFactory factory = JavaPsiFacade.getInstance(getProject()).getElementFactory();
    try {
      myCodeBehindMethod = factory.createMethodFromText(SynteticUtil.getJavaMethodByDef(myDef), null);
    } catch (IncorrectOperationException e) {
      LOG.error(e);
    }
  }

  @Override
  public String toString() {
    return "ClojureSyntheticMethod[" + getName() + "]";
  }

  public PsiType getReturnTypeNoResolve() {
    return null;
  }

  public String getText() {
    return null;
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
  }

  public PsiElement copy() {
    return null;
  }

  public boolean isExtensionMethod() {
    return false;
  }

  public PsiType getReturnType() {
    return null;
  }

  public PsiTypeElement getReturnTypeElement() {
    return null;
  }

  @Nonnull
  public PsiParameterList getParameterList() {
    return null;
  }

  @Nonnull
  public PsiReferenceList getThrowsList() {
    return null;
  }

  public PsiCodeBlock getBody() {
    return null;
  }

  public boolean isConstructor() {
    return false;
  }

  public boolean isVarArgs() {
    return false;
  }

  @Nonnull
  public MethodSignature getSignature(@Nonnull PsiSubstitutor substitutor) {
    return null;
  }

  public PsiIdentifier getNameIdentifier() {
    return null;
  }

  @Nonnull
  public PsiMethod[] findSuperMethods() {
    return new PsiMethod[0];
  }

  @Nonnull
  public PsiMethod[] findSuperMethods(boolean checkAccess) {
    return new PsiMethod[0];
  }

  @Nonnull
  public PsiMethod[] findSuperMethods(PsiClass parentClass) {
    return new PsiMethod[0];
  }

  @Nonnull
  public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(boolean checkAccess) {
    return null;
  }

  public PsiMethod findDeepestSuperMethod() {
    return null;
  }

  @Nonnull
  public PsiMethod[] findDeepestSuperMethods() {
    return new PsiMethod[0];
  }

  @Nonnull
  public PsiModifierList getModifierList() {
    return null;
  }

  public boolean hasModifierProperty(@Nonnull String name) {
    return false;
  }

  @Nonnull
  public String getName() {
    return null;
  }

  public PsiElement setName(@NonNls String name) throws IncorrectOperationException {
    return null;
  }

  @Nonnull
  public HierarchicalMethodSignature getHierarchicalMethodSignature() {
    return PsiSuperMethodImplUtil.getHierarchicalMethodSignature(this);
  }

  public PsiClass getContainingClass() {
    return myClass;
  }

  public PsiDocComment getDocComment() {
    return null;
  }

  public boolean isDeprecated() {
    return false;
  }

  public boolean hasTypeParameters() {
    return false;
  }

  public PsiTypeParameterList getTypeParameterList() {
    return null;
  }

  @Nonnull
  public PsiTypeParameter[] getTypeParameters() {
    return new PsiTypeParameter[0];
  }
}

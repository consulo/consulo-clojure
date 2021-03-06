package org.jetbrains.plugins.clojure.psi.impl.ns;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.clojure.file.ClojureFileType;
import org.jetbrains.plugins.clojure.psi.api.ns.ClNs;
import org.jetbrains.plugins.clojure.psi.impl.ClojurePsiManager;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaPackage;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import consulo.ide.IconDescriptorUpdaters;
import consulo.ui.image.Image;

/**
 * @author ilyas
 */
public class ClSyntheticNamespace extends LightElement implements PsiJavaPackage {
  @Nonnull
  private final String myName;
  @Nonnull
  private final String myQualifiedName;
  private ClNs myNamespace;

  protected ClSyntheticNamespace(PsiManager manager, String name, String fqn, ClNs ns) {
    super(manager, ClojureFileType.CLOJURE_LANGUAGE);
    myName = name;
    myQualifiedName = fqn;
    myNamespace = ns;
  }

  public String getText() {
    return "";
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    throw new IncorrectOperationException("Don't ever call it!");
  }

  public PsiElement copy() {
    throw new IncorrectOperationException("cannot copy: nonphysical element");
  }

  @Override
  public PsiFile getContainingFile() {
    return ClojurePsiManager.getInstance(getProject()).getDummyFile();
  }

  @Nonnull
  public String getQualifiedName() {
    return myQualifiedName;
  }

  public PsiJavaPackage getParentPackage() {
    return null;
  }

  @Nonnull
  public PsiJavaPackage[] getSubPackages() {
    return new PsiJavaPackage[0];
  }

  @Nonnull
  public PsiJavaPackage[] getSubPackages(@Nonnull GlobalSearchScope scope) {
    return new PsiJavaPackage[0];
  }

  @Nonnull
  public PsiClass[] getClasses() {
    return new PsiClass[0];
  }

  @Nonnull
  public PsiClass[] getClasses(@Nonnull GlobalSearchScope scope) {
    return new PsiClass[0];
  }

  public PsiModifierList getAnnotationList() {
    return null;
  }

  public void handleQualifiedNameChange(@Nonnull String newQualifiedName) {
  }

  public VirtualFile[] occursInPackagePrefixes() {
    return new VirtualFile[0];
  }

  public String getName() {
    return myName;
  }

  public boolean containsClassNamed(String name) {
    return false;
  }

  @Nonnull
  public PsiClass[] findClassByShortName(@Nonnull String name, @Nonnull GlobalSearchScope scope) {
    return new PsiClass[0];
  }

  public PsiQualifiedNamedElement getContainer() {
    return null;
  }

  public PsiElement setName(@NonNls String name) throws IncorrectOperationException {
    throw new IncorrectOperationException("cannot set name: nonphysical element");
  }

  public void checkSetName(String name) throws IncorrectOperationException {
    throw new IncorrectOperationException("cannot set name: nonphysical element");
  }

  public PsiModifierList getModifierList() {
    return null;
  }

  public boolean hasModifierProperty(String name) {
    return false;
  }

  @Nonnull
  public PsiDirectory[] getDirectories() {
    return new PsiDirectory[0];
  }

  @Nonnull
  public PsiDirectory[] getDirectories(@Nonnull GlobalSearchScope scope) {
    return new PsiDirectory[0];
  }

  @Override
  public String toString() {
    return "ClojureSyntheticNamespace:"+ getQualifiedName();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      public String getPresentableText() {
        final String name = getName();
        return name == null ? "<undefined>" : name;
      }

      @Nullable
      public String getLocationString() {
        String name = getContainingFile().getName();
        //todo show namespace
        return "(in " + name + ")";
      }

      @Nullable
      public Image getIcon() {
        return IconDescriptorUpdaters.getIcon(ClSyntheticNamespace.this, Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
      }
    };
  }

  @Nonnull
  @Override
  public PsiElement getNavigationElement() {
    if (myNamespace != null) return myNamespace;
    return super.getNavigationElement();
  }
}

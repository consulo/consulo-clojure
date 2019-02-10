package org.jetbrains.plugins.clojure.psi.impl.javaView;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;

import org.jetbrains.plugins.clojure.compiler.ClojureCompilerSettings;
import org.jetbrains.plugins.clojure.psi.api.ClojureFile;
import org.jetbrains.plugins.clojure.psi.stubs.index.ClojureFullScriptNameIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ilyas
 */
public class ClojureClassFinder extends PsiElementFinder {
  private final Project myProject;

  public ClojureClassFinder(Project project) {
    myProject = project;
  }

  @Nullable
  public PsiClass findClass(@Nonnull String qualifiedName, @Nonnull GlobalSearchScope scope) {
    return getClassByFQName(qualifiedName, scope);
  }

  @Nonnull
  public PsiClass[] findClasses(@Nonnull String qualifiedName, @Nonnull GlobalSearchScope scope) {
    return getClassesByFQName(qualifiedName, scope);
  }

  @Nonnull
  public PsiClass[] getClasses(@Nonnull PsiJavaPackage psiPackage, @Nonnull GlobalSearchScope scope) {
    if (!ClojureCompilerSettings.getInstance(psiPackage.getProject()).getState().COMPILE_CLOJURE) return PsiClass.EMPTY_ARRAY;

    List<PsiClass> result = new ArrayList<PsiClass>();
    for (final PsiDirectory dir : psiPackage.getDirectories(scope)) {
      for (final PsiFile file : dir.getFiles()) {
        if (file instanceof ClojureFile) {
          ClojureFile clojureFile = (ClojureFile) file;
          if (clojureFile.isClassDefiningFile() && clojureFile.getPackageName().equals(psiPackage.getQualifiedName())) {
            result.add(clojureFile.getDefinedClass());
          }
        }
      }
    }

    return result.toArray(new PsiClass[result.size()]);
  }

  private Collection<PsiClass> getScriptClassesByFQName(final String name, final GlobalSearchScope scope) {
    Collection<ClojureFile> scripts = StubIndex.getInstance().get(ClojureFullScriptNameIndex.KEY, name.hashCode(), myProject, scope);

    scripts = ContainerUtil.findAll(scripts, new Condition<ClojureFile>() {
      public boolean value(final ClojureFile clojureFile) {
        final PsiClass clazz = clojureFile.getDefinedClass();
        return clojureFile.isClassDefiningFile() && clazz != null && name.equals(clazz.getQualifiedName());
      }
    });
    return ContainerUtil.map(scripts, new Function<ClojureFile, PsiClass>() {
      public PsiClass fun(final ClojureFile clojureFile) {
        return clojureFile.getDefinedClass();
      }
    });
  }

  @Nullable
  private PsiClass getClassByFQName(@Nonnull @NonNls String name, @Nonnull GlobalSearchScope scope) {
    if (!areClassesCompiled()) return null;

    final Collection<PsiClass> scriptClasses = getScriptClassesByFQName(name, scope);
    for (PsiClass clazz : scriptClasses) {
      if (name.equals(clazz.getQualifiedName())) return clazz;
    }
    return null;
  }

  @Nonnull
  private PsiClass[] getClassesByFQName(@Nonnull @NonNls String fqn, @Nonnull GlobalSearchScope scope) {
    if (!areClassesCompiled()) return PsiClass.EMPTY_ARRAY;

    final Collection<PsiClass> result = getScriptClassesByFQName(fqn, scope);
    ArrayList<PsiClass> filtered = new ArrayList<PsiClass>();
    for (PsiClass clazz : result) {
      if (fqn.equals(clazz.getQualifiedName())) {
        filtered.add(clazz);
      }
    }
    return filtered.isEmpty() ? PsiClass.EMPTY_ARRAY : filtered.toArray(new PsiClass[filtered.size()]);
  }

  private boolean areClassesCompiled() {
    ClojureCompilerSettings settings = ClojureCompilerSettings.getInstance(myProject);
    return settings.getState().COMPILE_CLOJURE;
  }
}
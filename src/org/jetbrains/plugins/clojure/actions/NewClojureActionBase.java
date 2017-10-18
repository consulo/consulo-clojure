package org.jetbrains.plugins.clojure.actions;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.utils.ClojureNamesUtil;
import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaPackage;
import com.intellij.psi.PsiNameHelper;
import com.intellij.util.IncorrectOperationException;
import consulo.clojure.module.extension.ClojureModuleExtension;
import consulo.psi.PsiPackage;

/**
 * @author ilyas
 */
public abstract class NewClojureActionBase extends CreateElementActionBase {

  @NonNls
  private static final String CLOJURE_EXTENSION = ".clj";

  public NewClojureActionBase(String text, String description, Icon icon) {
    super(text, description, icon);
  }

  @NotNull
  protected final PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
    MyInputValidator validator = new MyInputValidator(project, directory);
    Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

    return validator.getCreatedElements();
  }

  protected abstract String getDialogPrompt();

  protected abstract String getDialogTitle();

  public void update(final AnActionEvent event) {
    super.update(event);
    final Presentation presentation = event.getPresentation();
    Module module = event.getData(LangDataKeys.MODULE);

    if (module == null) {
      presentation.setEnabled(false);
      presentation.setVisible(false);
      return;
    }

    if (ModuleUtilCore.getExtension(module, ClojureModuleExtension.class) == null ||
        !presentation.isEnabled() ||
        !isUnderSourceRoots(event)) {
      presentation.setEnabled(false);
      presentation.setVisible(false);
    } else {
      presentation.setEnabled(true);
      presentation.setVisible(true);
    }

  }

  public static boolean isUnderSourceRoots(final AnActionEvent e) {
    final IdeView view = e.getData(LangDataKeys.IDE_VIEW);
    final Project project = e.getProject();
    if (view != null && project != null) {
      ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
      PsiDirectory[] dirs = view.getDirectories();
      for (PsiDirectory dir : dirs) {
        PsiJavaPackage aPackage = JavaDirectoryService.getInstance().getPackage(dir);
        if (projectFileIndex.isInSourceContent(dir.getVirtualFile()) && aPackage != null) {
          return true;
        }
      }
    }

    return false;
  }

  @NotNull
  protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
    return doCreate(newName, directory);
  }

  @NotNull
  protected abstract PsiElement[] doCreate(String newName, PsiDirectory directory) throws Exception;

  protected static PsiFile createFileFromTemplate(final PsiDirectory directory, String className, @NonNls String templateName,
                                                  @NonNls String... parameters) throws IncorrectOperationException {
    final String name = StringUtil.trimEnd(className, CLOJURE_EXTENSION);
    return ClojureTemplatesFactory.createFromTemplate(directory, name, name + CLOJURE_EXTENSION, templateName, parameters);
  }


  protected String getErrorTitle() {
    return CommonBundle.getErrorTitle();
  }

  protected void checkBeforeCreate(String newName, PsiDirectory directory) throws IncorrectOperationException {
    checkCreateFile(directory, newName);
  }

  public static void checkCreateFile(@NotNull PsiDirectory directory, String name) throws IncorrectOperationException {
    final String trimmedName = StringUtil.trimEnd(name, CLOJURE_EXTENSION);
    if (!ClojureNamesUtil.isIdentifier(trimmedName)) {
      throw new IncorrectOperationException(ClojureBundle.message("0.is.not.an.identifier", name));
    }

    String fileName = trimmedName + "." + CLOJURE_EXTENSION;
    directory.checkCreateFile(fileName);

    PsiNameHelper helper = JavaPsiFacade.getInstance(directory.getProject()).getNameHelper();
    PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(directory);
    String qualifiedName = aPackage == null ? null : aPackage.getQualifiedName();
    if (!StringUtil.isEmpty(qualifiedName) && !helper.isQualifiedName(qualifiedName)) {
      throw new IncorrectOperationException("Cannot create class in invalid package: '" + qualifiedName + "'");
    }
  }

}


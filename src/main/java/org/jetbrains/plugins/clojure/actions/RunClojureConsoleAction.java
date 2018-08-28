package org.jetbrains.plugins.clojure.actions;

import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.ClojureIcons;
import consulo.clojure.module.extension.ClojureModuleExtension;
import org.jetbrains.plugins.clojure.repl.ClojureConsoleRunner;

import java.util.Arrays;

/**
 * @author ilyas
 */
public class RunClojureConsoleAction extends AnAction implements DumbAware {

  public RunClojureConsoleAction() {
    super();
    getTemplatePresentation().setIcon(ClojureIcons.REPL_GO);
  }

  @Override
  public void update(AnActionEvent e) {
    final Module m = getModule(e);
    final Presentation presentation = e.getPresentation();
    if (m == null) {
      presentation.setEnabledAndVisible(false);
      return;
    }
    presentation.setEnabledAndVisible(true);
    super.update(e);
  }


  @Override
  public void actionPerformed(AnActionEvent event) {
    final Module module = getModule(event);
    assert module != null : "Module is null";
    final String path = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();

    final String title = ClojureBundle.message("repl.toolWindowName");
    try {
      ClojureConsoleRunner.run(module, path);
    } catch (CantRunException e) {
      ExecutionHelper.showErrors(module.getProject(), Arrays.<Exception>asList(e), title, null);
    }

  }

  static Module getModule(AnActionEvent e) {
    Module module = e.getData(LangDataKeys.MODULE);
    if (module == null) {
      final Project project = e.getData(LangDataKeys.PROJECT);
      if (project == null) {
        return null;
      }
      final Module[] modules = ModuleManager.getInstance(project).getModules();
      if (modules.length == 1) {
        module = modules[0];

      } else {
        for (Module m : modules) {
          if (ModuleUtilCore.getExtension(m, ClojureModuleExtension.class) != null) {
            module = m;
            break;
          }
        }
      }
    }

    if (module != null && ModuleUtilCore.getExtension(module, ClojureModuleExtension.class) == null) {
      module = null;
    }
    return module;
  }
}

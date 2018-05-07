package org.jetbrains.plugins.clojure.compiler;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.Nls;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

/**
 * ilyas
 */
public class ClojureCompilerConfigurable implements Configurable {
  private JPanel myPanel;
  private JCheckBox myClojureBeforeCheckBox;
  private JCheckBox myCompileTaggedCb;
  private JCheckBox myCopySourcesCb;
  private ClojureCompilerSettings mySettings;

  public ClojureCompilerConfigurable(Project project) {
    mySettings = ClojureCompilerSettings.getInstance(project);
  }

  @Nls
  public String getDisplayName() {
    return "Clojure Compiler";
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    return myPanel;
  }

  public boolean isModified() {
    return mySettings.getState().CLOJURE_BEFORE != myClojureBeforeCheckBox.isSelected() ||
        mySettings.getState().COMPILE_CLOJURE != myCompileTaggedCb.isSelected() ||
        mySettings.getState().COPY_CLJ_SOURCES != myCopySourcesCb.isSelected();
  }

  public void apply() throws ConfigurationException {
    /*if (myClojureBeforeCheckBox.isSelected() && mySettings.getState().CLOJURE_BEFORE != myClojureBeforeCheckBox.isSelected()) {
      for (ClojureCompiler compiler: CompilerManager.getInstance(myProject).getCompilers(ClojureCompiler.class)) {
        CompilerManager.getInstance(myProject).removeCompiler(compiler);
      }
      HashSet<FileType> inputSet = new HashSet<FileType>(Arrays.asList(ClojureFileType.CLOJURE_FILE_TYPE, StdFileTypes.JAVA));
      HashSet<FileType> outputSet = new HashSet<FileType>(Arrays.asList(StdFileTypes.JAVA, StdFileTypes.CLASS));
      CompilerManager.getInstance(myProject).addTranslatingCompiler(new ClojureCompiler(myProject), inputSet, outputSet);
    } else if (!myClojureBeforeCheckBox.isSelected() && mySettings.getState().CLOJURE_BEFORE != myClojureBeforeCheckBox.isSelected()){
      for (ClojureCompiler compiler: CompilerManager.getInstance(myProject).getCompilers(ClojureCompiler.class)) {
        CompilerManager.getInstance(myProject).removeCompiler(compiler);
      }
      CompilerManager.getInstance(myProject).addCompiler(new ClojureCompiler(myProject));
    }      */

    mySettings.getState().CLOJURE_BEFORE = myClojureBeforeCheckBox.isSelected();
    mySettings.getState().COMPILE_CLOJURE = myCompileTaggedCb.isSelected();
    mySettings.getState().COPY_CLJ_SOURCES = myCopySourcesCb.isSelected();
  }

  public void reset() {
    myClojureBeforeCheckBox.setSelected(mySettings.getState().CLOJURE_BEFORE);
    myCompileTaggedCb.setSelected(mySettings.getState().COMPILE_CLOJURE);
    myCopySourcesCb.setSelected(mySettings.getState().COPY_CLJ_SOURCES);
  }

  public void disposeUIResources() {
  }
}

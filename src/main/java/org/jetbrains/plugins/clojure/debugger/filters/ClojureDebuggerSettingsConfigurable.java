package org.jetbrains.plugins.clojure.debugger.filters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.Nls;
import org.jetbrains.plugins.clojure.ClojureBundle;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

/**
 * @author ilyas
 */
public class ClojureDebuggerSettingsConfigurable implements Configurable {
  private JCheckBox myIgnoreClojureMethods;
  private JPanel myPanel;
  private boolean isModified = false;
  private final ClojureDebuggerSettings mySettings;

  public ClojureDebuggerSettingsConfigurable(final ClojureDebuggerSettings settings) {
    mySettings = settings;
    final Boolean flag = settings.DEBUG_DISABLE_SPECIFIC_CLOJURE_METHODS;
    myIgnoreClojureMethods.setSelected(flag == null || flag);

    myIgnoreClojureMethods.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        isModified = mySettings.DEBUG_DISABLE_SPECIFIC_CLOJURE_METHODS != myIgnoreClojureMethods.isSelected();
      }
    });
  }

  @Nls
  public String getDisplayName() {
    return ClojureBundle.message("settings.clojure.debug.caption");
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    return myPanel;
  }

  public boolean isModified() {
    return isModified;
  }

  public void apply() throws ConfigurationException {
    if (isModified) {
      mySettings.DEBUG_DISABLE_SPECIFIC_CLOJURE_METHODS = myIgnoreClojureMethods.isSelected();
    }
    isModified = false;
  }

  public void reset() {
    final Boolean flag = mySettings.DEBUG_DISABLE_SPECIFIC_CLOJURE_METHODS;
    myIgnoreClojureMethods.setSelected(flag == null || flag);
  }

  public void disposeUIResources() {
  }}


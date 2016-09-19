/*
 * Copyright 2000-2008 JetBrains s.r.o.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.plugins.clojure.config.ui;

import consulo.clojure.module.extension.ClojureMutableModuleExtension;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author ilyas
 */
public class ClojureFacetSettingsTab  extends JPanel{
  private JPanel myPanel;
  private JTextField myJvmOpts;
  private JTextField myReplOpts;
  private JTextField myReplClass;
  private JPanel myReplPanel;

  private final ClojureMutableModuleExtension myMutableModuleExtension;

  public ClojureFacetSettingsTab(ClojureMutableModuleExtension mutableModuleExtension) {

    myMutableModuleExtension = mutableModuleExtension;

    myJvmOpts.setText(myMutableModuleExtension.getJvmOpts());
    myReplClass.setText(myMutableModuleExtension.getReplClass());
    myReplOpts.setText(myMutableModuleExtension.getReplOpts());

    myJvmOpts.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        myMutableModuleExtension.setJvmOpts(myJvmOpts.getText());
      }
    });

    myReplClass.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        myMutableModuleExtension.setReplClass(myReplClass.getText());
      }
    });

    myReplOpts.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        myMutableModuleExtension.setReplOpts(myReplOpts.getText());
      }
    });
  }

  private void createUIComponents() {
    myPanel = this;
  }
}

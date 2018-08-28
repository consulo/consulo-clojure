/*
 * Copyright 2009 JetBrains s.r.o.
 *
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
package org.jetbrains.plugins.clojure.settings;

import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import consulo.annotations.RequiredDispatchThread;

/**
 * Project specific settings.
 * <p>
 * This is the extension implementation.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @see ClojureProjectSettings
 */
public final class ClojureConfigurable implements Configurable
{
	private final Project myProject;
	private ClojureProjectSettingsForm mySettingsForm;

	public ClojureConfigurable(Project project)
	{
		myProject = project;
	}

	// Configurable =============================================================

	@Nls
	public String getDisplayName()
	{
		return "Clojure";
	}

	public String getHelpTopic()
	{
		return null;
	}

	@RequiredDispatchThread
	public JComponent createComponent()
	{
		if(mySettingsForm == null)
		{
			mySettingsForm = new ClojureProjectSettingsForm(myProject);
		}
		return mySettingsForm.getComponent();
	}

	@RequiredDispatchThread
	public boolean isModified()
	{
		return mySettingsForm.isModified();
	}

	@RequiredDispatchThread
	public void apply() throws ConfigurationException
	{
		ClojureProjectSettings settings = ClojureProjectSettings.getInstance(myProject);
		settings.coloredParentheses = mySettingsForm.isColoredParentheses();
	}

	@RequiredDispatchThread
	public void reset()
	{
		if(mySettingsForm != null)
		{
			mySettingsForm.reset();
		}
	}

	@RequiredDispatchThread
	public void disposeUIResources()
	{
		mySettingsForm = null;
	}
}

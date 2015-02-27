package org.jetbrains.plugins.clojure.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.file.ClojureFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

/**
 * @author ilyas
 */
public class ClojurePsiManager implements ProjectComponent
{
	private final Project myProject;
	private PsiFile myDummyFile;

	public ClojurePsiManager(Project project)
	{
		myProject = project;
	}

	public void projectOpened()
	{
	}

	public void projectClosed()
	{
	}

	@NotNull
	public String getComponentName()
	{
		return "ClojurePsiManager";
	}

	public void initComponent()
	{
		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				myDummyFile = PsiFileFactory.getInstance(myProject).createFileFromText("dummy." + ClojureFileType.INSTANCE
						.getDefaultExtension(), "");
			}
		});
	}

	public void disposeComponent()
	{
	}

	public static ClojurePsiManager getInstance(Project project)
	{
		return project.getComponent(ClojurePsiManager.class);
	}

	public PsiFile getDummyFile()
	{
		return myDummyFile;
	}
}

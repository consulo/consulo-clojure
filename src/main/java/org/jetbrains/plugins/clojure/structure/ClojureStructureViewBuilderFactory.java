package org.jetbrains.plugins.clojure.structure;

import org.jetbrains.annotations.NotNull;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

/**
 * @author ilyas
 */
public class ClojureStructureViewBuilderFactory implements PsiStructureViewFactory
{
	@Override
	public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile)
	{
		return new TreeBasedStructureViewBuilder()
		{
			@Override
			@NotNull
			public StructureViewModel createStructureViewModel(Editor editor)
			{
				return new ClojureStructureViewModel(psiFile);
			}

			@Override
			public boolean isRootNodeShown()
			{
				return false;
			}
		};
	}
}
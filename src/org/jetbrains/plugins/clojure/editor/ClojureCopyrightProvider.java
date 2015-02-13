package org.jetbrains.plugins.clojure.editor;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.copyright.config.CopyrightFileConfig;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightsProvider;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;
import com.maddyhome.idea.copyright.ui.TemplateCommentPanel;

/**
 * @author peter
 */
public class ClojureCopyrightProvider extends UpdateCopyrightsProvider<CopyrightFileConfig>
{
	@NotNull
	@Override
	public UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(@NotNull PsiFile file, @NotNull CopyrightProfile copyrightProfile)
	{
		return new UpdatePsiFileCopyright<CopyrightFileConfig>(file, copyrightProfile)
		{
			@Override
			protected void scanFile()
			{
				PsiElement first = getFile().getFirstChild();
				PsiElement last = first;
				PsiElement next = first;
				while(next != null)
				{
					if(next instanceof PsiComment || next instanceof PsiWhiteSpace)
					{
						next = getNextSibling(next);
					}
					else
					{
						break;
					}
					last = next;
				}

				if(first != null)
				{
					checkComments(first, last, true);
				}

			}
		};
	}

	@NotNull
	@Override
	public CopyrightFileConfig createDefaultOptions()
	{
		return new CopyrightFileConfig();
	}

	@NotNull
	@Override
	public TemplateCommentPanel createConfigurable(@NotNull Project project, @NotNull TemplateCommentPanel parentPane, @NotNull FileType fileType)
	{
		return new TemplateCommentPanel(fileType, parentPane, project);
	}
}

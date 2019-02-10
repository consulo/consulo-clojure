package org.jetbrains.plugins.clojure.editor.braceHighlighter;

import javax.annotation.Nonnull;
import org.jetbrains.plugins.clojure.parser.ClojureElementTypes;
import org.jetbrains.plugins.clojure.psi.api.ClList;
import org.jetbrains.plugins.clojure.settings.ClojureProjectSettings;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author ilyas
 */
public class ClojureBraceHighlighter implements Annotator
{
	public void annotate(@Nonnull PsiElement psiElement, @Nonnull AnnotationHolder annotationHolder)
	{
		if(psiElement instanceof LeafPsiElement &&
				ClojureProjectSettings.getInstance(psiElement.getProject()).coloredParentheses)
		{
			IElementType type = ((LeafPsiElement) psiElement).getElementType();
			if(type == ClojureElementTypes.LEFT_PAREN || type == ClojureElementTypes.RIGHT_PAREN)
			{
				int level = getLevel(psiElement);
				if(level >= 0)
				{
					final EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
					TextAttributes attrs = ClojureBraceAttributes.getBraceAttributes(level, scheme.getDefaultBackground());
					annotationHolder.createInfoAnnotation(psiElement, null).setEnforcedTextAttributes(attrs);
				}
			}
		}
	}

	private static int getLevel(PsiElement psiElement)
	{
		int level = -1;
		PsiElement eachParent = psiElement;
		while(eachParent != null)
		{
			if(eachParent instanceof ClList)
			{
				level++;
			}
			eachParent = eachParent.getParent();
		}
		return level;
	}

}

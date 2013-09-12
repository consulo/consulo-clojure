package org.jetbrains.plugins.clojure;

import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDefMethod;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import org.jetbrains.plugins.clojure.psi.impl.ns.ClSyntheticNamespace;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class ClojureIconDescriptorUpdater implements IconDescriptorUpdater {
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags) {
		if(element instanceof ClSyntheticNamespace) {
			iconDescriptor.setMainIcon(ClojureIcons.NAMESPACE);
		}
		else if(element instanceof ClSymbol) {
			iconDescriptor.setMainIcon(ClojureIcons.SYMBOL);
		}
		else if(element instanceof ClDef) {
			iconDescriptor.setMainIcon(ClojureIcons.FUNCTION);
		}
		else if(element instanceof ClDefMethod) {
			iconDescriptor.setMainIcon(ClojureIcons.METHOD);
		}
	}
}

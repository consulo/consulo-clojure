package org.jetbrains.plugins.clojure.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.ClojureIcons;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 14:38/12.06.13
 */
public class ClojureModuleExtensionProvider implements ModuleExtensionProvider<ClojureModuleExtension, ClojureMutableModuleExtension>
{
	@Override
	@Nullable
	public Icon getIcon()
	{
		return ClojureIcons.CLOJURE_ICON_16x16;
	}

	@Override
	@NotNull
	public String getName()
	{
		return "Clojure";
	}

	@Override
	@NotNull
	public ClojureModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new ClojureModuleExtension(s, module);
	}

	@Override
	@NotNull
	public ClojureMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new ClojureMutableModuleExtension(s, module);
	}
}

package org.jetbrains.plugins.clojure.module.extension;

import org.consulo.module.extension.impl.ModuleExtensionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.utils.ClojureUtils;
import com.intellij.openapi.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 14:37/12.06.13
 */
public class ClojureModuleExtension extends ModuleExtensionImpl<ClojureModuleExtension>
{
	@NotNull
	protected String myReplClass = ClojureUtils.CLOJURE_MAIN;

	@NotNull
	protected String myJvmOpts = ClojureUtils.CLOJURE_DEFAULT_JVM_PARAMS;

	@NotNull
	protected String myReplOpts = "";

	public ClojureModuleExtension(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@NotNull
	public String getReplClass()
	{
		return myReplClass;
	}

	@NotNull
	public String getJvmOpts()
	{
		return myJvmOpts;
	}

	@NotNull
	public String getReplOpts()
	{
		return myReplOpts;
	}
}

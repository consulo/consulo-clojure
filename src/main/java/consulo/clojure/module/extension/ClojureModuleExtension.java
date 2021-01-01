package consulo.clojure.module.extension;

import consulo.module.extension.impl.ModuleExtensionImpl;
import consulo.roots.ModuleRootLayer;
import org.jetbrains.plugins.clojure.utils.ClojureUtils;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 14:37/12.06.13
 */
public class ClojureModuleExtension extends ModuleExtensionImpl<ClojureModuleExtension>
{
	@Nonnull
	protected String myReplClass = ClojureUtils.CLOJURE_MAIN;

	@Nonnull
	protected String myJvmOpts = ClojureUtils.CLOJURE_DEFAULT_JVM_PARAMS;

	@Nonnull
	protected String myReplOpts = "";

	public ClojureModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	@Nonnull
	public String getReplClass()
	{
		return myReplClass;
	}

	@Nonnull
	public String getJvmOpts()
	{
		return myJvmOpts;
	}

	@Nonnull
	public String getReplOpts()
	{
		return myReplOpts;
	}
}

package consulo.clojure.module.extension;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.config.ui.ClojureFacetSettingsTab;
import com.intellij.openapi.util.Comparing;
import consulo.module.extension.MutableModuleExtension;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 14:37/12.06.13
 */
public class ClojureMutableModuleExtension extends ClojureModuleExtension implements MutableModuleExtension<ClojureModuleExtension>
{
	public ClojureMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	public void setReplClass(@NotNull String replClass)
	{
		myReplClass = replClass;
	}

	public void setJvmOpts(@NotNull String jvmOpts)
	{
		myJvmOpts = jvmOpts;
	}

	public void setReplOpts(@NotNull String replOpts)
	{
		myReplOpts = replOpts;
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return new ClojureFacetSettingsTab(this);
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@NotNull ClojureModuleExtension extension)
	{
		boolean modified = false;
		modified |= isEnabled() != extension.isEnabled();
		modified |= !Comparing.equal(getJvmOpts(), extension.getJvmOpts());
		modified |= !Comparing.equal(getReplClass(), extension.getReplClass());
		modified |= !Comparing.equal(getReplOpts(), extension.getReplOpts());
		return modified;
	}
}

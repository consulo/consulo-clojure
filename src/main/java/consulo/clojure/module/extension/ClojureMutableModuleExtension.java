package consulo.clojure.module.extension;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import javax.annotation.Nullable;
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
	public ClojureMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
	}

	public void setReplClass(@Nonnull String replClass)
	{
		myReplClass = replClass;
	}

	public void setJvmOpts(@Nonnull String jvmOpts)
	{
		myJvmOpts = jvmOpts;
	}

	public void setReplOpts(@Nonnull String replOpts)
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
	public boolean isModified(@Nonnull ClojureModuleExtension extension)
	{
		boolean modified = false;
		modified |= isEnabled() != extension.isEnabled();
		modified |= !Comparing.equal(getJvmOpts(), extension.getJvmOpts());
		modified |= !Comparing.equal(getReplClass(), extension.getReplClass());
		modified |= !Comparing.equal(getReplOpts(), extension.getReplOpts());
		return modified;
	}
}

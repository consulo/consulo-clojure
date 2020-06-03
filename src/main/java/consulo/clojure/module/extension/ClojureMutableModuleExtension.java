package consulo.clojure.module.extension;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import javax.annotation.Nullable;
import org.jetbrains.plugins.clojure.config.ui.ClojureFacetSettingsTab;
import com.intellij.openapi.util.Comparing;
import consulo.disposer.Disposable;
import consulo.module.extension.MutableModuleExtension;
import consulo.module.extension.swing.SwingMutableModuleExtension;
import consulo.roots.ModuleRootLayer;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;

/**
 * @author VISTALL
 * @since 14:37/12.06.13
 */
public class ClojureMutableModuleExtension extends ClojureModuleExtension implements MutableModuleExtension<ClojureModuleExtension>, SwingMutableModuleExtension
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

	@RequiredUIAccess
	@Nullable
	@Override
	public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		return VerticalLayout.create().add(Label.create("Unsupported platform"));
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
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

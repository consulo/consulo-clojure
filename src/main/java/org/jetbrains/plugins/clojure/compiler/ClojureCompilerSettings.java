package org.jetbrains.plugins.clojure.compiler;

import javax.annotation.Nonnull;

import org.jetbrains.jps.clojure.model.impl.JpsClojureCompilerSettingsState;
import org.jetbrains.jps.clojure.model.impl.JpsClojureModelSerializerExtension;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * @author ilyas
 */
@State(name = JpsClojureModelSerializerExtension.CLOJURE_COMPILER_SETTINGS_COMPONENT_NAME, storages = {
		@Storage(file = "$PROJECT_CONFIG_DIR$/" + JpsClojureModelSerializerExtension.CLOJURE_COMPILER_SETTINGS_FILE)
})
public class ClojureCompilerSettings implements PersistentStateComponent<JpsClojureCompilerSettingsState>
{
	@Nonnull
	public static ClojureCompilerSettings getInstance(@Nonnull Project project)
	{
		return ServiceManager.getService(project, ClojureCompilerSettings.class);
	}

	private JpsClojureCompilerSettingsState myState = new JpsClojureCompilerSettingsState();

	public JpsClojureCompilerSettingsState getState()
	{
		return myState;
	}

	public void loadState(JpsClojureCompilerSettingsState state)
	{
		XmlSerializerUtil.copyBean(state, myState);
	}

}

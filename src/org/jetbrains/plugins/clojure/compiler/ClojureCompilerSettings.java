package org.jetbrains.plugins.clojure.compiler;

import org.jetbrains.jps.clojure.model.impl.JpsClojureCompilerSettingsState;
import org.jetbrains.jps.clojure.model.impl.JpsClojureModelSerializerExtension;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.util.xmlb.XmlSerializerUtil;
import consulo.lombok.annotations.ProjectService;

/**
 * @author ilyas
 */
@State(
  name = JpsClojureModelSerializerExtension.CLOJURE_COMPILER_SETTINGS_COMPONENT_NAME,
  storages = {
    @Storage(id = "default", file = "$PROJECT_FILE$")
   ,@Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/" + JpsClojureModelSerializerExtension.CLOJURE_COMPILER_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
    }
)
@ProjectService
public class ClojureCompilerSettings implements PersistentStateComponent<JpsClojureCompilerSettingsState> {
  private JpsClojureCompilerSettingsState myState = new JpsClojureCompilerSettingsState();

  public JpsClojureCompilerSettingsState getState() {
    return myState;
  }
                
  public void loadState(JpsClojureCompilerSettingsState state) {
    XmlSerializerUtil.copyBean(state, myState);
  }

}

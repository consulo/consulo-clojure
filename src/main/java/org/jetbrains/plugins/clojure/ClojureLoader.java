package org.jetbrains.plugins.clojure;

import clojure.lang.RT;
import clojure.lang.Var;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by IntelliJ IDEA.
 * User: peter
 * Date: Jan 16, 2009
 * Time: 4:34:18 PM
 * Copyright 2007, 2008, 2009 Red Shark Technology
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClojureLoader {
  static {
    adjustClojureCompilerLoader();
  }

  private static void adjustClojureCompilerLoader() {
    // Hack in order to adjust Clojure ClassLoaders with PluginClassLoader
    final Application application = ApplicationManager.getApplication();
    final ClassLoader loader = ClojureLoader.class.getClassLoader();

    final Runnable runnable = new Runnable() {
      public void run() {
        final Thread thread = new Thread() {
          @Override
          public void run() {
            new RT();                          // dummy

            application.invokeLater(new Runnable() {
              public void run() {
                Var.pushThreadBindings(RT.map(clojure.lang.Compiler.LOADER, loader));
              }
            });
          }
        };
        thread.setContextClassLoader(loader);
        thread.start();
      }
    };

    application.invokeLater(runnable);

  }
}

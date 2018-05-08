
/**
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
package org.jetbrains.plugins.clojure;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;

public interface ClojureIcons {
  @NonNls
  final Image CLOJURE_ICON_16x16 = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/clojure_icon_16x16.png");

  final Image FUNCTION = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/def_tmp.png");
  final Image METHOD = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/meth_tmp.png");
  final Image JAVA_METHOD = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/method.png");
  final Image JAVA_FIELD = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/field.png");
  final Image SYMBOL = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/symbol.png");
  final Image NAMESPACE = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/namespace.png");

  final Icon REPL_CONSOLE = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_console.png");
  final Icon REPL_ADD = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_add.png");
  final Icon REPL_CLOSE = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_close.png");
  final Icon REPL_LOAD = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_run.png");
  final Icon REPL_GO = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_go.png");
  final Icon REPL_EVAL = IconLoader.findIcon("/org/jetbrains/plugins/clojure/icons/repl_eval.png");
}
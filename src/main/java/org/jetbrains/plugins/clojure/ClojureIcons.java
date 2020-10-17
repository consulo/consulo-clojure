
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

import consulo.clojure.icon.ClojureIconGroup;
import consulo.ui.image.Image;

public interface ClojureIcons {
  final Image CLOJURE_ICON_16x16 = ClojureIconGroup.clojure_icon_16x16();

  final Image FUNCTION = ClojureIconGroup.def_tmp();
  final Image METHOD = ClojureIconGroup.meth_tmp();
  final Image JAVA_METHOD = ClojureIconGroup.method();
  final Image JAVA_FIELD = ClojureIconGroup.field();
  final Image SYMBOL = ClojureIconGroup.symbol();
  final Image NAMESPACE = ClojureIconGroup.namespace();

  final Image REPL_CONSOLE = ClojureIconGroup.repl_console();
  final Image REPL_ADD = ClojureIconGroup.repl_add();
  final Image REPL_CLOSE = ClojureIconGroup.repl_close();
  final Image REPL_LOAD = ClojureIconGroup.repl_run();
  final Image REPL_GO = ClojureIconGroup.repl_go();
  final Image REPL_EVAL = ClojureIconGroup.repl_eval();
}
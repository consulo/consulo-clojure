package org.jetbrains.plugins.clojure.runner.console;

import javax.annotation.Nonnull;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;

/**
 * @author ilyas
 */
public class ClojureConsoleFilterProvider implements ConsoleFilterProvider {
  @Nonnull
  public Filter[] getDefaultFilters(@Nonnull Project project) {
    return new Filter[]{new ClojureFilter(project)};
  }
}

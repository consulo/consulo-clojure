package org.jetbrains.plugins.clojure.findUsages;

import javax.annotation.Nonnull;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.RequestResultProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.UsageSearchContext;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import org.jetbrains.plugins.clojure.file.ClojureFileType;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;

/**
 * @author ilyas
 */
public class ClojureReferenceSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
  public ClojureReferenceSearcher() {
    super(true);
  }

  @Override
  public void processQuery(@Nonnull ReferencesSearch.SearchParameters params, @Nonnull Processor<PsiReference> consumer) {
    final PsiElement elem = params.getElementToSearch();
    SearchScope scope = params.getEffectiveSearchScope();
    if (elem instanceof PsiNamedElement
        /* An optimization for Java refactorings */
        && !(elem instanceof PsiVariable)) {
      final PsiNamedElement symbolToSearch = (PsiNamedElement) elem;
      final String name = symbolToSearch.getName();
      if (name != null) {
        RequestResultProcessor processor = new RequestResultProcessor() {
          @Override
          public boolean processTextOccurrence(@Nonnull PsiElement element, int offsetInElement, @Nonnull Processor<PsiReference> consumer) {
            if (element instanceof ClSymbol) {
              ClSymbol refSymbol = (ClSymbol) element;
              for (PsiReference ref : refSymbol.getReferences()) {
                if (ref.getRangeInElement().contains(offsetInElement) &&
                    // atom may refer to definition or to the symbol in it
                    (ref.resolve() == symbolToSearch ||
                        ref.resolve() == symbolToSearch.getParent())) {
                  if (!consumer.process(ref)) return false;
                }
              }
            }
            return true;
          }
        };
        if (scope instanceof GlobalSearchScope) {
          scope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope) scope, ClojureFileType.CLOJURE_FILE_TYPE);
        }
        for (String word : StringUtil.getWordsIn(name)) {
          params.getOptimizer().searchWord(word, scope, UsageSearchContext.ANY, true, processor);
        }
      }
    }
  }
}

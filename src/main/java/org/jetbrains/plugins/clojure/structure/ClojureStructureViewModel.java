package org.jetbrains.plugins.clojure.structure;

import javax.annotation.Nonnull;

import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.psi.PsiFile;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;

/**
 * @author ilyas
 */
public class ClojureStructureViewModel extends TextEditorBasedStructureViewModel {
    private PsiFile myFile;

    public ClojureStructureViewModel(final PsiFile file) {
        super(file);
        myFile = file;
    }

    @Nonnull
    public StructureViewTreeElement getRoot() {
        return new ClojureStructureViewElement(myFile);
    }

    @Nonnull
    public Grouper[] getGroupers() {
        return Grouper.EMPTY_ARRAY;
    }

    @Nonnull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Nonnull
    public Filter[] getFilters() {
        return Filter.EMPTY_ARRAY;
    }

    protected PsiFile getPsiFile() {
        return myFile;
    }

    @Nonnull
    protected Class[] getSuitableClasses() {
        return new Class[]{ClDef.class};
    }
}

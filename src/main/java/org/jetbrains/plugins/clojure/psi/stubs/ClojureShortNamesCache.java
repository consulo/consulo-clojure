package org.jetbrains.plugins.clojure.psi.stubs;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;

import org.jetbrains.plugins.clojure.compiler.ClojureCompilerSettings;
import org.jetbrains.plugins.clojure.psi.api.ClojureFile;
import org.jetbrains.plugins.clojure.psi.api.ns.ClNs;
import org.jetbrains.plugins.clojure.psi.stubs.index.ClojureClassNameIndex;
import org.jetbrains.plugins.clojure.psi.stubs.index.ClojureNsNameIndex;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Function;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashSet;
import com.intellij.util.indexing.IdFilter;

/**
 * @author ilyas
 */
public class ClojureShortNamesCache extends PsiShortNamesCache
{

	Project myProject;

	public ClojureShortNamesCache(Project project)
	{
		myProject = project;
	}

	public static ClojureShortNamesCache getInstance(Project project)
	{
		return new ClojureShortNamesCache(project);
	}

	public ClNs[] getNsByQualifiedName(String qualifiedName, GlobalSearchScope scope)
	{
		final Collection<? extends PsiElement> clNses = StubIndex.getInstance().get(ClojureNsNameIndex.KEY, qualifiedName, myProject, scope);
		ArrayList<ClNs> result = new ArrayList<ClNs>();
		for(PsiElement clNs : clNses)
		{
			if(clNs instanceof ClNs)
			{
				if(((ClNs) clNs).getName().equals(qualifiedName))
				{
					result.add((ClNs) clNs);
				}
			}
		}
		return result.toArray(new ClNs[result.size()]);
	}

	@Nonnull
	public PsiFile[] getFilesByName(@Nonnull String name)
	{
		return new PsiFile[0];
	}

	@Nonnull
	public String[] getAllFileNames()
	{
		return FilenameIndex.getAllFilenames(myProject);
	}

	private boolean areClassesCompiled()
	{
		ClojureCompilerSettings settings = ClojureCompilerSettings.getInstance(myProject);
		return settings.getState().COMPILE_CLOJURE;
	}

	@Nonnull
	public PsiClass[] getClassesByName(@Nonnull String name, @Nonnull GlobalSearchScope scope)
	{
		if(!areClassesCompiled())
		{
			return PsiClass.EMPTY_ARRAY;
		}

		Collection<PsiClass> allClasses = getAllScriptClasses(name, scope);
		if(allClasses.isEmpty())
		{
			return PsiClass.EMPTY_ARRAY;
		}
		return allClasses.toArray(new PsiClass[allClasses.size()]);
	}

	private Collection<PsiClass> getAllScriptClasses(String name, GlobalSearchScope scope)
	{
		if(!areClassesCompiled())
		{
			return new ArrayList<PsiClass>();
		}

		Collection<ClojureFile> files = StubIndex.getInstance().get(ClojureClassNameIndex.KEY, name, myProject, scope);
		files = ContainerUtil.findAll(files, new Condition<ClojureFile>()
		{
			public boolean value(ClojureFile clojureFile)
			{
				return clojureFile.isClassDefiningFile();
			}
		});
		return ContainerUtil.map(files, new Function<ClojureFile, PsiClass>()
		{
			public PsiClass fun(ClojureFile clojureFile)
			{
				assert clojureFile.isClassDefiningFile();
				return clojureFile.getDefinedClass();
			}
		});
	}

	@Nonnull
	public String[] getAllClassNames()
	{
		if(!areClassesCompiled())
		{
			return new String[0];
		}

		final Collection<String> classNames = StubIndex.getInstance().getAllKeys(ClojureClassNameIndex.KEY, myProject);
		return classNames.toArray(new String[classNames.size()]);
	}

	public void getAllClassNames(@Nonnull HashSet<String> dest)
	{
		if(!areClassesCompiled())
		{
			return;
		}

		final Collection<String> classNames = StubIndex.getInstance().getAllKeys(ClojureClassNameIndex.KEY, myProject);
		dest.addAll(classNames);
	}

	@Nonnull
	public PsiMethod[] getMethodsByName(@NonNls String name, @Nonnull GlobalSearchScope scope)
	{
		return new PsiMethod[0];
	}

	@Nonnull
	public PsiMethod[] getMethodsByNameIfNotMoreThan(@NonNls String name, @Nonnull GlobalSearchScope scope, int maxCount)
	{
		return new PsiMethod[0];
	}

	@Override
	public boolean processMethodsWithName(@NonNls @Nonnull String s, @Nonnull GlobalSearchScope globalSearchScope, @Nonnull Processor<PsiMethod> psiMethodProcessor)
	{
		return false;
	}

	@Override
	public boolean processMethodsWithName(@NonNls @Nonnull String s, @Nonnull Processor<? super PsiMethod> processor, @Nonnull GlobalSearchScope searchScope, @Nullable IdFilter idFilter)
	{
		return false;
	}

	@Nonnull
	public String[] getAllMethodNames()
	{
		return new String[0];
	}

	public void getAllMethodNames(@Nonnull HashSet<String> set)
	{
	}

	@Nonnull
	public PsiField[] getFieldsByName(@Nonnull String name, @Nonnull GlobalSearchScope scope)
	{
		return new PsiField[0];
	}

	@Nonnull
	public String[] getAllFieldNames()
	{
		return new String[0];
	}

	public void getAllFieldNames(@Nonnull HashSet<String> set)
	{
	}

	@Override
	public boolean processFieldsWithName(@Nonnull String s, @Nonnull Processor<? super PsiField> processor, @Nonnull GlobalSearchScope searchScope, @Nullable IdFilter idFilter)
	{
		return false;
	}

	@Override
	public boolean processClassesWithName(@Nonnull String s, @Nonnull Processor<? super PsiClass> processor, @Nonnull GlobalSearchScope searchScope, @Nullable IdFilter idFilter)
	{
		return false;
	}

	@Nonnull
	@Override
	public PsiField[] getFieldsByNameIfNotMoreThan(@NonNls @Nonnull String s, @Nonnull GlobalSearchScope globalSearchScope, int i)
	{
		return new PsiField[0];
	}
}

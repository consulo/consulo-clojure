package org.jetbrains.plugins.clojure.debugger;

import org.jetbrains.plugins.clojure.file.ClojureFileType;
import com.intellij.debugger.engine.JVMDebugProvider;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 17.08.14
 */
public class ClojureJVMDebugProvider implements JVMDebugProvider
{
	@Override
	public boolean supportsJVMDebugging(PsiFile psiFile)
	{
		return psiFile.getFileType() == ClojureFileType.INSTANCE;
	}
}

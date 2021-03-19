package org.jetbrains.plugins.clojure.compiler;

import com.intellij.compiler.OutputParser;
import com.intellij.compiler.impl.ModuleChunk;
import com.intellij.compiler.impl.javaCompiler.BackendCompiler;
import com.intellij.compiler.impl.javaCompiler.javac.JavacCompilerConfiguration;
import com.intellij.compiler.impl.javaCompiler.javac.JpsJavaCompilerOptions;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.io.URLUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.clojure.module.extension.ClojureModuleExtension;
import consulo.java.compiler.JavaCompilerUtil;
import consulo.java.compiler.impl.javaCompiler.BackendCompilerProcessBuilder;
import consulo.java.module.extension.JavaModuleExtension;
import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.file.ClojureFileType;
import org.jetbrains.plugins.clojure.psi.api.ClojureFile;
import org.jetbrains.plugins.clojure.utils.ClojureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ilyas
 */
public class ClojureBackendCompiler implements BackendCompiler
{

	private static final Logger LOG = Logger.getInstance("#org.jetbrains.plugins.clojure.compiler.ClojureBackendCompiler");

	private final Project myProject;

	public ClojureBackendCompiler(Project project)
	{
		myProject = project;
	}

	@Override
	public boolean checkCompiler(CompileScope scope)
	{
		VirtualFile[] files = scope.getFiles(ClojureFileType.INSTANCE, true);
		if(files.length == 0)
		{
			return true;
		}

		final ProjectFileIndex index = ProjectRootManager.getInstance(myProject).getFileIndex();
		Set<Module> modules = new HashSet<Module>();
		for(VirtualFile file : files)
		{
			Module module = index.getModuleForFile(file);
			if(module != null)
			{
				modules.add(module);
			}
		}

		for(Module module : modules)
		{
			if(ModuleUtilCore.getExtension(module, ClojureModuleExtension.class) == null)
			{
				Messages.showErrorDialog(myProject, ClojureBundle.message("cannot.compile.clojure.files.no.facet", module.getName()), ClojureBundle.message("cannot.compile"));
				return false;
			}
		}

		Set<Module> nojdkModules = new HashSet<Module>();
		for(Module module : scope.getAffectedModules())
		{
			ClojureModuleExtension clojureModuleExtension = ModuleUtilCore.getExtension(module, ClojureModuleExtension.class);
			if(clojureModuleExtension == null)
			{
				continue;
			}

			final Sdk sdk = ModuleUtilCore.getSdk(module, JavaModuleExtension.class);
			if(sdk == null || !(sdk.getSdkType() instanceof JavaSdkType))
			{
				nojdkModules.add(module);
			}
		}

		if(!nojdkModules.isEmpty())
		{
			final Module[] noJdkArray = nojdkModules.toArray(new Module[nojdkModules.size()]);
			if(noJdkArray.length == 1)
			{
				Messages.showErrorDialog(myProject, ClojureBundle.message("cannot.compile.clojure.files.no.sdk", noJdkArray[0].getName()), ClojureBundle.message("cannot.compile"));
			}
			else
			{
				StringBuilder modulesList = new StringBuilder();
				for(int i = 0; i < noJdkArray.length; i++)
				{
					if(i > 0)
					{
						modulesList.append(", ");
					}
					modulesList.append(noJdkArray[i].getName());
				}
				Messages.showErrorDialog(myProject, ClojureBundle.message("cannot.compile.clojure.files.no.sdk.mult", modulesList.toString()), ClojureBundle.message("cannot.compile"));
			}
			return false;
		}
		return true;
	}

	@Nonnull
	public String getId()
	{
		return "ClojureCompiler";
	}

	@Override
	@Nonnull
	public String getPresentableName()
	{
		return ClojureBundle.message("clojure.compiler.name");
	}

	@Nullable
	@Override
	public OutputParser createErrorParser(BackendCompilerProcessBuilder processBuilder, @Nonnull String outputDir, ProcessHandler process)
	{
		return new ClojureOutputParser();
	}

	@Nullable
	@Override
	public OutputParser createOutputParser(BackendCompilerProcessBuilder processBuilder, @Nonnull String outputDir)
	{
		return new OutputParser()
		{
			@Override
			public boolean processMessageLine(Callback callback)
			{
				return super.processMessageLine(callback) || callback.getCurrentLine() != null;
			}
		};
	}

	@Nonnull
	@Override
	public BackendCompilerProcessBuilder prepareProcess(@Nonnull ModuleChunk moduleChunk, @Nonnull String outputPath, @Nonnull CompileContext compileContext) throws IOException
	{
		JpsJavaCompilerOptions jpsJavaCompilerOptions = JavacCompilerConfiguration.getInstance(compileContext.getProject());
		return new BackendCompilerProcessBuilder(moduleChunk, outputPath, compileContext, jpsJavaCompilerOptions, false)
		{
			@RequiredReadAction
			@Nonnull
			@Override
			public GeneralCommandLine buildCommandLine() throws IOException
			{
				return createStartupCommandImpl(myModuleChunk, outputPath, myCompileContext.getCompileScope());
			}
		};
	}


	private GeneralCommandLine createStartupCommandImpl(ModuleChunk chunk, String outputPath, CompileScope scope) throws IOException
	{
		final Sdk jdk = JavaCompilerUtil.getSdkForCompilation(chunk);
		final String versionString = jdk.getVersionString();
		if(versionString == null || "".equals(versionString))
		{
			throw new IllegalArgumentException(ClojureBundle.message("javac.error.unknown.jdk.version", jdk.getName()));
		}
		final JavaSdkType sdkType = (JavaSdkType) jdk.getSdkType();

		final String toolsJarPath = sdkType.getToolsPath(jdk);
		if(toolsJarPath == null)
		{
			throw new IllegalArgumentException(ClojureBundle.message("javac.error.tools.jar.missing", jdk.getName()));
		}

		GeneralCommandLine generalCommandLine = new GeneralCommandLine();
		sdkType.setupCommandLine(generalCommandLine, jdk);

		// For debug
		//    commandLine.add("-Xdebug");
		//    commandLine.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=127.0.0.1:5448");


		final StringBuilder classPathBuilder = new StringBuilder();
		classPathBuilder.append(sdkType.getToolsPath(jdk)).append(File.pathSeparator);

		// Add classpath and sources

		for(Module module : chunk.getModules())
		{
			if(ModuleUtilCore.getExtension(module, ClojureModuleExtension.class) != null)
			{
				ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
				OrderEntry[] entries = moduleRootManager.getOrderEntries();
				Set<VirtualFile> cpVFiles = new HashSet<VirtualFile>();
				for(OrderEntry orderEntry : entries)
				{
					cpVFiles.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.CLASSES)));

					// Add module sources
					if(orderEntry instanceof ModuleSourceOrderEntry)
					{
						cpVFiles.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.SOURCES)));
					}
				}

				for(VirtualFile file : cpVFiles)
				{
					String path = file.getPath();
					int jarSeparatorIndex = path.indexOf(URLUtil.JAR_SEPARATOR);
					if(jarSeparatorIndex > 0)
					{
						path = path.substring(0, jarSeparatorIndex);
					}
					classPathBuilder.append(path).append(File.pathSeparator);
				}
			}
		}
		classPathBuilder.append(outputPath).append(File.separator);

		ParametersList parametersList = generalCommandLine.getParametersList();

		parametersList.add("-cp");
		parametersList.add(classPathBuilder.toString());

		//Add REPL class runner
		parametersList.add(ClojureUtils.CLOJURE_MAIN);


		try
		{
			File fileWithCompileScript = File.createTempFile("clojurekul", ".clj");
			fillFileWithClojureCompilerParams(chunk, fileWithCompileScript, outputPath, scope);

			parametersList.add(fileWithCompileScript.getPath());
		}
		catch(IOException e)
		{
			LOG.error(e);
		}
		return generalCommandLine;
	}


	private static void fillFileWithClojureCompilerParams(ModuleChunk chunk, File fileWithParameters, String outputPath, CompileScope scope) throws FileNotFoundException
	{

		VirtualFile[] files = scope.getFiles(ClojureFileType.CLOJURE_FILE_TYPE, true);
		if(files.length == 0)
		{
			return;
		}

		PrintStream printer = new PrintStream(new FileOutputStream(fileWithParameters));

		//print output path
		printer.print("(binding [*compile-path* ");
		printer.print("\"" + outputPath + "\"]\n");

		final Module[] modules = chunk.getModules();
		if(modules.length > 0)
		{
			final Project project = modules[0].getProject();
			final PsiManager manager = PsiManager.getInstance(project);
			//      printNicePrinter(printer);
			for(VirtualFile file : files)
			{
				final PsiFile psiFile = manager.findFile(file);
				if(psiFile != null && (psiFile instanceof ClojureFile))
				{
					final ClojureFile clojureFile = (ClojureFile) psiFile;
					final String ns = clojureFile.getNamespace();
					// Compile all compilable files
					// Compile only files with classes!
					if(ns != null && clojureFile.isClassDefiningFile())
					{

						printer.print("(try ");
						printCompileFile(printer, ns);
						//(let [st (.getStackTrace e)] (intellij-nice-printer st))
						printer.print("(catch Exception e (. *err* println (str \"comp_err:" + file.getPath() +
								":" + ns + "@" + "\" (let [msg (.getMessage e)] msg)  ) ) )");
						printer.print(")");

					}
				}
			}
		}

		printer.print(")");
		printer.close();
	}

	private static void printCompileFile(PrintStream printer, String ns)
	{
		printer.print("(. *err* println ");
		printer.print("\"compiling:" + ns + "\"");
		printer.print(")\n");


		printer.print("(compile '");
		printer.print(ns);
		printer.print(")\n");

		//Diagnostic log
		printer.print("(. *err* println ");
		printer.print("\"compiled:" + ns + ".class\"");
		printer.print(")\n");
	}

	private static void printNicePrinter(PrintStream printer)
	{
		printer.println("(defn intellij-nice-printer [arr]\n" + "  (reduce (fn [x y] (str (.toString x) \"^^\" y)) (. java.util.Arrays asList arr))" +
				")");
	}
}


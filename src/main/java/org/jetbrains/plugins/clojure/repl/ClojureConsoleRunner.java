package org.jetbrains.plugins.clojure.repl;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.config.ClojureConfigUtil;
import org.jetbrains.plugins.clojure.utils.ClojureUtils;
import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionHelper;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.configurations.CommandLineBuilder;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.console.ConsoleHistoryController;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.CommandLineArgumentsProvider;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.ide.CommonActionsManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.actions.ToggleUseSoftWrapsToolbarAction;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.softwrap.SoftWrapAppliancePlaces;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleSourceOrderEntry;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import consulo.clojure.module.extension.ClojureModuleExtension;
import consulo.compiler.roots.CompilerPathsImpl;
import consulo.java.execution.configurations.OwnJavaParameters;

/**
 * @author ilyas
 */
public class ClojureConsoleRunner
{

	public static final String REPL_TITLE = ClojureBundle.message("repl.toolWindowName");
	public static final String EXECUTE_ACTION_IMMEDIATELY_ID = "Clojure.Console.Execute.Immediately";
	public static final String EXECUTE_ACTION_ID = "Clojure.Console.Execute";


	private final Module myModule;
	private final Project myProject;
	private final String myConsoleTitle;
	private final CommandLineArgumentsProvider myProvider;
	private final String myWorkingDir;

	private ConsoleHistoryController myHistory;
	private ClojureConsole myConsoleView;
	private ProcessHandler myProcessHandler;

	private ClojureConsoleExecuteActionHandler myConsoleExecuteActionHandler;
	private AnAction myRunAction;


	public ClojureConsoleRunner(@NotNull Module module, @NotNull String consoleTitle, @NotNull CommandLineArgumentsProvider provider, @Nullable String workingDir)
	{
		myModule = module;
		myProject = module.getProject();
		myConsoleTitle = consoleTitle;
		myProvider = provider;
		myWorkingDir = workingDir;
	}

	public static void run(@NotNull final Module module, final String workingDir, final String... statements2execute) throws CantRunException
	{
		final ArrayList<String> args = createRuntimeArgs(module, workingDir);

		final CommandLineArgumentsProvider provider = new CommandLineArgumentsProvider()
		{
			public String[] getArguments()
			{
				return args.toArray(new String[args.size()]);
			}

			public boolean passParentEnvs()
			{
				return false;
			}

			public Map<String, String> getAdditionalEnvs()
			{
				// todo add extra env. variables
				return new HashMap<String, String>();
			}
		};

		final ClojureConsoleRunner runner = new ClojureConsoleRunner(module, REPL_TITLE, provider, workingDir);

		try
		{
			runner.initAndRun(module, statements2execute);
		}
		catch(ExecutionException e)
		{
			ExecutionHelper.showErrors(module.getProject(), Arrays.<Exception>asList(e), REPL_TITLE, null);
		}
	}

	public void initAndRun(Module module, final String... statements2execute) throws ExecutionException
	{
		// Create Server process
		final Process process = createProcess(myProvider);
		// !!! do not change order!!!
		myConsoleView = createConsoleView(module);
		myHistory = new ConsoleHistoryController("clojure", null, myConsoleView);
		myConsoleView.setHistoryController(myHistory);
		myProcessHandler = new ClojureConsoleProcessHandler(process, myProvider.getCommandLineString(), getConsoleView());
		myConsoleExecuteActionHandler = new ClojureConsoleExecuteActionHandler(getProcessHandler(), getProject(), false);
		getConsoleView().setExecuteHandler(myConsoleExecuteActionHandler);

		// Init a console view
		ProcessTerminatedListener.attach(myProcessHandler);

		myProcessHandler.addProcessListener(new ProcessAdapter()
		{
			@Override
			public void processTerminated(ProcessEvent event)
			{
				myRunAction.getTemplatePresentation().setEnabled(false);
				myConsoleView.setPrompt("");
				myConsoleView.getConsoleEditor().setRendererMode(true);
				ApplicationManager.getApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						myConsoleView.getConsoleEditor().getComponent().updateUI();
					}
				});
			}
		});

		// Attach a console view to the process
		myConsoleView.attachToProcess(myProcessHandler);

		// Runner creating
		final Executor defaultExecutor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID);
		final DefaultActionGroup toolbarActions = new DefaultActionGroup();
		final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, toolbarActions, false);

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(actionToolbar.getComponent(), BorderLayout.WEST);
		panel.add(myConsoleView.getComponent(), BorderLayout.CENTER);

		final RunContentDescriptor myDescriptor = new RunContentDescriptor(myConsoleView, myProcessHandler, panel, myConsoleTitle);

		// tool bar actions
		final AnAction[] actions = fillToolBarActions(toolbarActions, defaultExecutor, myDescriptor);
		registerActionShortcuts(actions, getConsoleView().getConsoleEditor().getComponent());
		registerActionShortcuts(actions, panel);
		panel.updateUI();

		// enter action
		createAndRegisterEnterAction(panel);

		// Show in run tool window
		ExecutionManager.getInstance(myProject).getContentManager().showRunContent(defaultExecutor, myDescriptor);

		// Request focus
		final ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(defaultExecutor.getId());
		window.activate(new Runnable()
		{
			public void run()
			{
				IdeFocusManager.getInstance(myProject).requestFocus(getConsoleView().getCurrentEditor().getContentComponent(), true);
			}
		});

		// Run
		myProcessHandler.startNotify();


		final ClojureConsole console = getConsoleView();
		for(String statement : statements2execute)
		{
			final String st = statement + "\n";
			ClojureConsoleHighlightingUtil.processOutput(console, st, ProcessOutputTypes.SYSTEM);
			final ClojureConsoleExecuteActionHandler actionHandler = getConsoleExecuteActionHandler();
			actionHandler.processLine(st);
		}

	}

	private void createAndRegisterEnterAction(JPanel panel)
	{
		final AnAction enterAction = new ClojureConsoleEnterAction(getConsoleView(), getProcessHandler(), getConsoleExecuteActionHandler());
		enterAction.registerCustomShortcutSet(enterAction.getShortcutSet(), getConsoleView().getConsoleEditor().getComponent());
		enterAction.registerCustomShortcutSet(enterAction.getShortcutSet(), panel);
	}

	private static void registerActionShortcuts(final AnAction[] actions, final JComponent component)
	{
		for(AnAction action : actions)
		{
			if(action.getShortcutSet() != null)
			{
				action.registerCustomShortcutSet(action.getShortcutSet(), component);
			}
		}
	}


	protected AnAction[] fillToolBarActions(final DefaultActionGroup toolbarActions, final Executor defaultExecutor, final RunContentDescriptor myDescriptor)
	{

		ArrayList<AnAction> actionList = new ArrayList<AnAction>();

		//stop
		final AnAction stopAction = createStopAction();
		actionList.add(stopAction);

		//close
		final AnAction closeAction = createCloseAction(defaultExecutor, myDescriptor);
		actionList.add(closeAction);

		// run and history actions
		ArrayList<AnAction> executionActions = createConsoleExecActions(getConsoleView(), myProcessHandler, myConsoleExecuteActionHandler, getHistoryController());
		myRunAction = executionActions.get(0);
		actionList.addAll(executionActions);

		actionList.add(new ToggleUseSoftWrapsToolbarAction(SoftWrapAppliancePlaces.CONSOLE)
		{
			@Override
			public void setSelected(AnActionEvent e, boolean state)
			{
				EditorEx consoleEditor = getConsoleView().getConsoleEditor();
				EditorEx historyViewer = getConsoleView().getHistoryViewer();

				consoleEditor.getSettings().setUseSoftWraps(state);
				historyViewer.getSettings().setUseSoftWraps(state);

				consoleEditor.reinitSettings();
				historyViewer.reinitSettings();
			}
		});

		// help action
		actionList.add(CommonActionsManager.getInstance().createHelpAction("interactive_console"));

		AnAction[] actions = actionList.toArray(new AnAction[actionList.size()]);
		toolbarActions.addAll(actions);
		return actions;
	}

	public static ArrayList<AnAction> createConsoleExecActions(final ClojureConsole languageConsole,
			final ProcessHandler processHandler,
			final ClojureConsoleExecuteActionHandler consoleExecuteActionHandler,
			final ConsoleHistoryController historyModel)
	{

		final AnAction runImmediatelyAction = new ClojureExecuteImmediatelyAction(languageConsole, processHandler, consoleExecuteActionHandler);

		final ConsoleHistoryController historyController = new ConsoleHistoryController("clojure", null, languageConsole);
		historyController.install();

		final AnAction upAction = historyController.getHistoryPrev();
		final AnAction downAction = historyController.getHistoryNext();

		final ArrayList<AnAction> list = new ArrayList<AnAction>();
		list.add(runImmediatelyAction);
		list.add(downAction);
		list.add(upAction);
		//    list.add(enterAction);
		return list;
	}


	protected AnAction createCloseAction(final Executor defaultExecutor, final RunContentDescriptor myDescriptor)
	{
		return new CloseAction(defaultExecutor, myDescriptor, myProject);
	}

	protected AnAction createStopAction()
	{
		return ActionManager.getInstance().getAction(IdeActions.ACTION_STOP_PROGRAM);
	}


	protected ClojureConsole createConsoleView(Module module)
	{
		return new ClojureConsole(getProject(), getConsoleTitle(), null, null);
	}

	private static ArrayList<String> createRuntimeArgs(Module module, String workingDir) throws CantRunException
	{
		final OwnJavaParameters params = new OwnJavaParameters();
		params.configureByModule(module, OwnJavaParameters.JDK_AND_CLASSES_AND_TESTS);
		// To avoid NCDFE while starting REPL

		final boolean sdkConfigured = ClojureConfigUtil.isClojureConfigured(module);
		if(!sdkConfigured)
		{
			final String jarPath = ClojureConfigUtil.CLOJURE_SDK;
			assert jarPath != null;
			params.getClassPath().add(jarPath);
		}

		Set<VirtualFile> cpVFiles = new HashSet<VirtualFile>();
		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
		OrderEntry[] entries = moduleRootManager.getOrderEntries();
		for(OrderEntry orderEntry : entries)
		{
			// Add module sources to classpath
			if(orderEntry instanceof ModuleSourceOrderEntry)
			{
				cpVFiles.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.SOURCES)));
			}
		}
		// Also add output folders
		final VirtualFile outputDirectory = CompilerPathsImpl.getModuleOutputDirectory(module, false);
		if(outputDirectory != null)
		{
			cpVFiles.add(outputDirectory);
		}

		for(VirtualFile file : cpVFiles)
		{
			params.getClassPath().add(file.getPath());
		}

		params.setMainClass(getMainReplClass(module));
		params.setWorkingDirectory(new File(workingDir));

		final GeneralCommandLine line = CommandLineBuilder.createFromJavaParameters(params, module.getProject(), true);
		final ArrayList<String> cmd = new ArrayList<String>();
		cmd.add(line.getExePath());
		cmd.addAll(getJvmClojureOptions(module));
		cmd.addAll(line.getParametersList().getList());
		cmd.addAll(getReplClojureOptions(module));
		if(!sdkConfigured)
		{
			ClojureConfigUtil.warningDefaultClojureJar(module);
		}
		return cmd;
	}

	private static String getMainReplClass(Module module)
	{
		final ClojureModuleExtension extension = getClojureException(module);
		if(extension == null)
		{
			return ClojureUtils.CLOJURE_MAIN;
		}
		return extension.getReplClass();
	}

	private GeneralCommandLine createCommandLine(Module module, String workingDir) throws CantRunException
	{
		final OwnJavaParameters params = new OwnJavaParameters();
		params.configureByModule(module, OwnJavaParameters.JDK_AND_CLASSES_AND_TESTS);
		params.getVMParametersList().addAll(getJvmClojureOptions(module));
		params.getProgramParametersList().addAll(getReplClojureOptions(module));
		// To avoid NCDFE while starting REPL

		final boolean sdkConfigured = ClojureConfigUtil.isClojureConfigured(module);
		if(!sdkConfigured)
		{
			final String jarPath = ClojureConfigUtil.CLOJURE_SDK;
			assert jarPath != null;
			params.getClassPath().add(jarPath);
		}

		Set<VirtualFile> cpVFiles = new HashSet<VirtualFile>();
		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
		OrderEntry[] entries = moduleRootManager.getOrderEntries();
		for(OrderEntry orderEntry : entries)
		{
			// Add module sources to classpath
			if(orderEntry instanceof ModuleSourceOrderEntry)
			{
				cpVFiles.addAll(Arrays.asList(orderEntry.getFiles(OrderRootType.SOURCES)));
			}
		}

		for(VirtualFile file : cpVFiles)
		{
			params.getClassPath().add(file.getPath());
		}

		params.setMainClass(getMainReplClass(module));
		params.setWorkingDirectory(new File(workingDir));

		final GeneralCommandLine line = params.toCommandLine();

		//final ArrayList<String> cmd = new ArrayList<String>();
		//cmd.add(executablePath);

		// cmd.addAll(line.getParametersList().getList());
		//        line.getParametersList().addAll();

		Map<String, String> envParams = new HashMap<String, String>();
		envParams.putAll(System.getenv());
		line.getEnvironment().putAll(envParams);

		if(!sdkConfigured)
		{
			ClojureConfigUtil.warningDefaultClojureJar(module);
		}
		return line;
	}

	private static List<String> getJvmClojureOptions(Module module)
	{
		final ClojureModuleExtension facet = getClojureException(module);
		String opts = facet != null ? facet.getJvmOpts() : null;
		if(opts == null || opts.trim().isEmpty())
		{
			return Arrays.asList();
		}
		return Arrays.asList(opts.split("\\s+"));
	}

	private static ClojureModuleExtension getClojureException(Module module)
	{
		return ModuleUtilCore.getExtension(module, ClojureModuleExtension.class);
	}

	private static List<String> getReplClojureOptions(Module module)
	{
		final ClojureModuleExtension facet = getClojureException(module);
		String opts = facet != null ? facet.getReplOpts() : null;
		if(opts == null || opts.trim().isEmpty())
		{
			return Arrays.asList();
		}
		return Arrays.asList(opts.split("\\s+"));
	}

	protected Process createProcess(CommandLineArgumentsProvider provider) throws ExecutionException
	{

		final GeneralCommandLine cmdline = createCommandLine(myModule, getWorkingDir());

		Process process = null;
		try
		{
			process = cmdline.createProcess();
		}
		catch(Exception e)
		{
			ExecutionHelper.showErrors(getProject(), Arrays.<Exception>asList(e), REPL_TITLE, null);
		}

		return process;

	}


	/*
	  A bunch of getters
	*/
	public Project getProject()
	{
		return myProject;
	}

	public String getConsoleTitle()
	{
		return myConsoleTitle;
	}

	public ClojureConsole getConsoleView()
	{
		return myConsoleView;
	}

	public ProcessHandler getProcessHandler()
	{
		return myProcessHandler;
	}

	public ClojureConsoleExecuteActionHandler getConsoleExecuteActionHandler()
	{
		return myConsoleExecuteActionHandler;
	}

	public ConsoleHistoryController getHistoryController()
	{
		return myHistory;
	}

	public String getWorkingDir()
	{
		return myWorkingDir;
	}


}
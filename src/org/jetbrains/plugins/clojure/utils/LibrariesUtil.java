package org.jetbrains.plugins.clojure.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.libraries.LibraryEx;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.config.ClojureConfigUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ilyas
 */
public class LibrariesUtil {

  public static Library[] getLibrariesByCondition(final Module module, final Condition<Library> condition) {
    if (module == null) return new Library[0];
    final ArrayList<Library> libraries = new ArrayList<Library>();
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      public void run() {
        ModuleRootManager manager = ModuleRootManager.getInstance(module);
        ModifiableRootModel model = manager.getModifiableModel();
        for (OrderEntry entry : model.getOrderEntries()) {
          if (entry instanceof LibraryOrderEntry) {
            LibraryOrderEntry libEntry = (LibraryOrderEntry) entry;
            Library library = libEntry.getLibrary();
            if (condition.value(library)) {
              libraries.add(library);
            }
          }
        }
        model.dispose();
      }
    });
    return libraries.toArray(new Library[libraries.size()]);
  }

  public static Library[] getLibraries(Condition<Library> condition) {
    LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTable();
    List<Library> libs = ContainerUtil.findAll(table.getLibraries(), condition);
    return libs.toArray(new Library[libs.size()]);
  }

  public static String[] getLibNames(Library[] libraries) {
    return ContainerUtil.map2Array(libraries, String.class, new Function<Library, String>() {
      public String fun(Library library) {
        return library.getName();
      }
    });
  }

  @NotNull
  public static String getClojureLibraryHome(Library library) {
    String path = "";
    if (library instanceof LibraryEx && ((LibraryEx) library).isDisposed()) return path;
    for (VirtualFile file : library.getFiles(OrderRootType.CLASSES)) {
      if (ClojureConfigUtil.CLOJURE_JAR_NAME_PREFIX.equals(file.getName())) {
        String jarPath = file.getPresentableUrl();
        File realFile = new File(jarPath);
        if (realFile.exists()) {
          File parentFile = realFile.getParentFile();
          if (parentFile != null) {
            File libHome = parentFile.getParentFile();
            if (libHome != null) {
              path = libHome.getPath();
            }
          }
        }
      }
    }
    return path;
  }

    public static Library[] getGlobalLibraries(Condition<Library> condition) {
    LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTable();
    List<Library> libs = ContainerUtil.findAll(table.getLibraries(), condition);
    return libs.toArray(new Library[libs.size()]);
  }

  @Nullable
  public static Library getLibraryByName(String name) {
    if (name == null) return null;
    LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTable();
    return table.getLibraryByName(name);
  }

  public static boolean libraryReferenced(ModuleRootManager rootManager, Library library) {
    final OrderEntry[] entries = rootManager.getOrderEntries();
    for (OrderEntry entry : entries) {
      if (entry instanceof LibraryOrderEntry && library.equals(((LibraryOrderEntry) entry).getLibrary())) return true;
    }
    return false;
  }

}

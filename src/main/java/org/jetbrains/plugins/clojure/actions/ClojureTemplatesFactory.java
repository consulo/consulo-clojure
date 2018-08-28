package org.jetbrains.plugins.clojure.actions;

import com.intellij.ide.fileTemplates.*;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.clojure.ClojureBundle;
import org.jetbrains.plugins.clojure.ClojureIcons;

import java.util.Properties;

/**
 * @author ilyas
 */
public class ClojureTemplatesFactory implements FileTemplateGroupDescriptorFactory {

  @NonNls
  private static final String CLOJURE_FILE = "ClojureFile.clj";
  @NonNls
  static final String NAME_TEMPLATE_PROPERTY = "NAME";
  @NonNls
  static final String LOW_CASE_NAME_TEMPLATE_PROPERTY = "lowCaseName";


  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    final FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor(ClojureBundle.message("file.template.group.title.clojure"),
        ClojureIcons.CLOJURE_ICON_16x16);
    group.addTemplate(new FileTemplateDescriptor(CLOJURE_FILE, ClojureIcons.CLOJURE_ICON_16x16));
    return group;
  }

  public static PsiFile createFromTemplate(final PsiDirectory directory, final String name, String fileName, String templateName,
                                           @NonNls String... parameters) throws IncorrectOperationException {
    final FileTemplate template = FileTemplateManager.getInstance().getJ2eeTemplate(templateName);
    Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());
    JavaTemplateUtil.setPackageNameAttribute(properties, directory);
    properties.setProperty(NAME_TEMPLATE_PROPERTY, name);
    properties.setProperty(LOW_CASE_NAME_TEMPLATE_PROPERTY, name.substring(0, 1).toLowerCase() + name.substring(1));
    for (int i = 0; i < parameters.length; i += 2) {
      properties.setProperty(parameters[i], parameters[i + 1]);
    }
    String text;
    try {
      text = template.getText(properties);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to load template for " + FileTemplateManager.getInstance().internalTemplateToSubject(templateName), e);
    }

    final PsiFileFactory factory = PsiFileFactory.getInstance(directory.getProject());
    final PsiFile file = factory.createFileFromText(fileName, text);
    return (PsiFile) directory.add(file);
  }

}

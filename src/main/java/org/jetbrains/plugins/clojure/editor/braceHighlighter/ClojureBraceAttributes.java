package org.jetbrains.plugins.clojure.editor.braceHighlighter;

import com.intellij.openapi.editor.markup.TextAttributes;
import consulo.ui.color.ColorValue;
import consulo.ui.color.RGBColor;
import consulo.ui.style.StandardColors;

/**
 * @author ilyas
 */
public abstract class ClojureBraceAttributes {
  private static final ColorValue[] CLOJURE_BRACE_COLORS =
      {
          StandardColors.BLUE, // 0
          new RGBColor(139, 0, 0), // 10 red
          new RGBColor(47, 79, 47),     // 1
          new RGBColor(199, 21, 133), // 7 MediumVioletRed
          new RGBColor(85, 26, 139), // 2 purple
          StandardColors.GRAY,   // 3
          new RGBColor(0, 0, 128), // 8 navy - blue
          StandardColors.RED,             // 5
          new RGBColor(47, 79, 47), // 6 Dark green
          new RGBColor(255, 100, 0),   // 1 orange
          new RGBColor(139, 101, 8), // 9 Dark golden

      };

  public static TextAttributes getBraceAttributes(int level, ColorValue background) {
    ColorValue braceColor = CLOJURE_BRACE_COLORS[level % CLOJURE_BRACE_COLORS.length];
    ColorValue adjustedBraceColor = RGBColor.fromRGBValue(RGBColor.toRGBValue(braceColor.toRGB()) ^ RGBColor.toRGBValue(background.toRGB()) ^ 0xFFFFFF);
    return new TextAttributes(adjustedBraceColor, null, null, null, 1);
  }
}

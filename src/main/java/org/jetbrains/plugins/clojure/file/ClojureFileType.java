/**
 * Copyright 2000-2007 JetBrains s.r.o.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.clojure.file;

import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.clojure.ClojureIcons;
import org.jetbrains.plugins.clojure.ClojureLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;

/**
 * Created by IntelliJ IDEA.
 * User: merlyn
 * Date: 16-Nov-2008
 * Time: 11:08:03 PM
 */
public class ClojureFileType extends LanguageFileType
{
	public static final ClojureFileType INSTANCE = new ClojureFileType();
	@Deprecated
	public static final ClojureFileType CLOJURE_FILE_TYPE = INSTANCE;
	@Deprecated
	public static final Language CLOJURE_LANGUAGE = ClojureLanguage.INSTANCE;

	@NonNls
	public static final String CLOJURE_DEFAULT_EXTENSION = "clj";

	private ClojureFileType()
	{
		super(ClojureLanguage.INSTANCE);
	}

	@Override
	@Nonnull
	public String getName()
	{
		return "Clojure";
	}

	@Override
	@Nonnull
	public String getDescription()
	{
		return "Clojure file";
	}

	@Override
	@Nonnull
	public String getDefaultExtension()
	{
		return CLOJURE_DEFAULT_EXTENSION;
	}

	@Override
	public Image getIcon()
	{
		return ClojureIcons.CLOJURE_ICON_16x16;
	}
}

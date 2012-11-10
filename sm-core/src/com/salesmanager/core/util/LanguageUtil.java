/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.cache.RefCache;

public class LanguageUtil {

	public static List parseLanguages(String langs) {
		List lst = new ArrayList();
		if (langs != null) {
			StringTokenizer st = new StringTokenizer(langs, ";");
			while (st != null && st.hasMoreTokens()) {
				String lang = st.nextToken();
				lst.add(lang);
			}
		}
		return lst;
	}

	public static int getLanguageNumberCode(String lang) {
		if (lang == null) {
			return 1;
		}
		Map langmap = RefCache.getLanguageswithcode();
		Language l = (Language) langmap.get(lang.toLowerCase());
		if (l != null) {
			return l.getLanguageId();
		} else {
			return 1;
		}
	}

	public static Language getLanguageByCode(String lang) {
		if (lang == null) {
			return null;
		}
		Map langmap = RefCache.getLanguageswithcode();
		Language l = (Language) langmap.get(lang.toLowerCase());
		return l;
	}

	public static String getLanguageStringCode(int lang) {

		Map langmap = RefCache.getLanguageswithindex();
		Language l = (Language) langmap.get(lang);
		if (l != null) {
			return l.getCode();
		} else {
			return Constants.ENGLISH_CODE;
		}
	}

	public static String getDefaultLanguage() {

		Configuration conf = PropertiesUtil.getConfiguration();
		String defaultLang = conf
				.getString("core.system.defaultlanguage", "en");
		return defaultLang;

	}
}

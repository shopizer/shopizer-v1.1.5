/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.shipping.ShippingModuleActionInterceptor;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.util.LanguageUtil;

public class LanguageHelper {
	
	private static Logger log = Logger
	.getLogger(LanguageHelper.class);

	/**
	 * Set the supported store/cart languages in the context
	 * 
	 * @param langs
	 * @param ctx
	 */
	public static void setLanguages(String langs, Context ctx) {
		Map langsmap = new HashMap();
		if (langs != null) {
			StringTokenizer st = new StringTokenizer(langs, ";");
			while (st != null && st.hasMoreTokens()) {
				String lang = st.nextToken();
				
				Language l = LanguageUtil.getLanguageByCode(lang);
				
				if(l==null) {
					log.warn("Trying to set language code " + lang + " but it does not exist in LANGUAGES table");
					continue;
				}
				langsmap.put(lang, l.getLanguageId());
			}
		}
		ctx.setSupportedlang(langsmap);
	}

}

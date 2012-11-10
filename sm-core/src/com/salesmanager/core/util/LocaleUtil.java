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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.cache.RefCache;

public class LocaleUtil {

	private static Logger log = Logger.getLogger(LocaleUtil.class);

	private LocaleUtil() {
	}

	public static Locale getLocale(String lang) {

		if (StringUtils.isBlank(lang)) {
			return getDefaultLocale();
		}

		if (lang.equals(Constants.ENGLISH_CODE)) {
			return Locale.ENGLISH;
		} else if (lang.equals(Constants.FRENCH_CODE)) {
			return Locale.FRENCH;
		} else {
			log.warn("Resources for this language " + lang
					+ " may not be handled by this system");
			return new Locale(lang);
		}

	}

	public static Locale getDefaultLocale() {
		Configuration conf = PropertiesUtil.getConfiguration();
		int defaultCountryId = conf.getInt("core.system.defaultcountryid", 38);
		
		Map countriesMap = RefCache.getAllcountriesmap(Constants.ENGLISH);
		
		if(countriesMap==null) {
			log.error("Cannot get object from database, check your database configuration");
		}
		
		Country country = (Country) countriesMap.get(defaultCountryId);
		Locale locale = new Locale(conf.getString(
				"core.system.defaultlanguage", Constants.ENGLISH_CODE), country
				.getCountryIsoCode2());
		return locale;
	}

	public static Locale getLocaleFromStoreEntity(MerchantStore store,
			String defaultLanguage) {
		Map countriesMap = RefCache.getAllcountriesmap(LanguageUtil
				.getLanguageNumberCode(defaultLanguage));
		if (countriesMap == null) {
			log.error("Cannnot get a Map of countries for language code "
					+ defaultLanguage);
			return getDefaultLocale();
		}
		Country country = (Country) countriesMap.get(store.getCountry());
		Locale locale = new Locale(country.getCountryIsoCode2());
		return locale;
	}

	public static Locale getLocale(HttpServletRequest req) {
		Locale locale = (Locale) req.getSession().getAttribute(
				"WW_TRANS_I18N_LOCALE");

		if (locale == null) {
			locale = LocaleUtil.getDefaultLocale();
		}
		return locale;
	}

	public static void setLocale(HttpServletRequest req, Locale locale) {
		req.getSession().setAttribute("WW_TRANS_I18N_LOCALE", locale);
	}

	public static void setLocaleToEntityCollection(Collection<I18NEntity> coll,
			Locale locale) {
		if (coll != null) {
			Iterator i = coll.iterator();
			while (i.hasNext()) {
				I18NEntity entity = (I18NEntity) i.next();
				entity.setLocale(locale);
			}
		}
	}

	public static void setLocaleToEntityCollection(Collection<I18NEntity> coll,
			Locale locale, String currency) {
		if (coll != null) {
			Iterator i = coll.iterator();
			while (i.hasNext()) {
				I18NEntity entity = (I18NEntity) i.next();
				entity.setLocale(locale, currency);
			}
		}
	}

	public static void setLocaleForRequest(HttpServletRequest request,
			HttpServletResponse response, ActionContext ctx, MerchantStore store)
			throws Exception {

		/**
		 * LOCALE
		 */

		Map sessions = ctx.getSession();

		if (ctx == null) {
			throw new Exception(
					"This request was not made inside Struts request, ActionContext is null");
		}

		Locale locale = null;

		// check in http request
		String req_locale = (String) request.getParameter("request_locale");
		if (!StringUtils.isBlank(req_locale)) {
			
			String l = null;
			String c = null;
			
			if(req_locale.length()==2) {//assume it is the language
				l = req_locale;
				c = CountryUtil.getCountryIsoCodeById(store.getCountry());
			}
			
			if(req_locale.length()==5) {
				
				try {
					l = req_locale.substring(0, 2);
					c = req_locale.substring(3);
				} catch (Exception e) {
					log.warn("Invalid locale format " + req_locale);
					l = null;
					c = null;
				}
				
			}


			if(l!=null && c != null) {
			
				String storeLang = null;
				Map languages = store.getGetSupportedLanguages();
				if (languages != null && languages.size() > 0) {
					Iterator i = languages.keySet().iterator();
					while (i.hasNext()) {
						Integer langKey = (Integer) i.next();
						Language lang = (Language) languages.get(langKey);
						if (lang.getCode().equals(l)) {
							storeLang = l;
							break;
						}
					}
				}
	
				if (storeLang == null) {
					l = store.getDefaultLang();
					if (StringUtils.isBlank(l)) {
						l = LanguageUtil.getDefaultLanguage();
					}
				}
	
				locale = new Locale(l, c);
				if (StringUtils.isBlank(locale.getLanguage())
						|| StringUtils.isBlank(locale.getCountry())) {
					log.error("Language or Country is not set in the new locale "
							+ req_locale);
					return;
				}
				sessions.put("WW_TRANS_I18N_LOCALE", locale);
			
			}
		}

		locale = (Locale) sessions.get("WW_TRANS_I18N_LOCALE");
		request.getSession().setAttribute("WW_TRANS_I18N_LOCALE", locale);

		if (locale == null) {

			String c = CountryUtil.getCountryIsoCodeById(store.getCountry());
			String lang = store.getDefaultLang();
			if (!StringUtils.isBlank(c) && !StringUtils.isBlank(lang)) {
				locale = new Locale(lang, c);
			} else {
				locale = LocaleUtil.getDefaultLocale();
				String langs = store.getSupportedlanguages();
				if (!StringUtils.isBlank(langs)) {
					Map languages = store.getGetSupportedLanguages();
					String defaultLang = locale.getLanguage();
					if (languages != null && languages.size() > 0) {
						Iterator i = languages.keySet().iterator();
						String storeLang = "";
						while (i.hasNext()) {
							Integer langKey = (Integer) i.next();
							Language l = (Language) languages.get(langKey);
							if (l.getCode().equals(defaultLang)) {
								storeLang = defaultLang;
								break;
							}
						}
						if (!storeLang.equals(defaultLang)) {
							defaultLang = storeLang;
						}
					}

					if (!StringUtils.isBlank(defaultLang)
							&& !StringUtils.isBlank(c)) {
						locale = new Locale(defaultLang, c);
					}

				}
			}

			sessions.put("WW_TRANS_I18N_LOCALE", locale);
		}

		if (locale != null) {
			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(locale);
			String lang = label.getText("label.language."
					+ locale.getLanguage());
			request.setAttribute("LANGUAGE", lang);
		}

		if (store.getLanguages() == null || store.getLanguages().size() == 0) {

			// languages
			if (!StringUtils.isBlank(store.getSupportedlanguages())) {
				List languages = new ArrayList();
				List langs = LanguageUtil.parseLanguages(store
						.getSupportedlanguages());
				for (Object o : langs) {
					String lang = (String) o;
					Language l = LanguageUtil.getLanguageByCode(lang);
					if (l != null) {
						l.setLocale(locale, store.getCurrency());
						languages.add(l);
					}
				}
				store.setLanguages(languages);
			}
		}

		request.setAttribute("LOCALE", locale);
		Cookie c = new Cookie("LOCALE", locale.getLanguage() + "_"
				+ locale.getCountry());
		c.setPath("/");
		c.setMaxAge(2 * 24 * 24);
		response.addCookie(c);

	}

}

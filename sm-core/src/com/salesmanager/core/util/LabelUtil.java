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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;

public class LabelUtil {

	private Locale locale;

	private LabelUtil() {

	}

	public static LabelUtil getInstance() {

		return new LabelUtil();
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getText(String aTextName) {

		if (locale == null) {
			setLocale(LocaleUtil.getDefaultLocale());
		}

		return getTextProvider().getText(getLocale(), aTextName);
	}

	public String getText(HttpServletRequest request, String aTextName) {

		setLocale(LocaleUtil.getLocale(request));
		return getTextProvider().getText(getLocale(), aTextName);
	}

	public String getText(Locale locale, String aTextName) {

		setLocale(locale);
		return getTextProvider().getText(getLocale(), aTextName);
	}

	public String getText(String lang, String aTextName, String parameter) {
		getLocale(lang);
		return getTextProvider().getText(getLocale(), aTextName, parameter);
	}

	public String getText(Locale locale, String aTextName, List params) {

		return getTextProvider().getText(getLocale(), aTextName, params);
	}

	public String getText(String lang, String aTextName) {

		getLocale(lang);
		return getTextProvider().getText(getLocale(), aTextName);
	}

	private void getLocale(String lang) {
		
		if(StringUtils.isBlank(lang)) {
			lang = LanguageUtil.getDefaultLanguage();
		}
		
		if (lang.equals("en")) {
			setLocale(Locale.ENGLISH);
		} else if (lang.equals("fr")) {
			setLocale(Locale.FRENCH);
		} else {
			setLocale(new Locale(lang));
		}
	}

	private SmTextProvider getTextProvider() {
		ActionContext ctx = ActionContext.getContext();
		if (ctx == null) {// use the other method
			return new CustomLabelUtil();
		} else {// use struts method
			return new StrutsLabelUtil();
		}
	}

}

interface SmTextProvider {
	String getText(Locale locale, String key);

	String getText(Locale locale, String key, List parameters);

	String getText(Locale locale, String key, String parameter);
}

class StrutsLabelUtil implements LocaleProvider, SmTextProvider {

	private Locale locale;

	private void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getText(Locale locale, String key) {
		this.setLocale(locale);
		TextProvider textProvider = (new TextProviderFactory()).createInstance(
				StrutsLabelUtil.class, this);
		return textProvider.getText(key);
	}

	public String getText(Locale locale, String key, List parameters) {
		this.setLocale(locale);
		TextProvider textProvider = (new TextProviderFactory()).createInstance(
				StrutsLabelUtil.class, this);
		return textProvider.getText(key, parameters);
	}

	public String getText(Locale locale, String key, String parameter) {
		this.setLocale(locale);
		TextProvider textProvider = (new TextProviderFactory()).createInstance(
				StrutsLabelUtil.class, this);
		return textProvider.getText(key, new String[] { parameter });
	}
}

class CustomLabelUtil implements SmTextProvider {

	private static List bundleList = new ArrayList();

	static {// load config files

		Configuration props = PropertiesUtil.getConfiguration();
		List lst = (List) props.getProperty("struts.custom.i18n.resources");
		if (lst != null) {
			bundleList = lst;
		}

	}

	public String getText(Locale locale, String key) {

		Iterator bundleListIterator = bundleList.iterator();
		ResourceBundle myResources = null;
		String label = "";
		while (bundleListIterator.hasNext()) {
			String bundle = (String) bundleListIterator.next();
			try {
				myResources = ResourceBundle.getBundle(bundle, locale);
				if (myResources != null) {
					String l = myResources.getString(key);
					if (l != null) {
						label = l;
						break;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return label;
	}

	public String getText(Locale locale, String key, List parameters) {

		Iterator bundleListIterator = bundleList.iterator();
		ResourceBundle myResources = null;
		String label = "";
		while (bundleListIterator.hasNext()) {
			String bundle = (String) bundleListIterator.next();

			try {

				myResources = ResourceBundle.getBundle(bundle, locale);
				if (myResources != null) {
					String l = myResources.getString(key);
					if (l != null) {
						MessageFormat mFormat = new MessageFormat(l);
						String[] params = new String[parameters.size()];
						params = (String[]) parameters.toArray(params);
						l = mFormat.format(params);
						label = l;
						break;
					}
				}

			} catch (Exception e) {
				// Handle exception
			}

		}
		return label;
	}

	public String getText(Locale locale, String key, String parameter) {
		Iterator bundleListIterator = bundleList.iterator();
		ResourceBundle myResources = null;
		String label = "";
		while (bundleListIterator.hasNext()) {
			String bundle = (String) bundleListIterator.next();

			try {

				myResources = ResourceBundle.getBundle(bundle, locale);
				if (myResources != null) {
					String l = myResources.getString(key);
					if (l != null) {
						MessageFormat mFormat = new MessageFormat(l);
						l = mFormat.format(parameter);
						label = l;
						break;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return label;
	}
}

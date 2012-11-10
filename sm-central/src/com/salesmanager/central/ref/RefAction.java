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
package com.salesmanager.central.ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.reference.CentralCreditCard;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;

public class RefAction extends BaseAction {

	private Logger log = Logger.getLogger(RefAction.class);
	private java.util.Calendar cal = new java.util.GregorianCalendar();

	private static Map creditActionsMap = new HashMap();
	private static Map creditCVVMap = new HashMap();
	private static Map typesMap = new HashMap();

	static {
		//Map actionsen = LanguageLabels.getCreditCardActions("en");
		//creditActionsMap.put("en", actionsen);
		//Map actionsfr = LanguageLabels.getCreditCardActions("fr");
		//creditActionsMap.put("fr", actionsfr);
		
		
		Map cvven = LanguageLabels.useCVV("en");
		creditCVVMap.put("en", cvven);
		Map cvvfr = LanguageLabels.useCVV("fr");
		creditCVVMap.put("fr", cvvfr);
		Collection types = com.salesmanager.core.service.cache.RefCache
				.getProductTypes();
		Map typen = LanguageLabels.getProductTypes("en", types);
		typesMap.put("en", typen);
		Map typfr = LanguageLabels.getProductTypes("fr", types);
		typesMap.put("fr", typfr);
	}

	public Collection getProductOptionTypes() {

		try {
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			return cservice.getProductOptionTypes();
		} catch (Exception e) {
			log.error(e);
		}
		return new ArrayList();

	}

	public Map getCreditpmactions() {
		
		Map cardactions = new HashMap();
		
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());


		cardactions.put(PaymentConstants.PREAUTH, label.getText("label.payment.gateway.transactiontype.1"));
		cardactions.put(PaymentConstants.SALE, label.getText("label.payment.gateway.transactiontype.0"));
		
		return cardactions;
	}

	public Map getProducttypesmap() {
		String lang = super.getLocale().getLanguage();
		return (Map) typesMap.get(lang);
	}

	public Map getCvvmap() {
		String lang = super.getLocale().getLanguage();
		return (Map) creditCVVMap.get(lang);
	}

	public String getTransactionType() {
		String trtype = (String) this.getServletRequest().getAttribute(
				"transactionType");
		if (trtype == null) {
			trtype = "";
		}
		return LabelUtil.getInstance().getText(super.getServletRequest(),
				"label.payment.gateway.transactiontype." + trtype);
	}

	public List getStatus() {
		
		Locale locale = LocaleUtil.getLocale(super.getServletRequest());
		String lang = locale.getLanguage();
		Map smap = com.salesmanager.core.service.cache.RefCache
				.getOrderstatuswithlang(LanguageUtil
						.getLanguageNumberCode(lang));
		Iterator i = smap.keySet().iterator();
		List l = new ArrayList();
		while (i.hasNext()) {
			int keyid = (Integer) i.next();
			l.add((OrderStatus) smap.get(keyid));
		}
		return l;
	}

	public Collection getProductTypes() {
		return com.salesmanager.core.service.cache.RefCache.getProductTypes();
	}

	public Collection getCreditCards() {

		List l = new ArrayList();
		Map ccmap = com.salesmanager.core.service.cache.RefCache
				.getSupportedCreditCards();

		if (ccmap != null) {
			Iterator i = ccmap.keySet().iterator();

			while (i.hasNext()) {
				int key = (Integer) i.next();
				l.add((CentralCreditCard) ccmap.get(key));
			}
		}
		return l;

	}

	public Collection getCreditCardYears() {
		List l = new ArrayList();
		int yearNow = cal.get(java.util.Calendar.YEAR);
		for (int i = 0; i < 10; i++) {
			int y = yearNow + i;
			l.add(y);
		}
		return l;
	}

	public Map getYesno() {
		HttpServletRequest req = this.getServletRequest();
		Locale loc = super.getLocale();
		return LanguageLabels.buildYesNo(loc);
	}

	public Map getTruefalse() {
		HttpServletRequest req = this.getServletRequest();
		Locale loc = super.getLocale();
		return LanguageLabels.buildTrueFalse(loc);
	}

	public Map getSuccessfail() {
		HttpServletRequest req = this.getServletRequest();
		Locale loc = super.getLocale();
		return LanguageLabels.buildSuccessFail(loc.getLanguage());
	}

	public Map getEnvironments() {
		HttpServletRequest req = this.getServletRequest();
		Locale loc = req.getLocale();
		Map env = new HashMap();
		env.put(new Integer(1).intValue(), "Production");
		env.put(new Integer(2).intValue(), "Test");

		return env;
	}

	public Collection getCreditCardMonths() {
		List l = new ArrayList();
		l.add("01");
		l.add("02");
		l.add("03");
		l.add("04");
		l.add("05");
		l.add("06");
		l.add("07");
		l.add("08");
		l.add("09");
		l.add("10");
		l.add("11");
		l.add("12");
		return l;
	}

	public Collection getWeightUnits() {
		// set lang to all objects
		Collection coll = com.salesmanager.core.service.cache.RefCache
				.getWeightunits().values();
		LocaleUtil.setLocaleToEntityCollection(coll, super.getLocale());
		return coll;
	}

	public Collection getSizeUnits() {
		Collection coll = com.salesmanager.core.service.cache.RefCache
				.getSizeunits().values();
		LocaleUtil.setLocaleToEntityCollection(coll, super.getLocale());
		return coll;
	}

	public Collection getAllCountries() {
		Context ctx = (Context) this.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Map countries = com.salesmanager.core.service.cache.RefCache
				.getAllcountriesmap(LanguageUtil.getLanguageNumberCode(ctx
						.getLang()));
		return countries.values();
	}

	public Collection getCurrencies() {

		Map currenciesMap = com.salesmanager.core.service.cache.RefCache
				.getCurrenciesListWithCodes();
		if (currenciesMap != null) {
			List returnlist = new ArrayList();
			Iterator i = currenciesMap.keySet().iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				com.salesmanager.core.entity.reference.Currency c = (com.salesmanager.core.entity.reference.Currency) currenciesMap
						.get(key);
				returnlist.add(c);
			}

			return returnlist;
		} else {
			return new ArrayList();
		}
	}

	public Collection getLanguages() {

		return com.salesmanager.core.service.cache.RefCache
				.getLanguageswithindex().values();

	}

	/** required by struts **/
	public void prepare() {

	}

}

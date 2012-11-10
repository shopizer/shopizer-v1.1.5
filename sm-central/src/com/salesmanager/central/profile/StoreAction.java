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
package com.salesmanager.central.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.salesmanager.central.CountrySelectBaseAction;
import com.salesmanager.central.util.LanguageHelper;
import com.salesmanager.central.web.Constants;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;

public class StoreAction extends CountrySelectBaseAction {

	private static final long serialVersionUID = 7448329639550683806L;

	private Logger log = Logger.getLogger(StoreAction.class);


	private String inBusinessSince;

	private MerchantStore merchantProfile;

	public MerchantStore getMerchantProfile() {
		return merchantProfile;
	}

	public void setMerchantProfile(MerchantStore merchantProfile) {
		this.merchantProfile = merchantProfile;
	}

	private Integer countryCode;
	private List supportedLanguages = new ArrayList();

	/**
	 * invoked when the page loads / refresh
	 * 
	 * @throws Exception
	 */
	public String fetchProfile() throws Exception {

		super.setPageTitle("label.menu.group.store");
		MerchantStore profile = null;

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			profile = mservice.getMerchantStore(merchantid.intValue());
			
			String user = super.getPrincipal().getRemoteUser();
			MerchantUserInformation userInfo = mservice.getMerchantUserInformation(user);

			//MerchantUserInformation userInfo = mservice
			//		.getMerchantUserInfo(merchantid.intValue());

			if (profile == null) {// should be created from the original
									// subscribtion process
				profile = new MerchantStore();
				String serverName = super.getServletRequest().getServerName();
				int serverPort = super.getServletRequest().getServerPort();
				
				if(serverPort>0) {
					serverName = serverName + ":" + String.valueOf(serverPort);
				}
				
				profile.setDomainName(serverName);
				
				
				profile.setTemplateModule(CatalogConstants.DEFAULT_TEMPLATE);

			}

			if (profile.getSupportedlanguages() != null
					&& !profile.getSupportedlanguages().equals("")) {

				LanguageHelper.setLanguages(profile.getSupportedlanguages(),
						ctx);
				Map m = ctx.getSupportedlang();
				if (m != null && m.size() > 0) {
					Set s = m.keySet();
					Iterator i = s.iterator();
					while (i.hasNext()) {
						String key = (String) i.next();
						supportedLanguages.add(key);
					}
				}
			}

			// set at least the user country code
			if (profile.getCountry() == 0) {
				profile.setCountry(userInfo.getUsercountrycode());
			}
			// set a default background
			if (profile.getBgcolorcode() == 0) {
				profile.setBgcolorcode(1);
			}

			if (profile.getStoreaddress() == null) {
				profile.setStoreaddress(userInfo.getUseraddress());
			}

			if (profile.getStorecity() == null) {
				profile.setStorecity(userInfo.getUsercity());
			}

			if (profile.getStorepostalcode() == null) {
				profile.setStorepostalcode(userInfo.getUserpostalcode());
			}

			if (profile.getBgcolorcode() == 0) {
				profile.setBgcolorcode(new Integer(1));// set to white
			}

			profile.setTemplateModule(profile.getTemplateModule());

			Date businessDate = profile.getInBusinessSince();
			if (businessDate == null) {
				businessDate = new Date();
			}
			this.setInBusinessSince(DateUtil.formatDate(businessDate));

			super.prepareSelections(profile.getCountry());

			this.merchantProfile = profile;
			return SUCCESS;

		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
			return ERROR;
		}
	}

	/**
	 * For display in the page
	 * 
	 * @throws Exception
	 */
	public String display() throws Exception {
		super.setPageTitle("label.menu.group.store");
		try {

			if (merchantProfile == null) {
				this.fetchProfile();
			}

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;
	}

	/**
	 * Invoked from CRUD actions
	 * 
	 * @return
	 */
	public String saveStore() {
		super.setPageTitle("label.menu.group.store");

		MerchantStore store = null;
		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			store = mservice.getMerchantStore(merchantid.intValue());
			
			//validation
/*			if (StringUtils.isBlank(merchantProfile.getTemplateModule())) {
				super.setErrorMessage("errors.store.emptytemplate");
				return INPUT;
			} */

			if (store == null) {
				store = new MerchantStore();
				store.setTemplateModule(CatalogConstants.DEFAULT_TEMPLATE);
			}else {
				store.setTemplateModule(merchantProfile.getTemplateModule());
			}
			


			java.util.Date dt = new java.util.Date();

			StringBuffer languages = new StringBuffer();
			List langs = this.getSupportedLanguages();
			if (langs != null && langs.size() > 0) {
				int sz = 0;
				Iterator i = langs.iterator();

				while (i.hasNext()) {
					String lang = (String) i.next();
					languages.append(lang);

					if (sz < langs.size() - 1) {
						languages.append(";");
					}
					sz++;

				}
				store.setSupportedlanguages(languages.toString());
			} else {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(
								"message.confirmation.languagerequired"));
				store.setSupportedlanguages(Constants.ENGLISH_CODE);
				return SUCCESS;
			}

			store.setStorename(merchantProfile.getStorename());
			store.setStoreemailaddress(merchantProfile.getStoreemailaddress());
			store.setStoreaddress(merchantProfile.getStoreaddress());
			store.setStorecity(merchantProfile.getStorecity());
			store.setStorepostalcode(merchantProfile.getStorepostalcode());
			store.setCountry(merchantProfile.getCountry());
			store.setZone(merchantProfile.getZone());
			store.setCurrency(merchantProfile.getCurrency());
			

			if (!StringUtils.isBlank(merchantProfile.getWeightunitcode())) {
				store.setWeightunitcode(merchantProfile.getWeightunitcode()
						.trim());
			}
			if (!StringUtils.isBlank(merchantProfile.getSeizeunitcode())) {
				store.setSeizeunitcode(merchantProfile.getSeizeunitcode()
						.trim());
			}
			store.setStorelogo(merchantProfile.getStorelogo());
			store.setStorephone(merchantProfile.getStorephone());
			store.setBgcolorcode(merchantProfile.getBgcolorcode());
			store.setContinueshoppingurl(merchantProfile
					.getContinueshoppingurl());
			store.setUseCache(merchantProfile.isUseCache());
			store.setDomainName(merchantProfile.getDomainName());

			store.setMerchantId(merchantid.intValue());
			store.setLastModified(new java.util.Date(dt.getTime()));

			if (!StringUtils.isNumeric(merchantProfile.getZone())) {
				store.setStorestateprovince(merchantProfile
						.getStorestateprovince());
				ctx.setZoneid(0);
			} else {// get the value from zone
				ctx.setZoneid(Integer.parseInt(merchantProfile.getZone()));
				Map zones = RefCache.getInstance().getAllZonesmap(
						LanguageUtil.getLanguageNumberCode(ctx.getLang()));
				Zone z = (Zone) zones.get(Integer.parseInt(merchantProfile
						.getZone()));
				if (z != null) {
					store.setStorestateprovince(z.getZoneName());// @todo,
																	// localization
				} else {
					store.setStorestateprovince("N/A");
				}
			}

			if (!StringUtils.isBlank(this.getInBusinessSince())) {
				Date businessDate = DateUtil.getDate(this.getInBusinessSince());
				store.setInBusinessSince(businessDate);
			}

			super.prepareSelections(store.getCountry());
			mservice.saveOrUpdateMerchantStore(store);

			super.getContext().setExistingStore(true);

			// refresh context

			ctx.setCountryid(merchantProfile.getCountry());
			ctx.setSizeunit(merchantProfile.getSeizeunitcode());
			ctx.setWeightunit(merchantProfile.getWeightunitcode());
			LanguageHelper.setLanguages(languages.toString(), ctx);
			ctx.setCurrency(merchantProfile.getCurrency());

			// refresh the locale
			Map countries = RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));
			Country c = (Country) countries.get(merchantProfile.getCountry());
			Locale locale = new Locale("en", c.getCountryIsoCode2());
			ActionContext.getContext().setLocale(locale);
			Map sessions = ActionContext.getContext().getSession();
			sessions.put("WW_TRANS_I18N_LOCALE", locale);


			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

	public List getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(List supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}


	public String getInBusinessSince() {
		return inBusinessSince;
	}

	public void setInBusinessSince(String inBusinessSince) {
		this.inBusinessSince = inBusinessSince;
	}

}

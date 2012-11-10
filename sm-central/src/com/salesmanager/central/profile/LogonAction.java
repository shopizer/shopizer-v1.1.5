package com.salesmanager.central.profile;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.util.LanguageHelper;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.merchant.MerchantRegistration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.merchant.MerchantUserRole;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;

public class LogonAction extends BaseAction {
	
	private Logger log = Logger.getLogger(LogonAction.class);
	
	private String username;
	private String password;
	
	private Logon logon;
	/**
	 * This is the entry point to the system
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logon() {

		logon = new Logon(); 
		
		try {

			// Logon script
			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantUserInformation merchantProfile = service.adminLogon(super.getServletRequest(), this.getUsername(), this.getPassword());

			// get MerchantUserRegistration
			MerchantRegistration registration = service
					.getMerchantRegistration(merchantProfile.getMerchantId());

			// get MerchantStore
			MerchantStore store = service.getMerchantStore(merchantProfile.getMerchantId());

			
			//get roles
			Collection roles = service.getUserRoles(merchantProfile.getAdminName());
			
			super.getServletRequest().getSession().setAttribute("roles", roles);
			
			//create context - stored in http session
			Context ctx = new Context();
			
			//set master role
			if (roles != null && roles.size() > 0) {
				Iterator i = roles.iterator();
				while (i.hasNext()) {
					MerchantUserRole r = (MerchantUserRole) i.next();
					if (r.getRoleCode().equals("superuser") || r.getRoleCode().equals("admin") || r.getRoleCode().equals("user")) {
						ctx.setMasterRole(r.getRoleCode());
					}
				}
			}
			
			
			ctx.setMerchantid(merchantProfile.getMerchantId());
			ctx.setRegistrationCode(registration
					.getMerchantRegistrationDefCode());
			ctx.setPromoCode(new Integer(registration.getPromoCode()));
			if (!StringUtils.isBlank(merchantProfile.getUserlang())) {
				ctx.setLang(merchantProfile.getUserlang());
			} else {
				ctx.setLang(LanguageUtil.getDefaultLanguage());
			}
			ctx.setUsername(super.getPrincipal().getRemoteUser());

			if (store != null) {

				ctx.setCurrency(store.getCurrency());
				ctx.setSizeunit(store.getSeizeunitcode());
				ctx.setWeightunit(store.getWeightunitcode());
				LanguageHelper.setLanguages(store.getSupportedlanguages(), ctx);

				if (store.getCountry() == 0) {

					ctx.setCountryid(merchantProfile.getUsercountrycode());

				} else {

					ctx.setCountryid(store.getCountry());

				}

				if (StringUtils.isBlank(store.getZone())) {

					if (StringUtils.isNumeric(merchantProfile.getUserstate())) {
						ctx.setZoneid(Integer.parseInt(merchantProfile
								.getUserstate()));
					} else {
						ctx.setZoneid(0);
					}

				} else {

					if (StringUtils.isNumeric(store.getZone())) {
						ctx.setZoneid(Integer.parseInt(store.getZone()));
					} else {
						ctx.setZoneid(0);
					}
				}

				// set default values
				if (StringUtils.isBlank(store.getCurrency())) {
					ctx.setCurrency(Constants.CURRENCY_CODE_USD);
				}

				if (StringUtils.isBlank(store.getWeightunitcode())) {
					ctx.setWeightunit(Constants.LB_WEIGHT_UNIT);
				}

				if (StringUtils.isBlank(store.getSeizeunitcode())) {
					ctx.setWeightunit(Constants.INCH_SIZE_UNIT);
				}

			} else {

				ctx.setCountryid(Constants.US_COUNTRY_ID);
				ctx.setCurrency(Constants.CURRENCY_CODE_USD);
				ctx.setZoneid(0);
				ctx.setExistingStore(false);
			}

			// If country / zone not set, set default values of user until user
			// decides to
			// configure

			// end default settings

			// cleanup previous sessions object
			super.getServletRequest().getSession().removeAttribute(
					ProfileConstants.context);
			super.getServletRequest().getSession().setAttribute(
					ProfileConstants.merchant, merchantProfile.getMerchantId());
			super.getServletRequest().getSession().setAttribute(
					ProfileConstants.context, ctx);
			setAdminTokenToSession(merchantProfile.getMerchantId());

			RefCache cache = RefCache.getInstance();

			Map countries = cache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));
			Country c = (Country) countries.get(ctx.getCountryid());

			//create locale
			Locale locale = new Locale(ctx.getLang(), c.getCountryIsoCode2());
			super.setLocale(locale);

		

		} catch (Throwable e) {
			
			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(super.getLocale());

			//super.getServletRequest().setAttribute("error_message",
			//		LabelUtil.getInstance().getText("errors.technical"));
			logon.setErrorMessage(label.getText("errors.technical"));

			if (e instanceof ServiceException) {
				ServiceException se = (ServiceException) e;

				if (se.getReason() == ErrorConstants.INVALID_CREDENTIALS) {
					//MessageUtil.addErrorMessage(super.getServletRequest(),
					//		LabelUtil.getInstance().getText(
					//				"errors.invalidcredentials"));
					logon.setErrorMessage(label.getText("errors.invalidcredentials"));
					//super.getServletRequest().setAttribute(
					//		"error_message",
					//		LabelUtil.getInstance().getText(
					//				"errors.invalidcredentials"));
					return SUCCESS;
				} 
			}

			//MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
			//		.getInstance().getText("messages.profilecreationerror"));
			log.error(e);
			return "SUCCESS";//need to return this anyway for json request
		} 

		return SUCCESS;
	}
	
	/**
	 * Logout action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {

		Locale locale = getLocale();
		String lang = locale.getLanguage();
		super.getServletRequest().setAttribute("lang", lang);
		return SUCCESS;
	}

	public Logon getLogon() {
		return logon;
	}

	public void setLogon(Logon logon) {
		this.logon = logon;
	}

}

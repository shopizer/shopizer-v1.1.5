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
package com.salesmanager.core.service.merchant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantRegistration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantStoreHeader;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.merchant.MerchantUserRole;
import com.salesmanager.core.entity.merchant.MerchantUserRoleDef;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.MerchantId;
import com.salesmanager.core.module.model.application.AdministrationLogonModule;
import com.salesmanager.core.module.model.application.PasswordGeneratorModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.common.impl.ServicesUtil;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.impl.MerchantConfigurationImpl;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantConfigurationDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantIdDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantRegistrationDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantStoreDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantUserInformationDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDefDao;
import com.salesmanager.core.service.merchant.impl.dao.MerchantUserRoleDefDao;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
public class MerchantService {

	public final static int INTEGRATION_TYPE_SHIPPING = 1;
	public final static int INTEGRATION_TYPE_PAYMENT = 2;

	private Logger log = Logger.getLogger(MerchantService.class);
	private static Configuration conf = PropertiesUtil.getConfiguration();

	@Autowired
	private IMerchantConfigurationDao merchantConfigurationDao;

	@Autowired
	private IMerchantRegistrationDao merchantRegistrationDao;

	@Autowired
	private IMerchantUserInformationDao merchantUserInformationDao;

	@Autowired
	private IMerchantStoreDao merchantStoreDao;

	@Autowired
	private IMerchantIdDao merchantIdDao;

	@Autowired
	private IMerchantUserRoleDao merchantUserRoleDao;
	
	@Autowired
	private IMerchantUserRoleDefDao merchantUserRoleDefDao;

	public String getUser(HttpServletRequest request) throws MerchantException {


		try {


			AdministrationLogonModule module = (AdministrationLogonModule) SpringUtil
					.getBean("merchantLogon");

			return module.getUser(request);
		} catch (Exception e) {
			throw new MerchantException(e);
		}
	}
	
	
	@Transactional
	public Collection<MerchantUserRoleDef> getMerchantUserRoleDef() throws Exception {
		return merchantUserRoleDefDao.findAll();
	}
	
	/**
	 * Creates a basic merchant id
	 * first name
	 * last name
	 * email
	 * admin name
	 * password
	 * Sends an email
	 * @param merchantId
	 * @param merchantUserInformation
	 * @param locale
	 */
	@Transactional
	public void createMerchantUserInformation(int merchantId, MerchantUserInformation merchantUserInformation, Collection<MerchantUserRole> roles, Locale locale) throws Exception {
		
		
		if(merchantUserInformation==null) {
			merchantUserInformation = new MerchantUserInformation();
		}
		merchantUserInformation.setMerchantId(merchantId);
		merchantUserInformation.setLastModified(new Date());
		merchantUserInformation.setUserlang(locale.getLanguage());

		PasswordGeneratorModule passwordGenerator = (PasswordGeneratorModule) SpringUtil
				.getBean("passwordgenerator");
		String key = EncryptionUtil.generatekey(String
				.valueOf(SecurityConstants.idConstant));
		String encrypted;
		String password = "";
		try {
			password = passwordGenerator.generatePassword();
			encrypted = EncryptionUtil.encrypt(key, password);
		} catch (Exception e) {
			log.error(e);
			throw new ServiceException(e);

		}
		merchantUserInformation.setAdminPass(encrypted);
		

		
		MerchantStore store = this.getMerchantStore(merchantId);
		
		merchantUserInformation.setUseraddress(store.getStoreaddress());
		merchantUserInformation.setUsercity(store.getStorecity());
		merchantUserInformation.setUsercountrycode(store.getCountry());
		merchantUserInformation.setUserphone(store.getStorephone());
		merchantUserInformation.setUserpostalcode(store.getStorepostalcode());
		merchantUserInformation.setUserstate(store.getZone());
		
		this.saveOrUpdateMerchantUserInformation(merchantUserInformation);
		
		// send an introduction email
		LabelUtil lhelper = LabelUtil.getInstance();
		String title = lhelper.getText(merchantUserInformation.getUserlang(),
				"label.profile.newmerchantemailtitle");
		String adminInfo = lhelper.getText(merchantUserInformation.getUserlang(),
				"label.profile.userinformation");
		String username = lhelper.getText(merchantUserInformation.getUserlang(),
				"username");
		String pwd = lhelper.getText(merchantUserInformation.getUserlang(),
				"password");
		String url = lhelper.getText(merchantUserInformation.getUserlang(),
				"label.profile.adminurl");
		String mailTitle = lhelper.getText(merchantUserInformation.getUserlang(),
		"label.profile.newusertitle");


		Map context = new HashMap();
		context.put("EMAIL_NEW_USER_TEXT", title);
		context.put("EMAIL_STORE_NAME", store.getStorename());
		context.put("EMAIL_ADMIN_LABEL", adminInfo);
		context.put("EMAIL_CUSTOMER_FIRSTNAME", merchantUserInformation
				.getUserfname());
		context.put("EMAIL_CUSTOMER_LAST", merchantUserInformation.getUserlname());
		context.put("EMAIL_ADMIN_NAME", merchantUserInformation.getAdminName());
		context.put("EMAIL_ADMIN_PASSWORD", password);
		context.put("EMAIL_ADMIN_USERNAME_LABEL", username);
		context.put("EMAIL_ADMIN_PASSWORD_LABEL", pwd);
		context.put("EMAIL_ADMIN_URL_LABEL", url);
		context.put("EMAIL_ADMIN_URL", ReferenceUtil
				.buildCentralUri(store));

		this.saveOrUpdateRoles(roles);

		String email = merchantUserInformation.getAdminEmail();

		CommonService cservice = new CommonService();
		cservice.sendHtmlEmail(email, mailTitle,
				store, context, "email_template_new_user.ftl",
				merchantUserInformation.getUserlang());
		
	}
	
	
	/**
	 * Deletes a MerchantUserInformation entity and roles attached
	 * @param merchantUserId
	 */
	@Transactional
	public void deleteMerchantUserInformation(MerchantUserInformation user) {
	
		merchantUserRoleDao.deleteByUserName(user.getAdminName());
		
		merchantUserInformationDao.delete(user);
		
		
		
	}
	
	/**
	 * Returns a MerchantUserInformation based on the administration email
	 * @param adminEmail
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public MerchantUserInformation getMerchantUserInformationByAdminEmail(String adminEmail) throws Exception {
		return merchantUserInformationDao.findByAdminEmail(adminEmail);
	}

	@Transactional
	public void createNewOrSaveMerchant(MerchantStore creatorStore,
			MerchantUserInformation merchantUserInfo,
			MerchantRegistration merchantRegistration) throws Exception {
		if (merchantUserInfo.getMerchantId() == 0) {
			// New Merchant User

			if (merchantUserInformationDao.findByAdminEmail(merchantUserInfo
					.getAdminEmail()) != null) {
				throw new ServiceException("Admin Email Already Exists",
						ErrorConstants.EMAIL_ALREADY_EXISTS);
			}
			
			
			String language = conf.getString("core.system.defaultlanguage");
			if(!StringUtils.isBlank(merchantUserInfo.getUserlang())) {
				language = merchantUserInfo.getUserlang();
			} else {
				merchantUserInfo.setUserlang(language);
			}

			
			Locale locale = LocaleUtil.getLocale(language);

			MerchantId merchantId = new MerchantId(0, new Date());
			Integer newMerchantId = merchantIdDao.saveMerchantId(merchantId);

			merchantUserInfo.setMerchantId(newMerchantId);
			merchantUserInfo.setLastModified(new Date());


			PasswordGeneratorModule passwordGenerator = (PasswordGeneratorModule) SpringUtil
					.getBean("passwordgenerator");
			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			String encrypted;
			String password = "";
			try {
				password = passwordGenerator.generatePassword();
				encrypted = EncryptionUtil.encrypt(key, password);
			} catch (Exception e) {
				log.error(e);
				throw new ServiceException(e);

			}
			merchantUserInfo.setAdminPass(encrypted);
			// merchantUserInfo.setAdminPass(password);

			merchantUserInformationDao.persist(merchantUserInfo);

			merchantRegistration.setMerchantId(newMerchantId);
			merchantRegistration.setDateAdded(new Date());
			merchantRegistration.setLastModified(new Date());
			merchantRegistration.setPromoCode(0);
			merchantRegistration.setPromoCodeExpiry(new Date());
			merchantRegistrationDao.persist(merchantRegistration);

			// create a MerchantStore
			MerchantStore mStore = new MerchantStore();
			mStore.setMerchantId(newMerchantId);
			mStore.setStorename(merchantUserInfo.getUserfname() + " "
					+ merchantUserInfo.getUserlname());
			mStore.setCountry(merchantUserInfo.getUsercountrycode());
			mStore.setZone(merchantUserInfo.getUserstate());
			mStore.setCurrency(CurrencyUtil.getDefaultCurrency());
			mStore.setTemplateModule("decotemplate");
			merchantStoreDao.saveOrUpdate(mStore);

			// send an introduction email
			LabelUtil lhelper = LabelUtil.getInstance();
			String title = lhelper.getText(merchantUserInfo.getUserlang(),
					"label.profile.newmerchantemailtitle");
			String adminInfo = lhelper.getText(merchantUserInfo.getUserlang(),
					"label.profile.userinformation");
			String username = lhelper.getText(merchantUserInfo.getUserlang(),
					"username");
			String pwd = lhelper.getText(merchantUserInfo.getUserlang(),
					"password");
			String url = lhelper.getText(merchantUserInfo.getUserlang(),
					"label.profile.adminurl");
			String mailTitle = lhelper.getText(merchantUserInfo.getUserlang(),
					"label.profile.newstoretitle");

			Map context = new HashMap();
			context.put("EMAIL_NEW_STORE_TEXT", title);
			context.put("EMAIL_STORE_NAME", mStore.getStorename());
			context.put("EMAIL_ADMIN_LABEL", adminInfo);
			context.put("EMAIL_CUSTOMER_FIRSTNAME", merchantUserInfo
					.getUserfname());
			context.put("EMAIL_CUSTOMER_LAST", merchantUserInfo.getUserlname());
			context.put("EMAIL_ADMIN_NAME", merchantUserInfo.getAdminName());
			context.put("EMAIL_ADMIN_PASSWORD", password);
			context.put("EMAIL_ADMIN_USERNAME_LABEL", username);
			context.put("EMAIL_ADMIN_PASSWORD_LABEL", pwd);
			context.put("EMAIL_ADMIN_URL_LABEL", url);
			context.put("EMAIL_ADMIN_URL", ReferenceUtil
					.buildCentralUri(mStore));

			// create role
			MerchantUserRole role = new MerchantUserRole();
			role.setAdminName(merchantUserInfo.getAdminName());
			role.setRoleCode(SecurityConstants.ADMINISTRATOR);
			merchantUserRoleDao.save(role);

			String email = merchantUserInfo.getAdminEmail();

			CommonService cservice = new CommonService();
			cservice.sendHtmlEmail(email, mailTitle,
					creatorStore, context, "email_template_new_store.ftl",
					merchantUserInfo.getUserlang());

		} else {
			MerchantUserInformation existingUserInfo = merchantUserInformationDao
					.findById(merchantUserInfo.getMerchantUserId().intValue());
			existingUserInfo.setLastModified(new Date());
			existingUserInfo.setAdminEmail(merchantUserInfo.getAdminEmail());
			existingUserInfo.setAdminName(merchantUserInfo.getAdminName());
			existingUserInfo.setUserfname(merchantUserInfo.getUserfname());
			existingUserInfo.setUserlname(merchantUserInfo.getUserlname());
			existingUserInfo.setUseraddress(merchantUserInfo.getUseraddress());
			existingUserInfo.setUserphone(merchantUserInfo.getUserphone());
			existingUserInfo.setUsercity(merchantUserInfo.getUsercity());
			existingUserInfo.setUserpostalcode(merchantUserInfo
					.getUserpostalcode());
			existingUserInfo.setUserstate(merchantUserInfo.getUserstate());
			existingUserInfo.setUsercountrycode(merchantUserInfo
					.getUsercountrycode());
			existingUserInfo.setUserlang(merchantUserInfo.getUserlang());
			merchantUserInformationDao.persist(existingUserInfo);

			MerchantRegistration existingMerchantReg = merchantRegistrationDao
					.findByMerchantId(merchantUserInfo.getMerchantId());
			existingMerchantReg.setLastModified(new Date());
			existingMerchantReg
					.setMerchantRegistrationDefCode(merchantRegistration
							.getMerchantRegistrationDefCode());
			merchantRegistrationDao.persist(existingMerchantReg);
		}
	}

	@Transactional
	public void deleteMerchant(int merchantId) throws Exception {
		MerchantConfiguration config = new MerchantConfiguration();
		config.setMerchantId(merchantId);
		merchantConfigurationDao.delete(config);

		MerchantRegistration merchantReg = merchantRegistrationDao
				.findByMerchantId(merchantId);
		if (merchantReg != null) {
			merchantRegistrationDao.delete(merchantReg);
		}

		Collection<MerchantUserInformation> merchantUsers = merchantUserInformationDao
				.findByMerchantId(merchantId);
		if (merchantUsers != null) {
			merchantUserInformationDao.deleteAll(merchantUsers);
		}

		MerchantStore store = merchantStoreDao.findByMerchantId(merchantId);
		if (store != null) {
			merchantStoreDao.delete(store);
		}

		MerchantId merchant = merchantIdDao.findById(merchantId);
		if (merchant != null) {
			merchantIdDao.delete(merchant);
		}

		// delete roles
		
		for(Object o : merchantUsers) {
			
			MerchantUserInformation userInfo  = (MerchantUserInformation)o;
			merchantUserRoleDao.deleteByUserName(userInfo.getAdminName());
		}
		
		

		// delete merchant configuration
		Collection configs = merchantConfigurationDao
				.findListMerchantId(merchantId);
		if (configs != null && configs.size() > 0) {
			merchantConfigurationDao.delete(configs);
		}

		// delete geo zones
		// delete zones to geozones
		// dynamic label

		ReferenceService referenceService = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		// referenceService.deleteGeoZones(merchantId);
		referenceService.deleteAllDynamicLabel(merchantId);

		// delete products
		// delete categories

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);
		cservice.deleteAllProducts(merchantId);
		cservice.deleteAllCategories(merchantId);

		// delete orders
		// delete orders_account

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);
		oservice.deleteAllOrders(merchantId);

		// delete customers
		CustomerService custservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
		custservice.deleteAllCustomers(merchantId);

		// tax class
		// tax rates
		TaxService taxService = (TaxService) ServiceFactory
				.getService(ServiceFactory.TaxService);
		taxService.deleteTaxConfiguration(merchantId);

	}

	@Transactional
	public List<MerchantStoreHeader> getAllMerchantStores() {
		List<MerchantStoreHeader> merchantStoreList = new ArrayList<MerchantStoreHeader>();
		MerchantStoreHeader header = null;
		// List<MerchantId> merchantIdList = merchantIdDao.loadAll();
		List<MerchantStore> storeList = merchantStoreDao.loadAll();
		for (MerchantStore store : storeList) {
			header = new MerchantStoreHeader();
			header.setMerchantId(store.getMerchantId());
			header.setAdminEmail(store.getStoreemailaddress());
			Collection<MerchantUserInformation> userInfo = merchantUserInformationDao.findByMerchantId(store.getMerchantId());
			if (userInfo != null && userInfo.size()>0) {
				//header.setAdminEmail(userInfo.getAdminEmail());
				MerchantUserInformation mUserInfo = (MerchantUserInformation)((List)userInfo).get(0);
				header.setAdminName(mUserInfo.getAdminName());
			}
			header.setStorename(store.getStorename());
			merchantStoreList.add(header);
		}
		return merchantStoreList;
	}

	/**
	 * Return a collection of roles for a given user
	 * 
	 * @param request
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<MerchantUserRole> getUserRoles(String userName)
			throws Exception {
		return merchantUserRoleDao.findByUserName(userName);
	}
	
	/**
	 * Deletes all user roles
	 * @param userName
	 * @throws Exception
	 */
	@Transactional
	public void deleteUserRoles(String userName) throws Exception {
			merchantUserRoleDao.deleteByUserName(userName);
	}
	
	@Transactional
	public void saveOrUpdateRoles(Collection<MerchantUserRole> roles) throws Exception {
			merchantUserRoleDao.saveOrUpdateAll(roles);
	}

	public MerchantUserInformation adminLogon(HttpServletRequest request, String user, String password)
			throws ServiceException {

		Class clz = null;

		try {

			AdministrationLogonModule module = (AdministrationLogonModule) SpringUtil
					.getBean("merchantLogon");

			return module.logon(request, user, password);

		} catch (Exception e) {
			if (e instanceof ServiceException) {
				throw (ServiceException) e;
			}
			throw new ServiceException(e);
		}

	}

	/**
	 * Get configuration for a given merchant ConfigurationRequestVO constructor
	 * accept: int merchantId, String configurationKey and int merchantId,
	 * boolean like, String configurationKey if boolean like=true then a request
	 * is made for configuration key like %configurationKey% and int merchnatId
	 * 
	 * @param ConfigurationRequest
	 *            request
	 * @return ConfigurationVO response
	 * @throws MerchantException
	 */
	public ConfigurationResponse getConfiguration(ConfigurationRequest request)
			throws MerchantException {

		try {

			MerchantConfigurationImpl impl = (MerchantConfigurationImpl) SpringUtil
					.getBean("merchantConfigurationImpl");
			ConfigurationResponse vo = impl.getConfigurationVO(request);
			return vo;
		} catch (Exception e) {
			throw new MerchantException(e);
		}
	}

	/**
	 * Get configurations for a given module for a given merchant
	 * ConfigurationRequest requires a merchantId
	 * 
	 * @param request
	 * @return
	 * @throws MerchantException
	 */

	public ConfigurationResponse getConfigurationByModule(
			ConfigurationRequest request, String moduleName)
			throws MerchantException {

		try {

			MerchantConfigurationImpl impl = (MerchantConfigurationImpl) SpringUtil
					.getBean("merchantConfigurationImpl");
			ConfigurationResponse vo = impl.getConfigurationVOByModule(request,
					moduleName);
			return vo;
		} catch (Exception e) {
			throw new MerchantException(e);
		}
	}

	/**
	 * Returns the registration details for a given account
	 * 
	 * @param merchantid
	 * @return
	 * @throws MerchantException
	 */
	@Transactional
	public MerchantRegistration getMerchantRegistration(int merchantid)
			throws MerchantException {
		return merchantRegistrationDao.findByMerchantId(merchantid);
	}

	/**
	 * Retreives MerchantProfile Entity Object based on the merchant id
	 * 
	 * @param merchantid
	 * @return MerchantProfile or null
	 * @throws MerchantException
	 */
	// @Transactional
	// public MerchantProfile getMerchantProfile(int merchantid) throws
	// MerchantException {

	// return merchantProfileDao.findById(merchantid);

	// }

	@Transactional
	public MerchantStore getMerchantStore(int merchantid)
			throws MerchantException {
		return merchantStoreDao.findByMerchantId(merchantid);
	}

	/**
	 * Retreives MerchantUserInformation Entity Object based on the
	 * administration name
	 * 
	 * @param merchantid
	 * @return MerchantProfile or null
	 * @throws MerchantException
	 */
	@Transactional
	public MerchantUserInformation getMerchantUserInformation(String adminName)
			throws Exception {
		return merchantUserInformationDao.findByUserName(adminName);
	}
	
	/**
	 * 
	 * @param long merchantUserInformationId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public MerchantUserInformation getMerchantUserInformation(long merchantUserInformationId)
			throws Exception {
		return merchantUserInformationDao.findById(merchantUserInformationId);
	}

	// @Transactional
	// public MerchantProfile getMerchantProfile(String adminName) throws
	// MerchantException {

	// return merchantProfileDao.findByAdminName(adminName);

	// }

	/**
	 * Get MerchantUserInformation by username and password. This is used for
	 * custom authentication
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@Transactional
	public MerchantUserInformation getMerchantInformationByUserNameAndPassword(
			String username, String password) throws Exception {

		// encrypt password
		String key = EncryptionUtil.generatekey(String
				.valueOf(SecurityConstants.idConstant));
		String enc = EncryptionUtil.encrypt(key, password);

		return merchantUserInformationDao.findByUserNameAndPassword(username,
				enc);

	}

	/**
	 * Returns a list of CentralIntegrationServices
	 * 
	 * @param integrationtype
	 * @param countryid
	 * @return
	 * @throws MerchantException
	 */
	public List<CoreModuleService> getModuleServices(int integrationtype,
			String countryIsoCode) throws MerchantException {

		switch (integrationtype) {
		case INTEGRATION_TYPE_SHIPPING:// shipping
			return ServicesUtil
					.getShippingRealTimeQuotesMethods(countryIsoCode);

		case INTEGRATION_TYPE_PAYMENT:// shipping

			return ServicesUtil.getPaymentMethodsList(countryIsoCode);
		}

		return null;

	}

	/**
	 * Will return a MerchantUserInformation for a given merchantId. There can
	 * be more than one MerchantUserInformation entity per merchantId, so it
	 * will return the latest...
	 * 
	 * @param merchantId
	 * @return
	 */
	@Transactional
	public Collection<MerchantUserInformation> getMerchantUserInfo(int merchantId) {
		return merchantUserInformationDao.findByMerchantId(merchantId);
	}

	/**
	 * Removes a MerchantConfiguration entity
	 * 
	 * @param config
	 * @throws MerchantException
	 */
	@Transactional
	public void deleteMerchantConfiguration(MerchantConfiguration config)
			throws MerchantException {

		merchantConfigurationDao.delete(config);

	}

	/**
	 * Removes a collection of MerchantConfiguration
	 * 
	 * @param configs
	 * @throws MerchantException
	 */
	@Transactional
	public void deleteMerchantConfigurations(
			Collection<MerchantConfiguration> configs) throws MerchantException {
		merchantConfigurationDao.delete(configs);
	}

	@Transactional
	public void cleanConfigurationKeys(String likeKey, int merchantid)
			throws MerchantException {

		merchantConfigurationDao.deleteLike(likeKey, merchantid);

	}

	@Transactional
	public void cleanConfigurationKey(String key, int merchantid)
			throws MerchantException {

		merchantConfigurationDao.deleteKey(key, merchantid);

	}

	@Transactional
	public void cleanConfigurationLikeKey(String likeKey, int merchantid)
			throws MerchantException {

		merchantConfigurationDao.deleteLike(likeKey, merchantid);

	}

	@Transactional
	public void cleanConfigurationLikeKeyModule(String likeKey,
			String moduleid, int merchantid) throws MerchantException {

		merchantConfigurationDao
				.deleteLikeModule(likeKey, moduleid, merchantid);

	}

	/**
	 * Returns a ConfigurationVO object for a given module / merchantId
	 * 
	 * @param moduleName
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ConfigurationResponse getConfigurationByModule(String moduleName,
			int merchantId) throws Exception {

		MerchantConfigurationImpl impl = (MerchantConfigurationImpl) SpringUtil
				.getBean("merchantConfigurationImpl");
		ConfigurationResponse vo = impl.getConfigurationVO(moduleName,
				merchantId);
		return vo;

	}

	@Transactional
	public void saveOrUpdateMerchantStore(MerchantStore store) throws Exception {
		merchantStoreDao.saveOrUpdate(store);
	}

	@Transactional
	public void saveOrUpdateMerchantUserInformation(MerchantUserInformation info)
			throws Exception {
		this.merchantUserInformationDao.saveOrUpdate(info);
	}

	@Transactional
	public void saveOrUpdateMerchantConfiguration(
			MerchantConfiguration configuration) throws MerchantException {

		merchantConfigurationDao.saveOrUpdate(configuration);
	}

	@Transactional
	public void saveOrUpdateMerchantConfigurations(
			List<MerchantConfiguration> configurations)
			throws MerchantException {

		if (configurations != null) {
			merchantConfigurationDao.saveOrUpdateAll(configurations);

		}

	}

}

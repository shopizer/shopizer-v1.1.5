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
package com.salesmanager.core.service.reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.reference.CentralCountryStatus;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.reference.ModuleConfiguration;
import com.salesmanager.core.entity.reference.ModuleConfigurationId;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.entity.reference.Portlet;
import com.salesmanager.core.entity.reference.ProductType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.impl.ServicesUtil;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.impl.dao.ICoreModuleServiceDao;
import com.salesmanager.core.service.reference.impl.dao.ICountryDao;
import com.salesmanager.core.service.reference.impl.dao.ICountryDescriptionDao;
import com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao;
import com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao;
import com.salesmanager.core.service.reference.impl.dao.IGeoZoneDao;
import com.salesmanager.core.service.reference.impl.dao.IGlobalReferenceDao;
import com.salesmanager.core.service.reference.impl.dao.IModuleConfigurationDao;
import com.salesmanager.core.service.reference.impl.dao.IPageDao;
import com.salesmanager.core.service.reference.impl.dao.IPortletDao;
import com.salesmanager.core.service.reference.impl.dao.IZoneDao;
import com.salesmanager.core.service.reference.impl.dao.IZoneToGeoZoneDao;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.ConfigurationFieldUtil;
import com.salesmanager.core.util.LanguageUtil;

@Service
public class ReferenceService {

	@Autowired
	private IZoneToGeoZoneDao zoneToGeoZoneDao;

	@Autowired
	private IGeoZoneDao geoZoneDao;

	@Autowired
	private IModuleConfigurationDao moduleConfigurationDao;

	@Autowired
	private ICoreModuleServiceDao coreModuleServiceDao;

	@Autowired
	private IDynamicLabelDao dynamicLabelDao;

	@Autowired
	private IDynamicLabelDescriptionDao dynamicLabelDescriptionDao;

	@Autowired
	private ICountryDescriptionDao countryDescriptionDao;

	@Autowired
	private ICountryDao countryDao;

	@Autowired
	private IGlobalReferenceDao globalReferenceDao;

	@Autowired
	private IZoneDao zoneDao;
	
	@Autowired
	private IPortletDao portletDao;
	
	@Autowired
	private IPageDao pageDao;

	/** Reference Data **/
	/**
	 * This list is used as a reference Collection representing Product Types
	 * 
	 * @return
	 */
	@Transactional
	public Collection<ProductType> getProductTypes() {
		return globalReferenceDao.getProductTypes();
	}

	/**
	 * Returns a Map containing creditCardId and CreditCard entity
	 * 
	 * @return
	 */
	@Transactional
	public Map getSupportedCreditCards() {
		return globalReferenceDao.getSupportedCreditCards();
	}

	/**
	 * Returns the complete country list
	 * 
	 * @return
	 */
	@Transactional
	public Collection<Country> getCountries() {
		return countryDao.getCountries();
	}

	/**
	 * Returns the complete list of languages
	 * 
	 * @return
	 */
	@Transactional
	public Collection<Language> getLanguages() {
		return globalReferenceDao.getLanguages();
	}

	/**
	 * Returns the complete zone list
	 * 
	 * @return
	 */
	@Transactional
	public Collection<Zone> getZones() {
		return globalReferenceDao.getZones();
	}

	/**
	 * Returns the complete measure units list
	 * 
	 * @return
	 */
	@Transactional
	public Collection<CentralMeasureUnits> getMeasureUnits() {
		return globalReferenceDao.getMeasureUnits();
	}

	/**
	 * Returns the complete measure currency list
	 * 
	 * @return
	 */
	@Transactional
	public Collection<Currency> getCurrencies() {
		return globalReferenceDao.getCurrencies();
	}

	/**
	 * Returns the complete order status list
	 * 
	 * @return
	 */
	@Transactional
	public Collection<OrderStatus> getOrderStatus() {
		return globalReferenceDao.getOrderStatus();
	}

	/**
	 * Returns a complete list of shipping country status
	 * 
	 * @return
	 */
	@Transactional
	public Collection<CentralCountryStatus> getCountryStatus() {
		return globalReferenceDao.getCountryStatus();
	}

	@Transactional
	public CountryDescription getCountryDescriptionByIsoCode(String isocode,
			int languageId) throws Exception {
		return countryDescriptionDao.findByIsoCode(isocode, languageId);
	}

	@Transactional
	public CountryDescription getCountryDescriptionByCountryId(int countryId,
			int languageId) throws Exception {
		return countryDescriptionDao.findByCountryId(countryId, languageId);
	}

	@Transactional
	public CountryDescription getCountryDescriptionByName(String name,
			int languageId) throws Exception {
		return countryDescriptionDao.findByCountryName(name, languageId);
	}

	@Transactional
	public Country getCountryByName(String name, int languageId)
			throws Exception {
		return countryDao.findByName(name, languageId);
	}

	@Transactional
	public Country getCountryByIsoCode(String isocode) throws Exception {
		return countryDao.findByIsoCode(isocode);
	}

	@Transactional
	public Zone getZoneByIsoCode(String isocode, int languageId)
			throws Exception {
		return zoneDao.findByCode(isocode, languageId);
	}

	@Transactional
	public Zone getZoneByName(String name, int languageId) throws Exception {
		return zoneDao.findByName(name, languageId);
	}

	@Transactional
	public void deleteGeoZones(int merchantId) throws Exception {
		Collection geoZonesColl = geoZoneDao.findByMerchantId(merchantId);

		if (geoZonesColl != null && geoZonesColl.size() > 0) {
			geoZoneDao.deleteAll(geoZonesColl);
		}

		Collection zoneToGeoCollection = zoneToGeoZoneDao
				.findByMerchantId(merchantId);
		if (zoneToGeoCollection != null && zoneToGeoCollection.size() > 0) {
			zoneToGeoZoneDao.deleteAll(zoneToGeoCollection);
		}
	}

	@Transactional
	public Collection<DynamicLabel> getDynamicLabels(int merchantId,
			int sectionId) throws Exception {
		return dynamicLabelDao.findByMerchantIdAndSectionId(merchantId,
				sectionId);
	}

	@Transactional
	public Collection<DynamicLabel> getDynamicLabels(int merchantId,
			int sectionId, Locale locale) throws Exception {
		return dynamicLabelDao.findByMerchantIdAndSectionIdAndLanguageId(
				merchantId, sectionId, LanguageUtil
						.getLanguageNumberCode(locale.getLanguage()));
	}
	
	@Transactional
	public Collection<DynamicLabel> getDynamicLabels(int merchantId,
			List<Integer> sections, Locale locale) throws Exception {
		return dynamicLabelDao.findByMerchantIdAnsSectionIdsAndLanguageId(
				merchantId, sections, LanguageUtil
						.getLanguageNumberCode(locale.getLanguage()));
	}

	@Transactional
	public Collection<DynamicLabel> getDynamicLabelsByIds(int merchantId, List<Long> ids, Locale locale) throws Exception {
		return dynamicLabelDao.findByMerchantIdAndLabelIdAndLanguageId(merchantId, ids, LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));
	}
	
	@Transactional
	public Collection<DynamicLabel> getDynamicLabelsByTitles(int merchantId, List<String> ids, Locale locale) throws Exception {
		return dynamicLabelDao.findByMerchantIdAndTitleAndLanguageId(merchantId, ids, LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));
	}
	
	@Transactional
	public DynamicLabel getDynamicLabel(long id) throws Exception {
		return dynamicLabelDao.findById(id);
	}

	@Transactional
	public Collection<DynamicLabel> getDynamicLabels(int merchantId)
			throws Exception {
		return dynamicLabelDao.findByMerchantId(merchantId);
	}

	@Transactional
	public Collection<DynamicLabel> getDynamicLabelsByLanguage(int merchantId,
			String language) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(language);

		return dynamicLabelDao.findByMerchantIdAndLanguageId(merchantId, l);
	}

	@Transactional
	public DynamicLabel getDynamicLabelByMerchantIdAndSeUrlAndLanguageId(
			int merchantId, String url, Locale locale) {
		return dynamicLabelDao.findByMerchantIdAndSeUrlAndLanguageId(
				merchantId, url, LanguageUtil.getLanguageNumberCode(locale
						.getLanguage()));
	}

	/**
	 * Deletes all DynamicLabel entities associated to a given merchantId
	 * 
	 * @param merchantId
	 * @throws Exception
	 */
	@Transactional
	public void deleteAllDynamicLabel(int merchantId) throws Exception {
		Collection labels = dynamicLabelDao.findByMerchantId(merchantId);
		List ids = new ArrayList();
		if (labels != null && labels.size() > 0) {
			Iterator labelsIterator = labels.iterator();
			while (labelsIterator.hasNext()) {
				DynamicLabel label = (DynamicLabel) labelsIterator.next();
				ids.add(label.getDynamicLabelId());
				Set descriptions = label.getDescriptions();
				if (descriptions != null) {
					dynamicLabelDescriptionDao.deleteAll(descriptions);
					label.setDescriptions(null);
				}
			}
			dynamicLabelDao.deleteAll(labels);
			
			if(ids.size()>0) {
				Collection portlets = portletDao.getDynamicLabels(ids, merchantId);
				portletDao.deleteAll(portlets);
			}
			
		}

	}

	@Transactional
	public void deleteAllDynamicLabel(Collection<DynamicLabel> labels)
			throws Exception {

		if (labels != null && labels.size() > 0) {
			
			int merchantId = 0;
			
			List ids = new ArrayList();
			Iterator labelsIterator = labels.iterator();
			while (labelsIterator.hasNext()) {
				DynamicLabel label = (DynamicLabel) labelsIterator.next();
				merchantId = label.getMerchantId();
				ids.add(label.getDynamicLabelId());
				Set descriptions = label.getDescriptions();
				if (descriptions != null) {
					dynamicLabelDescriptionDao.deleteAll(descriptions);
					label.setDescriptions(null);
				}
			}
			dynamicLabelDao.deleteAll(labels);
			
			if(ids.size()>0) {
				Collection portlets = portletDao.getDynamicLabels(ids, merchantId);
				portletDao.deleteAll(portlets);
			}
		}

	}

	@Transactional
	public void deleteDynamicLabel(DynamicLabel label) throws Exception {

		
		long id = label.getDynamicLabelId();
		List ids = new ArrayList();
		ids.add(id);
		Collection<Portlet> ps = portletDao.getDynamicLabels(ids, label.getMerchantId());
		if(ps!=null && ps.size()>0) {
			portletDao.deleteAll(ps);
		}
		
		Set descriptions = label.getDescriptions();
		label.setDescriptions(null);
		if (descriptions != null) {
			dynamicLabelDescriptionDao.deleteAll(descriptions);
		}
		dynamicLabelDao.delete(label);
	}
	
	@Transactional
	public DynamicLabel getDynamicLabel(
			int merchantId, String title, Locale locale) {
		return dynamicLabelDao.findByMerchantIdAndTitleAndLanguageId(merchantId, title, LanguageUtil.getLanguageNumberCode(locale.getLanguage()));
		
	}

	@Transactional
	public DynamicLabelDescription getDynamicLabelDescription(int merchantId,
			int sectionId, Locale locale) throws Exception {

		int langId = LanguageUtil.getLanguageNumberCode(locale.getLanguage());

		// get DynamicLabel
		DynamicLabel lbl = null;
		List lbls = (List) dynamicLabelDao.findByMerchantIdAndSectionId(
				merchantId, sectionId);

		if (lbls != null && lbls.size() > 0) {
			lbl = (DynamicLabel) lbls.get(0);
		}

		if (lbl != null) {
			Set descriptions = lbl.getDescriptions();
			if (descriptions != null) {
				Iterator i = descriptions.iterator();
				DynamicLabelDescription returnDesc = null;
				while (i.hasNext()) {
					DynamicLabelDescription desc = (DynamicLabelDescription) i
							.next();
					returnDesc = desc;
					DynamicLabelDescriptionId id = desc.getId();
					if (id.getLanguageId() == langId) {
						return desc;
					}

				}
				return returnDesc;
			}

		}
		return null;
	}

	@Transactional
	public void deleteDynamicLabelDescriptions(
			Collection<DynamicLabelDescription> coll) throws Exception {
		dynamicLabelDescriptionDao.deleteAll(coll);
	}

	@Transactional
	public void saveDynamicLabel(Collection<DynamicLabel> labels)
			throws Exception {
		// dynamicLabelDao.saveOrUpdateAll(labels);
		if (labels != null && labels.size() > 0) {
			Iterator i = labels.iterator();
			while (i.hasNext()) {
				DynamicLabel dl = (DynamicLabel) i.next();
				saveOrUpdateDynamicLabel(dl);
			}
		}

	}

	@Transactional
	public void saveOrUpdateDynamicLabel(DynamicLabel label) throws Exception {

		Set descriptions = label.getDescriptions();
		label.setDescriptions(null);
		dynamicLabelDao.saveOrUpdate(label);

		if (descriptions != null) {

			Iterator i = descriptions.iterator();
			while (i.hasNext()) {

				DynamicLabelDescription desc = (DynamicLabelDescription) i
						.next();
				DynamicLabelDescriptionId id = desc.getId();
				if (id == null) {
					throw new Exception("DynamicLabelDescriptionId is null");
				}
				id.setDynamicLabelId(label.getDynamicLabelId());
			}
			dynamicLabelDescriptionDao.saveOrUpdateAll(descriptions);

		}

	}

	/**
	 * Returns a Module Configuration line
	 * 
	 * @param moduleId
	 * @param configurationKey
	 * @param countryIsoCode
	 * @return
	 */
	@Transactional
	public ModuleConfiguration getModuleConfiguration(String moduleId,
			String configurationKey, String countryIsoCode) throws Exception {

		ModuleConfigurationId id = new ModuleConfigurationId();

		id.setConfigurationKey(configurationKey);
		id.setConfigurationModule(moduleId);
		id.setCountryIsoCode2(countryIsoCode);

		return moduleConfigurationDao.findById(id);

	}

	@Transactional
	public CoreModuleService getCoreModuleService(int countryId,
			String moduleName) throws Exception {
		return coreModuleServiceDao.findByModuleAndRegion(moduleName,
				CountryUtil.getCountryIsoCodeById(countryId));
	}

	@Transactional
	public CoreModuleService getCoreModuleService(String countryIsoCode,
			String moduleName) throws Exception {
		return coreModuleServiceDao.findByModuleAndRegion(moduleName,
				countryIsoCode);
	}

	@Transactional
	public Collection<CoreModuleService> getCoreModuleServices()
			throws Exception {
		return coreModuleServiceDao.getCoreModulesServices();
	}

	/**
	 * Returns a Collection of CoreModuleService for shipping and a given
	 * subType and a given country code
	 * 
	 * @param subType
	 * @param countryIsoCode
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<CoreModuleService> getShippingModules(int subType,
			String countryIsoCode) throws Exception {
		return coreModuleServiceDao.findByServiceTypeAndSubTypeByRegion(
				ShippingConstants.INTEGRATION_SERVICE_SHIPPING_RT_QUOTE,
				subType, countryIsoCode);
	}

	/**
	 * Returns a list of services for a given type (core_modules_service_code)
	 * and a Country iso code
	 * 
	 * @param type
	 * @param countryIsoCode
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<CoreModuleService> getCoreModules(int type,
			String countryIsoCode) throws Exception {
		return coreModuleServiceDao.findByServiceTypeAndByRegion(type,
				countryIsoCode);
	}

	public Collection<CoreModuleService> getPaymentMethodsList(int countryid) {
		String country = CountryUtil.getCountryIsoCodeById(countryid);
		return ServicesUtil.getPaymentMethodsList(country);
	}

	@Transactional
	public Collection<ModuleConfiguration> getModuleConfigurations(
			String configurationKey, int countryId) {
		return moduleConfigurationDao.findByConfigurationKeyAndCountryCode(
				configurationKey, CountryUtil.getCountryIsoCodeById(countryId));
	}
	
	@Transactional
	public Collection<ModuleConfiguration> getModuleConfigurations(
			List<String> ids) {
		return moduleConfigurationDao.findByModuleIds(ids);
	}

	@Transactional
	public Map<String, String> getModuleConfigurationsKeyValue(
			String configurationKey, int countryId) {
		Collection values = moduleConfigurationDao
				.findByConfigurationModuleAndCountryCode(configurationKey,
						CountryUtil.getCountryIsoCodeById(countryId));
		Map returnMap = new HashMap();
		if (values != null && values.size() > 0) {
			Iterator i = values.iterator();
			while (i.hasNext()) {
				ModuleConfiguration conf = (ModuleConfiguration) i.next();
				returnMap.put(conf.getId().getConfigurationKey(), conf
						.getConfigurationValue());
			}
		}
		return returnMap;
	}

	/**
	 * Returns a Map of moduleName,CoreModuleService
	 * 
	 * @param countryid
	 * @return
	 */
	// @Transactional
	public Map<String, CoreModuleService> getPaymentMethodsMap(int countryid) {

		String country = CountryUtil.getCountryIsoCodeById(countryid);

		return ServicesUtil.getPaymentMetodsMap(country);

	}
	
	/** Content Portlets and Page **/
	
	/**
	 * Retrieves a single Portlet based on its portletId
	 */
	@Transactional
	public Portlet getPortlet(long portletId) throws Exception {
		return portletDao.findById(portletId);
	}
	
	/**
	 * Delete a Portlet instance
	 * @param instance
	 * @throws Exception
	 */
	@Transactional
	public void deletePortlet(Portlet portlet) throws Exception {
		portletDao.delete(portlet);
		
		//need to remove portlet configuration
		//get page
		Page page = this.getPage(portlet.getPage(), portlet.getMerchantId());
		
		//get merchant configuration
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		ConfigurationRequest configRequest = new ConfigurationRequest(portlet.getMerchantId(),ConfigurationFieldUtil.getMerchantConfigurationKey(page.getTitle(),portlet.getTitle()));
		ConfigurationResponse configResponse = mservice.getConfiguration(configRequest);
		
		
		
		if(page!=null) {
		
			MerchantConfiguration conf = configResponse.getMerchantConfiguration(ConfigurationFieldUtil.getMerchantConfigurationKey(page.getTitle(),portlet.getTitle()));
			if(conf!=null) {
/*				Map updatebleMap = new HashMap();
				String f = conf.getConfigurationValue();
				if(!StringUtils.isBlank(f)) {
					Map fieldValues = FieldUtil.parseFieldsValues(f);
					for(Object o : fieldValues.keySet()) {
						String module = (String)o;
						if(!module.equals(portlet.getTitle())) {
							List fields = (List)fieldValues.get(module);
							updatebleMap.put(module, fields);
						}
					}
				}
				//update entries
				String line = FieldUtil.buildFieldValuesString(updatebleMap);
				conf.setConfigurationValue(line);*/
				mservice.deleteMerchantConfiguration(conf);
			}
		
		}
		
		
	}
	
	/**
	 * Retrieves a Collection of Portlet for a given page (pageId) and a given merchantId
	 * @param pageId
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<Portlet> getPortlets(long pageId, int merchantId) throws Exception {
		return portletDao.getPortlets(pageId, merchantId);
	}
	
	
	/**
	 * Retrieves a Collection of Portlet for a given page (pageId) 
	 * a given position (columnId) and a merchantId
	 * @param pageId
	 * @param columnId
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<Portlet> getPortlets(long pageId, String columnId, int merchantId) throws Exception {
		return portletDao.getPortlets(pageId, columnId, merchantId);
	}
	
	
	/**
	 * Save or update a Collection of Portlet instances
	 * @param instances
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateAllPortlets(Collection<Portlet> instances) throws Exception {
		portletDao.saveOrUpdateAll(instances);
	}
	
	/**
	 * Save or update a single Portlet instance
	 * @param instance
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdatePortlet(Portlet instance) throws Exception {
		portletDao.saveOrUpdate(instance);
	}
	
	/**
	 * Returns a Page instance based on the pageId and merchantId
	 * @param pageId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Page getPage(long pageId, int merchantId) throws Exception {
		return pageDao.getPage(pageId, merchantId);
	}
	
	/**
	 * Returns a Page instance based on the pageId and merchantId
	 * @param pageId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Page getPage(String title, int merchantId) throws Exception {
		return pageDao.getPage(title, merchantId);
	}
	
	/**
	 * Saves a new Page instance or update an existing Page instance
	 * @param page
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdatePage(Page page) throws Exception {
		pageDao.saveOrUpdate(page);
	}

}

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
package com.salesmanager.central.shipping;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.ref.RefAction;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.module.model.application.CalculatePackingModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.SpringUtil;

public class EditPackingAction extends BaseAction {

	private Collection services;
	private String moduleSelected = null;// user selection
	private CoreModuleService service;

	private Map boxInformation = new TreeMap();
	private Map pageInformation = new TreeMap();

	private String sizeUnit;
	private String weightUnit;

	private PackageDetail shippingInformation;

	private Logger log = Logger.getLogger(EditPackingAction.class);

	public String display() {

		try {
			
			super.setPageTitle("label.shipping.packing.title");

			Context ctx = super.getContext();
			
			weightUnit = ctx.getWeightunit();
			sizeUnit = ctx.getSizeunit();

	
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			services = rservice
					.getShippingModules(
							ShippingConstants.INTEGRATION_SERVICE_SHIPPING_PACKING_SUBTYPE,
							CountryUtil.getCountryIsoCodeById(ctx
									.getCountryid()));

			LocaleUtil.setLocaleToEntityCollection(services, super.getLocale());

			// get module selected
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationRequest request = new ConfigurationRequest(ctx
					.getMerchantid(), "SHP_PACK");
			ConfigurationResponse response = mservice.getConfiguration(request);

			// get MerchantConfiguration object
			if (response != null) {
				MerchantConfiguration conf = response
						.getMerchantConfiguration("SHP_PACK");

				if (conf != null) {

					// parse values (applies to box module)
					this.setModuleSelected(conf.getConfigurationValue());
				} else {
					this
							.setModuleSelected(ShippingConstants.DEFAULT_PACKING_MODULE);
				}

			} else {
				this
						.setModuleSelected(ShippingConstants.DEFAULT_PACKING_MODULE);
			}

			if (services != null) {
				Iterator it = services.iterator();
				while (it.hasNext()) {
					CoreModuleService conf = (CoreModuleService) it.next();
					String module = conf.getCoreModuleName();

					CalculatePackingModule mod = null;
					try {
						mod = (CalculatePackingModule) SpringUtil
								.getBean(module);
					} catch (Exception e) {
						log.warn("Bean " + module
								+ " not defined in sm-core-beans.xml");
					}

					if (mod != null) {

						String infos = mod
								.getConfigurationOptionsFileName(super
										.getLocale());
						if (!StringUtils.isBlank(infos)) {
							pageInformation.put(module, infos);
						}
						if (response != null
								&& response
										.getMerchantConfiguration("SHP_PACK") != null) {
							PackageDetail shinfos = mod
									.getConfigurationOptions(
											response
													.getMerchantConfiguration("SHP_PACK"),
											ctx.getCurrency());
							if (shinfos != null) {
								boxInformation.put(module, shinfos);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String editPackingOption() {

		try {
			
			super.setPageTitle("label.shipping.packing.title");

			this.display();

			Context ctx = super.getContext();

			// get the module submited
			if (service == null) {
				log.error("Service information not submited");
				super.setTechnicalMessage();
				return ERROR;
			}

			// validate submited information
			String module = service.getCoreModuleName();
			CalculatePackingModule mod = null;

			try {
				mod = (CalculatePackingModule) SpringUtil.getBean(module);
			} catch (Exception e) {
				log.error("Module " + module + " not defined");
				super.setTechnicalMessage();
				return ERROR;
			}

			// validate - save
			try {
				mod.storeConfiguration(ctx.getMerchantid(), null, super
						.getServletRequest());
			} catch (ValidationException e) {
				MessageUtil.addErrorMessage(super.getServletRequest(), e
						.getMessage());
				return ERROR;
			}

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public Map getBoxInformation() {
		return boxInformation;
	}

	public void setBoxInformation(Map boxInformation) {
		this.boxInformation = boxInformation;
	}

	public Collection<CoreModuleService> getServices() {
		return services;
	}

	public void setServices(Collection<CoreModuleService> services) {
		this.services = services;
	}

	public String getModuleSelected() {
		return moduleSelected;
	}

	public void setModuleSelected(String moduleSelected) {
		this.moduleSelected = moduleSelected;
	}

	public CoreModuleService getService() {
		return service;
	}

	public void setService(CoreModuleService service) {
		this.service = service;
	}

	public Map getPageInformation() {
		return pageInformation;
	}

	public void setPageInformation(Map pageInformation) {
		this.pageInformation = pageInformation;
	}

	public PackageDetail getShippingInformation() {
		return shippingInformation;
	}

	public void setShippingInformation(PackageDetail shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

}

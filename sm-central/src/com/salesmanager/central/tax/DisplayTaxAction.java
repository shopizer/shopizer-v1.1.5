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
package com.salesmanager.central.tax;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.TaxConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.tax.TaxRateTaxTemplate;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class DisplayTaxAction extends TaxAction {

	private Logger log = Logger.getLogger(DisplayTaxAction.class);

	private int configurationScheme = TaxConstants.NO_SCHEME;

	private ConfigurationResponse configuration = null;

	/**
	 * Displays tax basis options
	 * 
	 * @return
	 * @throws Exception
	 */
	public String displayTaxBasis() throws Exception {
		String displayTax = this.displayTax();// will set all required working
												// variables

		return SUCCESS;
	}

	/**
	 * Display tax class options
	 * 
	 * @return
	 * @throws Exception
	 */
	public String displayTaxClass() throws Exception {
		String displayTax = this.displayTax();// will set all required working
												// variables

		return SUCCESS;
	}

	public void setup() throws Exception {

		super.getServletRequest().setAttribute("SCHEMEID", new Integer(0));

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		ConfigurationRequest request = new ConfigurationRequest(merchantid,
				true, "MODULE_TAX_");

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		configuration = mservice.getConfiguration(request);

		if (configuration != null
				&& configuration.getMerchantConfigurations().size() > 0) {

			MerchantConfiguration conf = (MerchantConfiguration) configuration
					.getMerchantConfiguration(TaxConstants.MODULE_TAX_SCHEME);
			if (conf != null) {

				String value = conf.getConfigurationValue();
				try {
					int schemeid = Integer.parseInt(value);
					this.setConfigurationScheme(schemeid);

					// super.getServletRequest().setAttribute("taxbasis",taxbasis);
					super.getServletRequest().setAttribute("SCHEMEID",
							new Integer(this.getConfigurationScheme()));
					super.setSchemeid(schemeid);

				} catch (NumberFormatException nfe) {
					log.error("Cannot parse " + value + " for merchantid "
							+ merchantid);
				}

			}

			conf = (MerchantConfiguration) configuration
					.getMerchantConfiguration(TaxConstants.MODULE_TAX_BASIS);
			if (conf != null) {
				super.getServletRequest().setAttribute("taxbasis",
						conf.getConfigurationValue());
				super.setTaxbasis(conf.getConfigurationValue());

			}

		}

	}

	/**
	 * Step 1 Display the user's tax option if no tax scheme has been selected.
	 * Will check first if the user is configured for tax and then what kind of
	 * scheme the user has
	 * 
	 * @return
	 * @throws Exception
	 */
	public String displayTax() throws Exception {

		Context ctx = super.getContext();
		Integer merchantid = ctx.getMerchantid();

		try {
			
			super.setPageTitle("label.setuptax.title");

			if (configuration == null
					|| configuration.getMerchantConfigurations().size() == 0) {// no
																				// tax
																				// configured
																				// yet

				TaxService tservice = (TaxService) ServiceFactory
						.getService(ServiceFactory.TaxService);
				Collection<TaxRateTaxTemplate> tsvlist = tservice
						.findByGeoZoneCountryId(ctx.getCountryid());

				if (tsvlist != null && tsvlist.size() > 0) {
					Iterator i = tsvlist.iterator();
					TaxRateTaxTemplate tsv = (TaxRateTaxTemplate) i.next();
					this.setConfigurationScheme(tsv.getZoneToGeoZone()
							.getGeoZone().getSchemeid());
				} else {
					this.setConfigurationScheme(0);
				}

				super.getServletRequest().setAttribute("SCHEMEID",
						new Integer(this.getConfigurationScheme()));
				return "showoptions";

			}

			// need to get the taxrates !!!!!!

			return SUCCESS;

		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
			return "showoptions";
		}

	}

	public String removeTax() throws Exception {
		try {
			cleanupTax();
			return SUCCESS;
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * Remove entries from MerchantConfiguration ->Tax Basis, Tax Scheme
	 * 
	 * @throws Exception
	 */
	private void cleanupTax() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		try {

			TaxService service = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			service.deleteTaxConfiguration(merchantid);

		} catch (Exception e) {
			log.error(e);
		}

	}

	/**
	 * Step 2 - WIZARD - Forward the request to the appropriate configuration
	 * page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String configureTax() throws Exception {
		
		super.setPageTitle("label.setuptax.title");
		int selectedScheme = this.getConfigurationScheme();
		this.cleanupTax();

		try {

			// TaxLineBO line = null;
			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			if (selectedScheme == TaxConstants.US_SCHEME) {
				this.setInitialSetup(String.valueOf(TaxConstants.CA_SCHEME),
						TaxConstants.SHIPPING_TAX_BASIS);

				TaxTemplateHelper.createUSTaxLines(ctx);

				// get merchant country and region
				super.getServletRequest().setAttribute("taxbasis",
						TaxConstants.SHIPPING_TAX_BASIS);
				super.getServletRequest().setAttribute("scheme",
						String.valueOf(TaxConstants.US_SCHEME));

			} else if (selectedScheme == TaxConstants.CA_SCHEME) {
				this.setInitialSetup(String.valueOf(TaxConstants.CA_SCHEME),
						TaxConstants.SHIPPING_TAX_BASIS);

				TaxTemplateHelper.createCATaxLines(ctx);

				super.getServletRequest().setAttribute("taxbasis",
						TaxConstants.SHIPPING_TAX_BASIS);
				super.getServletRequest().setAttribute("scheme",
						String.valueOf(TaxConstants.CA_SCHEME));
			} else if (selectedScheme == TaxConstants.EU_SCHEME) {
				this.setInitialSetup(String.valueOf(TaxConstants.EU_SCHEME),
						TaxConstants.SHIPPING_TAX_BASIS);

				TaxTemplateHelper.createEUTaxLines(ctx);

				super.getServletRequest().setAttribute("taxbasis",
						TaxConstants.SHIPPING_TAX_BASIS);
				super.getServletRequest().setAttribute("scheme",
						String.valueOf(TaxConstants.EU_SCHEME));

			} else {
				this.setInitialSetup(String.valueOf(TaxConstants.NO_SCHEME),
						TaxConstants.SHIPPING_TAX_BASIS);
				// line = new NoSchemeTaxLineBO();

				super.getServletRequest().setAttribute("scheme",
						String.valueOf(TaxConstants.NO_SCHEME));
			}

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			return ERROR;
		}

	}

	public String resetTax() throws Exception {
		try {
			this.cleanupTax();
			return SUCCESS;
		} catch (Exception e) {
			log.error(e);
			return ERROR;
		}
	}

	private void persistConfiguration(MerchantConfiguration obj)
			throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		mservice.saveOrUpdateMerchantConfiguration(obj);

	}

	/**
	 * Step 3 Initial setup will set tax scheme and tax basis User selects
	 * custom from the initial choices
	 * 
	 * @throws Exception
	 */
	private void setInitialSetup(String scheme, String basis) throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		if (scheme != null) {

			// tax scheme
			MerchantConfiguration obj = new MerchantConfiguration();
			obj
					.setConfigurationKey(com.salesmanager.core.constants.TaxConstants.MODULE_TAX_SCHEME);
			obj.setConfigurationValue(scheme);
			obj.setMerchantId(ctx.getMerchantid());

			this.persistConfiguration(obj);

		}

		if (basis != null) {

			// tax basis
			MerchantConfiguration obj = new MerchantConfiguration();
			obj
					.setConfigurationKey(com.salesmanager.core.constants.TaxConstants.MODULE_TAX_BASIS);
			obj.setConfigurationValue(basis);
			obj.setMerchantId(ctx.getMerchantid());

			this.persistConfiguration(obj);

		}

	}

	public int getConfigurationScheme() {
		return configurationScheme;
	}

	public void setConfigurationScheme(int configurationScheme) {
		this.configurationScheme = configurationScheme;
	}

}

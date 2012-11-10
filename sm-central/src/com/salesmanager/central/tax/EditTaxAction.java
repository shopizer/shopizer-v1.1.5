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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.TaxConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.tax.TaxClass;
import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.entity.tax.TaxRateDescription;
import com.salesmanager.core.entity.tax.TaxRateDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class EditTaxAction extends TaxAction {

	private int taxlineaction = 0;
	private long taxlineid = 0;// geozoneid
	private String taxlineorder = "0";
	private long taxlineclassid = -1;
	private String taxlinerate = null;

	private int taxclassaction = -1;
	private long taxclassid = 1;

	private int choosecountry;
	private int choosezone;

	private boolean piggyback = false;

	private Logger log = Logger.getLogger(EditTaxAction.class);

	public void setup() throws Exception {

	}

	/**
	 * Edit tax basis option
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editTaxBasis() throws Exception {

		super.setPageTitle("label.tax.taxbasis.setup");
		
		Map p = this.gatherParameters();

		if (super.getLanguages() == null || super.getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		String taxbasis = (String) p.get(super.TAX_BASIS);

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationRequest request = new ConfigurationRequest(merchantid,
					TaxConstants.MODULE_TAX_BASIS);
			ConfigurationResponse response = mservice.getConfiguration(request);

			MerchantConfiguration config = response
					.getMerchantConfiguration(TaxConstants.MODULE_TAX_BASIS);

			if (config != null && taxbasis != null) {// tax configured
				// if the user is 'shipping' or 'billing' and is asked for
				// store, clean the tax

				config.setConfigurationValue(taxbasis);

				mservice.saveOrUpdateMerchantConfiguration(config);

			}

			super.getServletRequest().setAttribute("taxbasis", taxbasis);

			this.setupTax();

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;

	}

	/**
	 * Add, Modify and Remove a TaxClass
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editTaxClass() throws Exception {
		
		super.setPageTitle("label.tax.taxclass");

		if (super.getLanguages() == null || super.getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		try {

			if (super.getTaxclass() == null || super.getTaxclass().equals("")) {

				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"message.error.taxclass.title"));
				setupTax();
				return SUCCESS;
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			TaxService tservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			List txscl = tservice.getTaxClasses(merchantid);

			java.util.Date dt = new java.util.Date();

			if (taxclassaction == -1 || taxclassaction == 0) {// create
				if (txscl != null) {
					Iterator it = txscl.iterator();
					while (it.hasNext()) {
						TaxClass x = (TaxClass) it.next();
						if (x.getTaxClassTitle().equals(super.getTaxclass())) {
							MessageUtil
									.addErrorMessage(
											super.getServletRequest(),
											LabelUtil
													.getInstance()
													.getText(
															"message.error.taxclass.alreadyexist"));
							this.setupTax();
							return SUCCESS;
						}
					}
				}
			}

			if (taxclassaction == -1) {// create

				if (StringUtils.isBlank(super.getTaxclass())) {
					super.addFieldError("taxclasstitle",
							getText("errors.profile.storenotcreated"));
					return SUCCESS;
				}

				TaxClass tc = new TaxClass();
				tc.setDateAdded(new java.util.Date(dt.getTime()));
				tc.setMerchantId(merchantid);
				tc.setTaxClassDescription(super.getTaxclass());
				tc.setTaxClassTitle(super.getTaxclass());

				tservice.saveOrUpdateTaxClass(tc);

			} else if (taxclassaction == 0) {// update

				if (StringUtils.isBlank(super.getTaxclass())) {
					super.addFieldError("taxclasstitle",
							getText("errors.profile.storenotcreated"));
					return SUCCESS;
				}

				TaxClass tc = tservice.getTaxClass(this.getTaxclassid());

				if (tc == null) {
					log.error("TaxClass does not exist for id "
							+ this.getTaxclassid());
					MessageUtil
							.addErrorMessage(super.getServletRequest(),
									LabelUtil.getInstance().getText(super.getLocale(),
											"errors.technical"));

				}

				tc.setTaxClassTitle(super.getTaxclass());
				tservice.saveOrUpdateTaxClass(tc);

			} else {// delete

				TaxClass tc = tservice.getTaxClass(this.getTaxclassid());
				tservice.deleteTaxClass(tc);

			}

			this.setupTax();
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {

			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));

		}

		return SUCCESS;

	}

	public String addTaxRate() throws Exception {

		if (super.getLanguages() == null || super.getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		BigDecimal amount = null;

		try {

			super.gatherParameters();

			// int schemeid = schemeid;

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			TaxService tservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			HashSet descriptionsset = new HashSet();

			Integer priority = 0;

			// validate amount
			try {

				amount = CurrencyUtil.validateCurrency(this.getTaxlinerate(),
						ctx.getCurrency());

			} catch (Exception e) {

				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"message.error.rate.format"));
				this.setupTax();
				return SUCCESS;
			}

			// descriptions

			Iterator i = super.reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();
				String description = (String) this.getDescriptions().get(
						langcount);

				if (StringUtils.isBlank(description)) {
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(super.getLocale(),
									"message.error.description.required"));
					return SUCCESS;
				}

				int submitedlangid = (Integer) reflanguages.get(langcount);

				TaxRateDescription desc = new TaxRateDescription();
				TaxRateDescriptionId id = new TaxRateDescriptionId();
				id.setLanguageId(submitedlangid);
				desc.setTaxDescription(description);
				desc.setId(id);

				descriptionsset.add(desc);

			}

			TaxRate taxRate = new TaxRate();
			taxRate.setMerchantId(ctx.getMerchantid());
			taxRate.setDescriptions(descriptionsset);
			taxRate.setTaxClassId(TaxConstants.DEFAULT_TAX_CLASS_ID);
			taxRate.setTaxRate(amount);
			taxRate.setPiggyback(this.isPiggyback());

			tservice.saveOrUpdateTaxRate(taxRate, this.getChoosecountry(), this
					.getChoosezone(), ctx.getMerchantid());

			setupTax();
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			return SUCCESS;
		}

	}

	/**
	 * Edit an existing tax rate line
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editTaxRate() throws Exception {

		if (super.getLanguages() == null || super.getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		BigDecimal amount = null;

		try {

			super.gatherParameters();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			TaxService tservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			HashSet descriptionsset = new HashSet();

			Integer priority = 0;

			if (taxlineaction == 0) {// edit

				// validate amunt
				try {

					amount = CurrencyUtil.validateCurrency(this
							.getTaxlinerate(), ctx.getCurrency());

				} catch (Exception e) {

					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(super.getLocale(),
									"message.error.rate.format"));
					this.setupTax();
					return SUCCESS;
				}

				// validate priority
				try {

					priority = new Integer(this.getTaxlineorder());

				} catch (Exception e) {

					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(super.getLocale(),
									"message.error.priority.title"));
					this.setupTax();
					return SUCCESS;
				}

				// descriptions

				Iterator i = super.reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String description = (String) this.getDescriptions().get(
							langcount);

					if (StringUtils.isBlank(description)) {
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(super.getLocale(),
										"message.error.description.required"));
						return SUCCESS;
					}

					int submitedlangid = (Integer) reflanguages.get(langcount);

					TaxRateDescription desc = new TaxRateDescription();
					TaxRateDescriptionId id = new TaxRateDescriptionId();
					id.setLanguageId(submitedlangid);
					id.setTaxRateId(this.getTaxlineid());
					desc.setTaxDescription(description);
					desc.setId(id);

					descriptionsset.add(desc);

				}

			}

			long taxRateId = this.getTaxlineid();

			TaxRate taxRate = null;

			if (taxRateId > 0) {// modify
				taxRate = tservice.getTaxRate(taxRateId);

			}

			if (taxRate == null) {// throw a tech difficulties
				log.error("No tax rate id defined for this tax rate edition");
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),"errors.technical"));
				return SUCCESS;
			}

			if (this.getTaxlineaction() == 1) {
				tservice.deleteTaxRate(taxRate);
			} else {
				taxRate.setDescriptions(descriptionsset);
				taxRate.setTaxClassId(this.getTaxlineclassid());
				taxRate.setTaxRate(amount);
				taxRate.setTaxPriority(priority);
				taxRate.setPiggyback(this.isPiggyback());
				tservice.saveOrUpdateTaxRate(taxRate, this.getChoosecountry(),
						this.getChoosezone(), ctx.getMerchantid());
			}

			setupTax();
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			return SUCCESS;
		}

	}

	public int getTaxlineaction() {
		return taxlineaction;
	}

	public void setTaxlineaction(int taxlineaction) {
		this.taxlineaction = taxlineaction;
	}

	public long getTaxlineid() {
		return taxlineid;
	}

	public void setTaxlineid(long taxlineid) {
		this.taxlineid = taxlineid;
	}

	public long getTaxlineclassid() {
		return taxlineclassid;
	}

	public void setTaxlineclassid(long taxlineclassid) {
		this.taxlineclassid = taxlineclassid;
	}

	public String getTaxlinerate() {
		return taxlinerate;
	}

	public void setTaxlinerate(String taxlinerate) {
		this.taxlinerate = taxlinerate;
	}

	public String getTaxlineorder() {
		return taxlineorder;
	}

	public void setTaxlineorder(String taxlineorder) {
		this.taxlineorder = taxlineorder;
	}

	public int getTaxclassaction() {
		return taxclassaction;
	}

	public void setTaxclassaction(int taxclassaction) {
		this.taxclassaction = taxclassaction;
	}

	public long getTaxclassid() {
		return taxclassid;
	}

	public void setTaxclassid(long taxclassid) {
		this.taxclassid = taxclassid;
	}

	public int getChoosecountry() {
		return choosecountry;
	}

	public void setChoosecountry(int choosecountry) {
		this.choosecountry = choosecountry;
	}

	public int getChoosezone() {
		return choosezone;
	}

	public void setChoosezone(int choosezone) {
		this.choosezone = choosezone;
	}

	public boolean isPiggyback() {
		return piggyback;
	}

	public void setPiggyback(boolean piggyback) {
		this.piggyback = piggyback;
	}

}

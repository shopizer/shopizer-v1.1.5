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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.tax.TaxClass;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

/**
 * Manages 'Other Options'
 * 
 * @author Administrator
 * 
 */
public class ShippingOtherOptionsAction extends ShippingRatesAction implements
		Preparable {

	private Logger log = Logger.getLogger(ShippingOtherOptionsAction.class);

	private MerchantConfiguration shiptaxclass;
	private MerchantConfiguration freeshipinddestamnt;
	private MerchantConfiguration handlingfees;

	private String applytax;
	private String taxclass;
	private String handling;
	private String freeshipdest;
	private String freeshipamnt;
	private String applyfreeshipping;

	public String save() throws Exception {

		List config = null;

		try {

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			TaxService tservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			java.util.Date dt = new java.util.Date();

			// validate BigDecimals -- only if checkbox
			BigDecimal fsamount;
			BigDecimal hamount;
			if (this.getApplyfreeshipping() != null
					&& this.getFreeshipamnt() != null) {
				try {
					// strip , from amount
					fsamount = CurrencyUtil.validateCurrency(this
							.getFreeshipamnt(), ctx.getCurrency());

				} catch (Exception e) {
					
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(
									"message.error.invalidfreeshippingamount"));
					return SUCCESS;
				}
			}

			if (this.getHandling() != null) {
				if (!this.getHandling().equals("")) {
					try {
						// strip , from amount
						hamount = CurrencyUtil.validateCurrency(this
								.getHandling(), ctx.getCurrency());
					} catch (Exception e) {
						
						MessageUtil
								.addErrorMessage(
										super.getServletRequest(),
										LabelUtil
												.getInstance()
												.getText(
														"message.error.invalidhandlingfeeamount"));
						return SUCCESS;
					}
				}
			}

			// update tax class
			if (shiptaxclass != null) {// exist in database
				if (this.getApplytax() == null) {
					// remove shipping tax lines
					super
							.cleanupkey(ShippingConstants.MODULE_SHIPPING_TAX_CLASS);
				} else {
					if (this.getTaxclass() != null
							&& !this.getTaxclass().equals(
									shiptaxclass.getConfigurationValue())) {
						shiptaxclass.setConfigurationValue(this.getTaxclass());
						shiptaxclass.setLastModified(new java.util.Date(dt
								.getTime()));
						mservice
								.saveOrUpdateMerchantConfiguration(shiptaxclass);

					}
				}
			} else {// does not exist in database
				if (this.getApplytax() != null) {// submitted

					// Get tax class id
					MerchantConfiguration conf = new MerchantConfiguration();
					conf
							.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_TAX_CLASS);
					conf.setMerchantId(ctx.getMerchantid());
					conf.setConfigurationValue(this.getTaxclass());
					mservice.saveOrUpdateMerchantConfiguration(conf);

				}
			}

			// update freeshipping indicator
			if (freeshipinddestamnt != null) {// exist in database

				freeshipinddestamnt.setLastModified(new java.util.Date(dt
						.getTime()));
				if (this.getApplyfreeshipping() != null) {

					freeshipinddestamnt.setConfigurationValue("true");

					if (this.getFreeshipdest() != null) {
						freeshipinddestamnt.setConfigurationValue1(this
								.getFreeshipdest());
					}
					if (this.getFreeshipamnt() != null) {
						freeshipinddestamnt.setConfigurationValue2(this
								.getFreeshipamnt());
					}

				} else {

					freeshipinddestamnt.setConfigurationValue("false");
				}

				mservice.saveOrUpdateMerchantConfiguration(freeshipinddestamnt);

			} else {// does not exist in database
				// cleanup first

				if (this.getApplyfreeshipping() != null) {

					freeshipinddestamnt = new MerchantConfiguration();
					freeshipinddestamnt
							.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_FREE_IND_DEST_AMNT);

					freeshipinddestamnt.setConfigurationValue("true");
					freeshipinddestamnt.setMerchantId(ctx.getMerchantid());

					if (this.getFreeshipdest() != null) {
						freeshipinddestamnt.setConfigurationValue1(this
								.getFreeshipdest());
					}
					if (this.getFreeshipamnt() != null) {
						freeshipinddestamnt.setConfigurationValue2(this
								.getFreeshipamnt());
					}
					mservice
							.saveOrUpdateMerchantConfiguration(freeshipinddestamnt);

				}

			}

			// update handling fees
			if (handlingfees != null) {// exist in database
				if (this.getHandling() == null) {
					// remove shipping tax lines
					super
							.cleanupkey(ShippingConstants.MODULE_SHIPPING_HANDLING_FEES);
				} else {
					if (this.getHandling() != null
							&& !this.getHandling().equals(
									handlingfees.getConfigurationValue())) {
						handlingfees.setConfigurationValue(this.getHandling());
						handlingfees.setLastModified(new java.util.Date(dt
								.getTime()));
						mservice
								.saveOrUpdateMerchantConfiguration(handlingfees);

					}
				}
			} else {// does not exist in database
				if (this.getHandling() != null) {// submitted
					MerchantConfiguration conf = new MerchantConfiguration();
					conf
							.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_HANDLING_FEES);
					conf.setMerchantId(ctx.getMerchantid());
					conf.setConfigurationValue(this.getHandling());
					mservice.saveOrUpdateMerchantConfiguration(conf);

				}
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {

			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);

		}

		return SUCCESS;

	}

	public void prepare() throws Exception {

		try {
			
			super.setPageTitle("leftmenu.shipping.shippinghandling");

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			// Set tax classes
			TaxService tservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			List txscl = tservice.getTaxClasses(ctx.getMerchantid());

			Map classtaxesid = new TreeMap();

			if (txscl != null) {
				Iterator it = txscl.iterator();
				while (it.hasNext()) {
					TaxClass x = (TaxClass) it.next();
					classtaxesid.put(String.valueOf(x.getTaxClassId()), x
							.getTaxClassTitle());
				}
			}

			classtaxesid.put("1", "Tax");
			super.getServletRequest().setAttribute("taxclassmap", classtaxesid);

			ConfigurationRequest requestvo = new ConfigurationRequest(
					merchantid.intValue(), true, "SHP_");
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse responsevo = mservice
					.getConfiguration(requestvo);
			List config = responsevo.getMerchantConfigurationList();

			String shipping = null;
			Map szones = new HashMap();

			if (config != null) {
				Iterator it = config.iterator();
				while (it.hasNext()) {
					MerchantConfiguration m = (MerchantConfiguration) it.next();
					String key = m.getConfigurationKey();

					if (key.equals(ShippingConstants.MODULE_SHIPPING_TAX_CLASS)) {
						shiptaxclass = m;
						super.getServletRequest().setAttribute("shiptaxclass",
								m.getConfigurationValue());

					}
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_FREE_IND_DEST_AMNT)) {

						freeshipinddestamnt = m;

						if (m.getConfigurationValue() != null
								&& !m.getConfigurationValue().equals("")) {
							super.getServletRequest().setAttribute(
									"freeshippingindicator",
									m.getConfigurationValue());
						}
						if (m.getConfigurationValue1() != null
								&& !m.getConfigurationValue1().equals("")) {
							super.getServletRequest().setAttribute(
									"freeshippingregion",
									m.getConfigurationValue1());
						}
						if (m.getConfigurationValue2() != null
								&& !m.getConfigurationValue2().equals("")) {
							BigDecimal value = new BigDecimal(0);
							try {
								value = new BigDecimal(m
										.getConfigurationValue2());
							} catch (Exception e) {
								log.error("Invalid big decimal value "
										+ m.getConfigurationValue2());
							}
							super.getServletRequest().setAttribute(
									"freeshippingamount", value);
						}

						super.getServletRequest().setAttribute(
								"freeshippingindicator",
								m.getConfigurationValue());
					}

					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_HANDLING_FEES)) {
						handlingfees = m;
						BigDecimal value = new BigDecimal(0);
						try {
							value = new BigDecimal(m.getConfigurationValue());
						} catch (Exception e) {
							log.error("Invalid big decimal value "
									+ m.getConfigurationValue());
						}
						super.getServletRequest().setAttribute("handlingfees",
								value);
					}
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING)) {
						super.getServletRequest().setAttribute("zonesshipping",
								m.getConfigurationValue());
					}

				}

			}

		} catch (Exception e) {

			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);

		}

	}

	public String display() throws Exception {
		return SUCCESS;
	}

	public MerchantConfiguration getShiptaxclass() {
		return shiptaxclass;
	}

	public MerchantConfiguration getHandlingfees() {
		return handlingfees;
	}

	public String getApplytax() {
		return applytax;
	}

	public void setApplytax(String applytax) {
		this.applytax = applytax;
	}

	public String getTaxclass() {
		return taxclass;
	}

	public void setTaxclass(String taxclass) {
		this.taxclass = taxclass;
	}

	public String getFreeshipdest() {
		if (freeshipdest == null) {
			freeshipdest = ShippingConstants.DOMESTIC_SHIPPING;
		}
		return freeshipdest;
	}

	public void setFreeshipdest(String freeshipdest) {
		this.freeshipdest = freeshipdest;
	}

	public String getHandling() {
		return handling;
	}

	public void setHandling(String handling) {
		this.handling = handling;
	}

	public String getFreeshipamnt() {
		return freeshipamnt;
	}

	public void setFreeshipamnt(String freeshipamnt) {
		this.freeshipamnt = freeshipamnt;
	}

	public String getApplyfreeshipping() {
		return applyfreeshipping;
	}

	public void setApplyfreeshipping(String applyfreeshipping) {
		this.applyfreeshipping = applyfreeshipping;
	}

}

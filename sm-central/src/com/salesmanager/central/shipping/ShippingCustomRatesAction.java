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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.shipping.ShippingPricePound;
import com.salesmanager.core.entity.shipping.ShippingPriceRegion;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

/**
 * Manager custom shipping rates for domestic and international or international
 * shipping
 * 
 * @author Carl Samson
 * 
 */
public class ShippingCustomRatesAction extends BaseAction implements Preparable {

	private Logger log = Logger.getLogger(ShippingCustomRatesAction.class);
	private MerchantConfiguration indiczonescosts;// shipping zones costs
	private MerchantConfiguration zonesskipped;// shipping zones skiped
	private MerchantConfiguration shippingEstimate;// shipping estimate

	private String country;
	private String priceregion;
	private Integer zonepriceid;
	private String zone;
	private String maxprice;
	private String maxweight;

	public final static int MAX_PRICE_SIZE = 5;

	private Map zonesmap = new HashMap();

	private String enablezonequote = null;

	public String display() throws Exception {
		return SUCCESS;
	}

	public String save() throws Exception {
		return SUCCESS;
	}

	public String modifyPriceLine() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (indiczonescosts != null
				&& (indiczonescosts.getConfigurationValue2() != null && !indiczonescosts
						.getConfigurationValue2().equals(""))
				&& maxweight != null && maxprice != null) {

			try {

				Context ctx = (Context) super.getServletRequest().getSession()
						.getAttribute(ProfileConstants.context);
				Integer merchantid = ctx.getMerchantid();

				java.util.Date dt = new java.util.Date();

				BigDecimal _maxprice;

				try {

					_maxprice = CurrencyUtil.validateCurrency(this
							.getMaxprice(), ctx.getCurrency());
				} catch (Exception e) {
					// super.addActionError(getText("message.error.invalidfreeshippingamount"));
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(
									"errors.price.format"));
					return "error";
				}

				int _maxweight;

				try {

					_maxweight = Integer.parseInt(this.getMaxweight());
				} catch (Exception e) {
					log.error("Invalid maxweight value " + this.getMaxweight());
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(
									"errors.weight.format"));
					return "error";
				}

				String costline = this.modifyCostLine(2, indiczonescosts,
						_maxprice, _maxweight);

				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(ctx.getMerchantid());
				indiczonescosts.setConfigurationValue2(costline.toString());
				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);
				// session.update(indiczonescosts);
				// tx.commit();
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));

			} catch (Exception e) {
				// tx.rollback();
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("errors.technical"));
			}

		}
		return SUCCESS;

	}

	private String modifyCostLine(int action, MerchantConfiguration costs,
			BigDecimal price, int weight) throws Exception {

		if (costs == null)
			throw new Exception(" MerchantConfiguration costs is null");

		Map costsmap = buildPriceRegionWithPrice(new HashMap(), costs
				.getConfigurationValue2());

		Set keys = costsmap.keySet();
		Iterator it = keys.iterator();
		StringBuffer costline = new StringBuffer();
		// int i = 1;
		for (int i = 1; i <= ShippingConstants.MAX_PRICE_RANGE_COUNT; i++) {
			// while(it.hasNext()) {
			// int key = (Integer)it.next();
			ShippingPriceRegion spr = (ShippingPriceRegion) costsmap.get(i);
			if (i == zonepriceid) {// price need to be deleted from that id
				// @todo if only one country to be delete, also remove the
				// price-weight

				if (spr != null && spr.getPriceLine() != null) {

					String ln = spr.getPriceLine();
					StringTokenizer st = new StringTokenizer(ln, "|");
					while (st.hasMoreTokens()) {
						String tk = (String) st.nextToken();
						StringTokenizer prtk = new StringTokenizer(tk, ";");
						int countsemic = prtk.countTokens();
						int j = 1;
						while (prtk.hasMoreTokens()) {
							String wpr = (String) prtk.nextToken();
							int indxof = wpr.indexOf(":");
							boolean found = false;

							if (indxof != -1) {// got something

								if (action == 1) {// delete price
									String pr = wpr.substring(0, indxof);
									if (!pr.equals(String.valueOf(weight))) {// if
																				// dealing
																				// with
																				// delete
										costline.append(wpr);// 5:3
									} else {// the one we want to delete or
											// modify
										if (j == countsemic && countsemic == 1) {
											costline.append("*");
										}
										found = true;
									}
								} else {// modify price

									String pr = wpr.substring(0, indxof);
									if (!pr.equals(String.valueOf(weight))) {// if
																				// dealing
																				// with
																				// delete
										costline.append(wpr);// 5:3
									} else {// the one we want to delete or
											// modify
										costline.append(
												wpr.substring(0, indxof))
												.append(":").append(price);
									}
								}
							} else {
								costline.append(wpr);
							}
							if (j < countsemic && !found) {

								costline.append(";");
							}
							j++;
						}
					}
				} else {
					costline.append("*");
				}

			} else {

				if (spr != null && spr.getPriceLine() != null) {
					costline.append(spr.getPriceLine());
				} else {
					costline.append("*");
				}
			}

			if (i < costsmap.size()) {
				costline.append("|");
			}

		}

		return costline.toString();

	}

	public String removePriceLine() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (indiczonescosts != null
				&& (indiczonescosts.getConfigurationValue2() != null && !indiczonescosts
						.getConfigurationValue2().equals(""))
				&& maxweight != null) {

			try {

				Context ctx = (Context) super.getServletRequest().getSession()
						.getAttribute(ProfileConstants.context);
				Integer merchantid = ctx.getMerchantid();

				java.util.Date dt = new java.util.Date();

				int _maxweight;

				try {

					_maxweight = Integer.parseInt(this.getMaxweight());
				} catch (Exception e) {
					log.error("Invalid maxweight value " + this.getMaxweight());
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(
									"errors.weight.format"));
					return "error";
				}

				String costline = this.modifyCostLine(1, indiczonescosts,
						new BigDecimal(0), _maxweight);

				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(ctx.getMerchantid());
				indiczonescosts.setConfigurationValue2(costline.toString());
				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));

			} catch (Exception e) {
				// tx.rollback();
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("errors.technical"));
			}

		}
		return SUCCESS;
	}

	public String addMaxPrice() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (zonepriceid == null || maxprice == null || maxweight == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.generic"));
			log
					.error("Missing zonepriceid or maxprice or maxweight in addMaxPrice request");
			return "error";
		}

		int regioncount = 0;
		int _maxweignt = 0;

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		try {

			regioncount = new Integer(zonepriceid);
		} catch (Exception e) {
			log.error("Invalid region count value " + this.getPriceregion());
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.generic"));
			return "error";
		}

		try {

			_maxweignt = Integer.parseInt(this.getMaxweight());
		} catch (Exception e) {
			log.error("Invalid maxweight value " + this.getMaxweight());
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.weight.format"));
			return "error";
		}

		BigDecimal _maxprice;

		try {

			_maxprice = CurrencyUtil.validateCurrency(this.getMaxprice(), ctx
					.getCurrency());
		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.price.format"));
			return "error";
		}

		try {

			java.util.Date dt = new java.util.Date();

			if (indiczonescosts.getConfigurationValue2() == null) {// first
																	// entry

				StringBuffer costline = new StringBuffer();
				for (int i = 1; i <= ShippingConstants.MAX_PRICE_REGION_COUNT; i++) {
					if (i == regioncount) {
						costline.append(_maxprice).append(":").append(
								_maxweignt);
					} else {
						costline.append("*");
					}
					if (i < ShippingConstants.MAX_PRICE_REGION_COUNT) {
						costline.append("|");
					}

					indiczonescosts.setConfigurationValue2(costline.toString());
				}

				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(merchantid);
				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);

			} else {// entries already exist

				Map regionmap = buildPriceRegionWithPrice(new HashMap(),
						indiczonescosts.getConfigurationValue2());

				ShippingPriceRegion spr = (ShippingPriceRegion) regionmap
						.get(regioncount);
				ShippingPricePound spp = new ShippingPricePound();
				if (spr == null) {
					spr = new ShippingPriceRegion();
				}
				spp.setMaxpound(_maxweignt);
				spp.setPrice(_maxprice);
				spr.addPrice(spp);
				regionmap.put(regioncount, spr);

				// Get the line now
				StringBuffer costsline = new StringBuffer();
				for (int i = 1; i <= ShippingConstants.MAX_PRICE_REGION_COUNT; i++) {
					if (regionmap.containsKey(i)) {
						ShippingPriceRegion priceregion = (ShippingPriceRegion) regionmap
								.get(i);
						List prices = priceregion.getPrices();
						Collections.sort(prices);
						Iterator prit = prices.iterator();
						int j = 1;
						StringBuffer pricesbuffer = new StringBuffer();
						while (prit.hasNext()) {

							ShippingPricePound pp = (ShippingPricePound) prit
									.next();

							pricesbuffer.append(pp.getMaxpound());
							pricesbuffer.append(":");
							pricesbuffer.append(pp.getPrice());
							if (j < prices.size()) {
								pricesbuffer.append(";");
							}
							j++;
						}
						if (prices.size() == 0) {
							pricesbuffer.append("*");
						}
						costsline.append(pricesbuffer.toString());
					} else {// empty line
						costsline.append("*");
					}
					if (i < ShippingConstants.MAX_PRICE_REGION_COUNT) {
						costsline.append("|");
					}
				}
				indiczonescosts.setConfigurationValue2(costsline.toString());
				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(merchantid);

				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);

			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			return "error";
		}

		return SUCCESS;
	}

	/**
	 * Removes a zone from the configuration
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeZonePrice() throws Exception {

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			java.util.Date dt = new java.util.Date();

			List configurations = new ArrayList();

			if (indiczonescosts != null
					&& !StringUtils.isBlank(indiczonescosts
							.getConfigurationValue1())) {
				configurations.add(this.deleteLine(indiczonescosts,
						zonepriceid, 1));
			}

			if (indiczonescosts != null
					&& !StringUtils.isBlank(indiczonescosts
							.getConfigurationValue2())) {
				configurations.add(this.deleteLine(indiczonescosts,
						zonepriceid, 2));
			}

			if (shippingEstimate != null
					&& !StringUtils.isBlank(shippingEstimate
							.getConfigurationValue1())) {
				configurations.add(this.deleteShippingEstimate(
						shippingEstimate, zonepriceid));
			}

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			mservice.saveOrUpdateMerchantConfigurations(configurations);

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	/**
	 * Removes a custom shipping zone
	 * 
	 * @return
	 * @throws Exception
	 */

	public String removeCountry() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (indiczonescosts != null
				&& (indiczonescosts.getConfigurationValue1() != null && !indiczonescosts
						.getConfigurationValue1().equals(""))) {

			try {

				Context ctx = (Context) super.getServletRequest().getSession()
						.getAttribute(ProfileConstants.context);
				Integer merchantid = ctx.getMerchantid();

				java.util.Date dt = new java.util.Date();

				Map countriesmap = buildPriceRegionWithRegion(new HashMap(),
						indiczonescosts.getConfigurationValue1());

				Set keys = countriesmap.keySet();
				Iterator it = keys.iterator();
				StringBuffer countryline = new StringBuffer();

				for (int i = 1; i <= ShippingConstants.MAX_PRICE_RANGE_COUNT; i++) {

					ShippingPriceRegion spr = (ShippingPriceRegion) countriesmap
							.get(i);
					if (i == zonepriceid) {
						// @todo if only one country to be delete, also remove
						// the price-weight
						String ln = spr.getCountryline();
						String newline = "";
						if (ln != null && ln.length() == 2 && ln.contains(zone)) {// only
																					// one
																					// country
							// remove country line

							// remove price line
							this.deleteLine(indiczonescosts, zonepriceid, 2);

							newline = "*";
						} else {// must strip the country from the line received
							int idxof = ln.indexOf(zone);
							if (idxof != -1) {// not found
								if (idxof == 0) {
									newline = ln.substring(idxof + 3, ln
											.length());// first item
								} else if (idxof == ln.length() - 2) {// should
																		// be -1
																		// end
																		// item
									newline = ln.substring(0, ln.length() - 3);
								} else {// middle item
									newline = ln.substring(0, idxof - 1)
											+ ln.substring(idxof + 2, ln
													.length());
								}
							}
						}
						countryline.append(newline);
					} else {
						if (spr != null && spr.getCountryline() != null) {
							countryline.append(spr.getCountryline());

						} else {
							countryline.append("*");
						}
					}
					if (i < countriesmap.size()) {
						countryline.append("|");
					}
				}

				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(ctx.getMerchantid());
				indiczonescosts.setConfigurationValue1(countryline.toString());
				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));

			} catch (Exception e) {
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("errors.technical"));
			}

		}
		return SUCCESS;
	}

	/**
	 * Will add a Custom Shipping Zone
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (this.getCountry() == null || this.getPriceregion() == null) {
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.generic"));
			log.error("Missing country or priceregion fromadd request");
			return SUCCESS;
		}

		int regioncount = 0;

		try {

			regioncount = Integer.parseInt(this.getPriceregion());

		} catch (Exception e) {
			log.error("Invalid region count value " + this.getPriceregion());
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.generic"));
			return SUCCESS;
		}

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			java.util.Date dt = new java.util.Date();

			if (indiczonescosts == null
					|| indiczonescosts.getConfigurationValue1() == null
					|| indiczonescosts.getConfigurationValue1().equals("")) {// first
																				// entry
				indiczonescosts = new MerchantConfiguration();
				indiczonescosts
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);
				indiczonescosts.setConfigurationValue("");
				indiczonescosts.setConfigurationValue1("");
				indiczonescosts.setConfigurationValue2("");
				indiczonescosts.setDateAdded(new java.util.Date(dt.getTime()));
				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));

				StringBuffer countriesline = new StringBuffer();
				for (int i = 1; i <= ShippingConstants.MAX_PRICE_REGION_COUNT; i++) {
					if (i == regioncount) {
						countriesline.append(this.getCountry());
					} else {
						countriesline.append("*");
					}
					if (i < ShippingConstants.MAX_PRICE_REGION_COUNT) {
						countriesline.append("|");
					}
				}

				indiczonescosts
						.setConfigurationValue1(countriesline.toString());

				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(merchantid);

				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);

			} else {// entries already exist

				Map countriesmap = buildPriceRegionWithRegion(new HashMap(),
						indiczonescosts.getConfigurationValue1());
				ShippingPriceRegion spr = (ShippingPriceRegion) countriesmap
						.get(regioncount);
				if (spr == null) {
					spr = new ShippingPriceRegion();
				}
				spr.addCountry(this.getCountry());
				countriesmap.put(regioncount, spr);

				// Get the line now
				StringBuffer costsline = new StringBuffer();
				for (int i = 1; i <= ShippingConstants.MAX_PRICE_REGION_COUNT; i++) {
					if (countriesmap.containsKey(i)) {
						ShippingPriceRegion priceregion = (ShippingPriceRegion) countriesmap
								.get(i);
						List prl = priceregion.getCountries();
						Iterator prlit = prl.iterator();
						int j = 1;
						StringBuffer countriesbuffer = new StringBuffer();
						while (prlit.hasNext()) {
							countriesbuffer.append((String) prlit.next());
							if (j < prl.size()) {
								countriesbuffer.append(";");
							}
							j++;
						}
						if (prl.size() == 0) {
							countriesbuffer.append("*");
						}
						costsline.append(countriesbuffer.toString());
					} else {// empty line
						costsline.append("*");

					}
					if (i < ShippingConstants.MAX_PRICE_REGION_COUNT) {
						costsline.append("|");
					}
				}
				indiczonescosts.setConfigurationValue1(costsline.toString());
				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(merchantid);

				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);

			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;
	}

	public String editTableRate() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			java.util.Date dt = new java.util.Date();

			if (indiczonescosts != null) {
				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				indiczonescosts.setMerchantId(ctx.getMerchantid());
				if (this.getEnablezonequote() != null) {
					indiczonescosts.setConfigurationValue("true");
				} else {
					indiczonescosts.setConfigurationValue("false");
				}
				mservice.saveOrUpdateMerchantConfiguration(indiczonescosts);
			} else {
				MerchantConfiguration conf = new MerchantConfiguration();
				indiczonescosts = new MerchantConfiguration();
				indiczonescosts
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS);
				indiczonescosts.setConfigurationValue("");
				indiczonescosts.setConfigurationValue1("");
				indiczonescosts.setConfigurationValue2("");
				indiczonescosts.setDateAdded(new java.util.Date(dt.getTime()));
				indiczonescosts
						.setLastModified(new java.util.Date(dt.getTime()));
				if (this.getEnablezonequote() != null) {
					conf.setConfigurationValue("true");
				} else {
					conf.setConfigurationValue("false");
				}
				conf.setMerchantId(ctx.getMerchantid());
				mservice.saveOrUpdateMerchantConfiguration(conf);
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			// tx.rollback();
			log.error(e);
		}
		return SUCCESS;
	}

	public void prepare() throws Exception {

		try {
			
			super.setPageTitle("label.shipping.customrates");
			
			

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			ConfigurationRequest req = new ConfigurationRequest(merchantid,
					true, "SHP_");

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationResponse res = mservice.getConfiguration(req);

			List config = res.getMerchantConfigurationList();

			Map priceregionmap = null;

			String calculateEstimateTime = null;

			if (config != null) {

				Iterator it = config.iterator();
				while (it.hasNext()) {
					MerchantConfiguration c = (MerchantConfiguration) it.next();
					String key = c.getConfigurationKey();
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED)) {
						// determine if it is domestic or international
						Map szones = new HashMap();
						zonesskipped = c;
						String skipped = c.getConfigurationValue();
						StringTokenizer st = new StringTokenizer(skipped, ";");
						while (st.hasMoreTokens()) {
							String token = st.nextToken();
							szones.put(token, token);
						}
						super.getServletRequest().setAttribute("zonesskipped",
								szones);
					}
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_INDIC_COUNTRIES_COSTS)) {

						indiczonescosts = c;
						if (c.getConfigurationValue1() != null) {
							if (priceregionmap == null) {
								priceregionmap = new TreeMap();
							}
							priceregionmap = buildPriceRegionWithRegion(
									priceregionmap, c.getConfigurationValue1());
						}
						if (c.getConfigurationValue2() != null) {
							if (priceregionmap == null) {
								priceregionmap = new TreeMap();
							}
							priceregionmap = buildPriceRegionWithPrice(
									priceregionmap, c.getConfigurationValue2());
						}
						if (c.getConfigurationValue() != null) {
							super.getServletRequest().setAttribute(
									"shippingzonesindicator",
									c.getConfigurationValue());
						}
					}

					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY)) {
						shippingEstimate = c;
						calculateEstimateTime = c.getConfigurationValue1();
					}

					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING)) {
						super.getServletRequest().setAttribute("zonesshipping",
								c.getConfigurationValue());
					}
				}
			}

			if (calculateEstimateTime != null) {
				priceregionmap = buildPriceRegionWithEstimatedCosts(
						priceregionmap, calculateEstimateTime);
			}

			if (priceregionmap == null) {
				priceregionmap = new HashMap();
				priceregionmap.put(1, new ShippingPriceRegion());
				priceregionmap.put(2, new ShippingPriceRegion());
				priceregionmap.put(3, new ShippingPriceRegion());
				priceregionmap.put(4, new ShippingPriceRegion());
				priceregionmap.put(5, new ShippingPriceRegion());
			}
			
			super.getServletRequest().setAttribute("zonesmap", zonesmap);

			super.getServletRequest()
					.setAttribute("pricezones", priceregionmap);

		} catch (Exception e) {
			log.error(e);
		}

	}

	private MerchantConfiguration deleteLine(MerchantConfiguration conf,
			int index, int value) throws Exception {

		StringTokenizer cvtk = null;

		String confValue = "";

		if (value == 1) {// country
			confValue = conf.getConfigurationValue1();

		} else {// price
			confValue = conf.getConfigurationValue2();
		}

		cvtk = new StringTokenizer(confValue, "|");

		StringBuffer linebuffer = new StringBuffer();
		int i = 1;
		int count = cvtk.countTokens();
		while (cvtk.hasMoreTokens()) {

			String line = (String) cvtk.nextToken();
			if (i != index) {
				linebuffer.append(line);
			} else {
				linebuffer.append("*");// replace the line
			}
			if (i < count) {
				linebuffer.append("|");
			}
			i++;
		}

		if (value == 1) {
			conf.setConfigurationValue1(linebuffer.toString());
		} else {
			conf.setConfigurationValue2(linebuffer.toString());
		}
		java.util.Date dt = new java.util.Date();
		conf.setLastModified(dt);

		return conf;

	}

	private MerchantConfiguration deleteShippingEstimate(
			MerchantConfiguration conf, int index) throws Exception {

		StringTokenizer cvtk = null;

		String confValue = "";

		confValue = conf.getConfigurationValue1();

		cvtk = new StringTokenizer(confValue, "|");

		StringBuffer linebuffer = new StringBuffer();
		int i = 1;
		int count = cvtk.countTokens();
		while (cvtk.hasMoreTokens()) {

			String line = (String) cvtk.nextToken();
			if (!line.equals("*")) {
				String newLine = line.substring(0, 2);
				if (newLine.equals(new StringBuffer().append(index).append(":")
						.toString())) {
					linebuffer.append("*");// replace the line
				} else {
					linebuffer.append(line);
				}
			} else {
				linebuffer.append(line);
			}
			if (i < count) {
				linebuffer.append("|");
			}
			i++;
		}

		conf.setConfigurationValue1(linebuffer.toString());

		java.util.Date dt = new java.util.Date();
		conf.setLastModified(dt);

		return conf;

	}

	private Map buildPriceRegionWithPrice(Map map, String s) throws Exception {

		/**
		 * contains a map prices - List 0 --- ShippingPriceRegion
		 * -------------ShippingPricePound -------------ShippingPricePound 1 ---
		 * ShippingPriceRegion -------------ShippingPricePound
		 * -------------ShippingPricePound
		 */

		if (map == null) {
			map = new TreeMap();
		}

		Map returnmap = map;
		StringTokenizer cvtk = new StringTokenizer(s, "|");
		String costline = null;
		int i = 1;
		while (cvtk.hasMoreTokens()) {

			ShippingPriceRegion spr = null;
			if (returnmap.containsKey(i)) {
				spr = (ShippingPriceRegion) returnmap.get(i);
			} else {
				spr = new ShippingPriceRegion();
			}
			costline = cvtk.nextToken();// maxpound:price,maxpound:price...|
			StringTokenizer pricestk = new StringTokenizer(costline, ";");
			String poundpriceline = null;
			while (pricestk.hasMoreTokens()) {
				poundpriceline = pricestk.nextToken();// maxpound:price
				if (!poundpriceline.equals("*")) {
					// now get maxpound and price
					int j = 1;
					StringTokenizer poundprice = new StringTokenizer(
							poundpriceline, ":");
					ShippingPricePound spp = new ShippingPricePound();
					while (poundprice.hasMoreTokens()) {
						String val = poundprice.nextToken();
						if (j == 1) {
							spp.setMaxpound(Integer.parseInt(val));
						}
						if (j == 2) {
							spp.setPrice(new BigDecimal(val));
						}
						j++;
					}
					spr.addPrice(spp);
				}
			}
			spr.setPriceLine(costline);
			returnmap.put(i, spr);
			i++;
		}

		return returnmap;

	}

	private Map buildPriceRegionWithRegion(Map map, String s) throws Exception {

		/**
		 * contains a map countries - List 0 --- ShippingPriceRegion
		 * -------------String -------------String 1 --- ShippingPriceRegion
		 * -------------String -------------String
		 */

		if (map == null) {
			map = new TreeMap();
		}

		Map returnmap = map;
		StringTokenizer cvtk = new StringTokenizer(s, "|");
		String countryline = null;
		int i = 1;
		while (cvtk.hasMoreTokens()) {
			ShippingPriceRegion spr = null;
			if (returnmap.containsKey(i)) {
				spr = (ShippingPriceRegion) returnmap.get(i);
			} else {
				spr = new ShippingPriceRegion();
			}
			countryline = cvtk.nextToken();// maxpound:price,maxpound:price...|
			if (!countryline.equals("*")) {
				StringTokenizer countrystk = new StringTokenizer(countryline,
						";");
				String country = null;
				StringBuffer countrline = new StringBuffer();
				while (countrystk.hasMoreTokens()) {
					country = countrystk.nextToken();
					// now get maxpound and price
					spr.addCountry(country);
					zonesmap.put(country, country);
					countrline.append(country).append(";");
				}
				String line = countrline.toString();
				spr.setCountryline(line.substring(0, line.length() - 1));
			}
			returnmap.put(i, spr);
			i++;
		}

		return returnmap;

	}

	private Map buildPriceRegionWithEstimatedCosts(Map map, String s)
			throws Exception {

		/**
		 * contains a map countries - List 0 --- ShippingPriceRegion (0 is
		 * index) -------------String -------------String 1 ---
		 * ShippingPriceRegion -------------String -------------String
		 */

		if (map == null) {
			map = new TreeMap();
		}

		Map returnmap = map;
		StringTokenizer cvtk = new StringTokenizer(s, "|");// index:<MINCOST>;<MAXCOST>|
		String countryline = null;
		int i = 1;
		while (cvtk.hasMoreTokens()) {

			countryline = cvtk.nextToken();// index:<MINCOST>;<MAXCOST>

			StringTokenizer indextk = new StringTokenizer(countryline, ":");// index
			String configLine = null;
			// StringBuffer countrline = new StringBuffer();
			int indexCount = 1;
			ShippingPriceRegion spr = null;
			while (indextk != null && indextk.hasMoreTokens()) {
				configLine = indextk.nextToken();

				if (indexCount == 1) {// countries
					try {
						int index = Integer.parseInt(configLine);
						spr = (ShippingPriceRegion) returnmap.get(index);
						if (spr != null) {
							spr.setEstimatedTimeEnabled(true);
						}
					} catch (Exception e) {
						log.error("Cannot parse to an integer " + configLine);
					}
				}
				if (indexCount == 2) {// days
					// parse dates <mindate>;<maxdate>
					StringTokenizer datetk = new StringTokenizer(configLine,
							";");// date
					int dateCount = 1;
					while (datetk != null && datetk.hasMoreTokens()) {
						String date = (String) datetk.nextToken();

						try {

							if (spr != null) {
								if (dateCount == 1) {
									spr.setMinDays(Integer.parseInt(date));
								}
								if (dateCount == 2) {
									spr.setMaxDays(Integer.parseInt(date));
								}
							}

						} catch (Exception e) {
							log.error("Cannot parse integer " + date);
						}

						dateCount++;
					}
				}
				indexCount++;
			}
			i++;
		}
		return returnmap;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPriceregion() {
		return priceregion;
	}

	public void setPriceregion(String priceregion) {
		this.priceregion = priceregion;
	}

	public String getEnablezonequote() {
		return enablezonequote;
	}

	public void setEnablezonequote(String enablezonequote) {
		this.enablezonequote = enablezonequote;
	}

	public Integer getZonepriceid() {
		return zonepriceid;
	}

	public void setZonepriceid(Integer zonepriceid) {
		this.zonepriceid = zonepriceid;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(String maxprice) {
		this.maxprice = maxprice;
	}

	public String getMaxweight() {
		return maxweight;
	}

	public void setMaxweight(String maxweight) {
		this.maxweight = maxweight;
	}

}

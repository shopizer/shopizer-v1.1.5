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
package com.salesmanager.central.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceDescription;
import com.salesmanager.core.entity.catalog.ProductPriceDescriptionId;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MessageUtil;

public class EditProductPriceAction extends BaseAction {

	private static Logger log = Logger.getLogger(EditProductPriceAction.class);
	private Product product;
	private ProductPrice price;// submited
	private Set<ProductPrice> prices;// available prices for a given product
	private Collection pricesModules;// available prices modules
	private String productPriceAmount;// amount submited
	private String productName;

	private List<String> priceNames = new ArrayList<String>();

	private int action = -1;// actions -1 (add), 0 (modify) 1 (delete)

	public String editProductPrice() {

		try {
			
			super.setPageTitle("label.product.productprices.title");

			Context ctx = super.getContext();

			super.prepareLanguages();

			ProductPrice productPrice = this.getPrice();

			Set prices = this.getProduct().getPrices();

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			ProductPrice pprice = cservice.getProductPrice(this.getPrice()
					.getProductPriceId());

			if (this.getAction() == -1) {// add
				// get core module service
				CommonService cs = (CommonService) ServiceFactory
						.getService(ServiceFactory.CommonService);
				CoreModuleService cms = cs.getModule(CountryUtil
						.getCountryIsoCodeById(ctx.getCountryid()), this
						.getPrice().getProductPriceModuleName());
				price.setProductPriceTypeId(cms.getCoreModuleServiceSubtype());// 1
																				// is
																				// one
																				// time,
																				// 2
																				// is
																				// recursive
			}

			if (this.getAction() == 1) {// delete
				cservice.deleteProductPrice(pprice);
				super.setSuccessMessage();
				return SUCCESS;
			}

			if (pprice != null && this.getAction() == 0) {// modify
				price.setProductPriceModuleName(pprice
						.getProductPriceModuleName());
				price.setProductPriceTypeId(pprice.getProductPriceTypeId());
			}

			boolean hasError = false;

			// validate submitedamount
			BigDecimal bdprice = null;
			try {
				bdprice = CurrencyUtil.validateCurrency(this
						.getProductPriceAmount(), ctx.getCurrency());
				price.setProductPriceAmount(bdprice);

			} catch (Exception e) {
				if (this.getAction() == -1) {
					super.addFieldError("productPriceAmount",
							getText("error.message.price.format"));
				} else {
					MessageUtil.addMessage(getServletRequest(), LabelUtil
							.getInstance()
							.getText("error.message.price.format"));
				}
				hasError = true;
			}

			if (this.getAction() == -1) {// add
				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String priceName = (String) this.getPriceNames().get(
							langcount);

					if (StringUtils.isBlank(priceName)) {
						super
								.addFieldError(
										"priceName[" + langcount + "]",
										getText("error.message.storefront.contentpagetitlerequired"));
						hasError = true;
					}

					int submitedlangid = (Integer) reflanguages.get(langcount);
					// create
					ProductPriceDescriptionId id = new ProductPriceDescriptionId();
					id.setLanguageId(submitedlangid);
					id.setProductPriceId(price.getProductPriceId());

					ProductPriceDescription pdescription = new ProductPriceDescription();
					pdescription.setId(id);
					pdescription.setProductPriceName(priceName);

					Set descs = price.getPriceDescriptions();
					if (descs == null) {
						descs = new HashSet();
					}

					descs.add(pdescription);

					price.setPriceDescriptions(descs);

				}

			}

			if (hasError) {
				return INPUT;
			}

			price.setProductId(product.getProductId());
			cservice.saveOrUpdateProductPrice(price);

			if (price.isDefaultPrice()) {
				List updatePrices = new ArrayList();
				// set all other one to false
				if (prices != null) {
					Iterator pricesIterator = prices.iterator();
					while (pricesIterator.hasNext()) {
						ProductPrice pp = (ProductPrice) pricesIterator.next();
						if (pp.getProductPriceId() != price.getProductPriceId()) {
							pp.setDefaultPrice(false);
							updatePrices.add(pp);
						}
					}
				}
				if (updatePrices.size() > 0) {
					cservice.saveOrUpdateProductPrices(updatePrices);
				}
			}

			this.preparePriceDetails();

			super.setSuccessMessage();
		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);

			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	private void preparePriceDetails() throws Exception {
		
		super.setPageTitle("label.product.productprices.title");

		Context ctx = super.getContext();

		super.prepareLanguages();

		// get all modules
		CommonService commonService = (CommonService) ServiceFactory
				.getService(ServiceFactory.CommonService);
		pricesModules = commonService.getModules(
				super.getLocale().getCountry(),
				ProductConstants.PRICE_MODULE_TYPE);

		Map modulesMap = new HashMap();
		if (pricesModules != null) {
			Iterator i = pricesModules.iterator();
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				modulesMap.put(cms.getCoreModuleName(), cms.getDescription());
			}
		}

		super.getServletRequest().setAttribute("pricedescriptions", modulesMap);

		LocaleUtil
				.setLocaleToEntityCollection(pricesModules, super.getLocale());

		// get all ProductPrice for a Product
		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);
		product = cservice.getProduct(this.getProduct().getProductId());

		super.authorize(product);

		Set prices = product.getPrices();

		LocaleUtil.setLocaleToEntityCollection(prices, super.getLocale());

		super.getServletRequest().setAttribute("prices", prices);

		Set descriptionset = product.getDescriptions();
		int lang = LanguageUtil.getLanguageNumberCode(ctx.getLang());
		if (descriptionset != null) {
			Iterator i = descriptionset.iterator();
			while (i.hasNext()) {
				ProductDescription desc = (ProductDescription) i.next();
				if (desc.getId().getLanguageId() == lang) {
					productName = desc.getProductName();
					break;
				}
			}
		}

	}

	public String displayProductPrice() {
		
		super.setPageTitle("label.product.productprices.title");

		try {

			this.preparePriceDetails();
			return SUCCESS;

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return ERROR;
		}

	}

	public ProductPrice getPrice() {
		return price;
	}

	public void setPrice(ProductPrice price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Set<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(Set<ProductPrice> prices) {
		this.prices = prices;
	}

	public Collection<CoreModuleService> getPricesModules() {
		return pricesModules;
	}

	public void setPricesModules(Collection<CoreModuleService> pricesModules) {
		this.pricesModules = pricesModules;
	}

	public String getProductPriceAmount() {
		return productPriceAmount;
	}

	public void setProductPriceAmount(String productPriceAmount) {
		this.productPriceAmount = productPriceAmount;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<String> getPriceNames() {
		return priceNames;
	}

	public void setPriceNames(List<String> priceNames) {
		this.priceNames = priceNames;
	}

}

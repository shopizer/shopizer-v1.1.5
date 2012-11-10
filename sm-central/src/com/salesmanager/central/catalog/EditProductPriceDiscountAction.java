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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;

public class EditProductPriceDiscountAction extends BaseAction {

	private ProductPrice price;// parameter
	private Product product;// parameter
	private ProductPriceSpecial special;// used in the jsp
	private String productPrice;// display original price
	private Map days = new HashMap();

	private String sdate;
	private String edate;
	private String productName;

	private String day;// submited duration
	private String dstartdate;// submited
	private String denddate;// submited
	private String productNewPrice;// submited price

	private static Logger log = Logger
			.getLogger(EditProductPriceDiscountAction.class);

	public String displayProductPriceDiscount() {
		
		
		super.setPageTitle("label.product.productprices.discount");
		
		try {

			this.prepareProductPriceDiscountDetails();
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

	public String editProductPriceDiscount() {
		
		super.setPageTitle("label.product.productprices.discount");

		try {

			Context ctx = super.getContext();

			this.prepareProductPriceDiscountDetails();

			BigDecimal discountPrice;

			ProductPrice pp = this.getPrice();
			ProductPriceSpecial ppp;
			if (pp.getSpecial() != null) {
				ppp = pp.getSpecial();
			} else {
				ppp = new ProductPriceSpecial();
				ppp.setProductPriceId(pp.getProductPriceId());
			}

			// validate amount
			try {
				discountPrice = CurrencyUtil.validateCurrency(this
						.getProductNewPrice(), ctx.getCurrency());
				ppp.setProductPriceSpecialAmount(discountPrice);
			} catch (Exception e) {
				super.addFieldError("productPrice",
						getText("error.message.price.format"));
				return SUCCESS;
			}

			Date dt = new Date();
			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate = null;
			Date eDate = null;

			try {
				sDate = myDateFormat.parse(this.getDstartdate());
				eDate = myDateFormat.parse(this.getDenddate());
			} catch (Exception e) {
				log.error(e);
				sDate = new Date(dt.getTime());
				eDate = new Date(dt.getTime());
			}

			ppp.setProductPriceSpecialStartDate(sDate);
			ppp.setProductPriceSpecialEndDate(eDate);

			ppp.setProductPriceSpecialDurationDays(new Integer(this.getDay()));

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			cservice.saveOrUpdateProductPriceSpecial(ppp);

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return ERROR;
		}

		return SUCCESS;
	}

	public String deleteProductPriceDiscount() {
		
		super.setPageTitle("label.product.productprices.discount");

		try {

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			ProductPriceSpecial ppp = cservice.getProductPriceSpecial(this
					.getPrice().getProductPriceId());


			if (ppp != null) {
				cservice.deleteProductPriceSpecial(ppp);
			}

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return ERROR;
		}

		return SUCCESS;
	}

	private void prepareProductPriceDiscountDetails() throws Exception {
		
		super.setPageTitle("label.product.productprices.discount");

		Context ctx = super.getContext();

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		product = cservice.getProduct(this.getProduct().getProductId());

		super.authorize(product);

		// get price
		price = cservice.getProductPrice(this.getPrice().getProductPriceId());

		if (price == null || price.getProductId() != product.getProductId()) {
			throw new AuthorizationException("No price defined");
		}

		productPrice = CurrencyUtil.displayFormatedAmountNoCurrency(price
				.getProductPriceAmount(), ctx.getCurrency());

		// get discount
		special = price.getSpecial();

		if (special != null) {
			productNewPrice = CurrencyUtil.displayFormatedAmountNoCurrency(
					special.getProductPriceSpecialAmount(), ctx.getCurrency());
			sdate = DateUtil.formatDate(special
					.getProductPriceSpecialStartDate());
			edate = DateUtil
					.formatDate(special.getProductPriceSpecialEndDate());
			day = String.valueOf(special.getProductPriceSpecialDurationDays());
		}

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

		days = new HashMap();
		days.put("-1", LabelUtil.getInstance().getText(super.getLocale(),
				"label.product.productprices.discount.selectduration"));
		days.put("30", LabelUtil.getInstance().getText(super.getLocale(),
				"label.product.productprices.discount.30days"));
		days.put("60", LabelUtil.getInstance().getText(super.getLocale(),
				"label.product.productprices.discount.60days"));
		days.put("90", LabelUtil.getInstance().getText(super.getLocale(),
				"label.product.productprices.discount.90days"));
		days.put("120", LabelUtil.getInstance().getText(super.getLocale(),
				"label.product.productprices.discount.120days"));

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

	public ProductPriceSpecial getSpecial() {
		return special;
	}

	public void setSpecial(ProductPriceSpecial special) {
		this.special = special;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNewPrice() {
		return productNewPrice;
	}

	public void setProductNewPrice(String productNewPrice) {
		this.productNewPrice = productNewPrice;
	}

	public Map getDays() {
		return days;
	}

	public void setDays(Map days) {
		this.days = days;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDenddate() {
		return denddate;
	}

	public void setDenddate(String denddate) {
		this.denddate = denddate;
	}

	public String getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(String dstartdate) {
		this.dstartdate = dstartdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

}

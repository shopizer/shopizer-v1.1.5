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
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.Special;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;

public class EditDiscountAction extends BaseAction {

	private Product product;
	private Special special;

	private String productName;
	private String productPrice;
	private String productNewPrice;// also submited
	private String sdate;
	private String edate;

	private String dstartdate;// submited
	private String denddate;// submited

	private static Logger log = Logger.getLogger(EditDiscountAction.class);

	public String showDiscount() {

		try {
			
			super.setPageTitle("label.product.discount");

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			// get the product

			if (product == null || product.getProductId() == 0) {
				this.addActionError(getText("errors.technical"));
				return "unauthorized";
			}

			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			product = catalogservice.getProduct(product.getProductId());

			if (product == null) {
				this.addActionError(getText("errors.technical"));
				return "unauthorized";
			}

			productPrice = CurrencyUtil.displayFormatedAmountNoCurrency(product
					.getProductPrice(), ctx.getCurrency());

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

			// get a discount
			special = catalogservice.getSpecial(product.getProductId());

			if (special != null) {
				productNewPrice = CurrencyUtil.displayFormatedAmountNoCurrency(
						special.getSpecialNewProductPrice(), ctx.getCurrency());
				sdate = DateUtil.formatDate(special.getSpecialDateAvailable());
				edate = DateUtil.formatDate(special.getExpiresDate());
			}

		} catch (Exception e) {
			log.error(e);
			super.addActionError(getText("error.technical"));
		}

		return SUCCESS;
	}

	public String saveDiscount() {
		
		super.setPageTitle("label.product.discount");

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);

		try {
			String newPrice = this.getProductNewPrice();
			this.showDiscount();

			BigDecimal bdNewPrice = null;

			Date dt = new Date();

			try {
				bdNewPrice = CurrencyUtil.validateCurrency(newPrice, ctx
						.getCurrency());
				this.setProductNewPrice(newPrice);
			} catch (Exception e) {
				super.addFieldError("productPrice",
						getText("error.message.price.format"));
				return SUCCESS;
			}

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

			if (this.getSpecial() == null) {
				special = new Special();
				special.setStatus(1);
				special.setProductId(product.getProductId());
			} else {
				special.setSpecialLastModified(dt);
				special.setDateStatusChange(dt);

			}
			special.setSpecialNewProductPrice(bdNewPrice);
			special.setSpecialDateAvailable(sDate);
			special.setExpiresDate(eDate);

			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			catalogservice.saveOrUpdateSpecial(special);

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			super.addActionError(getText("error.technical"));
		}

		return SUCCESS;
	}

	public String deleteDiscount() {
		
		try {
			this.showDiscount();
			if (this.getSpecial() != null) {
				CatalogService catalogservice = (CatalogService) ServiceFactory
						.getService(ServiceFactory.CatalogService);
				catalogservice.deleteSpecial(special);
			}
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			super.addActionError(getText("error.technical"));
		}

		return SUCCESS;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

}

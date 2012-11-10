package com.salesmanager.core.util.www.tags;

import java.math.BigDecimal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.ProductUtil;

public class ProductPriceSpecialTag extends TagSupport {
	private Product product;
	private boolean displayCurrency;
	
	private Logger log = Logger.getLogger(ProductPriceSpecialTag.class);
	
	
	public int doStartTag() throws JspException {
		try {



			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			HttpSession session = request.getSession();
			
			Locale locale = (Locale) request.getAttribute("LOCALE");
			MerchantStore store = (MerchantStore)session.getAttribute("STORE");
			
			String pprice = "";

			if(ProductUtil.hasDiscount(this.getProduct())) {
				
				BigDecimal price = ProductUtil.determinePrice(product, locale, store.getCurrency());
				if(this.displayCurrency) {
					pprice = CurrencyUtil.displayFormatedAmountWithCurrency(price, store.getCurrency());
				} else {
					pprice = CurrencyUtil.displayFormatedAmountNoCurrency(price, store.getCurrency());
				}
			}

			pageContext.getOut().print(pprice);


			
		} catch (Exception ex) {
			log.error(ex);
		}
		return SKIP_BODY;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isDisplayCurrency() {
		return displayCurrency;
	}

	public void setDisplayCurrency(boolean displayCurrency) {
		this.displayCurrency = displayCurrency;
	}
}

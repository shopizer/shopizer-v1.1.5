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
package com.salesmanager.central.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductRelationship;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.www.SessionUtil;


/**
 * Used with AJAX / DWR requests
 * @author Carl Samson
 *
 */
public class AddProduct {

	private Logger log = Logger.getLogger(AddProduct.class);
	
	
	public String removeRelationshipItem(String productId,String relatedProductId, String relationShipType) {
		
		
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);

		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		
		Locale locale = LocaleUtil.getLocale(req);
		
		try {
			
			long lProductId = Long.parseLong(productId);
			int iRelationShipType = Integer.parseInt(relationShipType);
			long lRelatedProductId = Long.parseLong(relatedProductId);
			ProductRelationship pr = cservice.getProductRelationship(lProductId,lRelatedProductId,iRelationShipType,ctx.getMerchantid());
			
			if(pr==null) {
				log.debug("Error removing relation : Relationship type " + iRelationShipType + " for product id " + lProductId + " and related productId " + lRelatedProductId);
				return LabelUtil.getInstance().getText(locale,"error.message.invalidrelationship.remove");
			} else {
				cservice.removeProductRelationship(pr);
			}
			
		} catch (Exception e) {
			log.error(e);
			return LabelUtil.getInstance().getText(locale,"error.message.invalidrelationship.remove");
		}
		
		return "";
		
	}

	public String addRelationshipItem(String productId,String relatedProductId,String relationShipType) {
		
		
		
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);

		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		
		Locale locale = LocaleUtil.getLocale(req);
		
		try {
			long lProductId = Long.parseLong(productId);
			long lRelatedProductId = Long.parseLong(relatedProductId);
			int iRelationShipType = Integer.parseInt(relationShipType);
			

			Product p = cservice.getProduct(lProductId);
			
			if(p.getMerchantId()==ctx.getMerchantid()) {
				
				
				//check if the relationship exist
				ProductRelationship prExist = cservice.getProductRelationship(lProductId,lRelatedProductId,iRelationShipType,ctx.getMerchantid());
				if(prExist!=null) {
					return LabelUtil.getInstance().getText(locale,"error.message.invalidrelationship.exist");
				}
				
				ProductRelationship pr = new ProductRelationship();
				pr.setMerchantId(ctx.getMerchantid());
				pr.setProductId(p.getProductId());
				pr.setRelatedProductId(lRelatedProductId);
				pr.setRelationshipType(iRelationShipType);
				cservice.saveOrUpdateProductRelationship(pr);
				
				
			}
			
			return "";
			
		} catch (Exception e) {
			log.error(e);
			return LabelUtil.getInstance().getText(locale,"error.message.invalidrelationship");
		}
		
	}
	
	public Product[] getProductsHtmlListByCategoryId(String categoryId) {
		
		
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);


		Product[] returnArray = null;
		
		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);

		Locale locale = LocaleUtil.getLocale(req);
		try {
			long lCategoryId = Long.parseLong(categoryId);
			Collection products = cservice.getProductsByMerchantIdAndCategoryIdAndLanguageId(ctx.getMerchantid(),lCategoryId,LanguageUtil.getLanguageNumberCode(ctx.getLang()));
			if(products!=null && products.size()>0) {
				
				returnArray = new Product[products.size()];
				
				Iterator i = products.iterator();
				
				int count = 0;
				
				while(i.hasNext()) {
					StringBuffer productLine = new StringBuffer();
					com.salesmanager.core.entity.catalog.Product d = (com.salesmanager.core.entity.catalog.Product)i.next();
					
					ProductDescription desc = d.getProductDescription();
					

					if(desc == null) {
						desc = new ProductDescription();
						desc.setProductName(String.valueOf(d.getProductId()));
					}
					
					

					productLine.append("<a href=\"#\" rel=\"").append(desc.getProductName()).append("\">").append(desc.getProductName()).append("</a>");
					d.setName(productLine.toString());
						
					returnArray[count] = d;
					count++;
					
				}
			}
		
		} catch(Exception e) {
			log.error(e);
		}
			return returnArray;
		
	}

	/**
	 * Add OrderAttributes to an existing OrderProduct
	 * @param attributes
	 * @param productId
	 * @param lineId
	 * @return
	 */
	public OrderProduct addAttributes(OrderProductAttribute attributes[],long productId, int lineId) {


		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		HttpSession session = WebContextFactory.get().getSession();

		Context ctx = (Context)session.getAttribute(ProfileConstants.context);

		Locale locale = LocaleUtil.getLocale(req);

		try {



			List attrList = Arrays.asList(attributes);


			OrderProduct op = com.salesmanager.core.util.CheckoutUtil.addAttributesFromRawObjects(attrList, productId, String.valueOf(lineId), ctx.getCurrency(), req);


			return op;

		} catch (Exception e) {
			log.error(e);
			OrderProduct op = new OrderProduct();
			op.setErrorMessage(LabelUtil.getInstance().getText(locale,"messages.genericmessage"));
			return op;

		}

	}




	/**
	 * Synchronize Session objects with passed parameters
	 * Validates input parameters
	 * Then delegates to OrderService for OrderTotalSummary calculation
	 * @param products
	 */
	public OrderTotalSummary calculate(OrderProduct[] products, ShippingInformation shippingMethodLine) {

		//subtotal
		//quantity
		//tax
		//shipping
		//handling
		//other prices


		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();

		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);

		Order order = SessionUtil.getOrder(req);
		
		String currency = null;
		
		try {
			
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(ctx.getMerchantid());
			
			currency = store.getCurrency();
			

			if(order!=null && !StringUtils.isBlank(order.getCurrency())) {
				currency = order.getCurrency();
			}
			
			
			
			
		} catch (Exception e) {
			log.error(e);
		}
		
		OrderTotalSummary total = new OrderTotalSummary(currency);
		
		


		Customer customer = SessionUtil.getCustomer(req);
		
		Locale locale = LocaleUtil.getLocale(req);

		//Shipping
		ShippingInformation shippingInfo = SessionUtil.getShippingInformation(req);

		Shipping shipping = null;

		if(shippingInfo==null) {
			shippingInfo = new ShippingInformation();
		}

		if(shippingMethodLine != null && shippingMethodLine.getShippingMethodId()==null) {//reset shipping
			//shippingMethodLine = new ShippingInformation();

			if(req.getSession().getAttribute("PRODUCTLOADED")!=null) {
				shipping = new Shipping();
				shipping.setHandlingCost(shippingInfo.getHandlingCost());
				shipping.setShippingCost(shippingInfo.getShippingCost());
				shipping.setShippingDescription(shippingInfo.getShippingMethod());
				shipping.setShippingModule(shippingInfo.getShippingModule());
				req.getSession().removeAttribute("PRODUCTLOADED");

			} else {

				shippingInfo.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal("0"), ctx.getCurrency()));
				shippingInfo.setShippingMethodId(null);
				shippingInfo.setShippingMethod(null);
				shippingInfo.setShippingCost(new BigDecimal("0"));
				try {
					SessionUtil.removeShippingInformation(req);
				} catch (Exception e) {
					log.error(e);
				}



			}


		} else { //retreive shipping info in http session
			shipping = new Shipping();
			Map shippingOptionsMap = SessionUtil.getShippingMethods(req);
			String method = shippingMethodLine.getShippingMethodId();


			if(shippingInfo.getShippingCost()!=null && shippingInfo.getShippingMethod()!=null) {

				shipping.setHandlingCost(shippingInfo.getHandlingCost());
				shipping.setShippingCost(shippingInfo.getShippingCost());
				shipping.setShippingDescription(shippingInfo.getShippingMethod());
				shipping.setShippingModule(shippingInfo.getShippingModule());

			} else {

				if(shippingOptionsMap==null || method==null) {
					shippingMethodLine.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal("0"), ctx.getCurrency()));
					shippingInfo = shippingMethodLine;
				} else {//after a selection
					//retreive shipping option
					ShippingOption option = (ShippingOption)shippingOptionsMap.get(method);

					//get the latest shipping information (handling, free ...)

					shippingInfo.setShippingMethodId(option.getOptionId());
					shippingInfo.setShippingOptionSelected(option);
					shippingInfo.setShippingMethod(option.getDescription());

					shippingInfo.setShippingCost(option.getOptionPrice());
					shippingInfo.setShippingModule(option.getModule());

					shipping.setHandlingCost(shippingInfo.getHandlingCost());
					shipping.setShippingCost(shippingInfo.getShippingCost());
					shipping.setShippingDescription(option.getDescription());
					shipping.setShippingModule(option.getModule());

					//total.setShipping(true);



				}

			}
		}



		List productList = new ArrayList();

		try {

			//validate numeric quantity

			//validate numeric price
			if(products!=null) {

				//get products from httpsession
				Map savedOrderProducts = SessionUtil.getOrderProducts(req);
				Map currentProducts = new HashMap();



				if(savedOrderProducts==null) {
					savedOrderProducts = SessionUtil.createSavedOrderProducts(req);
				}




				total.setOrderProducts(products);

				if(order==null) {
					log.error("No order exist for the price calculation");
					total.setErrorMessage(LabelUtil.getInstance().getText(locale,"messages.genericmessage"));
					return total;
				}

				//validates amounts
				BigDecimal oneTimeSubTotal = total.getOneTimeSubTotal();

				for(int i=0;i<products.length;i++) {
					OrderProduct product = products[i];

					currentProducts.put(String.valueOf(product.getLineId()), product);

					//get the original line
					OrderProduct oproduct = (OrderProduct)savedOrderProducts.get(String.valueOf(product.getLineId()));

					if(oproduct==null) {
						oproduct=this.createOrderProduct(product.getProductId());
					}
					
					if(product.getProductQuantity()>oproduct.getProductQuantityOrderMax()) {
						product.setProductQuantity(oproduct.getProductQuantityOrderMax());
					}

					productList.add(oproduct);

					//check that productid match
					if(product.getProductId()!=oproduct.getProductId()) {//set an error message
						oproduct.setErrorMessage(LabelUtil.getInstance().getText(locale,"messages.invoice.product.invalid"));
						oproduct.setPriceText("0");
						oproduct.setProductPrice(new BigDecimal(0));
						oproduct.setPriceFormated(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal(0), ctx.getCurrency()));
						continue;
					}


					//validate and set the final price
					try {
						product.setPriceErrorMessage(null);//reset any error message
						product.setErrorMessage(null);
						//set price submited
						BigDecimal price = CurrencyUtil.validateCurrency(product.getPriceText(), ctx.getCurrency());
						oproduct.setPriceText(product.getPriceText());
						oproduct.setProductPrice(price);
						oproduct.setPriceFormated(CurrencyUtil.displayFormatedAmountWithCurrency(price, ctx.getCurrency()));
						oproduct.setProductQuantity(product.getProductQuantity());
						oproduct.setPriceErrorMessage(null);
						oproduct.setErrorMessage(null);


						double finalPrice = price.doubleValue() * product.getProductQuantity();
						BigDecimal bdFinalPrice = new BigDecimal(finalPrice);

						//price calculated
						oproduct.setCostText(CurrencyUtil.displayFormatedAmountWithCurrency(bdFinalPrice, ctx.getCurrency()));
						oproduct.setFinalPrice(bdFinalPrice);


					} catch (NumberFormatException nfe) {
						oproduct.setPriceErrorMessage(LabelUtil.getInstance().getText(locale,"messages.price.invalid"));
						oproduct.setPriceText("0");
						oproduct.setProductPrice(new BigDecimal(0));
						oproduct.setCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal(0), ctx.getCurrency()));
						oproduct.setPriceFormated(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal(0), ctx.getCurrency()));
						//set shipping to 0
						ShippingInformation info = new ShippingInformation();
						shippingMethodLine.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal("0"), ctx.getCurrency()));
						total.setShippingLine(info);
						total.setShippingTotal(new BigDecimal("0"));

					} catch(com.opensymphony.xwork2.validator.ValidationException e) {
						oproduct.setPriceErrorMessage(LabelUtil.getInstance().getText(locale,"messages.price.invalid"));
						oproduct.setPriceText("0");
						oproduct.setProductPrice(new BigDecimal(0));
						oproduct.setCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal(0), ctx.getCurrency()));
						oproduct.setPriceFormated(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal(0), ctx.getCurrency()));
						//set shipping to 0
						ShippingInformation info = new ShippingInformation();
						shippingMethodLine.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal("0"), ctx.getCurrency()));
						total.setShippingLine(info);
						total.setShippingTotal(new BigDecimal("0"));

					} catch(Exception e) {
						log.error(e);
					}




				}


				List removable = null;
				//cleanup http session
				Iterator it = savedOrderProducts.keySet().iterator();
				while(it.hasNext()) {
					String key = (String)it.next();
					if(!currentProducts.containsKey(key)) {
						if(removable==null) {
							removable = new ArrayList();
						}
						removable.add(key);
					}
				}

				if(removable!=null) {
					Iterator removIt = removable.iterator();
					while(removIt.hasNext()) {
						String key = (String)removIt.next();
						SessionUtil.removeOrderTotalLine(key, req);
					}
				}

				OrderService oservice = (OrderService)ServiceFactory.getService(ServiceFactory.OrderService);
				total = oservice.calculateTotal(order, productList, customer,shipping,ctx.getCurrency(), LocaleUtil.getLocale(req));


				OrderProduct[] opArray = new OrderProduct[productList.size()];
				OrderProduct[] o = (OrderProduct[])productList.toArray(opArray);
				total.setOrderProducts(o);

				total.setShippingLine(shippingInfo);


				Order savedOrder = SessionUtil.getOrder(req);
				savedOrder.setTotal(total.getTotal());
				savedOrder.setOrderTax(total.getTaxTotal());
				SessionUtil.setOrder(savedOrder,req);

			}


		} catch (Exception e) {
			log.error(e);
			total = new OrderTotalSummary(currency);
			total.setErrorMessage(LabelUtil.getInstance().getText(locale,"messages.genericmessage"));
		}

		ShippingInformation shippingLine = total.getShippingLine();
		if(shippingLine!=null) {
			shippingLine.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(shippingLine.getShippingCost(), ctx.getCurrency()));
		} else {
			shippingLine = new ShippingInformation();
			shippingLine.setShippingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(new BigDecimal("0"), ctx.getCurrency()));
		}

		if(shippingLine.getHandlingCost()!=null) {
			shippingLine.setHandlingCostText(CurrencyUtil.displayFormatedAmountWithCurrency(shippingMethodLine.getHandlingCost(), ctx.getCurrency()));
		}

		if(total.getShippingTotal()!=null) {
			total.setShippingTotalText(CurrencyUtil.displayFormatedAmountWithCurrency(total.getShippingTotal(), ctx.getCurrency()));
		}


		if(total.getOneTimeSubTotal()!=null){
			total.setOneTimeSubTotalText(CurrencyUtil.displayFormatedAmountWithCurrency(total.getOneTimeSubTotal(), ctx.getCurrency()));
		}

		if(total.getRecursiveSubTotal()!=null){
			total.setRecursiveSubTotalText(CurrencyUtil.displayFormatedAmountWithCurrency(total.getRecursiveSubTotal(), ctx.getCurrency()));
		}


		if(total.getTotal()!=null) {
			total.setTotalText(CurrencyUtil.displayFormatedAmountWithCurrency(total.getTotal(), ctx.getCurrency()));
		}
		return total;

	}

	public OrderProduct removeAttributes(long productId, int lineId) {

		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		HttpSession session = WebContextFactory.get().getSession();

		OrderProduct op;




		try {


			//revert back to original product
			op = this.createOrderProduct(productId);
			op.setLineId(lineId);
			return SessionUtil.resetProduct(op,productId,String.valueOf(lineId),req);

		} catch (Exception e) {
			log.error(e);
			op = new OrderProduct();
			op.setErrorMessage(LabelUtil.getInstance().getText(LocaleUtil.getLocale(req),"error.cart.noproduct"));
			return op;
		}






	}



	public void removeProduct(int lineId) {


		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();

		HttpSession session = WebContextFactory.get().getSession();

		try {

			SessionUtil.removeOrderTotalLine(String.valueOf(lineId), req);

		} catch (Exception e) {
			log.error(e);
		}

	}

	/**
	 * Validates prices inputed by the end user (for invoice)
	 * @param selections
	 * @return
	 */
	public OrderProduct[] validatePrices(OrderProduct[] selections) {


		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);

		try {


			if(selections!=null) {

				for(int i=0;i<selections.length;i++) {
					OrderProduct product = selections[i];
					try {
						CurrencyUtil.validateCurrency(product.getPriceText(), ctx.getCurrency());
					} catch (Exception e) {
						product.setPriceErrorMessage(LabelUtil.getInstance().getText(LocaleUtil.getLocale(req),"messages.price.invalid"));
					}
				}

			}


		} catch (Exception e) {
			log.error(e);
		}

		return selections;



	}


	/**
	 * Creates a ShoppingCart line
	 * @param orderId
	 * @param productId
	 * @param lineId
	 * @return
	 */
	public OrderProduct addProduct(long productId,int lineId) {



		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();




		HttpSession session = WebContextFactory.get().getSession();
		try {


				Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);


				OrderProduct op = this.createOrderProduct(productId);


				op.setLineId(lineId);

				SessionUtil.addOrderTotalLine(op, String.valueOf(lineId),req);

				return op;






		} catch (Exception e) {
			log.error(e);
			OrderProduct scp = new OrderProduct();
			scp.setErrorMessage(LabelUtil.getInstance().getText(LocaleUtil.getLocale(req),"error.cart.addproducterror"));
		}

		OrderProduct scp = new OrderProduct();
		scp.setErrorMessage(LabelUtil.getInstance().getText(LocaleUtil.getLocale(req),"error.cart.noproduct"));

		return scp;

	}

	public OrderProduct createOrderProduct(long productId) throws Exception {


		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);

		//Locale locale = req.getLocale();

		
		Locale locale = LocaleUtil.getLocale(req);

		
		

		return com.salesmanager.core.util.CheckoutUtil.createOrderProduct(productId,locale,ctx.getCurrency());




	}

	public OrderProduct[] getProductsByCategoryId(String categoryId) {

		try {

			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();


			Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);



			CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);

			Collection coll = cservice.getProductsByMerchantIdAndCategoryIdAndLanguageId(ctx.getMerchantid(),Long.parseLong(categoryId),LanguageUtil.getLanguageNumberCode(ctx.getLang()));



			if(coll!=null && coll.size()>0) {

				OrderProduct[] scparray = new OrderProduct[coll.size()];
				Iterator i = coll.iterator();
				int count = 0;
				while(i.hasNext()) {
					Product d = (Product)i.next();
					ProductDescription pd = d.getProductDescription();
					OrderProduct scp = new OrderProduct();
					scp.setProductId(d.getProductId());
					scp.setProductName(pd.getProductName());
					scp.setProductDescription(pd.getProductDescription());
					scp.setProductImage(d.getProductImage());
					scparray[count]=scp;
					count++;
				}

				return scparray;
			} else {
				log.warn("No products belong to categoryId " + categoryId);
			}






		} catch (Exception e) {
			log.error(e);
		}

		return null;

	}

}

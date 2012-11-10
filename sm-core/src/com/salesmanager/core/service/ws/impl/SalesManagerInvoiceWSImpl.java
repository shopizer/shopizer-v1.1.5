/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Nov 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.ws.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ws.Attribute;
import com.salesmanager.core.entity.orders.ws.CreateInvoiceWebServiceResponse;
import com.salesmanager.core.entity.orders.ws.Invoice;
import com.salesmanager.core.entity.orders.ws.Product;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingMethod;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.service.ws.SalesManagerInvoiceWS;
import com.salesmanager.core.service.ws.WebServiceCredentials;
import com.salesmanager.core.service.ws.utils.WebServiceUtils;
import com.salesmanager.core.util.CheckoutUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.OrderUtil;
import com.salesmanager.core.util.SpringUtil;

/**
 *  SalesManagerInvoiceWSImpl is a JAX-WS webservice implementation
 *  to create an invoice of products.
 * @author Anil.Talla
 */
@WebService
public class SalesManagerInvoiceWSImpl implements SalesManagerInvoiceWS{

	private Logger log = Logger.getLogger(SalesManagerInvoiceWSImpl.class);
	
	/**
	 * createInvoice is the WebMethod to create an invoice, this
	 * method is accessed by the webservice client supplying the credentails and invoice
	 * as arguments.
	 * @param WebServiceCredentials credentials
	 * @param Invoice invoice
	 * @return CreateInvoiceWebServiceResponse
	 */
	@WebMethod
	public  @WebResult CreateInvoiceWebServiceResponse createInvoice(@WebParam(name="credentials")WebServiceCredentials credentials,@WebParam(name="invoice") Invoice invoice) {
		MessageSource messageSource = (MessageSource)SpringUtil.getBean("messageSource");
		Locale locale = LocaleUtil.getLocale(invoice.getLanguage());
		
		CreateInvoiceWebServiceResponse response = new CreateInvoiceWebServiceResponse();
		try {
			//Validate if the user is authorized to do a create invoice or not.
			WebServiceUtils.validateCredentials(locale, credentials, log);
			
			//If Business validations fail then return.
			if(!isValidInvoice(credentials.getMerchantId(), invoice, messageSource, locale, response)){
				return response;
			}
			//Process invoice
			response = processNewInvoice(credentials.getMerchantId(), invoice, messageSource, locale, response);
			//set success status and success message.
			response.setStatus(1);

			
		} catch (Exception e) {
			if(e instanceof ServiceException) {
				//If instanceof ServiceException then there
				//is a user readable message set so return this message with response.
				String msg[] = {((ServiceException)e).getMessage()};
				response.setMessages(msg);
				response.setStatus(0);
			} else {
			
				log.error("Exception occurred while creating Invoice",e);
				response.setMessages(new String[]{messageSource.getMessage("errors.technical", 
						null, locale)});
				response.setStatus(0);
			}
		}
		return response;
	}
	
	/**
	 * processNewInvoice method processes the create invoice logic
	 * and sets the newly created invoice-id and OrderTotal to response.
	 * @param invoice {@link Invoice}
	 * @param messageSource {@link MessageSource}
	 * @param locale {@link Locale}
	 * @param response {@link CreateInvoiceWebServiceResponse}
	 * @throws Exception 
	 */
	private CreateInvoiceWebServiceResponse processNewInvoice(int merchantId, Invoice invoice,MessageSource messageSource,Locale locale,CreateInvoiceWebServiceResponse response)
					throws Exception{
		//Generate an id
		SystemService sservice = (SystemService) ServiceFactory.getService(ServiceFactory.SystemService);
		long nextOrderId = sservice.getNextOrderIdSequence();
		
		Order order = new Order();
		order.setChannel(OrderConstants.INVOICE_CHANNEL);
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		order.setDatePurchased(format.parse(invoice.getDate()));
		order.setOrderDateFinished(format.parse(invoice.getDueDate()));
		order.setOrderId(nextOrderId);
		order.setMerchantId(merchantId);
		order.setCurrency(invoice.getCurrency());
		
		
		//create customer info
		CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
		Customer customer = cservice.getCustomer(invoice.getCustomerId());
				
		
		order.setCustomerName(customer.getCustomerFirstname() + " " + customer.getCustomerLastname());
		order.setCustomerStreetAddress(customer.getCustomerStreetAddress());
		order.setCustomerCity(customer.getCustomerBillingCity());
		order.setCustomerPostcode(customer.getCustomerPostalCode());
		Map countries = RefCache.getAllcountriesmap(1);//default language 
		Country country = (Country)countries.get(customer.getCustomerCountryId());
		order.setCustomerCountry(country.getCountryName());

		Map zones = RefCache.getAllZonesmap(LanguageUtil.getLanguageNumberCode(invoice.getLanguage()));
		Zone zone = (Zone)zones.get(customer.getCustomerZoneId());
		order.setCustomerTelephone(customer.getCustomerTelephone());
		if(zone !=null){
			order.setCustomerState(zone.getZoneName());
		}
		
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(order.getMerchantId()); 
		//create products info
		List<OrderProduct> products = new ArrayList<OrderProduct>(); 
		//for each product
		if(invoice.getProducts() != null){
			for(Product product : invoice.getProducts()){
				OrderProduct orderProduct = com.salesmanager.core.util.CheckoutUtil.createOrderProduct(product.getProductId(),
						locale,order.getCurrency());
				orderProduct.setProductQuantity(product.getQuantity());
				if(product.isOverWritePrice()) {
					orderProduct.setProductPrice(new BigDecimal(product.getPrice()));
				}
				
				List<OrderProductAttribute> orderProductAttrbutes = new ArrayList<OrderProductAttribute>();
				if(product.getAttributes() != null){
					for(Attribute attribute:product.getAttributes()){
						OrderProductAttribute opa = new OrderProductAttribute();
						opa.setProductOptionValueId(attribute.getAttributeId());
						opa.setPrice(String.valueOf(attribute.getPrice()));
						orderProductAttrbutes.add(opa);
					}
				}
				CheckoutUtil.addAttributesToProduct(orderProductAttrbutes, orderProduct, order.getCurrency(), locale);
				products.add(orderProduct);
			}			
		}
		
		OrderService oservice = (OrderService)ServiceFactory.getService(ServiceFactory.OrderService);
		
		Shipping shipping = null;
		
		if(invoice.isCalculateShipping()) {
			
			boolean hasShipping = false;
			for (Object o: products) {
				OrderProduct op = (OrderProduct)o;
				if (op.isShipping()) {
					hasShipping = true;
					break;
				}
			}
			
			ShippingService shippingService = (ShippingService)ServiceFactory.getService(ServiceFactory.ShippingService);
			ShippingInformation shippingInformation = shippingService.getShippingQuote(products, customer, merchantId, locale, invoice.getCurrency());
			
			ShippingOption retainedOption = null;
			
			Collection methods = shippingInformation.getShippingMethods();
			//take less expensive
			for(Object o: methods) {
				ShippingMethod method = (ShippingMethod)o;//shipping carrier
				Collection options = method.getOptions();
				for(Object opt:options) {
					ShippingOption option = (ShippingOption)opt;
					BigDecimal price = option.getOptionPrice();
					if(retainedOption==null) {
						retainedOption = option;
					} else {
						if(price.floatValue() < retainedOption.getOptionPrice().floatValue()) {
							retainedOption = option;
						}
					}
				}
			}
			
			if(retainedOption!=null) {
			
				shipping = new Shipping();
				shipping.setHandlingCost(shippingInformation.getHandlingCost());
				shipping.setShippingCost(retainedOption.getOptionPrice());
				shipping.setShippingModule(retainedOption.getModule());
				shipping.setShippingDescription(retainedOption.getDescription());
			}
		}
		
		
		OrderTotalSummary summary = oservice.calculateTotal(order, products, customer, shipping, order.getCurrency(), locale); 
		Map totals = OrderUtil.getOrderTotals(order.getOrderId(),summary,order.getCurrency(), locale);
		HashSet s =	new HashSet(totals.entrySet()); 
		order.setOrderProducts(new HashSet(products));
		order.setOrderTotal(s);
		order.setTotal(summary.getTotal());
		order.setOrderTax(summary.getTaxTotal()); 
		//save invoice 
		oservice.saveInvoice(merchantId, order.getOrderId(), order.getDatePurchased(), order.getOrderDateFinished(), 
										"", false, products, customer, shipping, store, locale);
		
		
		response.setInvoiceId(order.getOrderId());
		
		//Return order totals in response.
		Order createdOrder = oservice.getOrder(order.getOrderId());
		List<com.salesmanager.core.entity.orders.ws.OrderTotal> orderTotalList = new ArrayList<com.salesmanager.core.entity.orders.ws.OrderTotal>();
		for(OrderTotal orderTotal:createdOrder.getOrderTotal()){
			com.salesmanager.core.entity.orders.ws.OrderTotal responseOrderTotal = new com.salesmanager.core.entity.orders.ws.OrderTotal();
			BeanUtils.copyProperties(responseOrderTotal, orderTotal);
			orderTotalList.add(responseOrderTotal);
		}
		
		response.setOrderTotals(orderTotalList.toArray(new com.salesmanager.core.entity.orders.ws.OrderTotal[]{}));
		return response;
	}
	private boolean isValidInvoice(int merchantId, Invoice invoice,MessageSource messageSource,Locale locale,CreateInvoiceWebServiceResponse response)
			throws Exception{
		
		if(invoice.getProducts() == null ||
				invoice.getProducts().length == 0){
			WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.invoice.products.required",null,0);
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		
		//Format Date
		try {
			if(StringUtils.isBlank(invoice.getDate())){
				//If Blank set current date.
				invoice.setDate(format.format(new Date()));
			}else{
				if(format.parse(invoice.getDate()) == null){
					//Invalid date format
					WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.date.invalid",new Object[]{Constants.DATE_FORMAT},0);
					return false;
				}
			}
		} catch (ParseException e) {
			//Invalid date format
			WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.date.invalid",new Object[]{Constants.DATE_FORMAT},0);
			return false;			
		}
		
		if(StringUtils.isBlank(invoice.getDueDate())){
			//If Blank set current date.
			invoice.setDueDate(format.format(new Date()));
		}else{
			if(format.parse(invoice.getDueDate()) == null){
				//Invalid date format
				WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.date.invalid",new Object[]{Constants.DATE_FORMAT},0);
				return false;
			}
		}
		
		CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
		com.salesmanager.core.entity.customer.Customer entityCustomer  = cservice.getCustomer(invoice.getCustomerId());
		//If Customer does not exist.
		if(entityCustomer == null){
			WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.customer.doesnotexist",null,0);
			return false;
		}
		
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		//If Merchant does not exist
		MerchantStore store = mservice.getMerchantStore(merchantId);
		if(store == null){
			WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.merchant.doesnotexist",null,0);
			return false;
		}
		
		if(entityCustomer.getMerchantId() != merchantId){
			WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.merchant.customer.mismatch",null,0);
			return false;
		}
		
		//ProductId exists and belongs to merchant.
		
		CatalogService catalogService = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		if(invoice.getProducts() != null){
			for(Product webProduct:invoice.getProducts()){
				com.salesmanager.core.entity.catalog.Product product = catalogService.getProduct(webProduct.getProductId());
				if(product == null){
					WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.product.doesnotexist",new Object[]{webProduct.getProductId()},0);
					return false;
				}
				
				//Attribute exists and belongs to product.
				if(webProduct.getAttributes() != null){
					for(Attribute webAttribute:webProduct.getAttributes()){
						com.salesmanager.core.entity.catalog.ProductAttribute prodAttribute = 
											catalogService.getProductAttribute(webAttribute.getAttributeId());
						if(prodAttribute == null){
							WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.productattribute.doesnotexist",new Object[]{webAttribute.getAttributeId()},0);
							return false;
						}
						if(prodAttribute.getProductAttributeId() != webAttribute.getAttributeId()){
							WebServiceUtils.setStatusMsg(messageSource, locale, response,"messages.product.productattribute.mismatch",
									new Object[]{webAttribute.getAttributeId(),webProduct.getProductId()},0);
							return false;
						}
					}
				}
			}			
		}
		
		return true;
	}

}

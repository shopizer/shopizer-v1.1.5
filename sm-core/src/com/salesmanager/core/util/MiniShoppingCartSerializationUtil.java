/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Mar 7, 2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



import org.apache.commons.lang.xwork.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.entity.orders.ShoppingCartProduct;
import com.salesmanager.core.entity.orders.ShoppingCartProductAttribute;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.entity.system.FieldOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;

/**
 * Responsible for serializing the mini shopping cart in JSON format so it can
 * be saved in the cookie' Also contains a de serialization to rebuild a
 * ShoppingCart from a JSON String
 * 
 * @author Carl Samson
 * 
 */
public class MiniShoppingCartSerializationUtil {

	/**
	 * Deserialize a Shopping cart string from the cookie
	 * {"ps":[{"p":{"pid","<productid>","qty":"<quantity>","as":[{"a":"<attributeid>"},{"a":"<attributeid>"}]}},{"p":...}]
	 * 
	 * @param json
	 * @return com.salesmanager.core.entity.orders.ShoppingCart
	 * @throws Exception
	 */
	public static ShoppingCart deserializeJSON(String json, MerchantStore store, Locale locale) throws Exception {

		
		if(StringUtils.isBlank(json)) {
			return null;
		}
		ShoppingCart cart = null;
		Map<String, String> data = new ObjectMapper().readValue(json, HashMap.class);
		
		Map<Long,ShoppingCartProduct> productsMap = null;
		List productsList = null;
		
		if(data!=null) {
			
			//Collection products = new ArrayList();
			productsMap = new HashMap();
			productsList = new ArrayList(); 
			
			for(Object o: data.keySet()) {

				if(o instanceof String && ((String)o).equals("ps")) {
					// can parse
					
					Object oo = data.get(o);
					if(oo instanceof List) {//List 
						
						for(Object ooo:(List)oo) {

							if(ooo instanceof LinkedHashMap) {
								
								Map m = (Map)ooo;
								
								//get each products
								Map field = (Map)m.get("p");

								String productId = (String)field.get("pid");
								String qty = (String)field.get("q");
								
								long pId = Long.parseLong(productId);
								ShoppingCartProduct scp = new ShoppingCartProduct();
								scp.setProductId(pId);
								scp.setQuantity(Integer.parseInt(qty));
								productsMap.put(pId, scp);
								productsList.add(scp);
								
								List attrList = (List)field.get("as");
								
								if(attrList!=null) {
									List attributesList  = new ArrayList();
									for(Object oooo:attrList) {

										Map values = (Map)oooo;
										String attrId = (String)values.get("a");
										ShoppingCartProductAttribute attribute = new ShoppingCartProductAttribute();
										attribute.setAttributeId(Long.parseLong(attrId));
										attributesList.add(attribute);
										
									}
									scp.setAttributes(attributesList);
								}
							}
						}
					}
				}
			}
		}
		
		//if shoppingcart != null
		//get all products, query the catalog and re-create a new ShoppingCart
		//using values from the database
		
		if(productsList!=null) {
			List productsIds = new ArrayList();
			List attributesIds = new ArrayList();
			//Map productAttributes = new HashMap();
				//int i = 0;
				for(Object o: productsList) {

					ShoppingCartProduct p = (ShoppingCartProduct)o;
					//p.setInternalId(i);

					productsIds.add(p.getProductId());
					List attrs = p.getAttributes();
					if(attrs!=null) {
						List attrsPerProduct = new ArrayList();
						for(Object oo: attrs) {
							ShoppingCartProductAttribute attr = (ShoppingCartProductAttribute)oo;
							attributesIds.add(attr.getAttributeId());//for doing the query
							attrsPerProduct.add(attr.getAttributeId());
						}
						//productAttributes.put(p.getInternalId(), attrsPerProduct);
					}
					//i++;
					
				}
				
				CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
				Collection productCollection = cservice.getProducts(productsIds);
				Collection attributes = null;
				
				Map<Long,Product> pMap = null;
				if(productCollection!=null && productCollection.size()>0) {
					pMap = new HashMap();
					for(Object o : productCollection) {
						Product p = (Product)o;
						pMap.put(p.getProductId(), p);
					}
				}
				
				
				Map<Long,ProductAttribute> productAttributesMap = null;
				if(attributesIds!=null && attributesIds.size()>0) {
					attributes = cservice.getProductAttributes(attributesIds, locale.getLanguage());
					productAttributesMap = new HashMap();
					for(Object o : attributes) {
						ProductAttribute pa = (ProductAttribute)o;
						productAttributesMap.put(pa.getProductAttributeId(), pa);
					}
				}
				

				
				
				
				//recreate ShoppingCart
				cart = new ShoppingCart();
				List shoppingCartProducts = new ArrayList();
				
				if(pMap!=null && pMap.size()>0) {
					LocaleUtil.setLocaleToEntityCollection(productCollection, locale, store.getCurrency());
					for(Object o: productsList) {
						
						ShoppingCartProduct scp = (ShoppingCartProduct)o;
						
						if(pMap.containsKey(scp.getProductId())) {
							
							Product p = pMap.get(scp.getProductId());
							
							scp.setImage(p.getSmallImagePath());
							scp.setProductName(p.getProductDescription().getProductName());
							
							List productAttributesList = null;
							List shoppingCartAttributesList = null;
							shoppingCartProducts.add(scp);
							
							List attrs = scp.getAttributes();
							if(attrs!=null && attrs.size()>0) {
								
								for(Object oo: attrs) {
									
									ShoppingCartProductAttribute scpa = (ShoppingCartProductAttribute)oo;
									
									if(productAttributesMap.containsKey(scpa.getAttributeId())) {
										
										
										ProductAttribute pa = (ProductAttribute)productAttributesMap.get(scpa.getAttributeId());
										if(productAttributesList==null) {
											productAttributesList = new ArrayList();
										}
										if(shoppingCartAttributesList==null) {
											shoppingCartAttributesList = new ArrayList();
										}
										productAttributesList.add(pa);
										shoppingCartAttributesList.add(scpa);
										
									}
									
								}
							}
							
							if(productAttributesList!=null) {
								BigDecimal priceWithAttributes = ProductUtil
								.determinePriceWithAttributes(p, productAttributesList, locale,
										store.getCurrency());
										scp.setPrice(priceWithAttributes);
										scp.setPriceText(CurrencyUtil
												.displayFormatedAmountWithCurrency(
														priceWithAttributes, store
														.getCurrency()));
										
								scp.setAttributes(shoppingCartAttributesList);
							} else {
								
								scp.setPrice(ProductUtil.determinePrice(p, locale,
										store.getCurrency()));
								BigDecimal price = ProductUtil.determinePrice(p,
										locale, store.getCurrency());
								scp.setPriceText(CurrencyUtil
										.displayFormatedAmountWithCurrency(price, store
												.getCurrency()));
								
							}

						}

					}
				}
				
	/*			
				for(Object o : productCollection){
					Product p = (Product)o;
					//p.setLocale(locale, currency);
					if(p.getMerchantId()==store.getMerchantId()) {
						
						ShoppingCartProduct scp = new ShoppingCartProduct();
						scp.setProductId(p.getProductId());
						scp.setImage(p.getSmallImagePath());
						scp.setQuantity(1);
						
						ShoppingCartProduct temp = productsMap.get(p.getProductId());
						
						if(temp!=null) {
							scp.setQuantity(temp.getQuantity());
						}

						scp.setProductName(p.getName());
						shoppingCartProducts.add(scp);
						
						if(attributes!=null) {
							
							List productAttributesList = new ArrayList();
							
							List productAttrs = (List)productAttributes.get(p.getProductId());
							
							for(Object x : productAttrs) {
								Long productAttribute = (Long)x;
								//get the object from loaded collection
								for(Object z : attributes) {
									
									ProductAttribute pa = (ProductAttribute)z;
									if(pa.getProductAttributeId()==productAttribute) {
										
										ShoppingCartProductAttribute productAttr = new ShoppingCartProductAttribute();
										productAttr.setAttributeId(productAttribute);
										productAttr.setAttributeValue(productAttr.getAttributeValue());
										productAttr.setTextValue(productAttr.getTextValue());
										productAttributesList.add(productAttr);	
										
										
										
									}
								}
							}
							
							if(productAttributesList.size()>0 && productAttributesMap.containsKey(p.getProductId())) {
								
								List attrs = productAttributesMap.get(p.getProductId());
								
								BigDecimal priceWithAttributes = ProductUtil
								.determinePriceWithAttributes(p, attrs, locale,
										store.getCurrency());
										scp.setPrice(priceWithAttributes);
										scp.setPriceText(CurrencyUtil
												.displayFormatedAmountWithCurrency(
														priceWithAttributes, store
														.getCurrency()));
								
								
								scp.setAttributes(productAttributesList);
							}	else {
								
								scp.setPrice(ProductUtil.determinePrice(p, locale,
										store.getCurrency()));
								BigDecimal price = ProductUtil.determinePrice(p,
										locale, store.getCurrency());
								scp.setPriceText(CurrencyUtil
										.displayFormatedAmountWithCurrency(price, store
												.getCurrency()));
							}
						}	
					}
				}*/
				
				
				cart.setProducts(shoppingCartProducts);
		}
		
		return cart;

	}

	
	
	/**
	 * {"ps":[{"p":{"pid","<productid>","qty":"<quantity>","as":[{"a":"<attributeid>"},{"a":"<attributeid>"}]}},{"p":...}]}
	 * @param shoppingCart
	 * @return String
	 * @throws Exception
	 */
	public static String serializeToJSON(ShoppingCart shoppingCart) throws Exception {


		if(shoppingCart==null) {
			return null;
		}
		
		Collection products = shoppingCart.getProducts();
		if(products == null || products.size()==0) {
			return null;
		}
		
		StringBuilder json = new StringBuilder();
		json.append("{\"ps\":[");
		
		int i = 1;
		

		
		for(Object o: products) {
			
				ShoppingCartProduct product = (ShoppingCartProduct)o;
				json.append("{\"p\":");
				json.append("{\"pid\":\"");
				json.append(product.getProductId());
				json.append("\"");
				json.append(",\"q\":");
				json.append("\"");
				json.append(product.getQuantity());
				json.append("\"");
				List attributes = product.getAttributes();
				if(attributes!=null) {
					json.append(",\"as\":[");
					int j = 1;
					for(Object oo : attributes) {
						
						ShoppingCartProductAttribute scpa = (ShoppingCartProductAttribute)oo;
						json.append("{\"a\":");
						json.append("\"");
						json.append(scpa.getAttributeValue());
						json.append("\"");
						json.append("}");
						if(j<attributes.size()) {
							json.append(",");
						}
						j++;
					}
					json.append("]");
				}
				json.append("}}");
				if(i<products.size()) {
					json.append(",");
				}
				i++;
		}
		json.append("]}");
		return json.toString();
	}

}

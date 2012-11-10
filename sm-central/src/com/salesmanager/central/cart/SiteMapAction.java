/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.cart;



import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.UrlUtil;


/**
 * This class manages SiteMap file
 * @author Carl Samson
 *
 */
public class SiteMapAction extends BaseAction {
	
	
	/**
	 * Creates a sitemap
	 */
	public String execute() throws Exception {
		
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		Map<Integer,Collection> categoriesMap = new HashMap();
		Map<Integer,Collection<Product>> productsMap = new HashMap();
		Map<Integer,Collection<DynamicLabel>> pagesMap = new HashMap();
		
		 
		
		
		//for each supported language
		MerchantStore store = mservice.getMerchantStore(super.getContext().getMerchantid().intValue());
		
		
		Map langs = store.getGetSupportedLanguages();
		
		if(langs==null) {
			//@todo return a message
		}
		
		for(Object o : langs.keySet()) {
			
			Integer lang = (Integer)o;
			Language l = (Language)langs.get(lang);
			
			//get all categories
			Map categories = cservice.getCategoriesByLang(store.getMerchantId(),l.getCode());
			if(categories!=null && categories.size()>0) {
				
				
				List<Object> list = new ArrayList<Object>(categories.entrySet());

				
				categoriesMap.put(l.getLanguageId(), list);
			}
			
			//get all products
			Collection products = cservice.getProductsByMerchantIdAndLanguageId(store.getMerchantId(),l.getLanguageId());
			if(products!=null && products.size()>0) {
				productsMap.put(l.getLanguageId(), products);
			}
			
			//get pages
			Collection pages = rservice.getDynamicLabels(store.getMerchantId(),LabelConstants.STORE_FRONT_CUSTOM_PAGES,super.getLocale());
			if(pages!=null && pages.size()>0) {
				pagesMap.put(l.getLanguageId(), pages);
			}
			
		}
		
		
		WebSitemapGenerator wsg; 
		// generate pages
		
		//urls
		String baseUrl = FileUtil.getDefaultCataloguePageUrl(store, super.getServletRequest());
		String siteMapUrl = new StringBuilder().append(UrlUtil.getUnsecuredDomain(super.getServletRequest())).append(FileUtil.getSiteMapUrl()).append(store.getMerchantId()).toString();

		//dirs
		String siteMapDir = new StringBuilder().append(FileUtil.getSiteMapFilePath()).append("/").append(store.getMerchantId()).toString();
		String index_file = new StringBuilder().append(FileUtil.getSiteMapFilePath()).append("/").append(store.getMerchantId()).append("/").append("sitemap.xml").toString();
		
		
		SitemapIndexGenerator sig = new SitemapIndexGenerator(siteMapUrl, new File(index_file));
		
		if(pagesMap.size()>0 || productsMap.size()>0 || categoriesMap.size()>0) {
			
			//check if folder exists
			File sm = new File(siteMapDir);
			boolean exists = sm.exists();
			if(exists) {//delete
				sm.delete();
			}
			boolean mkdir = sm.mkdir();
			
		}
		
		if(pagesMap.size()>0) {
			 
			for(Object o : langs.keySet()) {//languages
					Integer languageId = (Integer)o;
				
					Collection pages = (Collection)pagesMap.get(languageId);
				
					if(pages!=null && pages.size()>0) {
					
					wsg = WebSitemapGenerator.builder(baseUrl,new File(siteMapDir)).fileNamePrefix("pages_" + languageId).build();
					
					for(Object oo : pages) {
						
						DynamicLabel l = (DynamicLabel)oo;
						wsg.addUrl(new StringBuilder().append(baseUrl).append("/content/").append(l.getDynamicLabelDescription().getSeUrl()).toString());
					}
					wsg.write(); // generate pages sitemap 
					sig.addUrl(siteMapUrl + "/pages_" + languageId +  ".xml");
				
				}
			}
		}
		
		
		if(productsMap.size()>0) {
			 
			for(Object o : langs.keySet()) {//languages
				Integer languageId = (Integer)o;
				
				Collection products = (Collection)productsMap.get(languageId);
				
					if(products!=null && products.size()>0) {
					
					wsg = WebSitemapGenerator.builder(baseUrl,new File(siteMapDir)).fileNamePrefix("products_" + languageId).build();
					
					int i = 1;
					for(Object oo : products) {
						if(i>50000) {break;}
						Product p = (Product)oo;
						wsg.addUrl(new StringBuilder().append(ReferenceUtil.buildCatalogUri(store)).append("/product/").append(p.getProductDescription().getSeUrl()).toString());
						i++;
					}
					wsg.write(); // generate pages sitemap 
					sig.addUrl(siteMapUrl + "/products_" + languageId +  ".xml");
				
				}
			}
			
		}
		
		if(categoriesMap.size()>0) {
			
			
			for(Object o : langs.keySet()) {//languages 
				Integer languageId = (Integer)o;
				
				Collection categories = (Collection)categoriesMap.get(languageId);
				if(categories!=null && categories.size()>0) {
					wsg = WebSitemapGenerator.builder(baseUrl,new File(siteMapDir)).fileNamePrefix("categories_"+languageId).build();
					
					int i = 1;
					for(Object oo : categories) {
						if(i>50000) {break;}
						Map.Entry entry = (Map.Entry)oo;
						Category c = (Category)entry.getValue();
						if(c.getCategoryId()>0) {
							wsg.addUrl(new StringBuilder().append(ReferenceUtil.buildCatalogUri(store)).append("/category/").append(c.getCategoryDescription().getSeUrl()).toString());
						}
						i++;
					}
					wsg.write(); // generate pages sitemap
					sig.addUrl(siteMapUrl + "/categories_" + languageId +  ".xml");
				}
			}
			
		}
		
		 
		
		//for (int i = 0; i &lt; 5; i++) 
		//	wsg.addUrl("http://www.example.com/foo"+i+".html"); 
		
		
		
		
		//wsg = WebSitemapGenerator.builder("http://www.example.com", myDir).fileNamePrefix("bar").build(); 
		//for (int i = 0; i &lt; 5; i++) 
		//	wsg.addUrl("http://www.example.com/bar"+i+".html"); 
		//wsg.write(); // generate sitemap index for foo + bar  
		

		

		
		if(pagesMap.size()>0 || productsMap.size()>0 || categoriesMap.size()>0) {
		
			sig.write();
			
			//write in configuration
			ConfigurationRequest req = new ConfigurationRequest(store.getMerchantId(),ConfigurationConstants.SITEMAP);
			ConfigurationResponse resp = mservice.getConfiguration(req);
			MerchantConfiguration conf = resp.getMerchantConfiguration(ConfigurationConstants.SITEMAP);
			
			if(conf==null) {
				conf = new MerchantConfiguration();
			}
			
			conf.setConfigurationKey(ConfigurationConstants.SITEMAP);
			conf.setMerchantId(store.getMerchantId());
			conf.setConfigurationValue(siteMapUrl + "/sitemap.xml");
			conf.setConfigurationValue1(DateUtil.formatDate(new Date()));
			
			mservice.saveOrUpdateMerchantConfiguration(conf);
	
			
			super.setSuccessMessage();
		
		}
		
		return SUCCESS;
		
	}

}

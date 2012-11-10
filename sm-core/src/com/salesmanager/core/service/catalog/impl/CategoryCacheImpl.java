/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.catalog.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.SpringUtil;

public class CategoryCacheImpl {

	/*
	 * private static Map masterCategoriesMapByLang =
	 * Collections.synchronizedMap(new HashMap());//contains a map of <langid,
	 * map[categoryid, Category]> private static Map categoriesMapByLang =
	 * Collections.synchronizedMap(new HashMap());//contains a map of <langid,
	 * map[categoryid, Category]> private static Map subCategoriesMapByLang =
	 * Collections.synchronizedMap(new HashMap());//contains a map of <langid,
	 * map[categoryid, map-subcategories[categoryid, Category]]>
	 * 
	 * //categories of merchantId 0 private static Map
	 * genericCategoriesMapByLang = Collections.synchronizedMap(new
	 * HashMap());//contains a map of <langid, map[categoryid,
	 * map-subcategories[categoryid, Category]]>
	 */

	private Map masterCategoriesMapByLang = Collections
			.synchronizedMap(new HashMap());// contains a map of <langid,
											// map[categoryid, Category]>
	private Map categoriesMapByLang = Collections
			.synchronizedMap(new HashMap());// contains a map of <langid,
											// map[categoryid, Category]>
	private Map subCategoriesMapByLang = Collections
			.synchronizedMap(new HashMap());// contains a map of <langid,
											// map[categoryid,
											// map-subcategories[categoryid,
											// Category]]>

	// categories of merchantId 0
	private Map genericCategoriesMapByLang = Collections
			.synchronizedMap(new HashMap());// contains a map of <langid,
											// map[categoryid,
											// map-subcategories[categoryid,
											// Category]]>

	private static Logger log = Logger.getLogger(CategoryCacheImpl.class);
	private static boolean loaded = false;

	private static CategoryCacheImpl instance = null;

	private CategoryCacheImpl() {
		try {
			loadCategoriesInCache();
		} catch (Exception e) {
			log.error(e);
		}

	}

	public static CategoryCacheImpl getInstance() {
		if (instance == null) {
			instance = new CategoryCacheImpl();
		}
		return instance;
	}

	public synchronized void loadCategoriesInCache() throws Exception {

		if (loaded) {
			return;
		}

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		Iterator langs = RefCache.getLanguageswithindex().entrySet().iterator();

		Map categoriesMap = null;
		Map masterCategoriesMap = null;
		Map subCategoriesMap = null;

		Map genericCategoriesMap = null;

		while (langs.hasNext()) {

			categoriesMap = new LinkedHashMap();
			masterCategoriesMap = new LinkedHashMap();
			subCategoriesMap = new LinkedHashMap();

			genericCategoriesMap = new LinkedHashMap();

			Entry e = (Entry) langs.next();

			Language l = (Language) e.getValue();

			// Get all description
			List catdesc =

			cservice.getAllCategoriesByLang(l.getLanguageId());

			Iterator allcategsit = catdesc.iterator();
			while (allcategsit.hasNext()) {

				CategoryDescription desc = (CategoryDescription) allcategsit
						.next();

				Category cat = cservice.getCategory(desc.getId()
						.getCategoryId());

				if (cat != null) {

					cat.setName(desc.getCategoryName());

					Category categ = new Category();

					try {
						BeanUtils bu = new BeanUtils();
						bu.copyProperties(categ, cat);
					} catch (Exception ie) {
						log.error(ie);
					}

					if (!categoriesMap.containsKey(categ.getCategoryId())) {
						categoriesMap.put(categ.getCategoryId(), categ);
					}

					if (categ.getParentId() == 0) {// this is a master category
						masterCategoriesMap.put(categ.getCategoryId(), categ);
					}

					if (categ.getMerchantId() == Constants.GLOBAL_MERCHANT_ID) {
						if (!genericCategoriesMap.containsKey(categ
								.getCategoryId())) {
							genericCategoriesMap.put(categ.getCategoryId(),
									categ);
						}
					}

					// populate sub categories

					long supint = categ.getParentId();
					if (supint == 0) {
						continue;
					}
					if (!subCategoriesMap.containsKey(supint)) {
						Map submap = Collections
								.synchronizedMap(new LinkedHashMap());
						submap.put(categ.getCategoryId(), categ);
						subCategoriesMap.put(supint, submap);
					} else {
						Map submap = (Map) subCategoriesMap.get(supint);
						submap.put(categ.getCategoryId(), categ);
					}

				}

			}

			Map masters = ((Map) ((LinkedHashMap) masterCategoriesMap).clone());
			Map categs = ((Map) ((LinkedHashMap) categoriesMap).clone());
			Map subs = ((Map) ((LinkedHashMap) subCategoriesMap).clone());
			Map gen = ((Map) ((LinkedHashMap) genericCategoriesMap).clone());

			masterCategoriesMapByLang.put(l.getLanguageId(), Collections
					.synchronizedMap(masters));

			categoriesMapByLang.put(l.getLanguageId(), Collections
					.synchronizedMap(categs));

			subCategoriesMapByLang.put(l.getLanguageId(), Collections
					.synchronizedMap(subs));

			genericCategoriesMapByLang.put(l.getLanguageId(), Collections
					.synchronizedMap(gen));

		}

		CacheModule module = (CacheModule) SpringUtil.getBean("cache");

		// simulate a store
		MerchantStore store = new MerchantStore();
		store.setUseCache(true);

		module.putInCache("masterCategoriesMapByLang",
				masterCategoriesMapByLang, Constants.CACHE_CATEGORIES, store);
		module.putInCache("categoriesMapByLang", categoriesMapByLang,
				Constants.CACHE_CATEGORIES, store);
		module.putInCache("subCategoriesMapByLang", subCategoriesMapByLang,
				Constants.CACHE_CATEGORIES, store);
		module.putInCache("genericCategoriesMapByLang", subCategoriesMapByLang,
				Constants.CACHE_CATEGORIES, store);

		loaded = true;

	}

	/*
	 * private static void setCategoriesMapByLang(Map categoriesMapByLang) {
	 * CategoryCacheImpl.categoriesMapByLang = categoriesMapByLang; } private
	 * static void setMasterCategoriesMapByLang(Map masterCategoriesMapByLang) {
	 * CategoryCacheImpl.masterCategoriesMapByLang = masterCategoriesMapByLang;
	 * }
	 * 
	 * private static void setSubCategoriesMapByLang(Map subCategoriesMapByLang)
	 * { CategoryCacheImpl.subCategoriesMapByLang = subCategoriesMapByLang; }
	 * 
	 * private static void setGenericCategoriesMapByLang(Map
	 * genericCategoriesMapByLang) {
	 * CategoryCacheImpl.genericCategoriesMapByLang =
	 * genericCategoriesMapByLang; }
	 */

	/**
	 * Exposed getters
	 * 
	 * @return
	 */

	/*
	 * public Map getCategoriesMapByLang() {
	 * 
	 * 
	 * 
	 * if(categoriesMapByLang==null) { try { loaded = false;
	 * instance.getInstance().loadCategoriesInCache(); } catch (Exception e) {
	 * log.error(e); }
	 * 
	 * } return categoriesMapByLang; }
	 */

	public Map getMasterCategoriesMapByLang() {
		if (masterCategoriesMapByLang == null) {
			try {
				loaded = false;
				instance.getInstance().loadCategoriesInCache();
			} catch (Exception e) {
				log.error(e);
			}

		}
		return masterCategoriesMapByLang;
	}

	public Map getSubCategoriesMapByLang() {
		if (subCategoriesMapByLang == null) {
			try {
				loaded = false;
				instance.getInstance().loadCategoriesInCache();
			} catch (Exception e) {
				log.error(e);
			}

		}
		return subCategoriesMapByLang;
	}

	/*
	 * public static Map getGenericCategoriesMapByLang() {
	 * if(genericCategoriesMapByLang==null) { try { loaded = false;
	 * instance.getInstance().loadCategoriesInCache(); } catch (Exception e) {
	 * log.error(e); }
	 * 
	 * } return genericCategoriesMapByLang; }
	 */

}

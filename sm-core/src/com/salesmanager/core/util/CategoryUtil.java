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
package com.salesmanager.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryPadding;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;

/**
 * Helper for categories.jsp
 * 
 * @author Carl Samson
 * 
 */
public class CategoryUtil {

	private CategoryUtil() {
	}

	private static Logger log = Logger.getLogger(CategoryUtil.class);

	private static Configuration conf = PropertiesUtil.getConfiguration();

	static {

	}

	/**
	 * Get the number of products per category
	 * 
	 * @param req
	 * @param categoryid
	 * @return
	 */
	public static String getItemPerCategoryCount(HttpServletRequest req,
			String lang, Category category) {

		try {

			CatalogService service = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			int count = service.countProductsPerCategoryAndSubCategories(
					(List) req.getAttribute("PRODUCTS"), lang, category);

			if (count == 0) {
				return "";
			} else {
				return "<b><font color='red'>[" + count + "]</font></b>";
			}

		} catch (Exception e) {
			log.error(e);
			return "";
		}

	}

	/**
	 * Determine if a catagory has products
	 * 
	 * @param req
	 * @param categoryid
	 * @return
	 */
	public static boolean categoryHasItems(HttpServletRequest req,
			long categoryid) {

		Map reccount = (Map) req.getSession().getAttribute("PRODUCTCOUNT");
		if (reccount != null) {
			Integer count = (Integer) reccount.get(categoryid);
			if (count == null)
				return false;
			return true;
		} else {
			return false;
		}

	}



	/**
	 * returns a tree path for a given category from the lowest level
	 * 
	 * @param req
	 * @param categoriesid
	 * @return
	 */
	public static List getCategoryPath(String lang, int merchantId,
			long categoryid) {

		List returnlist = new ArrayList();

		try {

			CatalogService service = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			Map cat = service.getCategoriesByLang(merchantId, lang);

			if (cat == null || categoryid == 0) {
				return returnlist;
			}

			boolean atroot = false;
			long curcateg = categoryid;
			// while root category not reached
			while (!atroot) {
				Category categ = (Category) cat.get(curcateg);
				long parentcategid = categ.getParentId();
				returnlist.add(categ);
				curcateg = parentcategid;
				if (parentcategid == 0)
					atroot = true;
			}

			Collections.reverse(returnlist);

		} catch (Exception e) {
			log.error(e);
		}

		return returnlist;

	}

	public static Category getRootCategoryforCategory(String lang,
			int merchantId, long categoryid) {
		List path = getCategoryPath(lang, merchantId, categoryid);
		Category c = (Category) path.get(path.size() - 1);
		return c;
	}

	public static List getCategoriesForDropDownBox(int merchantId, String lang) {

		String usecache = conf.getString("core.cachecategoriesstructure");
		if (usecache != null && usecache.equals("true")) {
			// use os cache
			CacheUtil cache = CacheUtil.getInstance();
			if (cache.containsCache("categoriesdropdown")) {
				Map cachemap = cache.getCacheMap("categoriesdropdown");
				List elements = (List) cachemap.get("categoriesdropdown-"
						+ lang);
				if (elements != null) {
					return elements;
				}
			}
		}

		List returnlist = new ArrayList();

		try {

			CatalogService service = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			Collection categories = service
					.findCategoriesByMerchantIdAndLineageAndLanguageId(
							merchantId, CatalogConstants.LINEAGE_DELIMITER,
							lang);

			Map newClassification = new LinkedHashMap();

			List returnList = new LinkedList();

			if (categories != null && categories.size() > 0) {

				Iterator i = categories.iterator();

				Map classification = new LinkedHashMap();

				String lineage = "";
				int padding = 1;
				while (i.hasNext()) {

					Category c = (Category) i.next();

					// need parent id
					long parentId = c.getParentId();
					if (c.getCategoryId() == CatalogConstants.ROOT_CATEGORY_ID)
						continue;
					Map subCategoriesMap = (Map) classification.get(parentId);
					if (subCategoriesMap == null) {
						subCategoriesMap = new TreeMap();
						classification.put(parentId, subCategoriesMap);
					}

					CategoryPadding cpadding = new CategoryPadding();
					StringBuffer spaddingname = new StringBuffer();

					for (int pad = 0; pad < c.getDepth() - 1; pad++) {
						spaddingname.append("&nbsp;&nbsp;");
					}

					spaddingname.append(c.getName());
					cpadding.setName(spaddingname.toString());
					cpadding.setCategoryId(c.getCategoryId());
					subCategoriesMap.put(c.getCategoryId(), cpadding);

				}

				// re-order

				Iterator classificationIterator = classification.keySet()
						.iterator();
				while (classificationIterator.hasNext()) {
					Long parentId = (Long) classificationIterator.next();
					Map subCategoriesMap = (Map) classification.get(parentId);
					Iterator subCategoriesMapIterator = subCategoriesMap
							.keySet().iterator();
					while (subCategoriesMapIterator.hasNext()) {
						Long categoryId = (Long) subCategoriesMapIterator
								.next();

						CategoryPadding cpadding = (CategoryPadding) subCategoriesMap
								.get(categoryId);
						if (!newClassification.containsKey(cpadding
								.getCategoryId())) {
							newClassification.put(cpadding.getCategoryId(),
									cpadding);

							// get children
							newClassification = walkCategories(
									newClassification, classification, cpadding
											.getCategoryId());
						}

					}
				}

			}

			return new LinkedList(newClassification.values());

		} catch (Exception e) {
			log.error(e);
		}

		return returnlist;

	}

	private static Map walkCategories(Map returnMap, Map classification,
			long categoryId) {

		Map csMap = (Map) classification.get(categoryId);
		if (csMap != null) {
			Iterator csMapIterator = csMap.keySet().iterator();
			while (csMapIterator.hasNext()) {
				// get a subcategory
				Long csKey = (Long) csMapIterator.next();
				CategoryPadding cp = (CategoryPadding) csMap.get(csKey);
				returnMap.put(cp.getCategoryId(), cp);
				returnMap = walkCategories(returnMap, classification, cp
						.getCategoryId());
			}
		}

		return returnMap;

	}

}

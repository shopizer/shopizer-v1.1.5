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
package com.salesmanager.central.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.catalog.Category;

/**
 * Helper for categories.jsp
 * 
 * @author Carl Samson
 * 
 */
public class CategoryHelper {

	private CategoryHelper() {
	}

	private static Logger log = Logger.getLogger(CategoryHelper.class);

	/**
	 * Get the number of products per category
	 * 
	 * @param req
	 * @param categoryid
	 * @return
	 */
	public static String getItemPerCategoryCount(HttpServletRequest req,
			int categoryid) {

		Map reccount = (Map) req.getSession().getAttribute("PRODUCTCOUNT");
		if (reccount != null) {
			Integer count = (Integer) reccount.get(categoryid);
			if (count == null)
				return "";
			return "<b><font color='red'>[" + count.intValue() + "]</font></b>";
		} else {
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
			int categoryid) {

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
	 * @deprecated
	 */
	public static List getCategoryPath(String lang, int categoriesid) {

		List returnlist = new ArrayList();

		// Map cat = CatalogService.getCategoriesMapByLang(lang);
		Map cat = null;
		// Map cat = RefCache.getCategoriesWithIndex(lang);
		if (cat == null) {
			return returnlist;
		}

		boolean atroot = false;
		long curcateg = categoriesid;
		// while root category not reached
		while (!atroot) {
			Category categ = (Category) cat.get(curcateg);
			long parentcategid = categ.getParentId();
			returnlist.add(categ);
			curcateg = parentcategid;
			if (parentcategid == 0)
				atroot = true;
		}
		return returnlist;

	}

}

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.salesmanager.central.PageBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;

public class ProductListAction extends PageBaseAction {

	private static Logger log = Logger.getLogger(ProductListAction.class);

	private static Configuration config = PropertiesHelper.getConfiguration();
	private static int listsize = 20;

	static {

		listsize = config.getInt("core.productlist.maxsize", 20);
	}

	/**
	 * For updating the availability
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateavailability() throws Exception {

		super.setPageTitle("label.prodlist.title");
		
		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			String check[] = super.getServletRequest().getParameterValues(
					"prodavailability");
			String original[] = super.getServletRequest().getParameterValues(
					"prodlist");

			Map checked = new HashMap();

			// Set the availability for checked entries
			if (check != null && check.length > 0) {

				List cids = new ArrayList();
				for (int i = 0; i < check.length; i++) {
					// retrieve product id
					String id = check[i];
					cids.add(Long.valueOf(id));

					checked.put(id, id);
				}

				CatalogService cservice = (CatalogService) ServiceFactory
						.getService(ServiceFactory.CatalogService);

				cservice.updateProductListAvailability(true, super.getContext()
						.getMerchantid(), cids);

			}

			// unset the availability for unchecked entries
			if (original.length != checked.size()
					&& original.length >= checked.size()) {
				int ifound = original.length - checked.size();
				if (original != null && original.length > 0) {

					List ids = new ArrayList();
					int curfound = 0;
					for (int i = 0; i < original.length; i++) {
						String id = original[i];
						if (!checked.containsKey(id)) {
							ids.add(Long.valueOf(id));
							curfound++;
						}
					}

					CatalogService cservice = (CatalogService) ServiceFactory
							.getService(ServiceFactory.CatalogService);

					cservice.updateProductListAvailability(false, super
							.getContext().getMerchantid(), ids);

				}
			}

			super.setSuccessMessage();
			return SUCCESS;
		} catch (HibernateException e) {
			log.error(e);
			throw e;
		}

	}

	/**
	 * For displaying the listing page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {

		super.setPageTitle("label.prodlist.title");
		
		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			// this property is specific to central

			String sstartindex = super.getServletRequest().getParameter(
					"startindex");
			String categ = super.getServletRequest().getParameter("categ");
			String productname = super.getServletRequest().getParameter(
					"productname");
			String availability = super.getServletRequest().getParameter(
					"availability");
			String status = super.getServletRequest().getParameter("status");

			super.getServletRequest().setAttribute("categ", -1);
			super.getServletRequest().setAttribute("productname", "");
			super.getServletRequest().setAttribute("availability", "2");
			super.getServletRequest().setAttribute("status", "2");



			SearchProductCriteria criteria = new SearchProductCriteria();

			// include the requested category in the query
			if (categ != null && !categ.equals("") && !categ.equals("-1")) {
				try {
					int categid = Integer.parseInt(categ);
					criteria.setCategoryid(categid);
					super.getServletRequest().setAttribute("categoryfilter",
							categ);
				} catch (Exception e) {
					log
							.error("Cannot parse String " + categ
									+ " to categoryid");
				}
			}
			if (productname != null && !productname.equals("")) {
				criteria.setDescription(productname);
				super.getServletRequest().setAttribute("productname",
						productname);
			}

			if (availability != null && !availability.equals("")) {// availability
				if (availability.equals("1")) {
					criteria
							.setVisible(ProductSearchFilterCriteria.VISIBLETRUE);
				}
				if (availability.equals("2")) {
					criteria.setVisible(ProductSearchFilterCriteria.VISIBLEALL);
				}
				if (availability.equals("0")) {
					criteria
							.setVisible(ProductSearchFilterCriteria.VISIBLEFALSE);
				}
				super.getServletRequest().setAttribute("availability",
						availability);
			}

			if (status != null && !status.equals("")) {// visibility

				if (status.equals("1")) {
					criteria
							.setStatus(ProductSearchFilterCriteria.STATUSINSTOCK);
				}
				if (status.equals("2")) {
					criteria.setStatus(ProductSearchFilterCriteria.STATUSALL);
				}
				if (status.equals("0")) {
					criteria
							.setStatus(ProductSearchFilterCriteria.STATUSOUTSTOCK);
				}
				super.getServletRequest().setAttribute("status", status);
			}

			setSize(listsize);
			super.setPageStartNumber();

			criteria.setMerchantId(super.getContext().getMerchantid());
			criteria.setQuantity(this.getSize());
			criteria.setStartindex(this.getPageStartIndex());
			criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
					.getContext().getLang()));

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			SearchProductResponse response = cservice.findProducts(criteria);

			super.getServletRequest().setAttribute("PRODUCTSLIST",
					response.getProducts());
			super.setListingCount(response.getCount());
			super.setRealCount(response.getProducts().size());
			super.setPageElements();

			Collection prds = response.getProducts();
			LocaleUtil.setLocaleToEntityCollection(prds, super.getLocale());

			return SUCCESS;
		} catch (HibernateException e) {
			log.error(e);
			throw e;
		}
	}

}

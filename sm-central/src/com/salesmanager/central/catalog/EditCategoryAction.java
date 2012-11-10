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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogException;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.CategoryUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class EditCategoryAction extends BaseAction implements Preparable {

	private Collection<Language> languages;// used in the page as an index

	private Map<Integer, Integer> reflanguages = new HashMap();// reference
																// count -
																// languageId

	private List<String> names = new ArrayList<String>();
	private List<String> descriptions = new ArrayList<String>();
	private List<String> metaDescriptions = new ArrayList<String>();
	private List<String> title = new ArrayList<String>();

	public List<String> getMetaDescriptions() {
		return metaDescriptions;
	}

	public void setMetaDescriptions(List<String> metaDescriptions) {
		this.metaDescriptions = metaDescriptions;
	}

	public List<String> getSefurl() {
		return sefurl;
	}

	public void setSefurl(List<String> sefurl) {
		this.sefurl = sefurl;
	}

	private List<String> sefurl = new ArrayList<String>();

	private String categoryid;
	private String parentcategoryid;
	private Category category;
	private String parentcategory;
	private String categ;// from combo box

	private String categoryname;

	private int action = -1;

	private static Logger log = Logger.getLogger(EditCategoryAction.class);

	/**
	 * Displays the categories listing page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String display() {

		try {
			
			super.setPageTitle("label.allcategories");

			String categid = this.getCategoryid();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			long categoryId = 0;

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			if (categid != null) {
				try {
					categoryId = new Long(categid).longValue();
				} catch (Exception e) {
					log.error("Cannot convert to integer " + categid);
					this.categoryid = "0";// set to root
				}
			}

			this.setCategoryid(String.valueOf(categoryId));

			if (categid == null || categoryId == 0) {// get root categories

				Map mastercategories = cservice
						.getSubCategoriesByCategoryAndLang(ctx.getMerchantid(),
								0, ctx.getLang());
				super.getServletRequest().setAttribute("CATEGORIES",
						mastercategories);
			} else {

				Map subcategories = cservice.getSubCategoriesByCategoryAndLang(
						ctx.getMerchantid(), categoryId, ctx.getLang());

				CategoryDescription c = cservice.getCategoryDescriptionByLang(
						ctx.getMerchantid(), categoryId, ctx.getLang());
				if (c != null) {
					this.setCategoryname(c.getCategoryName());
				}

				super.getServletRequest().setAttribute("CATEGORIES",
						subcategories);
			}

			super.getServletRequest().setAttribute("CATEGORYID",
					this.categoryid);

			Map reccount = new HashMap();

			super.getServletRequest().getSession().removeAttribute(
					"PRODUCTCOUNT");

			Collection products = cservice.getProducts(ctx.getMerchantid());

			super.getServletRequest().setAttribute("PRODUCTS", products);

			if (products == null) {
				return SUCCESS;
			}

			Iterator countit = products.iterator();
			while (countit.hasNext()) {
				Product prd = (Product) countit.next();
				long categ = prd.getMasterCategoryId();
				Integer itcount = (Integer) reccount.get(categ);
				if (itcount == null) {
					reccount.put(categ, 1);
				} else {
					int recval = itcount.intValue();
					recval++;
					reccount.put(categ, recval);
				}
			}

			super.getServletRequest().getSession().setAttribute("PRODUCTCOUNT",
					reccount);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public void prepare() {

		try {

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantStore mstore = service.getMerchantStore(merchantid);

			if (mstore == null) {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"errors.profile.storenotcreated"));
			}

			Map languagesMap = mstore.getGetSupportedLanguages();

			languages = languagesMap.values();// collection reverse the map

			int count = 0;
			Iterator langit = languagesMap.keySet().iterator();
			while (langit.hasNext()) {
				Integer langid = (Integer) langit.next();
				Language lang = (Language) languagesMap.get(langid);
				reflanguages.put(count, langid);
				count++;
			}

			super.getServletRequest().setAttribute("languages", languages);

		} catch (Exception e) {
			log.error(e);
		}

	}

	public String showEditCategory() throws Exception {

		try {
			
			super.setPageTitle("label.category.editcategory");

			// Get parent id name
			String lang = super.getLocale().getLanguage();
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			// Map categoriesByLang = cservice.getCategoriesMapByLang(lang);
			Map categoriesByLang = cservice.getCategoriesByLang(super
					.getContext().getMerchantid(), lang);

			/** Only avaliable when creating a category **/
			if (!StringUtils.isBlank(this.getParentcategoryid())) {
				Category category = (Category) categoriesByLang.get(Long
						.parseLong(this.getParentcategoryid()));
				if (category != null) {
					this.setParentcategory(category.getName());
				}
			}

			Category c = new Category();
			this.setCategory(c);

			// get save category
			if (!StringUtils.isBlank(this.getCategoryid())) {

				c = cservice
						.getCategory(Long.parseLong(this.getCategoryid()));
				this.setCategory(c);

				Category category = (Category) categoriesByLang.get(c
						.getParentId());
				this.setParentcategory(category.getName());

				// this is for the category combo box
				super.getServletRequest().setAttribute("categoryfilter",
						String.valueOf(c.getParentId()));// for drop down box

				// this is for the category combo box
				List remove = new ArrayList();
				remove.add(c.getParentId());
				remove.add(c.getCategoryId());
				super.getServletRequest()
						.setAttribute("removecategory", remove);

				// language id-CategoryDescription
				Map iddescmap = new HashMap();

				List descriptionslist = cservice.getCategoryDescriptions(c
						.getCategoryId());
				if (descriptionslist != null) {
					Iterator i = descriptionslist.iterator();
					while (i.hasNext()) {
						Object o = i.next();
						if (o instanceof CategoryDescription) {
							CategoryDescription desc = (CategoryDescription) o;
							iddescmap.put(desc.getId().getLanguageId(), desc);

						}
					}
				}

				// iterate through languages for appropriate order
				for (int count = 0; count < reflanguages.size(); count++) {
					int langid = (Integer) reflanguages.get(count);
					CategoryDescription desc = (CategoryDescription) iddescmap
							.get(langid);
					if (desc != null) {
						names.add(desc.getCategoryName());
						descriptions.add(desc.getCategoryDescription());
						metaDescriptions.add(desc.getMetatagDescription());
						sefurl.add(desc.getSeUrl());
						title.add(desc.getCategoryTitle());
					}
				}
			}

			if (!StringUtils.isBlank(this.getParentcategoryid())) {
				long parentid = 0;
				try {
					parentid = Long.parseLong(this.getParentcategoryid());
				} catch (Exception e) {
					log.error("Wrong value for parentid "
							+ this.getParentcategoryid());
				}

				c.setParentId(parentid);
			}

		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
		}

		return SUCCESS;
	}

	/**
	 * action -1 is default for editing. It may be a creation too action 0 is
	 * move category
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveCategory() throws Exception {

		try {
			super.setPageTitle("label.category.editcategory");

			boolean hasError = false;

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			if (this.reflanguages.size() == 0) {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"errors.profile.storenotcreated"));
				return INPUT;
			}

			// Get parent id name
			String lang = super.getLocale().getLanguage();
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			Map categoriesByLang = cservice.getCategoriesByLang(super
					.getContext().getMerchantid(), lang);
			Category parentCategory = (Category) categoriesByLang.get(this
					.getCategory().getParentId());
			if (parentCategory != null) {
				this.setParentcategory(parentCategory.getName());
			} else {
				parentCategory = new Category();
			}

			if (StringUtils.isBlank(this.getCateg()) && this.getAction() == 0) {// blank
																				// submission
				return INPUT;
			}

			long pId = parentCategory.getCategoryId();

			/** From switch category **/
			if (!StringUtils.isBlank(this.getCateg()) && this.getAction() == 0) {// change
																					// the
																					// category

				long newparentcategoryid = -1;
				try {
					newparentcategoryid = Integer.parseInt(this.getCateg());
				} catch (Exception e) {
					MessageUtil
							.addErrorMessage(super.getServletRequest(),
									LabelUtil.getInstance().getText(super.getLocale(),
											"errors.technical"));
					return SUCCESS;
				}

				// refuse to switch category if the original category is under
				// root and the target
				// category is a child of that category
				Category switched = this.getCategory();
				pId = switched.getParentId();
				if (pId == ProductConstants.ROOT_CATEGORY_ID) {

					Category switchee = CategoryUtil
							.getRootCategoryforCategory(ctx.getLang(), ctx
									.getMerchantid(), newparentcategoryid);
					if (switchee.getCategoryId() == switched.getCategoryId()) {
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(super.getLocale(),
										"messages.category.cannotmove"));
						return SUCCESS;
					}
				}
				// get the new parentId
				parentCategory = (Category) categoriesByLang
						.get(newparentcategoryid);
				this.getCategory().setParentId(newparentcategoryid);
				this.getCategory().setParent(parentCategory);
			}

			Map descriptionsmap = new HashMap();

			if (this.getCategory().getSortOrder() == null) {
				super.addFieldError("category.sortOrder",
						getText("messages.required.sortOrder"));
				hasError = true;
			}

			if (this.getAction() == -1) {

				// names - description and seurl
				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String name = (String) this.getNames().get(langcount);
					if (StringUtils.isBlank(name)) {
						super.addFieldError("names[" + langcount + "]",
								getText("messages.required.categoryName"));
						hasError = true;
					}
					int submitedlangid = (Integer) reflanguages.get(langcount);
					CategoryDescription desc = new CategoryDescription();
					CategoryDescriptionId id = new CategoryDescriptionId();
					id.setLanguageId(submitedlangid);
					desc.setCategoryName(name);
					desc.setId(id);

					String description = (String) this.getDescriptions().get(
							langcount);
					String metaDescription = (String) this
							.getMetaDescriptions().get(langcount);
					String seurl = (String) this.getSefurl().get(langcount);
					
					String title = (String) this.getTitle().get(langcount);

					desc.setCategoryDescription(description);
					if (!StringUtils.isBlank(metaDescription)) {
						desc.setMetatagDescription(metaDescription);
					}
					if (!StringUtils.isBlank(seurl)) {
						desc.setSeUrl(seurl);
					}
					if(!StringUtils.isBlank(title)) {
						desc.setCategoryTitle(title);
					}

					descriptionsmap.put(submitedlangid, desc);
				}

				if (hasError) {
					return INPUT;
				}

			}

			Date date = new Date();

			Category c = this.getCategory();

			if (c.getDateAdded() == null) {
				c.setDateAdded(new Date(date.getTime()));
			}
			c.setLastModified(new Date(date.getTime()));
			c.setCategoryStatus(true);
			c.setMerchantId((Integer) getServletRequest().getSession()
					.getAttribute(ProfileConstants.merchant));
			c.setParent(parentCategory);

			if (this.getAction() == 0) {
				cservice.moveCategory(pId, c);
			} else {
				cservice.saveOrUpdateCategory(c, descriptionsmap.values());
			}

			super.setSuccessMessage();

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return INPUT;
		}

		return SUCCESS;

	}

	public String deleteCategory() throws Exception {
		
		super.setPageTitle("label.category.editcategory");
		
		try {
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			if ((Integer) getServletRequest().getSession().getAttribute(
					ProfileConstants.merchant) != null) {
				cservice.deleteCategory(this.getCategory().getCategoryId(),
						((Integer) getServletRequest().getSession()
								.getAttribute(ProfileConstants.merchant))
								.intValue());
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (CatalogException e) {
			if (e.getReason() == ErrorConstants.DELETE_UNSUCCESS_CATEGORY_NO_MERCHANT) {
				MessageUtil
						.addErrorMessage(
								super.getServletRequest(),
								LabelUtil
										.getInstance()
										.getText(
												"error.category.delete.failure.merchant.notexists"));
			} else if (e.getReason() == ErrorConstants.DELETE_UNSUCCESS_PRODUCTS_ATTACHED) {
				MessageUtil
						.addErrorMessage(
								super.getServletRequest(),
								LabelUtil
										.getInstance()
										.getText(
												"error.category.delete.failure.products.exists"));
			} else {
				log.error(e);
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),"errors.technical"));
			}
			return ERROR;
		}
	}

	public List getNames() {
		return names;
	}

	public void setNames(List names) {
		this.names = names;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getParentcategoryid() {
		return parentcategoryid;
	}

	public void setParentcategoryid(String parentcategoryid) {
		this.parentcategoryid = parentcategoryid;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public String getParentcategory() {
		return parentcategory;
	}

	public void setParentcategory(String parentcategory) {
		this.parentcategory = parentcategory;
	}

	public String getCateg() {
		return categ;
	}

	public void setCateg(String categ) {
		this.categ = categ;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

}

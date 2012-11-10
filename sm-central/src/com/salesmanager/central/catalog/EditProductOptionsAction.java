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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescription;
import com.salesmanager.core.entity.catalog.ProductOptionDescriptionId;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;

public class EditProductOptionsAction extends BaseAction implements Preparable {

	private List<String> names = new ArrayList<String>();
	private List<String> comments = new ArrayList<String>();
	private ProductOption productOption = null;
	private int action = -1; // 0 is add 1 is delete

	private Collection<Language> languages;// used in the page as an index
	private Map<Integer, Integer> reflanguages = new HashMap();// reference
																// count -
																// languageId

	private Logger log = Logger.getLogger(EditProductOptionsAction.class);

	public void prepare() {

		try {
			
			super.setPageTitle("label.product.productoptions.title");

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
			} else {

				Map languagesMap = mstore.getGetSupportedLanguages();

				languages = languagesMap.values();// collection reverse the map

				super.getServletRequest().setAttribute("languages", languages);

				// int count = languagesMap.size()-1;
				int count = 0;
				Iterator langit = languagesMap.keySet().iterator();
				while (langit.hasNext()) {
					Integer langid = (Integer) langit.next();
					Language lang = (Language) languagesMap.get(langid);
					reflanguages.put(count, langid);
					count++;
				}

			}

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			Collection options = cservice.getProductOptions(merchantid);
			super.getServletRequest().setAttribute("options", options);

			Collection optionTypes = cservice.getProductOptionTypes();
			super.getServletRequest().setAttribute("optionTypes", optionTypes);

		} catch (Exception e) {
			log.error(e);
		}

	}

	public String displayProductOptions() throws Exception {

		return SUCCESS;
	}

	public String editProductOptions() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		HashSet descriptionsset = new HashSet();

		if (this.getProductOption() == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error("Should have received a ProductOption");
			return SUCCESS;
		}

		if (getLanguages() == null || getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		try {

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			if (this.getAction() == 0) {

				// names

				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String name = (String) this.getNames().get(langcount);
					String comment = (String) this.getComments().get(langcount);

					int submitedlangid = (Integer) reflanguages.get(langcount);
					String langCode = LanguageUtil
							.getLanguageStringCode(submitedlangid);

					if (StringUtils.isBlank(name)) {
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(super.getLocale(),
										"messages.productoption.name.required")
										+ " (" + langCode + ")");
						return SUCCESS;
					}

					ProductOptionDescription desc = new ProductOptionDescription();
					ProductOptionDescriptionId id = new ProductOptionDescriptionId();
					id.setLanguageId(submitedlangid);
					desc.setProductOptionName(name);
					desc.setProductOptionComment(comment);
					desc.setId(id);

					descriptionsset.add(desc);

				}

			}

			ProductOption option = this.getProductOption();

			if (this.getAction() == 0) { // add
				option.setMerchantId(merchantid);

				option.setDescriptions(descriptionsset);

				cservice.saveOrUpdateProductOption(option);

			} else {
				cservice.deleteProductOption(option.getProductOptionId());
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;

	}

	public String addProductOption() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		HashSet descriptionsset = new HashSet();

		if (this.getProductOption() == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error("Should have received a ProductOption");
			return SUCCESS;
		}

		if (getLanguages() == null || getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		try {

			// names

			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();
				String name = (String) this.getNames().get(langcount);
				String comment = (String) this.getComments().get(langcount);

				int submitedlangid = (Integer) reflanguages.get(langcount);
				String langCode = LanguageUtil
						.getLanguageStringCode(submitedlangid);

				if (StringUtils.isBlank(name)) {
					MessageUtil.addErrorMessage(super.getServletRequest(),
							LabelUtil.getInstance().getText(super.getLocale(),
									"messages.productoption.name.required")
									+ " (" + langCode + ")");
					return SUCCESS;
				}

				ProductOptionDescription desc = new ProductOptionDescription();
				ProductOptionDescriptionId id = new ProductOptionDescriptionId();
				id.setLanguageId(submitedlangid);
				desc.setProductOptionName(name);
				desc.setProductOptionComment(comment);
				desc.setId(id);

				descriptionsset.add(desc);

			}

			ProductOption option = this.getProductOption();
			option.setMerchantId(merchantid);
			option.setDescriptions(descriptionsset);

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			cservice.saveOrUpdateProductOption(option);

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;

	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public Map<Integer, Integer> getReflanguages() {
		return reflanguages;
	}

	public void setReflanguages(Map<Integer, Integer> reflanguages) {
		this.reflanguages = reflanguages;
	}

	public ProductOption getProductOption() {
		return productOption;
	}

	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}

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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescriptionId;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.module.impl.application.files.FileException;
import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;

public class EditProductOptionsValuesAction extends BaseAction implements
		Preparable {

	private List<String> names = new ArrayList<String>();

	private ProductOption productOption;
	private ProductOptionDisplay productOptionDisplay;

	private ProductOptionValue productOptionValue = null;
	private int action = -1; // 0 is add 1 is delete

	private Collection<Language> languages;// used in the page as an index
	private Map<Integer, Integer> reflanguages = new HashMap();// reference
																// count -
																// languageId

	private Collection optionList = null;
	private Long productOptionValueId = null;

	// image upload
	private String uploadimagefilename;
	private String uploadimagecontenttype;
	private File uploadimage;

	private static Configuration conf = PropertiesUtil.getConfiguration();

	private Logger log = Logger.getLogger(EditProductOptionsValuesAction.class);

	public void prepare() {
		
		super.setPageTitle("label.product.productoptionsvalues.title");

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

		} catch (Exception e) {
			log.error(e);
		}

	}

	public String displayProductOptionsValues() throws Exception {

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			Collection values = null;

			// Get optionValues
			if (this.getProductOption() != null) {// get values for a given
													// ProductOption
				long id = productOption.getProductOptionId();
				productOption = cservice.getProductOptionWithValues(this
						.getProductOption().getProductOptionId());

				if (productOption == null) {
					log
							.error("ProductOption was not supposed to be null for id "
									+ id);
					MessageUtil
							.addErrorMessage(super.getServletRequest(),
									LabelUtil.getInstance().getText(super.getLocale(),
											"errors.technical"));
					return SUCCESS;
				}

				ProductOptionDisplay pod = new ProductOptionDisplay();
				pod.setProductOptionId(productOption.getProductOptionId());
				pod.setProductOptionName(String.valueOf(productOption
						.getProductOptionId()));

				Set optdescs = productOption.getDescriptions();
				if (optdescs != null) {
					Iterator desci = optdescs.iterator();
					while (desci.hasNext()) {
						ProductOptionDescription description = (ProductOptionDescription) desci
								.next();
						if (description.getId().getLanguageId() == LanguageUtil
								.getLanguageNumberCode(ctx.getLang())) {
							pod.setProductOptionName(description
									.getProductOptionName());
						}
					}
				}

				this.setProductOptionDisplay(pod);

				values = productOption.getValues();

				// prepare association list
				Collection alllist = cservice.getProductOptionValues(ctx
						.getMerchantid());
				List displaylist = new ArrayList();
				if (alllist != null) {
					Iterator i = alllist.iterator();
					while (i.hasNext()) {
						ProductOptionValue value = (ProductOptionValue) i
								.next();
						if (!values.contains(value)) {
							ProductOptionValueDisplay pov = new ProductOptionValueDisplay();
							pov.setProductOptionValueId(value
									.getProductOptionValueId());
							pov.setProductOptionValueName(String.valueOf(value
									.getProductOptionValueId()));
							Set descs = value.getDescriptions();
							if (descs != null) {
								Iterator desci = descs.iterator();
								while (desci.hasNext()) {
									ProductOptionValueDescription description = (ProductOptionValueDescription) desci
											.next();
									if (description.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(ctx
													.getLang())) {
										pov
												.setProductOptionValueName(description
														.getProductOptionValueName());
									}
								}
							}
							displaylist.add(pov);
						}

					}
				}
				optionList = displaylist;

			} else {// get all values
				values = cservice.getProductOptionValues(ctx.getMerchantid());
			}

			super.getServletRequest().setAttribute("optionsvalues", values);

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;
	}

	public String associateProductOptionValue() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		if (this.getProductOption() == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error("Should have received a ProductOptionValue");
			return "associate-success";
		}

		if (this.getProductOptionValueId() == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error("Should have received a ProductOptionValue");
			return "associate-success";
		}

		if (getLanguages() == null || getLanguages().size() == 0) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
			return SUCCESS;
		}

		try {

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			cservice.associateProductOptionValueToProductOption(this
					.getProductOption().getProductOptionId(), this
					.getProductOptionValueId());

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return "associate-success";

	}

	public String editProductOptionsValues() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		HashSet descriptionsset = new HashSet();

		if (this.getProductOptionValue() == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error("Should have received a ProductOptionValue");
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

			if (this.getAction() == 0) {// add

				// names

				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String name = (String) this.getNames().get(langcount);

					int submitedlangid = (Integer) reflanguages.get(langcount);
					String langCode = LanguageUtil
							.getLanguageStringCode(submitedlangid);

					if (StringUtils.isBlank(name)) {
						MessageUtil
								.addErrorMessage(
										super.getServletRequest(),
										LabelUtil
												.getInstance()
												.getText(
														"messages.productoptionvalue.name.required")
												+ " (" + langCode + ")");
						return SUCCESS;
					}

					ProductOptionValueDescription desc = new ProductOptionValueDescription();
					ProductOptionValueDescriptionId id = new ProductOptionValueDescriptionId();
					id.setLanguageId(submitedlangid);
					desc.setProductOptionValueName(name);
					desc.setId(id);

					descriptionsset.add(desc);

				}

			}

			ProductOption option = this.getProductOption();
			ProductOptionValue optionValue = this.getProductOptionValue();

			if (this.getAction() == 0) { // add
				optionValue.setMerchantId(merchantid);
				optionValue.setDescriptions(descriptionsset);
				if (option != null && option.getProductOptionId() > 0) {
					cservice.saveOrUpdateProductOptionValueToProductOption(
							optionValue, option.getProductOptionId());
					MessageUtil.addMessage(super.getServletRequest(), LabelUtil
							.getInstance().getText(
									"message.confirmation.success"));
					return "associate-success";

				} else {
					cservice.saveOrUpdateProductOptionValue(optionValue);
					MessageUtil.addMessage(super.getServletRequest(), LabelUtil
							.getInstance().getText(
									"message.confirmation.success"));
					return SUCCESS;
				}

			} else if (this.getAction() == 1) {// delete

				optionValue = cservice.getProductOptionValue(optionValue
						.getProductOptionValueId());

				FileModule fh = (FileModule) SpringUtil.getBean("localfile");
				if (!StringUtils.isBlank(optionValue
						.getProductOptionValueImage())) {
					//String folder = conf
					//		.getString("core.product.image.filefolder")
					String folder = FileUtil.getProductFilePath()
							+ "/" + merchantid + "/";
					fh.deleteFile(merchantid, new File(new StringBuffer()
							.append(folder).append(
									optionValue.getProductOptionValueImage())
							.toString()));
				}

				cservice.deleteProductOptionValue(optionValue);

				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));
				if (option != null && option.getProductOptionId() > 0) {
					return "associate-success";
				} else {
					return SUCCESS;
				}

			} else if (this.getAction() == 2) {// remove association

				if (option == null || option.getProductOptionId() == 0) {
					MessageUtil
							.addErrorMessage(super.getServletRequest(),
									LabelUtil.getInstance().getText(super.getLocale(),
											"errors.technical"));
					log.error("Should have received a ProductOption");
					return SUCCESS;
				}

				cservice.removeProductOptionValueToProductOption(option
						.getProductOptionId(), optionValue
						.getProductOptionValueId());
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));

				return "associate-success";

			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			return SUCCESS;
		}

	}

	public String addProductOptionValue() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		HashSet descriptionsset = new HashSet();

		if (this.getProductOptionValue() == null) {
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

				int submitedlangid = (Integer) reflanguages.get(langcount);
				String langCode = LanguageUtil
						.getLanguageStringCode(submitedlangid);

				if (StringUtils.isBlank(name)) {
					MessageUtil
							.addErrorMessage(
									super.getServletRequest(),
									LabelUtil
											.getInstance()
											.getText(
													"messages.productoptionvalue.name.required")
											+ " (" + langCode + ")");
					return SUCCESS;
				}

				ProductOptionValueDescription desc = new ProductOptionValueDescription();
				ProductOptionValueDescriptionId id = new ProductOptionValueDescriptionId();
				id.setLanguageId(submitedlangid);
				desc.setProductOptionValueName(name);
				desc.setId(id);

				descriptionsset.add(desc);

			}

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			ProductOptionValue optionValue = this.getProductOptionValue();
			optionValue.setMerchantId(merchantid);
			optionValue.setDescriptions(descriptionsset);

			if (this.getProductOption() != null
					&& this.getProductOption().getProductOptionId() > 0) {

				cservice.saveOrUpdateProductOptionValueToProductOption(
						optionValue, this.getProductOption()
								.getProductOptionId());

				if (this.getUploadimage() != null
						&& !StringUtils.isBlank(this.getUploadimageFileName())) {
					try {
						FileModule fh = (FileModule) SpringUtil
								.getBean("localfile");
						// String folder =
						// conf.getString("core.product.image.filefolder") + "/"
						// + merchantid + "/";
						String optionName = new StringBuffer().append(
								optionValue.getProductOptionValueId()).append(
								"_").append(this.getUploadimageFileName())
								.toString();
						fh.uploadFile(merchantid, "core.product.image", this
								.getUploadimage(), optionName,
								this.uploadimagecontenttype);
						optionValue.setProductOptionValueImage(optionName);
						cservice.saveOrUpdateProductOptionValue(optionValue);
					} catch (FileException e) {
						displayProductOptionsValues();
						if (e instanceof FileException) {
							this.addActionError(getText(e.getMessage()));
							return INPUT;
						} else {
							log.error(e);
							this
									.addActionError(getText("error.message.imagesnotuploaded"));
							return INPUT;
						}
					}
				}

				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));
				return "associate-success";

			} else {

				cservice.saveOrUpdateProductOptionValue(optionValue);
				MessageUtil.addMessage(super.getServletRequest(), LabelUtil
						.getInstance().getText("message.confirmation.success"));

				if (this.getUploadimage() != null
						&& !StringUtils.isBlank(this.getUploadimageFileName())) {
					try {
						FileModule fh = (FileModule) SpringUtil
								.getBean("localfile");

						String optionName = new StringBuffer().append(
								optionValue.getProductOptionValueId()).append(
								"_").append(this.getUploadimageFileName())
								.toString();
						fh.uploadFile(merchantid, "core.product.image", this
								.getUploadimage(), optionName,
								this.uploadimagecontenttype);
						optionValue.setProductOptionValueImage(optionName);
						cservice.saveOrUpdateProductOptionValue(optionValue);
					} catch (FileException e) {
						displayProductOptionsValues();
						if (e instanceof FileException) {
							this.addActionError(getText(e.getMessage()));
							return INPUT;
						} else {
							log.error(e);
							this
									.addActionError(getText("error.message.imagesnotuploaded"));
							return INPUT;
						}
					}
				}

				return SUCCESS;
			}

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			return SUCCESS;
		}

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

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public ProductOption getProductOption() {
		return productOption;
	}

	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
	}

	public ProductOptionValue getProductOptionValue() {
		return productOptionValue;
	}

	public void setProductOptionValue(ProductOptionValue productOptionValue) {
		this.productOptionValue = productOptionValue;
	}

	public Collection getOptionList() {
		return optionList;
	}

	public void setOptionList(Collection optionList) {
		this.optionList = optionList;
	}

	public Long getProductOptionValueId() {
		return productOptionValueId;
	}

	public void setProductOptionValueId(Long productOptionValueId) {
		this.productOptionValueId = productOptionValueId;
	}

	public ProductOptionDisplay getProductOptionDisplay() {
		return productOptionDisplay;
	}

	public void setProductOptionDisplay(
			ProductOptionDisplay productOptionDisplay) {
		this.productOptionDisplay = productOptionDisplay;
	}

	public File getUploadimage() {
		return uploadimage;
	}

	public void setUploadimage(File uploadimage) {
		this.uploadimage = uploadimage;
	}

	public String getUploadimageFileName() {
		return uploadimagefilename;
	}

	public void setUploadimageFileName(String uploadimagefilename) {
		this.uploadimagefilename = uploadimagefilename;
	}

	public String getUploadimageContentType() {
		return uploadimagecontenttype;
	}

	public void setUploadimageContentType(String uploadimagecontenttype) {
		this.uploadimagecontenttype = uploadimagecontenttype;
	}

}

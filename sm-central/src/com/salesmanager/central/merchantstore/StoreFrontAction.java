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
package com.salesmanager.central.merchantstore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.util.FileException;
import com.salesmanager.central.web.DynamicImage;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.entity.reference.ModuleConfiguration;
import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.SpringUtil;

public class StoreFrontAction extends ContentAction {

	private Logger log = Logger.getLogger(StoreFrontAction.class);
	private Collection templates = new ArrayList();// store front templates

	private CoreModuleService currrentTempate = null;

	private List<String> storeDescription = new ArrayList<String>();// store
																	// text
																	// submited
	private List<String> storeFrontPageTitle = new ArrayList<String>();// text submited

	private List<String> metaKeywords = new ArrayList<String>();// text submited
	private List<String> metaDescription = new ArrayList<String>();// text
																	// submited

	
	
	ModuleConfiguration sliderConf = null;
	




	private void prepareContent() throws Exception {

		super.setPageTitle("label.storesetup");
		
		Context ctx = super.getContext();

		String countryCode = CountryUtil.getCountryIsoCodeById(ctx
				.getCountryid());

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		templates = rservice.getCoreModules(
				CatalogConstants.STORE_FRONT_TEMPLATES_CODE, countryCode);

		// overwrite name
/*		if (templates != null && templates.size() > 0) {
			Iterator i = templates.iterator();
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				try {
					String title = LabelUtil.getInstance().getText(
							ctx.getLang(),
							"module." + cms.getCoreModuleName() + ".title");
					cms.setCoreModuleServiceDescription(title);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}*/

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		// get current template
		MerchantStore store = mservice.getMerchantStore(ctx.getMerchantid());

		String templateModule = store.getTemplateModule();
		// selected
		if (!StringUtils.isBlank(templateModule)) {

			currrentTempate = rservice.getCoreModuleService(ctx.getLang(),
					templateModule);
			if (currrentTempate != null) {
				currrentTempate.setCoreModuleServiceDescription(templateModule);
			}

		}
		
		//get module configuration slider for current template module
		sliderConf = rservice.getModuleConfiguration(store.getTemplateModule(), ConfigurationConstants.SLIDER_CONFIGURATION_KEY, Constants.ALLCOUNTRY_ISOCODE);

		Collection<DynamicLabel> dynamicLabels = rservice
				.getDynamicLabels(super.getContext().getMerchantid().intValue());

		if (dynamicLabels != null && dynamicLabels.size() > 0) {

			Iterator i = dynamicLabels.iterator();

			while (i.hasNext()) {

				DynamicLabel dl = (DynamicLabel) i.next();

				Set dynamicLabelSet = dl.getDescriptions();
				Iterator labelSetIterator = dynamicLabelSet.iterator();

				if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_DESCRIPTION) {

					Map labelMap = new HashMap();

					while (labelSetIterator.hasNext()) {
						DynamicLabelDescription description = (DynamicLabelDescription) labelSetIterator
								.next();
						labelMap.put(description.getId().getLanguageId(),
								description);
					}

					for (int icount = 0; icount < reflanguages.size(); icount++) {
						int langid = (Integer) reflanguages.get(icount);
						DynamicLabelDescription desc = (DynamicLabelDescription) labelMap
								.get(langid);
						if (desc != null) {
							storeDescription.add(desc
									.getDynamicLabelDescription());
						}
					}
				}

				else if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_PAGE_TITLE) {

					Map labelMap = new HashMap();

					while (labelSetIterator.hasNext()) {
						DynamicLabelDescription description = (DynamicLabelDescription) labelSetIterator
								.next();
						labelMap.put(description.getId().getLanguageId(),
								description);
					}

					for (int icount = 0; icount < reflanguages.size(); icount++) {
						int langid = (Integer) reflanguages.get(icount);
						DynamicLabelDescription desc = (DynamicLabelDescription) labelMap
								.get(langid);
						if (desc != null) {
							storeFrontPageTitle.add(desc.getDynamicLabelDescription());
						}
					}
				}
				if (dl.getSectionId() == LabelConstants.SLIDER_SECTION) {
					if(sliderConf!=null) {
						if(pages==null) {
							pages = new ArrayList();
						}
						pages.add(dl);
					}
				}

				if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_KEYWORDS) {

					Map labelMap = new HashMap();

					while (labelSetIterator.hasNext()) {
						DynamicLabelDescription description = (DynamicLabelDescription) labelSetIterator
								.next();
						labelMap.put(description.getId().getLanguageId(),
								description);
					}

					for (int icount = 0; icount < reflanguages.size(); icount++) {
						int langid = (Integer) reflanguages.get(icount);
						DynamicLabelDescription desc = (DynamicLabelDescription) labelMap
								.get(langid);
						if (desc != null) {
							metaKeywords.add(desc.getDynamicLabelDescription());
						}
					}

				}

				if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_DESCRIPTION) {

					Map labelMap = new HashMap();

					while (labelSetIterator.hasNext()) {
						DynamicLabelDescription description = (DynamicLabelDescription) labelSetIterator
								.next();
						labelMap.put(description.getId().getLanguageId(),
								description);
					}

					for (int icount = 0; icount < reflanguages.size(); icount++) {
						int langid = (Integer) reflanguages.get(icount);
						DynamicLabelDescription desc = (DynamicLabelDescription) labelMap
								.get(langid);
						if (desc != null) {
							metaDescription.add(desc
									.getDynamicLabelDescription());
						}
					}
				}
			}
		}
	}

	/**
	 * Displays the page allowing basic store front configuration
	 * 
	 * @return
	 */
	public String displayStoreFrontConfig() {

		try {

			Context ctx = super.getContext();

			prepareLanguages();

			prepareContent();


		} catch (Exception e) {
			log.error(e);
		}
		return SUCCESS;

	}

	public String editStoreFontConfig() {

		try {

			prepareLanguages();

			prepareContent();

			Context ctx = super.getContext();

			if (this.reflanguages.size() == 0) {
				log.error("Languages were not loaded");
				super.setTechnicalMessage();
				return INPUT;
			}

			// retreive current values
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Collection<DynamicLabel> dynamicLabels = rservice
					.getDynamicLabels(super.getContext().getMerchantid()
							.intValue());

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			Map submited = new HashMap();

			if (this.getStoreDescription().size() > 0) {
				submited.put(LabelConstants.STORE_FRONT_LANDING_DESCRIPTION,
						this.getStoreDescription());
			}

			if (this.getStoreFrontPageTitle().size() > 0) {
				submited.put(LabelConstants.STORE_FRONT_LANDING_PAGE_TITLE,
						this.getStoreFrontPageTitle());
			}

			if (this.getMetaKeywords().size() > 0) {
				submited.put(LabelConstants.STORE_FRONT_LANDING_META_KEYWORDS,
						this.getMetaKeywords());
			}

			if (this.getMetaDescription().size() > 0) {
				submited.put(
						LabelConstants.STORE_FRONT_LANDING_META_DESCRIPTION,
						this.getMetaDescription());
			}

			if (dynamicLabels != null && dynamicLabels.size() > 0) {

				Collection removable = new ArrayList();

				Iterator i = dynamicLabels.iterator();

				while (i.hasNext()) {

					DynamicLabel dl = (DynamicLabel) i.next();
					if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_DESCRIPTION) {
						removable.add(dl);
					}

					else if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_PAGE_TITLE) {
						removable.add(dl);
					}

					if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_KEYWORDS) {
						removable.add(dl);
					}

					if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_DESCRIPTION) {
						removable.add(dl);
					}

				}

				rservice.deleteAllDynamicLabel(removable);
			}

			Map newLabels = new HashMap();

			Map elements = new HashMap();

			Iterator submitedIterator = submited.keySet().iterator();
			while (submitedIterator.hasNext()) {
				int section = (Integer) submitedIterator.next();

				List valuesSubmited = (List) submited.get(section);

				Iterator valuesSubmitedIterator = valuesSubmited.iterator();

				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();

					String desc = (String) valuesSubmited.get(langcount);

					// if not blank
					if (!StringUtils.isBlank(desc)) {

						DynamicLabel label = null;

						int submitedlangid = (Integer) reflanguages
								.get(langcount);

						if (!newLabels.containsKey(section)) {
							label = new DynamicLabel();
							newLabels.put(section, label);
						} else {
							label = (DynamicLabel) newLabels.get(section);
						}
						// create
						DynamicLabelDescriptionId id = new DynamicLabelDescriptionId();
						id.setLanguageId(submitedlangid);

						DynamicLabelDescription description = new DynamicLabelDescription();
						description.setId(id);
						description.setDynamicLabelDescription(desc);
						description.setDynamicLabelTitle(" ");

						Set descs = label.getDescriptions();
						if (descs == null) {
							descs = new HashSet();
						}

						descs.add(description);

						label.setMerchantId(ctx.getMerchantid());
						label.setSectionId(section);
						label.setDescriptions(descs);

					}

				}

			}


			rservice.saveDynamicLabel(newLabels.values());
			super.setSuccessMessage();

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

	}
	
	public String updateSlideList() {
		
		
		
		try {
		
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			// get all slides
			Collection<DynamicLabel> labels = rservice.getDynamicLabels(super
					.getContext().getMerchantid(),LabelConstants.SLIDER_SECTION, super
					.getLocale());
			
			labels = super.updatePageList(labels);
			
			
			if(labels!=null) {
				rservice.saveDynamicLabel(labels);
				super.setSuccessMessage();
			}
			

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();

		}

		displayStoreFrontConfig();
		return SUCCESS;
	}
	
	public String viewSlide() {
		
		super.setPageTitle("label.storefront.slides.title");
		
		super.getPageDetails();
		
		DynamicLabel l = super.getLabel();
		
		if(l!=null) {
			//get image
			if (!StringUtils.isBlank(l.getImage())) {
				// set image info in the request
				DynamicImage img = new DynamicImage();
				img.setEntityId(String.valueOf(l.getDynamicLabelId()));
				img.setImageName(l.getImage());

				String imgPath = FileUtil.getFileTreeBinPathForImages(super.getContext().getMerchantid());
				img.setImagePath(imgPath);
				super.getServletRequest().setAttribute("SLIDE", img);
			}
		}	
		return SUCCESS;
	}
	
	public String deleteSlide() {
		
		try {
			ReferenceService rservice = (ReferenceService) ServiceFactory
			.getService(ServiceFactory.ReferenceService);
			DynamicLabel l = rservice.getDynamicLabel(label.getDynamicLabelId());
			rservice.deleteDynamicLabel(l);
			
			//delete image
			
			if(!StringUtils.isBlank(l.getImage())) {
			
				String imgfolder = FileUtil.getFileTreeBinPathForImages(super.getContext().getMerchantid());
				FileModule futil = (FileModule) SpringUtil.getBean("localfile");
				futil.deleteFile(super.getContext().getMerchantid(), new File(imgfolder + "/" + l.getImage()));
				
			}
			
			super.setSuccessMessage();
			
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}
		
		
		return SUCCESS;
	}
	

	
	public String editSlide() throws Exception {
		
		super.setPageTitle("label.storefront.slides.title");
		try {
			
			
			super.prepareLanguages();
			DynamicImage img = null;
			
			if(label==null) {
				return SUCCESS;
			}
			
			boolean hasError = super.populateLabel();
			
			if(hasError) {
				return INPUT;
			}
			
			if (!StringUtils.isBlank(super.getUploadImageFileName())) {
			
				FileModule futil = (FileModule) SpringUtil
				.getBean("localfile");
				
				String finalfilename = futil.uploadFile(
				super.getContext().getMerchantid(), "core.bin.images", super.getUploadImage(), super.getUploadImageFileName(), super.getUploadImageContentType());
				
				
				super.getLabel().setImage(super.getUploadImageFileName());
				

	

			
			}
			
			if(!StringUtils.isBlank(super.getLabel().getImage())) {
				
				
				// set image info in the request
				img = new DynamicImage();
				img.setImageName(super.getLabel().getImage());

				String imgPath = FileUtil.getFileTreeBinPathForImages(super.getContext().getMerchantid());
				img.setImagePath(imgPath);
				super.getServletRequest().setAttribute("SLIDE", img);
				
			}
			
			ReferenceService rservice = (ReferenceService) ServiceFactory
			.getService(ServiceFactory.ReferenceService);
			rservice.saveOrUpdateDynamicLabel(super.getLabel());
			
			if(img!=null) {
				img.setEntityId(String.valueOf(super.getLabel().getDynamicLabelId()));
			}

			super.setSuccessMessage();
			
		} catch (Exception e) {
			
			if(e instanceof FileException) {
				
				super.setMessage("errors.filetoolarge");
				
			}
			
			throw(e);
		}
		

		
		return SUCCESS;
	}
	
	public String deleteFile() throws Exception {
		
		super.setPageTitle("label.storefront.slides.title");
		
		super.prepareLanguages();
		
		ReferenceService rservice = (ReferenceService) ServiceFactory
		.getService(ServiceFactory.ReferenceService);
		
		label = rservice.getDynamicLabel(super.getLabel().getDynamicLabelId());
		
		if(label==null) {
			throw new Exception ("label.dynamicLabelId is null");
		}
		
		if(label!=null && label.getMerchantId()!=super.getContext().getMerchantid()) {
			return "unauthorized";
		}
		
		FileModule futil = (FileModule) SpringUtil
		.getBean("localfile");
		
		String imgPath = FileUtil.getFileTreeBinPathForImages(super.getContext().getMerchantid());
		
		futil.deleteFile(super.getContext().getMerchantid(), new File(new StringBuffer()
		.append(imgPath).append(label.getImage()).toString()));
		
		label.setImage(null);
		
		rservice.saveOrUpdateDynamicLabel(label);
		
		super.setSuccessMessage();
		
		return SUCCESS;
	}

	public Collection getTemplates() {
		return templates;
	}

	public void setTemplates(Collection templates) {
		this.templates = templates;
	}

	public CoreModuleService getCurrrentTempate() {
		return currrentTempate;
	}

	public void setCurrrentTempate(CoreModuleService currrentTempate) {
		this.currrentTempate = currrentTempate;
	}

	public List<String> getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(List<String> metaDescription) {
		this.metaDescription = metaDescription;
	}

	public List<String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public List<String> getStoreFrontPageTitle() {
		return storeFrontPageTitle;
	}

	public void setStoreFrontPageTitle(List<String> pageTitle) {
		this.storeFrontPageTitle = pageTitle;
	}

	public List<String> getStoreDescription() {
		return storeDescription;
	}

	public void setStoreDescription(List<String> storeDescription) {
		this.storeDescription = storeDescription;
	}
	


	public ModuleConfiguration getSliderConf() {
		return sliderConf;
	}
	
	public String[] getSlideList() {
		return visible;
	}

}

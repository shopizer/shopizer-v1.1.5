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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.PrincipalProxy;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.catalog.EditProductAction;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.PropertiesUtil;


/**
 * Common content attributes
 * @author Carl Samson
 *
 */
public abstract class ContentAction extends BaseAction {
	
	
	/**
	 * 
	 */
	

	private static final long serialVersionUID = -2458466574834932255L;
	protected List<String> titles = new ArrayList<String>();
	protected List<String> descriptions = new ArrayList<String>();
	
	// image upload
	private String uploadImageFileName;
	private String uploadImageContentType;
	private File uploadImage;
	private static long maximagesize;
	private static long maxfilesize;
	private static Map imgctypes = new HashMap();
	
	private static Logger log = Logger.getLogger(ContentAction.class);
	
	String[] visible;// selected page content
	
	
	protected List<String> sefurl = new ArrayList<String>();
	//protected String title;//unique identifier
	
	protected DynamicLabel label = null;

	protected Collection<DynamicLabel> pages = null;
	
	private static Configuration conf = PropertiesUtil.getConfiguration();
	
	static {

		String smaxfsize = conf.getString("core.product.image.maxfilesize");
		if (smaxfsize == null) {
			log
					.error("Properties core.product.image.maxfilesize not defined in config.properties");
			smaxfsize = "100000";
		}
		long maxsize = 0;
		try {
			maxsize = Long.parseLong(smaxfsize);

		} catch (Exception e) {
			log
					.error("Properties core.product.image.maxfilesize not an integer");
			maxsize = 100000;
		}

		maximagesize = maxsize;

		smaxfsize = conf.getString("core.product.file.maxfilesize");
		if (smaxfsize == null) {
			log
					.error("Properties core.product.file.maxfilesize not defined in config.properties");
			smaxfsize = "8000000";
		}
		try {
			maxsize = Long.parseLong(smaxfsize);

		} catch (Exception e) {
			log
					.error("Properties core.product.file.maxfilesize not an integer");
			maxsize = 100000;
		}

		String ctlist = conf.getString("core.product.image.contenttypes");

		if (ctlist == null) {
			log.error("No content types defined for images");
		} else {

			StringTokenizer st = new StringTokenizer(ctlist, ";");
			while (st.hasMoreTokens()) {
				String ct = (String) st.nextToken();
				imgctypes.put(ct, ct);
			}
		}
		maxfilesize = maxsize;
	}
	
	public boolean populateLabel() {
		
		
		
		boolean hasError = false;
		
		
		if(label!=null) {
		
			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();
	
				String description = (String) this.getDescriptions().get(
						langcount);
				String title = "";
				if(this.getTitles()!=null && this.getTitles().size()>0) {
				      title = (String)this.getTitles().get(
						langcount);
				}
	
	
				int submitedlangid = (Integer) reflanguages.get(langcount);
				// create
				DynamicLabelDescriptionId id = new DynamicLabelDescriptionId();
				id.setLanguageId(submitedlangid);
				if (label != null) {
					id.setDynamicLabelId(label.getDynamicLabelId());
				}
	
				DynamicLabelDescription dldescription = new DynamicLabelDescription();
				dldescription.setId(id);
				dldescription.setDynamicLabelDescription(description);
				
				dldescription.setDynamicLabelTitle("--");
				if(!StringUtils.isBlank(title)) {
					dldescription.setDynamicLabelTitle(title);
				}
	
	
				Set descs = label.getDescriptions();
				if (descs == null) {
					descs = new HashSet();
				}
	
				descs.add(dldescription);
	
				label.setMerchantId(super.getContext().getMerchantid());
				label.setDescriptions(descs);
	
			}
			
			
			// image upload validation
			if (!StringUtils.isBlank(this.getUploadImageContentType())
					&& !StringUtils.isBlank(this.getUploadImageFileName())) {
				String ct = this.getUploadImageContentType();
	
				if (!imgctypes.containsKey(ct)) {
					super
							.addFieldError(
									"uploadimage",
									getText("error.message.product.image.invalidfiletype")
											+ " "
											+ getText("label.product.uploadimage"));
					hasError = true;
				}
			}
	
			if (this.getUploadImage() != null
					&& !StringUtils.isBlank(this.getUploadImageFileName())) {
				java.io.File f = this.getUploadImage();
	
				if (f.length() > this.maximagesize) {
	
					super.addFieldError("uploadimage",
							getText("error.message.product.image.file") + " "
									+ getText("label.product.uploadimage"));
					hasError = true;
	
				}
			}
		}
		return hasError;
		
	}
	
	
	

	public String[] getVisible() {
		return visible;
	}

	public void setVisible(String[] visible) {
		this.visible = visible;
	}

	
	
	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public List<String> getSefurl() {
		return sefurl;
	}

	public void setSefurl(List<String> sefurl) {
		this.sefurl = sefurl;
	}



	public DynamicLabel getLabel() {
		return label;
	}

	public void setLabel(DynamicLabel label) {
		this.label = label;
	}

	public Collection<DynamicLabel> getPages() {
		return pages;
	}

	public void setPages(Collection<DynamicLabel> pages) {
		this.pages = pages;
	}

	
	protected Collection<DynamicLabel> updatePageList(Collection<DynamicLabel> pages) {
		
		
		if (pages != null && pages.size()>0) {
			
			for (Object o : pages) {
		
				DynamicLabel dl = (DynamicLabel) o;
		
				String[] labelIds = this.getVisible();
		
				if (labelIds != null && labelIds.length > 0) {
		
					boolean found = false;
					for (int i = 0; i < labelIds.length; i++) {
						String sId = labelIds[i];
						try {
							long id = Long.parseLong(sId);
							if (dl.getDynamicLabelId() == id) {
								found = true;
							}
		
						} catch (Exception e) {
							log.error("Wrong id " + sId);
							if (sId.equals("false")) {
								dl.setVisible(false);
							} else {
								dl.setVisible(true);
							}
						}
		
					}
					if (found == true) {
						dl.setVisible(true);
					} else {
						dl.setVisible(false);
					}
		
				} else {
					dl.setVisible(false);
				}
			}
		
			return pages;
		
		}
		
		return null;

		
	}
	

	protected void getPageDetails() {

		try {

				
				super.prepareLanguages();
				ReferenceService rservice = (ReferenceService) ServiceFactory
						.getService(ServiceFactory.ReferenceService);
		
				if (label != null) {
		
					// get label
		
					label = rservice.getDynamicLabel(label.getDynamicLabelId());
		
					Set descriptionsSet = label.getDescriptions();
		
					Map descriptionsMap = new HashMap();
		
					if (descriptionsSet != null) {
		
						for (Object desc : descriptionsSet) {
							DynamicLabelDescription description = (DynamicLabelDescription) desc;
							descriptionsMap.put(
									description.getId().getLanguageId(),
									description);
						}
		
						// iterate through languages for appropriate order
						for (int count = 0; count < reflanguages.size(); count++) {
							int langid = (Integer) reflanguages.get(count);
							DynamicLabelDescription description = (DynamicLabelDescription) descriptionsMap
									.get(langid);
							if (description != null) {
								descriptions.add(description
										.getDynamicLabelDescription());
							}
						}
					}
				}
				
				

				


	} catch (Exception e) {
		log.error(e);
		super.setTechnicalMessage();

	}

		
	}




	public String getUploadImageFileName() {
		return uploadImageFileName;
	}




	public void setUploadImageFileName(String uploadImageFileName) {
		this.uploadImageFileName = uploadImageFileName;
	}




	public String getUploadImageContentType() {
		return uploadImageContentType;
	}




	public void setUploadImageContentType(String uploadImageContentType) {
		this.uploadImageContentType = uploadImageContentType;
	}




	public File getUploadImage() {
		return uploadImage;
	}




	public void setUploadImage(File uploadImage) {
		this.uploadImage = uploadImage;
	}







}

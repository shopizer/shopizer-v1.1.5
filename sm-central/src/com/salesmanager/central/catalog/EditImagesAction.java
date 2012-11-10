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
package com.salesmanager.central.catalog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.web.DynamicImage;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.ProductImageUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;

/**
 * Upload and manage product images
 * @author Carl Samson
 *
 */
public class EditImagesAction extends BaseAction {
	
	private File[] upload;
    private String[] uploadFileName;
    private String[] uploadContentType;

    
    private static Logger log = Logger.getLogger(EditImagesAction.class);
    
	// image validation
	private static long maximagesize;
	private static long maxfilesize;
	private static Map imgctypes = new HashMap();
	private static Configuration conf = PropertiesUtil.getConfiguration();
	
    private Product product;
    
    //id of image to be deleted
    private String imageId = null;
    
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
    
    

    public String displayImages() throws Exception {
    	
    	super.setPageTitle("label.product.images");
    	
    	//get the product
		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		
		Product p = cservice.getProduct(this.getProduct().getProductId());
		if(p==null || p.getMerchantId()!=super.getContext().getMerchantid()) {
			super.setAuthorizationMessage();
			displayImages();
			return INPUT;
		}
		
		p.setLocale(super.getLocale());
    	
		this.setProduct(p);
		
    	//do this for 4 images
    	if (!StringUtils.isBlank(product.getProductImage1())) {
    		
    		
			DynamicImage img = new DynamicImage();
			img
					.setImageName(new StringBuffer()
							.append(conf
									.getString("core.product.image.large.prefix"))
									.append("-").append(
									this.getProduct().getProductImage1())
							.toString());

			img.setImagePath(FileUtil.getProductFilePath()
					+ "/" + super.getContext().getMerchantid() + "/");
			
			
			super.getServletRequest().setAttribute("DYNIMG1", img);
			super.getServletRequest().setAttribute("uploadimagename1",
					product.getProductImage1());
			
    	}
    	
    	if (!StringUtils.isBlank(product.getProductImage2())) {
    		
    		
			DynamicImage img = new DynamicImage();
			img
					.setImageName(new StringBuffer()
							.append(conf
									.getString("core.product.image.large.prefix"))
									.append("-").append(
									this.getProduct().getProductImage2())
							.toString());

			img.setImagePath(FileUtil.getProductFilePath()
					+ "/" + super.getContext().getMerchantid() + "/");
			
			
			super.getServletRequest().setAttribute("DYNIMG2", img);
			super.getServletRequest().setAttribute("uploadimagename2",
					product.getProductImage2());
			
    	}
    	
    	if (!StringUtils.isBlank(product.getProductImage3())) {
    		
    		
			DynamicImage img = new DynamicImage();
			img
					.setImageName(new StringBuffer()
							.append(conf
									.getString("core.product.image.large.prefix"))
									.append("-").append(
									this.getProduct().getProductImage3())
							.toString());

			img.setImagePath(FileUtil.getProductFilePath()
					+ "/" + super.getContext().getMerchantid() + "/");
			
			
			super.getServletRequest().setAttribute("DYNIMG3", img);
			super.getServletRequest().setAttribute("uploadimagename3",
					product.getProductImage3());
			
    	}
    	
    	if (!StringUtils.isBlank(product.getProductImage4())) {
    		
    		
			DynamicImage img = new DynamicImage();
			img
					.setImageName(new StringBuffer()
							.append(conf
									.getString("core.product.image.large.prefix"))
									.append("-").append(
									this.getProduct().getProductImage4())
							.toString());

			img.setImagePath(FileUtil.getProductFilePath()
					+ "/" + super.getContext().getMerchantid() + "/");
			
			
			super.getServletRequest().setAttribute("DYNIMG4", img);
			super.getServletRequest().setAttribute("uploadimagename4",
					product.getProductImage4());
			
    	}
    	
    	super.getServletRequest().setAttribute("imagewidth","200");
    	super.getServletRequest().setAttribute("imageheight","200");
    	super.getServletRequest().setAttribute("product.productId",String.valueOf(p.getProductId()));
    	
    	return SUCCESS;
    	
    }
    
	
	public String saveImages() throws Exception {
		
		super.setPageTitle("label.product.images");
		
		displayImages();
		
		//get product first
		CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
		
		Product p = cservice.getProduct(this.getProduct().getProductId());
		if(p==null || p.getMerchantId()!=super.getContext().getMerchantid()) {
			super.setAuthorizationMessage();
			return INPUT;
		}
		
		this.getProduct().setMerchantId(p.getMerchantId());
		
		p.setLocale(super.getLocale());
		
		
		
		
		if(upload==null || upload.length==0) {
			return INPUT;
		}
		
		boolean hasError = false;
		

		
		//validate images
		if (this.getUploadContentType()!=null && this.getUploadContentType().length>0) {
			
			for (String c: uploadContentType) {
				if (!imgctypes.containsKey(c)) {
					super
							.addActionError(
									getText("error.message.product.image.invalidfiletype")
											+ " "
											+ getText("label.product.uploadimage"));
					hasError = true;
				}
	        }
		}

		if (this.getUpload()!=null && this.getUpload().length>0) {
			
			
			for (File u: upload) {

				if (u.length() > this.maximagesize) {

					super.addActionError(
							getText("error.message.product.image.file") + " "
									+ getText("label.product.uploadimage"));
					hasError = true;
				}
	        }
		}
		
		if(hasError) {
			return INPUT;
		}
		
		//upload pictures
		

		
		
		ReferenceService rservice = (ReferenceService) ServiceFactory
		.getService(ServiceFactory.ReferenceService);
		MerchantService service = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		MerchantStore mStore = service.getMerchantStore(super.getContext()
		.getMerchantid());
		Map<String, String> moduleConfigMap = rservice
		.getModuleConfigurationsKeyValue(mStore
				.getTemplateModule(), mStore.getCountry());


		for (int i = 0; i< this.getUpload().length;i++) {
			
			File f = this
			.getUpload()[i];
			
			ProductImageUtil imageSpecifications = new ProductImageUtil();
			imageSpecifications.uploadProductImages(
			f, this.getUploadFileName()[i],
			this.getUploadContentType()[i],
			this.getProduct(), moduleConfigMap);
			
			
		}
		
		for(int i=0;i<uploadFileName.length;i++) {
			
			if(i==0) {
				if(StringUtils.isBlank(p.getProductImage1())) {
					p.setProductImage1(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage2())) {
					p.setProductImage2(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage3())) {
					p.setProductImage3(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage4())) {
					p.setProductImage4(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
			}
			
			if(i==1) {
				if(StringUtils.isBlank(p.getProductImage1())) {
					p.setProductImage1(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage2())) {
					p.setProductImage2(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage3())) {
					p.setProductImage3(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage4())) {
					p.setProductImage4(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
			}
			
			if(i==2) {
				if(StringUtils.isBlank(p.getProductImage1())) {
					p.setProductImage1(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage2())) {
					p.setProductImage2(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage3())) {
					p.setProductImage3(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage4())) {
					p.setProductImage4(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
			}
			
			if(i==3) {
				if(StringUtils.isBlank(p.getProductImage1())) {
					p.setProductImage1(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage2())) {
					p.setProductImage2(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage3())) {
					p.setProductImage3(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
				
				if(StringUtils.isBlank(p.getProductImage4())) {
					p.setProductImage4(new StringBuffer().append(p.getProductId()).append("-").append(
							uploadFileName[i]).toString());
					continue;
				}
			}
			
			
		}
		
		
		cservice.saveOrUpdateProduct(p);
		
		super.setSuccessMessage();
		
		
		
		return SUCCESS;
		
		
	}
	
	public String deleteImage() throws Exception {
		


			// get the product firts
			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			this.displayImages();

			CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
			
			Product p = cservice.getProduct(this.getProduct().getProductId());
			if(p==null || p.getMerchantId()!=super.getContext().getMerchantid()) {
				super.setAuthorizationMessage();
				return INPUT;
			}
			
			if(StringUtils.isBlank(this.getImageId())) {
				log.error("Image id is null");
				super.setTechnicalMessage();
				return INPUT;
			}

			String folder = null;
			String imagename = null;
			
			int iId = 0;
			
			try {
				iId = Integer.parseInt(this.getImageId());
			} catch (Exception e) {
				log.error("Image id is not numeric " + this.getImageId());
				super.setTechnicalMessage();
				return INPUT;
			}
			
			String imageName = null;
			
			switch (iId) {
			
				case 1:
					imageName = p.getProductImage1();
					p.setProductImage1(null);
					
					break;
			
				case 2:
					imageName = p.getProductImage2();
					p.setProductImage2(null);
					
					break;
			
				case 3:
					imageName = p.getProductImage3();
					p.setProductImage3(null);
					
					break;
			
				case 4:
					imageName = p.getProductImage4();
					p.setProductImage4(null);
					
					break;
					
				
			
			
			}

			if(imageName==null) {
				log.error("Invalid image id " + this.getImageId());
				super.setTechnicalMessage();
				return INPUT;
			}

			ProductImageUtil imutil = new ProductImageUtil();



			folder = FileUtil.getProductFilePath()
					+ "/"
					+ ctx.getMerchantid() + "/";


			cservice.saveOrUpdateProduct(p);

				FileModule fh = (FileModule) SpringUtil.getBean("localfile");
				fh.deleteFile(ctx.getMerchantid(), new File(new StringBuffer()
						.append(folder).append(imageName).toString()));// delete
																		// regular
				fh
						.deleteFile(
								ctx.getMerchantid(),
								new File(
										new StringBuffer()
												.append(folder)
												.append(
														conf
																.getString("core.product.image.small.prefix"))
												.append("-").append(imageName)
												.toString()));
				fh
						.deleteFile(
								ctx.getMerchantid(),
								new File(
										new StringBuffer()
												.append(folder)
												.append(
														conf
																.getString("core.product.image.large.prefix"))
												.append("-").append(imageName)
												.toString()));



			super.setSuccessMessage();

			return SUCCESS;


		
		
	}
	
	

	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}


	public File[] getUpload() {
		return upload;
	}


	public void setUpload(File[] upload) {
		this.upload = upload;
	}


	public String[] getUploadFileName() {
		return uploadFileName;
	}


	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}


	public String[] getUploadContentType() {
		return uploadContentType;
	}


	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}


	public String getImageId() {
		return imageId;
	}


	public void setImageId(String imageId) {
		this.imageId = imageId;
	}


}

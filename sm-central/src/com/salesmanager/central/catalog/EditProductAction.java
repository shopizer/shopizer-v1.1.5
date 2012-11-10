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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.web.DynamicImage;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductDescriptionId;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.tax.TaxClass;
import com.salesmanager.core.module.impl.application.files.FileException;
import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.ProductImageUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;

/**
 * Manage the creation and edition of a product
 * 
 * @author Carl Samson
 * 
 */
public class EditProductAction extends BaseAction implements Preparable {

	private static Logger log = Logger.getLogger(EditProductAction.class);
	private long productid;

	// v2
	private Collection<Language> languages;// used in the page as an index
	private Map<Integer, Integer> reflanguages = new HashMap();// reference
																// count -
																// languageId
	private Collection<TaxClass> taxclass;

	private String productId; // from list page
	private String categoryId; // from category page
	private Product product;
	// I18N
	private List<String> names = new ArrayList<String>();
	private List<String> descriptions = new ArrayList<String>();
	private List<String> highlights = new ArrayList<String>();
	private List<String> urls = new ArrayList<String>();
	private List<String> seo = new ArrayList<String>();
	private List<String> metadescriptions = new ArrayList<String>();
	private List<String> metakeywords = new ArrayList<String>();
	private List<String> downloadurl = new ArrayList<String>();
	private List<String> title = new ArrayList<String>();

	private String price;
	private String weight;
	private String width;
	private String length;
	private String height;

	private String categ;
	private String availabledate;
	private String dateavailable;// submited from the page

	// image upload
	private String uploadimagefilename;
	private String uploadimagecontenttype;
	private File uploadimage;

	// image validation
	private static long maximagesize;
	private static long maxfilesize;
	private static Map imgctypes = new HashMap();

	private long newProductId;// once a product is created

	// delete file
	private String filename;

	private String productImage;
	private String productImageLarge;

	// cloneflag
	private boolean productIsClone = false;

	// Image Crop Properties
	private int x1;
	private int y1;
	private int productImageWidth;
	private int productImageHeight;
	private String cropImageUrl;
	
	private String productUrl = null;
	private String productImageUrl = null;
	private String url;
	
	private MerchantStore store = null;

	private ProductImageUtil imageSpecifications;

	public ProductImageUtil getImageSpecifications() {
		return imageSpecifications;
	}

	public void setImageSpecifications(ProductImageUtil imageSpecifications) {
		this.imageSpecifications = imageSpecifications;
	}

	private static Configuration conf = PropertiesUtil.getConfiguration();

	// properties for validating the image
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

	/**
	 * Prepares the language
	 */
	public void prepare() {

		try {
			
			super.setPageTitle("label.productedit.title");

			// need to cleanup image stuff
			super.getServletRequest().removeAttribute("DYNIMG1");
			super.getServletRequest().removeAttribute("DYNIMG2");
			super.getServletRequest().getSession().removeAttribute("img1");
			super.getServletRequest().getSession().removeAttribute("img2");
			super.getServletRequest().removeAttribute("uploadimagename");
			super.getServletRequest().removeAttribute("uploadimagename2");

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			// get the taxclass
			TaxService taxservice = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);
			Collection ltaxes = new ArrayList();
			TaxClass tc = new TaxClass();
			tc.setTaxClassId(-1);
			tc.setTaxClassTitle("N/A");
			ltaxes.add(tc);
			Collection coll = taxservice.getTaxClasses(merchantid);
			ltaxes.addAll(coll);
			this.setTaxclass(ltaxes);

			store = service.getMerchantStore(merchantid);

			if (store == null) {
				// MessageUtil.addErrorMessage(super.getServletRequest(),
				// LabelUtil.getInstance().getText("errors.profile.storenotcreated"));
				addActionError(getText("errors.profile.storenotcreated"));
			}

			Map languagesMap = store.getGetSupportedLanguages();

			languages = languagesMap.values();

			int count = 0;
			Iterator langit = languagesMap.keySet().iterator();
			while (langit.hasNext()) {
				Integer langid = (Integer) langit.next();
				Language lang = (Language) languagesMap.get(langid);
				reflanguages.put(count, langid);
				count++;
			}

			super.getServletRequest().setAttribute("languages", languages);
			
			StringBuffer productUrlBuffer = new StringBuffer();
			StringBuffer urlBuffer = new StringBuffer();
			String u = store.getDomainName();
			if(StringUtils.isBlank(u)) {
				u = ReferenceUtil.getUnSecureDomain(store);
				urlBuffer.append(u);
			} else {
				urlBuffer.append("http://");
				urlBuffer.append(u);
			}
			
			url = urlBuffer.toString();
			
			productUrlBuffer.append(url);
			productUrlBuffer.append(conf.getString("core.salesmanager.catalog.url"));
			productUrlBuffer.append("/product/");
			productUrl = productUrlBuffer.toString();

		} catch (Exception e) {
			log.error(e);
		}

	}

	/**
	 * Displays product for edition creation mode
	 * 
	 * @return
	 */
	public String showProductEditMode() {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);

		if (!StringUtils.isBlank(this.getCategoryId())) {// comming from
															// category page
			super.getServletRequest().setAttribute("categoryfilter",
					this.getCategoryId());// for category drop down box
		}

		if (this.getProduct() != null && this.getProduct().getProductId() > 0) {// we
																				// are
																				// in
																				// edit
																				// mode

			long lproductid = this.getProduct().getProductId();

			Product product;
			try {
				CatalogService catalogservice = (CatalogService) ServiceFactory
						.getService(ServiceFactory.CatalogService);
				product = catalogservice.getProduct(lproductid);
			} catch (Exception e) {
				log.error(e);
				super.addActionError(getText("error.technical"));
				return INPUT;
			}

			this.setProduct(product);

			// language id-CategoryDescription
			Map idprodmap = new HashMap();

			Collection descriptionslist = product.getDescriptions();
			if (descriptionslist != null) {
				Iterator i = descriptionslist.iterator();
				while (i.hasNext()) {
					Object o = i.next();
					if (o instanceof ProductDescription) {
						ProductDescription desc = (ProductDescription) o;
						idprodmap.put(desc.getId().getLanguageId(), desc);
					}
				}
			}

			// iterate through languages for appropriate order
			for (int count = 0; count < reflanguages.size(); count++) {
				int langid = (Integer) reflanguages.get(count);
				ProductDescription desc = (ProductDescription) idprodmap
						.get(langid);
				if (desc != null) {
					names.add(desc.getProductName());
					descriptions.add(desc.getProductDescription());
					highlights.add(desc.getProductHighlight());
					urls.add(desc.getProductUrl());
					metadescriptions.add(desc.getMetatagDescription());
					seo.add(desc.getSeUrl());
					downloadurl.add(desc.getProductExternalDl());
					title.add(desc.getProductTitle());
				}
			}

			// set price, width, length, height
			this.setPrice(CurrencyUtil.displayFormatedAmountNoCurrency(product
					.getProductPrice(), ctx.getCurrency()));

			this.setWeight(CurrencyUtil.displayMeasure(product
					.getProductWeight(), ctx.getCurrency()));
			this.setWidth(CurrencyUtil.displayMeasure(
					product.getProductWidth(), ctx.getCurrency()));
			this.setLength(CurrencyUtil.displayMeasure(product
					.getProductLength(), ctx.getCurrency()));
			this.setHeight(CurrencyUtil.displayMeasure(product
					.getProductHeight(), ctx.getCurrency()));

			// set categ
			super.getServletRequest().setAttribute("categoryfilter",
					String.valueOf(product.getMasterCategoryId()));
			this.setCateg(String.valueOf(product.getMasterCategoryId()));

			// set availabledate
			this.setAvailabledate(DateUtil.formatDate(product
					.getProductDateAvailable()));

			// set product image
			Configuration conf = PropertiesUtil.getConfiguration();
			

			if (!StringUtils.isBlank(product.getProductImage())) {
				
				StringBuffer productImageUrlBuffer = new StringBuffer();
				productImageUrlBuffer.append(url);
				productImageUrlBuffer.append(conf
						.getString("core.store.mediaurl"));
				productImageUrlBuffer.append(conf
						.getString("core.products.images.uri"));
				productImageUrlBuffer.append("/" + ctx.getMerchantid() + "/");
				productImageUrlBuffer.append(product.getProductImage());
				productImageUrl = productImageUrlBuffer.toString();
				
				
				this.setProductImage(product.getProductImage());// for
																// displaying in
																// the jsp
				DynamicImage img = new DynamicImage();
				img
						.setImageName(new StringBuffer()
								.append(
										conf
												.getString("core.product.image.small.prefix"))
								.append("-").append(
										this.getProduct().getProductImage())
								.toString());

				img.setImagePath(FileUtil.getProductFilePath()
						+ "/" + ctx.getMerchantid() + "/");
				super.getServletRequest().setAttribute("DYNIMG1", img);
				super.getServletRequest().setAttribute("uploadimagename",
						product.getProductImage());
			}

		}

		return SUCCESS;
	}

	/**
	 * Will create the same product definition but will set the productid to 0
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cloneProduct() throws Exception {
		try {

			long lproductId = -1;
			if (!StringUtils.isBlank(this.getProductId())) {
				lproductId = Long.parseLong(this.getProductId());
			}
			Product clone = new Product();
			clone.setProductId(lproductId);
			this.showProductEditMode();
			clone = this.getProduct();

			// ajust seo urls
			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {

				int langcount = (Integer) i.next();
				int submitedlangid = (Integer) reflanguages.get(langcount);
				String code = LanguageUtil
						.getLanguageStringCode(submitedlangid);

				if (langcount > this.getSeo().size() - 1)
					continue;

				String seoUrl = (String) this.getSeo().get(langcount);
				seoUrl = String.valueOf(lproductId);

			}

			clone.setProductId(0);
			clone.setProductVirtual(false);
			clone.setProductDateAdded(new Date(new Date().getTime()));

			this.setProduct(clone);

			super.getServletRequest().removeAttribute("DYNIMG1");
			super.getServletRequest().removeAttribute("DYNIMG2");
			super.getServletRequest().getSession().removeAttribute("img1");
			super.getServletRequest().getSession().removeAttribute("img2");
			super.getServletRequest().removeAttribute("uploadimagename");
			super.getServletRequest().removeAttribute("uploadimagename2");

			this.setNewProductId(clone.getProductId());

			this.setProductImage("");
			this.setProductImageLarge("");

			this.setProductIsClone(true);

		} catch (Exception e) {
			log.error(e);
			this.addActionError(getText("errors.technical"));
		}
		return SUCCESS;
	}

	public String saveProduct() throws Exception {

		try {
			
			if(super.isActionError()) {
				this.product = null;
				return INPUT;
			}

			if (this.reflanguages.size() == 0) {
				addActionError(getText("errors.profile.storenotcreated"));
				return INPUT;
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			boolean hasError = false;

			if (StringUtils.isBlank(this.getAvailabledate())) {

				DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date myDate = null;
				try {
					myDate = myDateFormat.parse(this.getDateavailable());
					this.getProduct().setProductDateAvailable(myDate);
					this.setAvailabledate(DateUtil.formatDate(myDate));
				} catch (Exception e) {
					log.error(e);
				}

			}

			// category
			if (this.getCateg().equals("-1")) {
				super.addFieldError("categ",
						getText("error.message.invalidcategory"));
				hasError = true;
			}

			this.getProduct().setMasterCategoryId(
					Integer.parseInt(this.getCateg()));
			super.getServletRequest().setAttribute("categoryfilter",
					String.valueOf(product.getMasterCategoryId()));

			// price validation
			BigDecimal price = null;
			try {
				String p = String.valueOf(this.getPrice());
				price = CurrencyUtil.validateCurrency(p, ctx.getCurrency());
				this.getProduct().setProductPrice(price);

			} catch (Exception e) {
				super.addFieldError("price",
						getText("error.message.price.format"));
				hasError = true;

			}

			// weight validation
			BigDecimal weight = null;
			try {
				String w = String.valueOf(this.getWeight());
				weight = CurrencyUtil.validateMeasure(w, ctx.getCurrency());
				this.getProduct().setProductWeight(weight);

			} catch (Exception e) {
				super.addFieldError("weight",
						getText("invalid.fieldvalue.product.productWeight"));
				hasError = true;

			}

			// width validation
			BigDecimal width = null;
			try {
				String w = String.valueOf(this.getWidth());
				width = CurrencyUtil.validateMeasure(w, ctx.getCurrency());
				this.getProduct().setProductWidth(width);

			} catch (Exception e) {
				super.addFieldError("width",
						getText("invalid.fieldvalue.product.productWidth"));
				hasError = true;
			}

			// length validation
			BigDecimal length = null;
			try {
				String l = String.valueOf(this.getLength());
				length = CurrencyUtil.validateMeasure(l, ctx.getCurrency());
				this.getProduct().setProductLength(length);

			} catch (Exception e) {
				super.addFieldError("length",
						getText("invalid.fieldvalue.product.productLength"));
				hasError = true;
			}

			// height validation
			BigDecimal height = null;
			try {
				String h = String.valueOf(this.getHeight());
				height = CurrencyUtil.validateMeasure(h, ctx.getCurrency());
				this.getProduct().setProductHeight(height);

			} catch (Exception e) {
				super.addFieldError("height",
						getText("invalid.fieldvalue.product.productHeight"));
				hasError = true;
			}

			this.getProduct().setProductPrice(price);

			this.getProduct().setProductDateAvailable(
					DateUtil.getDate(this.getDateavailable()));

			// Map descriptionsmap = new HashMap();
			HashSet descriptionset = new HashSet();

			// I18N validation
			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {

				int langcount = (Integer) i.next();
				int submitedlangid = (Integer) reflanguages.get(langcount);
				String code = LanguageUtil
						.getLanguageStringCode(submitedlangid);

				// validate name
				String name = (String) this.getNames().get(langcount);
				if (StringUtils.isBlank(name)) {
					super.addFieldError("names[" + langcount + "]",
							getText("error.message.productname.required")
									+ " (" + code + ")");
					hasError = true;
				}

				// validate description
				String description = (String) this.getDescriptions().get(
						langcount);
				if (StringUtils.isBlank(description)) {
					super.addFieldError("descriptions[" + langcount + "]",
							getText("error.message.productdesc.required")
									+ " (" + code + ")");
					hasError = true;
				}

				ProductDescription productdescription = new ProductDescription();
				ProductDescriptionId id = new ProductDescriptionId();
				id.setLanguageId(submitedlangid);
				id.setProductId(this.getProduct().getProductId());
				productdescription.setId(id);
				productdescription.setProductName(name);
				productdescription.setProductDescription(description);

				String seoUrl = (String) this.getSeo().get(langcount);

				if (!StringUtils.isBlank(seoUrl)) {
					productdescription.setSeUrl(seoUrl);
				} else {
					productdescription.setSeUrl(String.valueOf(this
							.getProduct().getProductId()));
				}
				
				String dlUrl = (String) this.getDownloadurl().get(langcount);

				if (!StringUtils.isBlank(dlUrl)) {
					productdescription.setProductExternalDl(dlUrl);
				} 

				String metaDescription = (String) this.getMetadescriptions()
						.get(langcount);
				productdescription.setMetatagDescription(metaDescription);
				if (this.getHighlights().size() > 0) {

					String highlight = (String) this.getHighlights().get(
							langcount);

					if (highlight != null) {

						productdescription.setProductHighlight(highlight);
					}
				}

				if (this.getUrls().size() > 0) {

					String url = (String) this.getUrls().get(langcount);

					if (url != null) {

						productdescription.setProductUrl(url);

					}
				}
				
				if (this.getTitle().size() > 0) {

					String title = (String) this.getTitle().get(langcount);

					if (title != null) {

						productdescription.setProductTitle(title);

					}
				}

				// descriptionsmap.put(submitedlangid,productdescription);
				descriptionset.add(productdescription);

			}

			// image upload validation
			if (!StringUtils.isBlank(this.getUploadimageContentType())
					&& !StringUtils.isBlank(this.getUploadimageFileName())) {
				String ct = this.getUploadimageContentType();

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

			if (this.getUploadimage() != null
					&& !StringUtils.isBlank(this.getUploadimageFileName())) {
				java.io.File f = this.getUploadimage();

				if (f.length() > this.maximagesize) {

					super.addFieldError("uploadimage",
							getText("error.message.product.image.file") + " "
									+ getText("label.product.uploadimage"));
					hasError = true;

				}
			}


			if (hasError) {

				return INPUT;
			}


			this.getProduct().setMerchantId(ctx.getMerchantid());
			this.getProduct().setDescriptions(descriptionset);

			if (this.getProduct().getProductDateAdded() == null) {
				this.getProduct().setProductDateAdded(
						new Date(new Date().getTime()));
			}

			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			if (catalogservice.isProductVirtual(this.getProduct())) {
				this.getProduct().setProductVirtual(true);
			}

			boolean productNeedsUpdate = false;

			// image uploaded or from hidden field
			if (this.getUploadimage() != null
					&& !StringUtils.isBlank(this.getUploadimageFileName())) {// image
																				// uploaded
				productNeedsUpdate = true;
			}


			Product updated = catalogservice.saveOrUpdateProduct(this
					.getProduct());
			this.setProduct(updated);
			this.setNewProductId(updated.getProductId());

			if (this.getUploadimage() != null
					&& !StringUtils.isBlank(this.getUploadimageFileName())) {
				// upload images
				try {
					ReferenceService rservice = (ReferenceService) ServiceFactory
							.getService(ServiceFactory.ReferenceService);
					MerchantService service = (MerchantService) ServiceFactory
							.getService(ServiceFactory.MerchantService);
					MerchantStore mStore = service.getMerchantStore(ctx
							.getMerchantid());
					Map<String, String> moduleConfigMap = rservice
							.getModuleConfigurationsKeyValue(mStore
									.getTemplateModule(), mStore.getCountry());

					imageSpecifications = new ProductImageUtil();
					imageSpecifications.uploadProductImages(this
							.getUploadimage(), this.getUploadimageFileName(),
							this.getUploadimageContentType(),
							this.getProduct(), moduleConfigMap);

				} catch (FileException e) {
					if (e instanceof FileException) {
						log.error(e);
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

			if (!StringUtils.isBlank(this.getProduct().getProductImage())) {
				DynamicImage img = new DynamicImage();
				img
						.setImageName(new StringBuffer()
								.append(
										conf
												.getString("core.product.image.small.prefix"))
								.append("-").append(
										this.getProduct().getProductImage())
								.toString());
/*				img.setImagePath(conf
						.getString("core.product.image.filefolder")
						+ "/" + ctx.getMerchantid() + "/");*/
				img.setImagePath(FileUtil.getProductFilePath()
						+ "/" + ctx.getMerchantid() + "/");
				
				
				super.getServletRequest().setAttribute("DYNIMG1", img);
			}

			if (productNeedsUpdate) {
				catalogservice.saveOrUpdateProduct(this.getProduct());
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

			return SUCCESS;

		} catch (Throwable e) {
			log.error(e);
			this.addActionError(getText("errors.technical"));
			return INPUT;
		}

	}

	public String cropProductImage() {

		if (this.getProduct() == null || this.getProduct().getProductId() == 0) {
			return "productList";
		}

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore mStore = service
					.getMerchantStore(ctx.getMerchantid());
			Map<String, String> moduleConfigMap = rservice
					.getModuleConfigurationsKeyValue(
							mStore.getTemplateModule(), mStore.getCountry());

			product = catalogservice.getProduct(this.getProduct()
					.getProductId());
			//String folder = conf.getString("core.product.image.filefolder")
			String folder = FileUtil.getProductFilePath()
					+ "/" + this.getProduct().getMerchantId() + "/";
			ProductImageUtil imutil = new ProductImageUtil();
			File croppedImg = imutil.getCroppedImage(new File(folder
					+ this.getProduct().getProductImage()), x1, y1,
					productImageWidth, productImageHeight);
			imutil.uploadCropedProductImages(croppedImg, this.getProduct()
					.getProductImageLarge(), new MimetypesFileTypeMap()
					.getContentType(croppedImg), this.getProduct(),
					moduleConfigMap);

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.addActionError(getText("error.technical"));
			return SUCCESS;
		}

		return SUCCESS;

	}

	public String showCropProductImage() {
		if (this.getProduct() == null || this.getProduct().getProductId() == 0) {
			return "productList";
		}

		try {
			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore mStore = service
					.getMerchantStore(ctx.getMerchantid());
			Map<String, String> moduleConfigMap = rservice
					.getModuleConfigurationsKeyValue(
							mStore.getTemplateModule(), mStore.getCountry());

			product = catalogservice.getProduct(this.getProduct()
					.getProductId());

			imageSpecifications = new ProductImageUtil();
			imageSpecifications.initCropImage(this.getProduct(),
					moduleConfigMap);

			setCropImageUrl(FileUtil.getProductImagePath(ctx.getMerchantid(),
					this.getProduct().getProductImage()));

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return SUCCESS;
		}
		return SUCCESS;
	}

	public String deleteProduct() {

		try {

			ProductImageUtil imutil = new ProductImageUtil();
			// delete images
			this.showProductEditMode();
			if (this.getProduct() == null
					|| this.getProduct().getProductId() == 0) {
				return SUCCESS;
			}

			FileModule fh = (FileModule) SpringUtil.getBean("localfile");
			if (!StringUtils.isBlank(this.getProduct().getProductImage())) {
				//String folder = conf.getString("core.product.image.filefolder")
				String folder = FileUtil.getProductFilePath()
						+ "/" + this.getProduct().getMerchantId() + "/";
				fh.deleteFile(this.getProduct().getMerchantId(),
								new File(new StringBuffer().append(folder)
										.append(
												this.getProduct()
														.getProductImage())
										.toString()));
				fh
						.deleteFile(
								this.getProduct().getMerchantId(),
								new File(
										new StringBuffer()
												.append(folder)
												.append(
														conf
																.getString("core.product.image.small.prefix"))
												.append("-")
												.append(
														product
																.getProductImage())
												.toString()));
				fh
						.deleteFile(
								this.getProduct().getMerchantId(),
								new File(
										new StringBuffer()
												.append(folder)
												.append(
														conf
																.getString("core.product.image.large.prefix"))
												.append("-")
												.append(
														product
																.getProductImage())
												.toString()));
			}

			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			catalogservice.deleteProduct(this.getProduct());

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));
		} catch (Exception e) {
			log.error(e);
			this.addActionError(getText("errors.technical"));

		}

		return SUCCESS;

	}

	/**
	 * Delete product image & virtual product
	 * 
	 * @return
	 */
	public String deleteFile() {
		try {

			// get the product firts
			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			this.showProductEditMode();

			if (this.getProduct() == null
					|| this.getProduct().getProductId() == 0) {
				return SUCCESS;
			}

			String folder = null;
			String imagename = null;

			CatalogService catalogservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			ProductImageUtil imutil = new ProductImageUtil();

			if (this.getFilename().equals("uploadimage")) {// small image
				imagename = this.getProduct().getProductImage();
				this.getProduct().setProductImage(null);
				//folder = conf.getString("core.product.image.filefolder")
				folder = FileUtil.getProductFilePath()
						+ "/"
						+ ctx.getMerchantid() + "/";
				super.getServletRequest().removeAttribute("DYNIMG1");
				super.getServletRequest().removeAttribute("uploadimagename");
				this.setProductImage("");

				catalogservice.saveOrUpdateProduct(this.getProduct());

				FileModule fh = (FileModule) SpringUtil.getBean("localfile");
				fh.deleteFile(ctx.getMerchantid(), new File(new StringBuffer()
						.append(folder).append(imagename).toString()));// delete
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
												.append("-").append(imagename)
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
												.append("-").append(imagename)
												.toString()));

			} else if (this.getFilename().equals("uploadfile")) {
				catalogservice.deleteUploadProduct(this.getProduct());
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

			return SUCCESS;
		} catch (Exception e) {
			log.error(e);
			this.addActionError(getText("errors.technical"));
			return INPUT;
		}

	}

	/**
	 * Set the selected category id in the request for dropdown selection in
	 * editproduct.jsp
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setcategory() throws Exception {

		String catid = super.getServletRequest().getParameter("categoryfilter");
		String ssubcategfilter = super.getServletRequest().getParameter(
				"subcategoryfilter");

		if (catid != null && ssubcategfilter == null) {
			super.getServletRequest().setAttribute("categoryfilter", catid);
		}

		if (ssubcategfilter != null) {
			super.getServletRequest().setAttribute("subcategoryfilter",
					ssubcategfilter);
		}

		return SUCCESS;

	}

	public long getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

	public Map<Integer, Integer> getReflanguages() {
		return reflanguages;
	}

	public void setReflanguages(Map<Integer, Integer> reflanguages) {
		this.reflanguages = reflanguages;
	}

	public Collection<TaxClass> getTaxclass() {
		return taxclass;
	}

	public void setTaxclass(Collection<TaxClass> taxclass) {
		this.taxclass = taxclass;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getAvailabledate() {
		return availabledate;
	}

	public void setAvailabledate(String availabledate) {
		this.availabledate = availabledate;
	}

	public String getCateg() {
		return categ;
	}

	public void setCateg(String categ) {
		this.categ = categ;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public List<String> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDateavailable() {
		return dateavailable;
	}

	public void setDateavailable(String dateavailable) {
		this.dateavailable = dateavailable;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductImageLarge() {
		return productImageLarge;
	}

	public void setProductImageLarge(String productImageLarge) {
		this.productImageLarge = productImageLarge;
	}

	public boolean isProductIsClone() {
		return productIsClone;
	}

	public void setProductIsClone(boolean productIsClone) {
		this.productIsClone = productIsClone;
	}

	public long getNewProductId() {
		return newProductId;
	}

	public void setNewProductId(long newProductId) {
		this.newProductId = newProductId;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public List<String> getSeo() {
		return seo;
	}

	public void setSeo(List<String> seo) {
		this.seo = seo;
	}

	public List<String> getMetadescriptions() {
		return metadescriptions;
	}

	public void setMetadescriptions(List<String> metadescriptions) {
		this.metadescriptions = metadescriptions;
	}

	public List<String> getMetakeywords() {
		return metakeywords;
	}

	public void setMetakeywords(List<String> metakeywords) {
		this.metakeywords = metakeywords;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getProductImageWidth() {
		return productImageWidth;
	}

	public void setProductImageWidth(int productImageWidth) {
		this.productImageWidth = productImageWidth;
	}

	public int getProductImageHeight() {
		return productImageHeight;
	}

	public void setProductImageHeight(int productImageHeight) {
		this.productImageHeight = productImageHeight;
	}

	public String getCropImageUrl() {
		return cropImageUrl;
	}

	public void setCropImageUrl(String cropImageUrl) {
		this.cropImageUrl = cropImageUrl;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public List<String> getDownloadurl() {
		return downloadurl;
	}

	public void setDownloadurl(List<String> downloadurl) {
		this.downloadurl = downloadurl;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

}

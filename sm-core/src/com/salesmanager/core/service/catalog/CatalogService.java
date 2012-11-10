/*
 * Licensed to csti consulting
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Jun 2, 2008 Consultation CS-TI inc.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.catalog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductAttributeDownload;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescription;
import com.salesmanager.core.entity.catalog.ProductOptionDescriptionId;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescriptionId;
import com.salesmanager.core.entity.catalog.ProductOptionValueToProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionValueToProductOptionId;
import com.salesmanager.core.entity.catalog.ProductPrice;
import com.salesmanager.core.entity.catalog.ProductPriceDescription;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.entity.catalog.ProductRelationship;
import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.ReviewDescription;
import com.salesmanager.core.entity.catalog.ReviewDescriptionId;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.entity.catalog.Special;
import com.salesmanager.core.entity.common.Counter;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.orders.FileHistory;
import com.salesmanager.core.entity.orders.FileHistoryId;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.reference.Manufacturers;
import com.salesmanager.core.entity.reference.ManufacturersInfo;
import com.salesmanager.core.entity.reference.ProductOptionType;
import com.salesmanager.core.module.impl.application.files.FileException;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.module.model.application.ProductFileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.impl.CategoryCacheImpl;
import com.salesmanager.core.service.catalog.impl.db.dao.ICategoryDao;
import com.salesmanager.core.service.catalog.impl.db.dao.ICategoryDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductAttributeDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductAttributeDownloadDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductOptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductOptionDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductOptionValueDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductOptionValueDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductOptionValueToProductOptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductPriceDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductPriceDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductPriceSpecialDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IProductRelationshipDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IReviewDao;
import com.salesmanager.core.service.catalog.impl.db.dao.IReviewDescriptionDao;
import com.salesmanager.core.service.catalog.impl.db.dao.ISpecialDao;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.reference.impl.dao.IManufacturerDao;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
public class CatalogService {

	private static Configuration conf = PropertiesUtil.getConfiguration();
	private static Logger log = Logger.getLogger(CatalogService.class);

	private static int DOWNLOAD_MAXDAYS;
	private static int DOWNLOAD_MAX_COUNT;

	static {

		try {
			DOWNLOAD_MAXDAYS = conf.getInt("core.product.file.downloadmaxdays");
			DOWNLOAD_MAX_COUNT = conf
					.getInt("core.product.file.downloadmaxcount");
		} catch (Exception e) {
			log.error("INIT ERROR ", e);
		}
	}

	@Autowired
	private ICategoryDao categoryDao;

	@Autowired
	private ICategoryDescriptionDao categoryDescriptionDao;

	@Autowired
	private IProductDao productDao;

	@Autowired
	private IProductPriceDao productPriceDao;

	@Autowired
	private IProductPriceSpecialDao productPriceSpecialDao;

	@Autowired
	private IProductDescriptionDao productDescriptionDao;

	@Autowired
	private IProductAttributeDao productAttributeDao;

	@Autowired
	private IProductAttributeDownloadDao productAttributeDownloadDao;

	@Autowired
	private ISpecialDao specialDao;

	@Autowired
	private IProductOptionDao productOptionDao;

	@Autowired
	private IProductOptionDescriptionDao productOptionDescriptionDao;

	@Autowired
	private IProductOptionValueDao productOptionValueDao;

	@Autowired
	private IProductOptionValueDescriptionDao productOptionValueDescriptionDao;

	@Autowired
	private IProductOptionValueToProductOptionDao productOptionValueToProductOptionDao;

	@Autowired
	private IProductRelationshipDao productRelationshipDao;

	@Autowired
	private IManufacturerDao manufacturerDao;

	@Autowired
	private IReviewDao reviewDao;

	@Autowired
	private IReviewDescriptionDao reviewDescriptionDao;

	@Autowired
	private IProductPriceDescriptionDao productPriceDescriptionDao;

	public boolean isProductVirtual(Product product) {
		if (product.getProductType() == 3 || product.getProductType() == 4) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isProductSubscribtion(Product product) {
		if (product.getProductType() == 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get download information related to a given Product entity
	 * 
	 * @param productId
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public ProductAttributeDownload getProductDownload(long productId)
			throws CatalogException {

		Collection attributes = productAttributeDao.findByProductId(productId);
		if (attributes != null) {
			Iterator attriter = attributes.iterator();
			while (attriter.hasNext()) {
				ProductAttribute pa = (ProductAttribute) attriter.next();
				// get download
				ProductAttributeDownload pad = productAttributeDownloadDao
						.findById(pa.getProductAttributeId());
				if (pad != null) {// returns the first one, for now, only one
									// download per product
					return pad;
				}
			}

		}
		return null;

	}

	/**
	 * Get a ProductAttribute entity
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductAttribute getProductAttribute(long productAttributeId)
			throws Exception {

		return productAttributeDao.findById(productAttributeId);

	}

	/**
	 * Get a ProductAttribute entity for a given language
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductAttribute getProductAttribute(long productAttributeId,
			String language) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(language);

		return productAttributeDao.findById(productAttributeId, l);

	}

	/**
	 * Returns a collection of ProductAttribute populated with all collections
	 * for a given productId, language code
	 * 
	 * @param productId
	 * @param languageCode
	 * @param displayOnly
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<ProductAttribute> getProductAttributes(long productId,
			String languageCode) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(languageCode);

		return productAttributeDao.findAttributesByProductId(productId, l);

	}

	/**
	 * Returns a list of product attributes based on attributes id and language
	 * 
	 * @param productAttributeIds
	 * @param languageCode
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<ProductAttribute> getProductAttributes(
			List<Long> productAttributeIds, String languageCode)
			throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(languageCode);

		return productAttributeDao.findAttributesByIds(productAttributeIds, l);
	}

	/**
	 * Get a ProductAttribute entity based on options_values_id and product_id
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductAttribute getProductAttributeByOptionValueAndProduct(
			long productId, long productOptionValueId) throws Exception {

		return productAttributeDao.findByProductIdAndOptionValueId(productId,
				productOptionValueId);

	}

	/**
	 * Deletes a ProductAttribute entity
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void deleteProductAttribute(long productAttributeId)
			throws Exception {

		ProductAttribute productAttribute = productAttributeDao
				.findById(productAttributeId);
		if (productAttribute != null) {
			productAttributeDao.delete(productAttribute);
		}
	}

	/**
	 * Deletes a ProductAttribute entity
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductAttribute(ProductAttribute attribute)
			throws Exception {

		productAttributeDao.saveOrUpdate(attribute);

	}

	/**
	 * Fetch a single ProductOption based on a productOptionId
	 * 
	 * @param productOptionId
	 * @return
	 * @throws Exception
	 */
/*	@Transactional
	public ProductOption getProductOption(long productOptionId)
			throws Exception {

		return productOptionDao.findById(productOptionId);
	}*/

	/**
	 * Retreives ProductOptionDescription for a given language
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductOptionDescription getProductOptionDescription(int languageId,
			long productOptionId) throws Exception {

		ProductOptionDescriptionId id = new ProductOptionDescriptionId();
		id.setLanguageId(languageId);
		id.setProductOptionId(productOptionId);

		return productOptionDescriptionDao.findById(id);

	}

	/**
	 * Fetch a Collection of ProductOption entities belonging to a given
	 * merchantId
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<ProductOption> getProductOptions(int merchantId)
			throws Exception {

		Collection options = productOptionDao.findByMerchantId(merchantId);

		return options;
	}

	/**
	 * Returns ProductOption entity loaded with a collection of
	 * ProductOptionValue (values) loaded
	 * 
	 * @param productOptionId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductOption getProductOptionWithValues(long productOptionId)
			throws Exception {

		ProductOption option = productOptionDao
				.findOptionsValuesByProductOptionId(productOptionId);

		return option;
	}

	@Transactional
	public void deleteProductOption(long productOptionId) throws Exception {

		ProductOption option = productOptionDao.findById(productOptionId);
		if (option != null) {
			Set descriptions = option.getDescriptions();
			if (descriptions != null) {
				productOptionDescriptionDao.deleteAll(descriptions);
			}

			Collection values = productOptionValueToProductOptionDao
					.findByIdProductOptionId(productOptionId);
			if (values != null) {
				productOptionValueToProductOptionDao.deleteAll(values);
			}

			productOptionDao.delete(option);

		}

	}

	@Transactional
	public void saveOrUpdateManufacturers(Manufacturers manufacturers,
			ManufacturersInfo manuInfo) {
		manufacturers.setDateAdded(new Date());
		manufacturers.setLastModified(new Date());
		manufacturerDao.saveOrUpdateManufacturers(manufacturers);
		manuInfo.setDateLastClick(new Date());
		manuInfo.getId().setManufacturersId(manufacturers.getManufacturersId());
		manufacturerDao.saveOrUpdateManufacturersInfo(manuInfo);
	}

	/**
	 * Updates or Creates a ProductOption entity
	 * 
	 * @param option
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductOption(ProductOption option)
			throws Exception {

		productOptionDao.saveOrUpdate(option);

		if (option.getDescriptions() != null) {

			Iterator i = option.getDescriptions().iterator();
			while (i.hasNext()) {
				ProductOptionDescription description = (ProductOptionDescription) i
						.next();
				description.getId().setProductOptionId(
						option.getProductOptionId());
			}
			productOptionDescriptionDao.saveOrUpdateAll(option
					.getDescriptions());
		}

	}

	@Transactional
	public ProductOptionValue getProductOptionValue(long productOptionId)
			throws Exception {
		return productOptionValueDao.findById(productOptionId);
	}

	/**
	 * Retreives a Collection of ProductOptionValue for a given merchantId
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<ProductOptionValue> getProductOptionValues(int merchantId)
			throws Exception {

		return productOptionValueDao.findByMerchantId(merchantId);
	}

	/**
	 * Retreives ProductOptionValueDescription for a given language
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductOptionValueDescription getProductOptionValueDescription(
			int languageId, long productOptionValueId) throws Exception {

		ProductOptionValueDescriptionId id = new ProductOptionValueDescriptionId();
		id.setLanguageId(languageId);
		id.setProductOptionValueId(productOptionValueId);

		return productOptionValueDescriptionDao.findById(id);

	}

	@Transactional
	public Collection<ProductOptionValueDescription> getProductOptionValueDescriptions(
			long productOptionValueId) throws Exception {

		return productOptionValueDescriptionDao
				.findByProductOptionValueId(productOptionValueId);

	}

	/**
	 * Saves or updates a ProductOptionValue entity and its descriptions
	 * 
	 * @param value
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductOptionValue(ProductOptionValue value)
			throws Exception {

		productOptionValueDao.saveOrUpdate(value);

		if (value.getDescriptions() != null) {

			Iterator i = value.getDescriptions().iterator();
			while (i.hasNext()) {

				ProductOptionValueDescription description = (ProductOptionValueDescription) i
						.next();
				description.getId().setProductOptionValueId(
						value.getProductOptionValueId());

			}

			productOptionValueDescriptionDao.saveOrUpdateAll(value
					.getDescriptions());
		}

	}

	/**
	 * Create or update a Collection of ProductOptionValueDescription
	 * 
	 * @param descriptions
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateOptionValueDescriptions(
			Collection<ProductOptionValueDescription> descriptions)
			throws Exception {
		productOptionValueDescriptionDao.saveOrUpdateAll(descriptions);
	}

	/**
	 * Creates an association between a ProductOption and a ProductOptionValue
	 * 
	 * @param productOptionId
	 * @param productOptionValueId
	 * @throws Exception
	 */
	@Transactional
	public void associateProductOptionValueToProductOption(
			Long productOptionId, Long productOptionValueId) throws Exception {
		ProductOptionValueToProductOption po = new ProductOptionValueToProductOption();
		ProductOptionValueToProductOptionId id = new ProductOptionValueToProductOptionId();
		id.setProductOptionId(productOptionId);
		id.setProductOptionValueId(productOptionValueId);
		po.setId(id);
		productOptionValueToProductOptionDao.persist(po);
	}

	@Transactional
	public void removeProductRelationship(ProductRelationship relationship)
			throws Exception {
		this.productRelationshipDao.delete(relationship);
	}

	/**
	 * Removes an association between a ProductOption and a ProductOptionValue
	 * 
	 * @param productOptionId
	 * @param productOptionValueId
	 * @throws Exception
	 */
	@Transactional
	public void removeProductOptionValueToProductOption(Long productOptionId,
			Long productOptionValueId) throws Exception {
		ProductOptionValueToProductOption po = new ProductOptionValueToProductOption();
		ProductOptionValueToProductOptionId id = new ProductOptionValueToProductOptionId();
		id.setProductOptionId(productOptionId);
		id.setProductOptionValueId(productOptionValueId);
		po.setId(id);
		productOptionValueToProductOptionDao.delete(po);
	}

	/**
	 * Saves or updates a ProductOptionValue entity and its descriptions. Also
	 * creates an association in ProductOptionValueToProductOption
	 * 
	 * @param value
	 * @param productOptionValueId
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductOptionValueToProductOption(
			ProductOptionValue value, Long productOptionId) throws Exception {

		productOptionValueDao.saveOrUpdate(value);

		if (value.getDescriptions() != null) {

			Iterator i = value.getDescriptions().iterator();
			while (i.hasNext()) {

				ProductOptionValueDescription description = (ProductOptionValueDescription) i
						.next();
				description.getId().setProductOptionValueId(
						value.getProductOptionValueId());

			}

			productOptionValueDescriptionDao.saveOrUpdateAll(value
					.getDescriptions());
		}

		ProductOptionValueToProductOption po = new ProductOptionValueToProductOption();
		ProductOptionValueToProductOptionId id = new ProductOptionValueToProductOptionId();
		id.setProductOptionId(productOptionId);
		id.setProductOptionValueId(value.getProductOptionValueId());
		po.setId(id);
		productOptionValueToProductOptionDao.saveOrUpdate(po);

	}

	/**
	 * Deletes a ProductOptionValue and relations from
	 * ProductOptionValueToProductOption
	 * 
	 * @param productOptionValueId
	 * @throws Exception
	 */
	@Transactional
	public void deleteProductOptionValue(ProductOptionValue optionValue)
			throws Exception {
		// ProductOptionValue optionValue =
		// productOptionValueDao.findById(productOptionValueId);

		if (optionValue != null) {

			Set descriptions = optionValue.getDescriptions();
			if (descriptions != null) {
				productOptionValueDescriptionDao.deleteAll(descriptions);
			}

			Collection values = productOptionValueToProductOptionDao
					.findByIdProductOptionValueId(optionValue
							.getProductOptionValueId());
			if (values != null) {
				productOptionValueToProductOptionDao.deleteAll(values);
			}

			productOptionValueDao.delete(optionValue);

			// @TODO, delete relations in productattributes

		}

	}

	/**
	 * Method to upload a virtual product to the system, will create a
	 * ProductAttribute with optionId=-1 and optionValueId=-1
	 * 
	 * @param Product
	 *            [requires an existing entity]
	 * @param File
	 *            [uploaded file]
	 * @param fileName
	 *            [the filename uploaded]
	 * @throws CatalogException
	 */
	@Transactional
	public void persistUploadProduct(Product product, File file,
			String fileName, String contentType) throws Exception,
			FileException {

		ProductFileModule fhp = (ProductFileModule) SpringUtil
				.getBean("productfile");

		// was there a download for that product before ? cleanup
		this.cleanupVirtualProduct(product.getMerchantId(), product
				.getProductId());

		// ProductAttribute
		ProductAttribute pa = new ProductAttribute();
		pa.setProductId(product.getProductId());
		pa.setOptionId(-1);
		pa.setOptionValueId(-1);

		productAttributeDao.persist(pa);

		// ProductDownloadAttribute
		ProductAttributeDownload pad = new ProductAttributeDownload();
		pad.setProductAttributeFilename(fileName);
		pad.setProductAttributeId(pa.getProductAttributeId());
		pad.setProductAttributeMaxcount(DOWNLOAD_MAX_COUNT);
		pad.setProductAttributeMaxdays(DOWNLOAD_MAXDAYS);

		productAttributeDownloadDao.persist(pad);

		// FileHistory

		FileHistoryId fhid = new FileHistoryId();
		fhid.setMerchantId(product.getMerchantId());
		fhid.setFileid(pa.getProductAttributeId());

		FileHistory fhist = new FileHistory();
		fhist.setId(fhid);
		fhist.setDateAdded(new Date(new Date().getTime()));
		fhist.setFilesize((int) file.length());

		try {
			OrderService os = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			os.createVirtualProductFileData(fhist);

		} catch (Exception e) {
			log.error(e);
		}

		// upload product

		fhp.setProductId(product.getProductId());

		String finalfilename = fhp.uploadFile(product.getMerchantId(),
				"core.product.file", file, fileName, contentType);

		// Save Product
		product.setProductVirtual(true);
		productDao.saveOrUpdate(product);

	}

	@Transactional
	public void deleteUploadProduct(Product product) throws Exception {

		this.cleanupVirtualProduct(product.getMerchantId(), product
				.getProductId());

		product.setProductVirtual(false);
		productDao.saveOrUpdate(product);

	}

	/**
	 * Retreives a Special entity based on the productId
	 * 
	 * @param productId
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public Special getSpecial(long productId) throws CatalogException {
		return specialDao.findByProductId(productId);
	}

	@Transactional
	public void saveOrUpdateSpecial(Special special) throws CatalogException {

		specialDao.saveOrUpdate(special);

	}

	@Transactional
	public void deleteSpecial(Special special) throws CatalogException {
		specialDao.delete(special);
	}

	/**
	 * Deletes a ProductPrice entity
	 * 
	 * @param price
	 * @throws Exception
	 */
	@Transactional
	public void deleteProductPrice(ProductPrice price) throws Exception {
		Set descriptions = price.getPriceDescriptions();
		if (descriptions != null) {

			productPriceDescriptionDao.deleteAll(descriptions);
			price.setPriceDescriptions(null);
		}

		if (price.getSpecial() != null) {
			ProductPriceSpecial sp = price.getSpecial();
			productPriceSpecialDao.delete(sp);
		}

		productPriceDao.delete(price);
	}

	/**
	 * Saves and updates a ProductPriceDescription entity
	 * 
	 * @param price
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductPriceDescription(
			ProductPriceDescription description) throws Exception {
		productPriceDescriptionDao.saveOrUpdate(description);
	}

	/**
	 * Saves or updates a Collection of ProductPriceDescription
	 * 
	 * @param prices
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductPriceDescriptions(
			Collection<ProductPriceDescription> descriptions) throws Exception {
		productPriceDescriptionDao.saveOrUpdateAll(descriptions);
	}

	/**
	 * Saves and updates a ProductPrice entity
	 * 
	 * @param price
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductPrice(ProductPrice price) throws Exception {

		Set descriptions = price.getPriceDescriptions();

		productPriceDao.saveOrUpdate(price);

		if (descriptions != null) {

			for (Object o : descriptions) {
				ProductPriceDescription ppd = (ProductPriceDescription) o;
				ppd.getId().setProductPriceId(price.getProductPriceId());
			}

			productPriceDescriptionDao.saveOrUpdateAll(descriptions);
		}

	}

	/**
	 * Saves or updates a Collection of ProductPrice
	 * 
	 * @param prices
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductPrices(Collection<ProductPrice> prices)
			throws Exception {
		productPriceDao.saveOrUpdateAll(prices);
	}

	/**
	 * Retreives a ProductPrice entity
	 * 
	 * @param productPriceId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductPrice getProductPrice(long productPriceId) throws Exception {
		return productPriceDao.findById(productPriceId);
	}

	/**
	 * Get a ProductPriceSpecial for a given ProductPrice
	 * 
	 * @param productPriceId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ProductPriceSpecial getProductPriceSpecial(long productPriceId)
			throws Exception {
		return productPriceSpecialDao.findByProductPriceId(productPriceId);
	}

	@Transactional
	public ProductRelationship getProductRelationship(long productId,
			long relatedProductId, int relationShipType, int merchantId)
			throws Exception {
		return this.productRelationshipDao.findRelationshipLine(productId,
				relatedProductId, merchantId, relationShipType);
	}

	/**
	 * Returns a list of ProductRelationship items related to a given productId
	 * relation types are : 0 -> featured items. Featured items are not attached
	 * to a given productId so the productId must be -1 5 -> related items 10 ->
	 * accessories
	 * 
	 * @param productId
	 * @param merchantId
	 * @param relationType
	 */
	@Transactional
	public Collection<ProductRelationship> getProductRelationShip(
			long productId, int merchantId, int relationType) {

		return this.productRelationshipDao
				.findByProductIdAndMerchantIdAndRelationTypeId(productId,
						merchantId, relationType);
	}

	/**
	 * Will return product relation for a given base product and a given
	 * merchant id. Relation types can be
	 * CatalogConstants.PRODUCT_RELATIONSHIP_FEATURED_ITEMS
	 * CatalogConstants.PRODUCT_RELATIONSHIP_RELATED_ITEMS
	 * CatalogConstants.PRODUCT_RELATIONSHIP_ACCESSORIES_ITEMS
	 * checkAvailabilityFlag if set to true will remove the item from the list
	 * if the product is not available. If productId==-1, all products will be
	 * returned for a given relationship type
	 * 
	 * @param productId
	 * @param merchantId
	 * @param relationType
	 * @param lang
	 * @param checkAvailability
	 * @return
	 */
	@Transactional
	public Collection<Product> getProductRelationShip(long productId,
			int merchantId, int relationType, String lang,
			boolean checkAvailability) {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Collection returnList = new ArrayList();

		Collection relations = null;
		if (productId != -1) {
			relations = productRelationshipDao
					.findByProductIdAndMerchantIdAndRelationTypeId(productId,
							merchantId, relationType);
		} else {
			relations = productRelationshipDao
					.findByMerchantIdAndRelationTypeId(merchantId, relationType);
		}

		if (relations != null && relations.size() > 0) {
			List ids = new ArrayList();
			Iterator i = relations.iterator();
			while (i.hasNext()) {
				ProductRelationship pr = (ProductRelationship) i.next();
				ids.add(pr.getProductId());
			}
			if (checkAvailability) {
				returnList = this.productDao
						.findAvailableProductsByProductsIdAndLanguageId(ids, l);
			} else {
				returnList = this.productDao
						.findProductsByProductsIdAndLanguageId(ids, l);
			}
		}
		return returnList;
	}

	/**
	 * Deletes a ProductPriceSpecial entity
	 * 
	 * @param instance
	 * @throws Exception
	 */
	@Transactional
	public void deleteProductPriceSpecial(ProductPriceSpecial instance)
			throws Exception {
		productPriceSpecialDao.delete(instance);
	}

	/**
	 * Creates or updates a ProductPriceSpecial
	 * 
	 * @param instance
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateProductPriceSpecial(ProductPriceSpecial instance)
			throws Exception {
		productPriceSpecialDao.saveOrUpdate(instance);
	}

	@Transactional
	public int getProductCount(int merchantId) throws Exception {

		return productDao.countProduct(merchantId);

	}

	/**
	 * Returns a Product entity based on the product id
	 * 
	 * @param productid
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public Product getProduct(long productid) throws CatalogException {
		Product p = productDao.findById(productid);
		return p;
	}

	/**
	 * Returns a Product entity based on the product id Will populate
	 * description based on the language
	 * 
	 * @param productid
	 * @param language
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public Product getProductByLanguage(long productid, String language)
			throws CatalogException {
		int l = LanguageUtil.getLanguageNumberCode(language);
		Product p = productDao.findById(productid, l);
		return p;
	}

	@Transactional
	public Collection<Product> getProducts(Collection<Long> ids)
			throws Exception {
		return productDao.findByIds(ids);
	}

	@Transactional
	public void saveOrUpdateProductRelationship(ProductRelationship relationship)
			throws Exception {
		productRelationshipDao.saveOrUpdate(relationship);

	}

	@Transactional
	public void saveOrUpdateAllProducts(Collection<Product> products)
			throws Exception {
		productDao.saveOrUpdateAll(products);

	}

	/**
	 * Creates a product entity and the descriptions contained, returns the
	 * product with the id set
	 * 
	 * @param product
	 * @throws CatalogException
	 */
	@Transactional
	public Product saveOrUpdateProduct(Product product) throws CatalogException {

		boolean update = true;

		if (product.getProductId() == 0) {
			productDao.persist(product);
			update = false;
		} else {
			productDao.saveOrUpdate(product);
		}

		Set descriptions = product.getDescriptions();
		if (descriptions != null) {
			Iterator i = descriptions.iterator();

			while (i.hasNext()) {
				Object o = i.next();
				ProductDescription desc = (ProductDescription) o;
				desc.getId().setProductId(product.getProductId());
				if (update) {
					productDescriptionDao.saveOrUpdate(desc);
				} else {
					productDescriptionDao.persist(desc);
				}
			}

		}

		return product;

	}

	@Transactional
	public void saveOrUpdateProductDescription(ProductDescription desc) {
		productDescriptionDao.saveOrUpdate(desc);
	}

	/**
	 * Returns a list of Product
	 * 
	 * @param merchantid
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public Collection<com.salesmanager.core.entity.catalog.Product> getProducts(
			int merchantid) throws CatalogException {

		return productDao.findByMerchantId(merchantid);

	}

	@Transactional
	public Collection<com.salesmanager.core.entity.catalog.Product> findProductsByCategoriesIdAndMerchantIdAndLanguageId(
			int merchantId, List<Long> categoriesId, int languageId)
			throws Exception {
		return productDao.findProductsByCategoriesIdAndMerchantIdAndLanguageId(
				categoriesId, merchantId, languageId);
	}

	/** Many queries **/
	@Transactional
	public Collection<com.salesmanager.core.entity.catalog.Product> getProductsByMerchantIdAndCategoryIdAndLanguageId(
			int merchantId, long categoryId, int languageId) throws Exception {
		return productDao.findProductByCategoryIdAndMerchantIdAndLanguageId(
				categoryId, merchantId, languageId);
	}

	@Transactional
	public Collection<com.salesmanager.core.entity.catalog.Product> getProductsByMerchantIdAndCategoryId(
			int merchantId, long categoryId) throws Exception {
		return productDao.findByMerchantIdAndCategoryId(merchantId, categoryId);
	}
	
	@Transactional
	public Collection<com.salesmanager.core.entity.catalog.Product> getProductsByMerchantIdAndLanguageId(
			int merchantId, int languageId) throws Exception {
		return productDao.findByMerchantIdAndLanguageId(merchantId, languageId);
	}

	@Transactional
	public Product getProductByMerchantIdAndSeoURLAndByLang(int merchantId,
			String url, String language) {

		int l = LanguageUtil.getLanguageNumberCode(language);

		return this.productDao.findProductByMerchantIdAndSeoURLAndByLang(
				merchantId, url, l);

	}

	@Transactional
	public ProductDescription getProductDescription(long productId,
			int languageId) {
		return productDescriptionDao.findByProductId(productId, languageId);
	}

	public List getCategoriesIdPerSubCategories(String lang, Category category)
			throws Exception {
		return this.walkcategoriesForGettingCategoriesId(null, lang, category);
	}

	@Transactional
	public List<Category> findSubCategories(long categoryId) throws Exception {
		List categs = this.categoryDao.findSubCategories(categoryId);
		return categs;
	}

	@Transactional
	public Collection<Category> findCategoriesByMerchantIdAndLineage(
			int merchantId, String lineage) throws Exception {
		return categoryDao.findByMerchantIdAndLineage(merchantId, lineage);
	}

	@Transactional
	public Collection<Category> findCategoriesByMerchantIdAndLineageAndLanguageId(
			int merchantId, String lineage, String language) throws Exception {
		return categoryDao.findByMerchantIdAndLanguageIdAndLineage(merchantId,
				LanguageUtil.getLanguageNumberCode(language), lineage);
	}

	/**
	 * Returns the number of products belonging to a category and its sub
	 * categories
	 * 
	 * @param products
	 * @param lang
	 * @param categoryid
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int countProductsPerCategoryAndSubCategories(
			List<com.salesmanager.core.entity.catalog.Product> products,
			String lang, Category category) throws Exception {

		Map productsmap = new HashMap();

		if (products != null) {
			Iterator it = products.iterator();
			while (it.hasNext()) {
				Product p = (Product) it.next();
				if (productsmap.containsKey(p.getMasterCategoryId())) {
					int qty = (Integer) productsmap
							.get(p.getMasterCategoryId());
					qty++;
					productsmap.put(p.getMasterCategoryId(), qty);
				} else {
					productsmap.put(p.getMasterCategoryId(), 1);
				}

			}
		}
		// Map itemsmap =
		// this.walkcategoriesForGettingNumberOfProducts(null,productsmap, lang,
		// categoryid);
		Map itemsmap = this.walkCategoriesForGettingNumberOfProducts(null,
				productsmap, lang, category);
		int count = 0;
		if (itemsmap != null) {
			Iterator rmapiter = itemsmap.keySet().iterator();
			while (rmapiter.hasNext()) {
				long categid = (Long) rmapiter.next();
				count = count + (Integer) itemsmap.get(categid);
			}
		}
		return count;

	}

	/**
	 * removes all products for a given merchantId
	 * 
	 * @param merchantId
	 * @throws Exception
	 */
	@Transactional
	public void deleteAllProducts(int merchantId) throws Exception {

		Collection products = productDao.findByMerchantId(merchantId);
		if (products != null && products.size() > 0) {
			Iterator i = products.iterator();
			while (i.hasNext()) {
				Product product = (Product) i.next();
				deleteProduct(product);
			}
		}

	}

	/**
	 * Deletes a Product entity, downloadable file and all related entities:
	 * ProductAttribute, Special
	 * 
	 * @param p
	 * @throws CatalogException
	 */
	@Transactional
	public void deleteProduct(Product p) throws Exception {

		long productid = p.getProductId();

		ProductFileModule fhp = (ProductFileModule) SpringUtil
				.getBean("productfile");

		// delete virtual product
		this.cleanupVirtualProduct(p.getMerchantId(), p.getProductId());

		Collection attributes = productAttributeDao.findByProductId(productid);
		if (attributes != null) {
			// delete attributes
			productAttributeDao.deleteAll(attributes);
		}

		// update files history
		try {
			OrderService os = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			os.updateDeleteVirtualProductFileData(p.getMerchantId(), p
					.getProductId());
		} catch (Exception e) {
			log.error("CANNOT DELETE ACCOUNT VIRTUAL PRODUCT");
		}

		// delete discount
		Special special = this.getSpecial(p.getProductId());
		if (special != null) {
			this.deleteSpecial(special);
		}

		Set descriptions = p.getDescriptions();

		Set prices = p.getPrices();

		// delete product prices
		if (prices != null && prices.size() > 0) {

			// delete productprice discounts

			Iterator pIterator = prices.iterator();
			while (pIterator.hasNext()) {
				ProductPrice pp = (ProductPrice) pIterator.next();
				ProductPriceSpecial pps = pp.getSpecial();
				if (pps != null) {
					productPriceSpecialDao.delete(pps);
				}
			}

			productPriceDao.deleteAll(prices);
		}

		this.deleteProductReviewsByProduct(p.getProductId());

		// delete product
		this.productDao.delete(p);

		this.productDescriptionDao.deleteProductDescriptions(descriptions);

	}

	@Transactional
	public void deleteCategory(Category c) throws Exception {
		this.deleteCategory(c.getCategoryId(), c.getMerchantId());

	}

	/**
	 * Deletes all categories for a given merchantId. This method does not
	 * delete products attached to categories deleted
	 * 
	 * @param categoryids
	 * @param merchantId
	 * @throws CatalogException
	 */
	@Transactional
	public void deleteAllCategories(int merchantId) throws Exception {

		Collection categories = categoryDao.findByMerchantId(merchantId);
		if (categories != null && categories.size() > 0) {

			List ids = new ArrayList();
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category c = (Category) i.next();
				if(c.getCategoryId()>0) {//don't delete root !
					ids.add(c.getCategoryId());
				}
			}

			Collection descriptions = categoryDescriptionDao
					.findByCategoryIds(ids);
			categoryDescriptionDao.deleteCategoriesDescriptions(descriptions);
			categoryDao.deleteCategories(categories);

		}

		CacheModule module = (CacheModule) SpringUtil.getBean("cache");
		module.flushCacheGroup(Constants.CACHE_CATEGORIES, null);

	}

	@Transactional
	private void validateDeleteCategory(Collection<Long> categoryids,
			int merchantId) throws CatalogException {
		Collection<Product> products = productDao
				.findByMerchantIdAndCategories(merchantId, categoryids);
		if (products != null && !products.isEmpty()) {
			throw new CatalogException(
					"Products are attached to Category,Unable to delete.",
					ErrorConstants.DELETE_UNSUCCESS_PRODUCTS_ATTACHED);
		}
	}

	@Transactional
	public void deleteCategory(long categoryid, int merchantId)
			throws Exception {

		Category c = this.getCategory(categoryid);

		if (c.getMerchantId() == 0) {
			throw new CatalogException("Category does not have a merchant.",
					ErrorConstants.DELETE_UNSUCCESS_CATEGORY_NO_MERCHANT);
		}

		List catidstodeletelist = new ArrayList();

		String lineageQuery = new StringBuffer().append(c.getLineage()).append(
				c.getCategoryId()).append(CatalogConstants.LINEAGE_DELIMITER)
				.toString();

		Collection categories = categoryDao.findByMerchantIdAndLineage(
				merchantId, lineageQuery);

		if (categories != null && categories.size() > 0) {
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category cat = (Category) i.next();
				catidstodeletelist.add(cat.getCategoryId());
			}
		}

		catidstodeletelist.add(c.getCategoryId());
		categories.add(c);

		// For each supported language
		Map languages = RefCache.getLanguageswithindex();
		// Iterator i = languages.keySet().iterator();

		// get all ids to be deleted

		validateDeleteCategory(catidstodeletelist, merchantId);

		// delete category

		Collection cats = categoryDao.findByCategoryIds(catidstodeletelist);
		categoryDao.deleteCategories(cats);

		// delete category descriptions

		Collection descriptions = categoryDescriptionDao
				.findByCategoryIds(catidstodeletelist);
		categoryDescriptionDao.deleteCategoriesDescriptions(descriptions);

		// delete references from the cache
		Iterator i = languages.keySet().iterator();

		// get all ids to be deleted
		// @TODO may be removed
		while (i.hasNext()) {

			Integer langid = (Integer) i.next();
			Language lang = (Language) languages.get(langid);
			// get subcategories
			if (c.getParentId() == 0) {
				Map msters = this.getMasterCategoriesMapByLang(lang.getCode());
				if (msters != null) {
					msters.remove(c.getCategoryId());
				}
			}

			Map categs = this.getCategoriesByLang(merchantId, lang.getCode());

			Map subcategs = this.getSubCategoriesMapByLang(lang.getCode());

			Iterator it = catidstodeletelist.iterator();
			while (it.hasNext()) {
				Long cid = (Long) it.next();

				// remove all ids
				if (categs != null) {
					categs.remove(cid);
				}

				// remove all subcategs ids
				if (subcategs != null) {
					Map scategmap = (Map) subcategs.get(c.getParentId());
					if (scategmap != null) {
						scategmap.remove(cid);
					}
				}
			}

		}

		CacheModule module = (CacheModule) SpringUtil.getBean("cache");
		module.flushCacheGroup(Constants.CACHE_CATEGORIES, null);

	}

	/**
	 * 
	 * Get a Category by id
	 * 
	 * @param categoryId
	 * @return
	 * @throws CatalogException
	 */
	@Transactional
	public Category getCategory(long categoryId) throws CatalogException {

		Category c = categoryDao.findById(categoryId);
		return c;

	}

	@Transactional
	public SearchProductResponse searchProductsForText(
			SearchProductCriteria criteria) throws Exception {
		return productDao.findProductsByDescription(criteria);
	}

	@Transactional
	public SearchProductResponse findProducts(SearchProductCriteria criteria)
			throws Exception {
		return productDao.searchProduct(criteria);

	}

	@Transactional
	public SearchProductResponse findProductsByCategoryList(
			SearchProductCriteria criteria) throws Exception {
		return productDao
				.findProductsByAvailabilityCategoriesIdAndMerchantIdAndLanguageId(criteria);

	}

	@Transactional
	public void updateProductListAvailability(boolean available,
			int merchantId, List<Long> ids) {
		productDao.updateProductListAvailability(available, merchantId, ids);
	}

	/**
	 * Invoked at startup
	 * 
	 * @throws Exception
	 */
	public void loadCategoriesCache() throws Exception {
		CategoryCacheImpl.getInstance().loadCategoriesInCache();
	}

	/**
	 * Move a category Requires a new Category populated with the new
	 * getParentId, Category parent must be set This method will adjust depth
	 * and lineage Update cache
	 * 
	 * @param c
	 * @param descriptions
	 * @throws CatalogException
	 */
	@Transactional
	public void moveCategory(long previousParentId, Category c)
			throws Exception {

		// change parent category

		Category newUpdate = categoryDao.findById(c.getCategoryId());
		newUpdate.setParent(c.getParent());
		newUpdate.setParentId(c.getParentId());

		// adjust lineage
		String lineage = c.getLineage();

		String queryLineage = new StringBuffer().append(lineage).append(
				c.getCategoryId()).append(CatalogConstants.LINEAGE_DELIMITER)
				.toString();

		// ajust subcategories
		Collection categories = categoryDao.findByMerchantIdAndLineage(c
				.getMerchantId(), queryLineage);

		Category newParent = c.getParent();
		StringBuffer newLineage = new StringBuffer();
		newLineage.append(newParent.getLineage()).append(
				newParent.getCategoryId()).append(
				CatalogConstants.LINEAGE_DELIMITER);

		int newMoveDepth = newParent.getDepth() + 1;
		int originalCategoryDepth = c.getDepth();

		if (categories != null && categories.size() > 0) {
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category cat = (Category) i.next();

				cat.setDepth((cat.getDepth() - originalCategoryDepth)
						+ newMoveDepth);
				String catLineage = cat.getLineage();
				int index = lineage.length();
				String newCatLineage = catLineage.substring(index);

				cat.setLineage(new StringBuffer().append(newLineage.toString())
						.append(newCatLineage).toString());

				categoryDao.saveOrUpdate(cat);
			}
		}

		newUpdate.setLineage(newLineage.toString());
		newUpdate.setDepth(newParent.getDepth() + 1);

		categoryDao.saveOrUpdate(newUpdate);

		CacheModule module = (CacheModule) SpringUtil.getBean("cache");
		module.flushCacheGroup(Constants.CACHE_CATEGORIES, null);

	}

	@Transactional
	public void saveOrUpdateCategories(Collection<Category> categories)
			throws Exception {
		categoryDao.saveOrUpdateAll(categories);

		List descriptions = new ArrayList();

		if (categories != null && categories.size() > 0) {
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category c = (Category) i.next();
				Set descs = c.getDescriptions();
				if (descs != null && descs.size() > 0) {
					descriptions.addAll(descs);
				}
			}
		}

		if (descriptions.size() > 0) {
			categoryDescriptionDao.saveOrUpdateAll(descriptions);
		}
	}

	@Transactional
	public void uploadCategories(Category category,
			Collection<CategoryDescription> descriptions) throws Exception {
		if (category.getCategoryId() != (long) 0) {
			// With Upload user has an option to set category id to be inserted
			Category existingCategory = categoryDao.findById(category
					.getCategoryId());
			if (existingCategory != null) {
				// Category already exists, So update descriptions
				if (descriptions != null && !descriptions.isEmpty()) {
					for (CategoryDescription desc : descriptions) {
						categoryDescriptionDao.merge(desc);
					}
				}
			} else {
				// Category does not exist, So create a new category and create
				// new descriptions
				categoryDao.save(category);
				if (descriptions != null && !descriptions.isEmpty()) {
					for (CategoryDescription desc : descriptions) {
						categoryDescriptionDao.merge(desc);
					}
				}
			}
		} else {
			saveOrUpdateCategory(category, descriptions);
		}
	}

	/**
	 * Saves a category and descriptions. Update cache If the category has to be
	 * updated, it needs to have Category parent set
	 * 
	 * @param c
	 * @param descriptions
	 * @throws CatalogException
	 */
	@Transactional
	public void saveOrUpdateCategory(Category c,
			Collection<CategoryDescription> descriptions) throws Exception {

		boolean update = true;

		// lineage
		Category parent = c.getParent();
		long parentId = c.getParentId();
		if (parent != null) {
			int parentDepth = parent.getDepth();
			c.setDepth(parentDepth + 1);
		} else {
			if (parentId == 0) {// root
				c.setDepth(1);
			}
		}
		String lineage = c.getLineage();
		if (StringUtils.isBlank(lineage)) {// new category
			StringBuffer newLineage = new StringBuffer();
			if (parent != null) {
				String parentLineage = parent.getLineage();
				if (CatalogConstants.LINEAGE_DELIMITER.equals(parentLineage)) {
					newLineage.append(parentLineage).append(
							parent.getCategoryId()).append(
							CatalogConstants.LINEAGE_DELIMITER);
				} else {
					newLineage.append(parentLineage).append(
							parent.getCategoryId()).append(
							CatalogConstants.LINEAGE_DELIMITER);
				}
			} else {// put it under root
				newLineage.append(CatalogConstants.LINEAGE_DELIMITER).append(
						CatalogConstants.ROOT_CATEGORY_ID);
			}
			c.setLineage(newLineage.toString());
		}

		if (c.getCategoryId() == 0) {
			categoryDao.persist(c);
			update = false;
		} else {
			categoryDao.saveOrUpdate(c);
		}

		if (descriptions != null && descriptions.size() > 0) {

			Iterator i = descriptions.iterator();

			while (i.hasNext()) {

				CategoryDescription desc = (CategoryDescription) i.next();
				CategoryDescriptionId id = desc.getId();
				if (id == null) {
					throw new Exception(
							"Must set the categorydescription id with the appropriate language");
				}
				id.setCategoryId(c.getCategoryId());
				if (update) {
					categoryDescriptionDao.saveOrUpdate(desc);
				} else {
					categoryDescriptionDao.persist(desc);
				}
			}
		}

	}

	/**
	 * Returns a list of CategoryDescription belonging to a given merchantId for
	 * a given language. It will only return first level categories
	 * 
	 * @param merchantId
	 * @param categoryId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<CategoryDescription> getSubCategoriesDescriptionByCategoryAndLang(
			int merchantId, long categoryId, String lang) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		List categoryList = categoryDescriptionDao
				.findByParentCategoryIDMerchantIdandLanguageId(merchantId,
						categoryId, l);

		return categoryList;

	}

	@Transactional
	public List<Category> getSubCategoriesByParentCategoryAndLang(
			int merchantId, long categoryId, String lang) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		List categoryList = categoryDao.findSubCategoriesByLang(merchantId,
				categoryId, l);

		return categoryList;

	}

	/**
	 * Returns a CategoryDescription for a given categoryId, a given merchantId
	 * and a language
	 * 
	 * @param merchantId
	 * @param categoryId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public CategoryDescription getCategoryDescriptionByLang(int merchantId,
			long categoryId, String lang) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		CategoryDescription categ = categoryDescriptionDao
				.findByMerchantIdAndCategoryIdAndLanguageId(merchantId,
						categoryId, l);

		Category c = categ.getCategory();
		c.setName(categ.getCategoryName());

		return categ;

	}

	/**
	 * Query a category description by an identifier An identifier is a
	 * categoryId or an seo url
	 * 
	 * @param merchantId
	 * @param category
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Category getCategoryByMerchantIdAndSeoURLAndByLang(int merchantId,
			String seoUrl, String lang) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		return categoryDao.findCategoryByMerchantIdAndSeoURLAndByLang(
				merchantId, seoUrl, l);

	}

	/**
	 * Returns a Map<Long(categoryId),Category> of sub categories belonging to a
	 * given parent category. It will only return first level categories
	 * 
	 * @param merchantId
	 * @param categoryId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<Long, Category> getSubCategoriesByCategoryAndLang(
			int merchantId, long categoryId, String lang) throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Map returnMap = new LinkedHashMap();

		List categoryList = categoryDescriptionDao
				.findByParentCategoryIDMerchantIdandLanguageId(merchantId,
						categoryId, l);

		Iterator i = categoryList.iterator();
		while (i.hasNext()) {
			CategoryDescription c = (CategoryDescription) i.next();
			Category cat = c.getCategory();
			cat.setName(c.getCategoryName());
			returnMap.put(cat.getCategoryId(), cat);

		}

		return returnMap;

	}

	@Transactional
	public List<CategoryDescription> getCategoryDescriptions(long categoryid)
			throws CatalogException {
		return categoryDescriptionDao.findByCategoryId(categoryid);
	}

	@Transactional
	public List<CategoryDescription> getAllCategoriesByLang(int languageId)
			throws Exception {
		return categoryDescriptionDao.findByLanguageId(languageId);
	}

	/**
	 * Returns all categories for a given merchantId
	 * 
	 * @param merchantId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<Long, Category> getCategoriesByLang(int merchantId, String lang)
			throws Exception {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Map returnMap = new LinkedHashMap();

		Collection categoryList = categoryDao.findByMerchantIdAndLanguageId(
				merchantId, l);

		Iterator i = categoryList.iterator();
		while (i.hasNext()) {
			Category c = (Category) i.next();
			returnMap.put(c.getCategoryId(), c);

		}

		return returnMap;

	}

	/**
	 * Add or update a product review and sends an html email to store owner
	 * 
	 * @param review
	 * @throws Exception
	 */
	@Transactional
	public void addProductReview(MerchantStore store, Review review)
			throws Exception {

		Set descriptions = review.getDescriptions();
		review.setDescriptions(null);

		// suppose to have one description

		ReviewDescription description = null;

		reviewDao.saveOrUpdate(review);

		if (descriptions != null) {
			
			Locale l = null;

			Iterator i = descriptions.iterator();
			while (i.hasNext()) {
				ReviewDescription d = (ReviewDescription) i.next();
				description = d;
				ReviewDescriptionId id = d.getId();
				if (id == null) {
					id = new ReviewDescriptionId();
				}
				l = review.getLocale();
				if (l == null) {
					l = LocaleUtil.getDefaultLocale();
				}
				id.setLanguageId(LanguageUtil.getLanguageNumberCode(l
						.getLanguage()));
				id.setReviewId(review.getReviewId());
				d.setId(id);
			}
			reviewDescriptionDao.saveOrUpdateAll(descriptions);
			review.setDescriptions(descriptions);

			// email
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			//MerchantUserInformation minfo = mservice.getMerchantUserInfo(store
			//		.getMerchantId());

			Configuration conf = PropertiesUtil.getConfiguration();

			LabelUtil lhelper = LabelUtil.getInstance();
			String subject = lhelper.getText(l.getLanguage(),
					"message.review.created");

			String reviewText = "";
			if (description != null) {
				reviewText = description.getReviewText();
			}

			Map context = new HashMap();
			context.put("EMAIL_STORE_NAME", store.getStorename());
			context.put("EMAIL_SUBJECT", subject);
			context.put("EMAIL_PRODUCT_NAME", review.getProductName());
			context.put("EMAIL_CUSTOMER_REVIEW", review.getCustomerName());
			context.put("EMAIL_REVIEW_RATING", review.getReviewRating());
			context.put("EMAIL_REVIEW_TEXT", reviewText);

			CommonService cservice = new CommonService();
			cservice.sendHtmlEmail(store.getStoreemailaddress(), subject
					+ " - " + review.getProductName(), store, context,
					"email_template_new_review.ftl", store.getDefaultLang());

		}
	}

	@Transactional
	public void deleteProductReview(Review review) throws Exception {

		Collection descs = review.getDescriptions();
		if (descs != null) {
			reviewDescriptionDao.deleteAll(descs);
			review.setDescriptions(null);
		}
		review.setCustomer(null);
		reviewDao.delete(review);

	}

	@Transactional
	public void deleteProductReviewsByProduct(long productId) throws Exception {
		Collection reviews = reviewDao.findByProductId(productId);
		List desc = new ArrayList();
		if (reviews != null && reviews.size() > 0) {
			Iterator i = reviews.iterator();
			while (i.hasNext()) {
				Review r = (Review) i.next();
				Collection descs = r.getDescriptions();
				desc.addAll(descs);
			}
		}

		reviewDescriptionDao.deleteAll(desc);
		reviewDao.deleteAll(reviews);
	}

	@Transactional
	public Collection<Review> getProductReviewsByProduct(long productId,
			int languageId) throws Exception {
		return reviewDao.findByProductId(productId, languageId);
	}

	@Transactional
	public Review getProductReview(long reviewId) throws Exception {
		return reviewDao.findById(reviewId);
	}

	/**
	 * Returns a Collection of Review with appropriate description. Does not
	 * fill the Customer object
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public SearchReviewResponse searchProductReviewsByCustomer(
			SearchReviewCriteria criteria) throws Exception {
		SearchReviewResponse resp = reviewDao.searchByCustomerId(criteria);
		Collection revs = resp.getReviews();
		if (revs != null && revs.size() > 0) {
			Iterator i = revs.iterator();
			while (i.hasNext()) {
				Review r = (Review) i.next();
				ProductDescription p = this.getProductDescription(r
						.getProductId(), criteria.getLanguageId());
				if (p != null) {
					r.setProductName(p.getProductName());
				}
			}
		}
		return resp;

	}

	@Transactional
	public SearchReviewResponse searchProductReviewsByProduct(
			SearchReviewCriteria criteria) throws Exception {
		return reviewDao.searchByProductId(criteria);
	}

	/**
	 * Returns an average rating count for a given productId
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Counter countAverageRatingPerProduct(long productId)
			throws Exception {
		return reviewDao.countAverageRatingByProduct(productId);
	}

	/**
	 * A map containing only mastercategories <Category.categoryId,Category>
	 * 
	 * @param lang
	 * @return
	 */
	public static Map getMasterCategoriesMapByLang(String lang) {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Map mp = (Map) CategoryCacheImpl.getInstance()
				.getMasterCategoriesMapByLang().get(l);

		if (mp == null) {
			return new HashMap();
		} else {
			return mp;
		}

	}

	/**
	 * A map containing subcategories map
	 * <Category.categoryId,Map<Category.categoryId,Category>>
	 * 
	 * @param lang
	 * @return
	 */
	public static Map getSubCategoriesMapByLang(String lang) {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Map mp = (Map) CategoryCacheImpl.getInstance()
				.getSubCategoriesMapByLang().get(l);

		if (mp == null) {
			return new HashMap();
		} else {
			return mp;
		}

	}

	public static List<Category> getSubCategoriesListByLang(String lang) {

		int l = LanguageUtil.getLanguageNumberCode(lang);

		Map mp = (Map) CategoryCacheImpl.getInstance()
				.getSubCategoriesMapByLang().get(l);

		Collection collCategories = mp.values();
		List list = Arrays.asList(collCategories);

		return list;

	}

	@Transactional
	public Collection<ProductOptionType> getProductOptionTypes()
			throws Exception {
		return productOptionDao.findAllProductOptionTypes();
	}

	/*
	 * *****************************
	 * private methods *****************************
	 */

	private List walkcategoriesForGettingCategoriesId(List returnlist,
			String lang, Category category) {

		Collection categories = categoryDao.findByMerchantIdAndLineage(category
				.getMerchantId(), category.getLineage());

		if (categories != null && categories.size() > 0) {
			if (returnlist == null) {
				returnlist = new ArrayList();
			}
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category c = (Category) i.next();
				long categoryId = c.getCategoryId();
				returnlist.add(c.getCategoryId());
			}
		}

		return returnlist;

	}

	/**
	 * 
	 * @param returnmap
	 * @param productmap
	 * @param lang
	 * @param categoryId
	 * @return
	 */
	private Map walkCategoriesForGettingNumberOfProducts(Map returnmap,
			Map productmap, String lang, Category category) {

		// get categories
		Collection categories = categoryDao.findByMerchantIdAndLineage(category
				.getMerchantId(), category.getLineage());

		if (categories != null && categories.size() > 0) {
			Iterator i = categories.iterator();
			while (i.hasNext()) {
				Category c = (Category) i.next();
				long categoryId = c.getCategoryId();

				if (productmap.containsKey(categoryId)) {
					int qty = (Integer) productmap.get(categoryId);
					returnmap.put(categoryId, qty);
				}

			}
		}

		return returnmap;

	}

	/*	*//**
	 * @deprecated
	 * @param returnmap
	 * @param productmap
	 * @param lang
	 * @param categoryId
	 * @return
	 */
	/*
	 * private Map walkcategoriesForGettingNumberOfProducts(Map returnmap,Map
	 * productmap,String lang, long categoryId) {
	 * 
	 * Map categoriesbylang = this.getSubCategoriesMapByLang(lang); Map
	 * subcategs = (Map)categoriesbylang.get(categoryId);
	 * 
	 * if(returnmap==null) { returnmap = new HashMap(); }
	 * 
	 * //int count = 0;
	 * 
	 * //if current category has a product Integer currentCategoryCount =
	 * (Integer)productmap.get(categoryId); if(currentCategoryCount!=null) {
	 * returnmap.put(categoryId,currentCategoryCount.intValue()); //count =
	 * currentCategoryCount.intValue(); }
	 * 
	 * 
	 * 
	 * 
	 * if(subcategs!=null){ Iterator catit = subcategs.keySet().iterator();
	 * while(catit.hasNext()) {
	 * 
	 * Long key = (Long)catit.next(); Category c = (Category)subcategs.get(key);
	 * 
	 * if(productmap.containsKey(c.getCategoryId())) { int qty =
	 * (Integer)productmap.get(c.getCategoryId());
	 * 
	 * returnmap.put(c.getCategoryId(), qty); }
	 * 
	 * returnmap =
	 * walkcategoriesForGettingNumberOfProducts(returnmap,productmap,
	 * lang,c.getCategoryId());
	 * 
	 * } }
	 * 
	 * return returnmap;
	 * 
	 * }
	 */

	private void cleanupVirtualProduct(int merchantId, long productId)
			throws Exception {

		ProductFileModule fhp = (ProductFileModule) SpringUtil
				.getBean("productfile");

		// delete virtual product

		Collection attributes = productAttributeDao.findByProductId(productId);
		if (attributes != null) {
			Iterator attriter = attributes.iterator();
			while (attriter.hasNext()) {
				ProductAttribute pa = (ProductAttribute) attriter.next();
				// get download
				if (pa.getOptionId() == -1) {
					ProductAttributeDownload pad = productAttributeDownloadDao
							.findById(pa.getProductAttributeId());
					if (pad != null) {

						//String folder = conf
								//.getString("core.product.file.filefolder");
						String folder = FileUtil.getDownloadFilePath();
						if (folder != null) {
							folder = new StringBuffer().append(folder).append(
									"/").append(merchantId).toString();
						}

						fhp.setProductId(productId);
						if (!fhp.deleteFile(merchantId, new File(pad
								.getProductAttributeFilename()), folder)) {
							log
									.error("WATCHOUT -- File "
											+ pad.getProductAttributeFilename()
											+ " Cannot be deleted from Remote for user "
											+ merchantId + " and product "
											+ productId);
						}

						productAttributeDownloadDao.delete(pad);
						productAttributeDao.delete(pa);

						OrderService os = (OrderService) ServiceFactory
								.getService(ServiceFactory.OrderService);
						os.updateDeleteVirtualProductFileData(merchantId, pa
								.getProductAttributeId());

					}
				}
			}

		}

	}

}

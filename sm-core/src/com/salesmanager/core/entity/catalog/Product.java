/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.catalog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.entity.merchant.IMerchant;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.ProductUtil;

/**
 * This is an object that contains data related to the products table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="products"
 */
@Indexed
public class Product implements Serializable, IMerchant, I18NEntity {

	private static final long serialVersionUID = 495017001547328553L;
	public static String REF = "Product";
	public static String PROP_PRODUCT_QUANTITY_ORDER_UNITS = "productQuantityOrderUnits";
	public static String PROP_PRODUCT_QUANTITY_ORDER_MIN = "productQuantityOrderMin";
	public static String PROP_PRODUCT_LAST_MODIFIED = "productLastModified";
	public static String PROP_ISBN = "isbn";
	public static String PROP_ASIN = "asin";
	public static String PROP_PRODUCT_SORT_ORDER = "productSortOrder";
	public static String PROP_PRODUCT_WIDTH = "productWidth";
	public static String PROP_PRODUCT_IS_CALL = "productIsCall";
	public static String PROP_PRODUCT_IS_FREE = "productIsFree";
	public static String PROP_PRODUCT_ID = "productId";
	public static String PROP_EAN = "ean";
	public static String PROP_PRODUCT_VIRTUAL = "productVirtual";
	public static String PROP_MASTER_CATEGORIES_ID = "masterCategoriesId";
	public static String PROP_PRODUCT_IMAGE_LARGE = "productImageLarge";
	public static String PROP_UPC = "upc";
	public static String PROP_SKU = "sku";
	public static String PROP_PRODUCT_ORDERED = "productOrdered";
	public static String PROP_PRODUCT_LENGTH = "productLength";
	public static String PROP_PRODUCT_DATE_ADDED = "productDateAdded";
	public static String PROP_PRODUCT_QUANTITY = "productQuantity";
	public static String PROP_PRODUCT_HEIGHT = "productHeight";
	public static String PROP_PRODUCT_STATUS = "productStatus";
	public static String PROP_MERCHANTID = "merchantid";
	public static String PROP_PRODUCT_DATE_AVAILABLE = "productDateAvailable";
	public static String PROP_PRODUCT_TYPE = "productType";
	public static String PROP_PRODUCT_PRICE = "productPrice";
	public static String PROP_PRODUCT_IS_ALWAYS_FREE_SHIPPING = "productIsAlwaysFreeShipping";
	public static String PROP_PRODUCT_IMAGE = "productImage";
	public static String PROP_PRODUCT_WEIGHT = "productWeight";
	public static String PROP_PRODUCT_TAX_CLASS_ID = "productTaxClassId";
	public static String PROP_PRODUCT_MODEL = "productModel";
	public static String PROP_PRODUCT_QUANTITY_ORDER_MAX = "productQuantityOrderMax";
	public static String PROP_PRODUCT_MANUFACTURERS_ID = "productManufacturersId";

	// constructors
	public Product() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Product(int productId) {
		this.setProductId(productId);
		initialize();
	}

	protected void initialize() {

		productDateAdded = new java.util.Date(new java.util.Date().getTime());
		productLastModified = new java.util.Date(new java.util.Date().getTime());
		productDateAvailable = new java.util.Date(new java.util.Date()
				.getTime());
		productType = 1;
		productStatus = true;

	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	@DocumentId
	private long productId;

	// fields
	private int productType;
	private int productQuantity;

	private java.lang.String productModel;

	private java.lang.String productImage;
	
	
	private java.lang.String productImage1;
	private java.lang.String productImage2;
	private java.lang.String productImage3;
	private java.lang.String productImage4;
	
	private java.math.BigDecimal productPrice;
	private boolean productVirtual;
	private java.util.Date productDateAdded;
	private java.util.Date productLastModified;
	@Field
	private java.util.Date productDateAvailable;
	private java.math.BigDecimal productWeight;
	private java.math.BigDecimal productReviewAvg;
	private int productReviewCount;
	@Field
	private boolean productStatus;
	private long productTaxClassId;
	private int productManufacturersId;
	private int productOrdered;
	private int productQuantityOrderMin;
	private int productQuantityOrderUnits;
	private boolean productIsFree;
	private boolean productIsCall;
	private boolean productIsAlwaysFreeShipping;
	private int productQuantityOrderMax;
	private int productSortOrder;
	private long masterCategoryId;
	private boolean productImageCrop;
	private String productExternalDl;

	@Field
	private int merchantId;
	private java.math.BigDecimal productLength;
	private java.math.BigDecimal productWidth;
	private java.math.BigDecimal productHeight;
	private java.lang.String asin;
	private java.lang.String upc;
	private java.lang.String sku;
	private java.lang.String ean;
	private java.lang.String isbn;
	private java.lang.String productImageLarge;
	

	// private Special special;

	// collections
	@IndexedEmbedded
	private java.util.Set<com.salesmanager.core.entity.catalog.ProductDescription> descriptions;
	private java.util.Set<com.salesmanager.core.entity.catalog.ProductAttribute> attributes;
	private java.util.Set<com.salesmanager.core.entity.catalog.ProductPrice> prices;
	private java.util.Set<com.salesmanager.core.entity.catalog.Special> specials;

	// transient
	private String name;
	private Locale locale;
	private String currency;

	public String getName() {

		String nm = "";
		if (name != null) {
			nm = name;

		} else if (this.getDescriptions() != null
				&& this.getDescriptions().size() == 1) {
			ProductDescription[] descArray = (ProductDescription[]) this
					.getDescriptions().toArray(
							new ProductDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				nm = descArray[0].getProductName();
			}

		} else if (this.getDescriptions() != null
				&& this.getDescriptions().size() > 1) {

			if (locale == null) {
				locale = LocaleUtil.getDefaultLocale();
			}
			int language = LanguageUtil.getLanguageNumberCode(this.locale
					.getLanguage());
			for (Object o : this.getDescriptions()) {
				ProductDescription pd = (ProductDescription) o;
				if (pd.getId().getLanguageId() == language) {
					nm = pd.getProductName();
					break;
				}
			}
		}

		return nm;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		ProductDescription desc = null;
		if (this.getDescriptions() != null
				&& this.getDescriptions().size() == 1) {
			ProductDescription[] descArray = (ProductDescription[]) this
					.getDescriptions().toArray(
							new ProductDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				desc = descArray[0];
			}
		} else if (this.getDescriptions() != null
				&& this.getDescriptions().size() > 1) {
			int language = LanguageUtil.getLanguageNumberCode(this.locale
					.getLanguage());
			if (locale == null) {
				locale = LocaleUtil.getDefaultLocale();
			}
			for (Object o : this.getDescriptions()) {
				ProductDescription pd = (ProductDescription) o;
				if (pd.getId().getLanguageId() == language) {
					desc = pd;
					break;
				}
			}
		}

		return desc.getProductDescription();
	}

	public ProductDescription getProductDescription() {

		ProductDescription desc = null;
		if (this.getDescriptions() != null
				&& this.getDescriptions().size() == 1) {
			ProductDescription[] descArray = (ProductDescription[]) this
					.getDescriptions().toArray(
							new ProductDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				desc = descArray[0];
			}
		} else if (this.getDescriptions() != null
				&& this.getDescriptions().size() > 1) {
			int language = LanguageUtil.getLanguageNumberCode(this.locale
					.getLanguage());
			if (locale == null) {
				locale = LocaleUtil.getDefaultLocale();
			}
			for (Object o : this.getDescriptions()) {
				ProductDescription pd = (ProductDescription) o;
				if (pd.getId().getLanguageId() == language) {
					desc = pd;
					break;
				}
			}
		}

		return desc;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="hilo" column="products_id"
	 */
	public long getProductId() {
		return productId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param productId
	 *            the new ID
	 */
	public void setProductId(long productId) {
		this.productId = productId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: products_type
	 */
	public int getProductType() {
		return productType;
	}

	/**
	 * Set the value related to the column: products_type
	 * 
	 * @param productType
	 *            the products_type value
	 */
	public void setProductType(int productType) {
		this.productType = productType;
	}

	/**
	 * Return the value associated with the column: products_quantity
	 */
	public int getProductQuantity() {
		return productQuantity;
	}

	/**
	 * Set the value related to the column: products_quantity
	 * 
	 * @param productQuantity
	 *            the products_quantity value
	 */
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	/**
	 * Return the value associated with the column: products_model
	 */
	public java.lang.String getProductModel() {
		return productModel;
	}

	/**
	 * Set the value related to the column: products_model
	 * 
	 * @param productModel
	 *            the products_model value
	 */
	public void setProductModel(java.lang.String productModel) {
		this.productModel = productModel;
	}

	/**
	 * Return the value associated with the column: products_image
	 */
	public java.lang.String getProductImage() {
		return productImage;
	}

	/**
	 * Set the value related to the column: products_image
	 * 
	 * @param productImage
	 *            the products_image value
	 */
	public void setProductImage(java.lang.String productImage) {
		this.productImage = productImage;
	}

	/**
	 * Return the value associated with the column: products_price
	 */
	public java.math.BigDecimal getProductPrice() {
		return productPrice;
	}

	/**
	 * Set the value related to the column: products_price
	 * 
	 * @param productPrice
	 *            the products_price value
	 */
	public void setProductPrice(java.math.BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * Return the value associated with the column: products_virtual
	 */
	public boolean isProductVirtual() {
		return productVirtual;
	}

	/**
	 * Set the value related to the column: products_virtual
	 * 
	 * @param productVirtual
	 *            the products_virtual value
	 */
	public void setProductVirtual(boolean productVirtual) {
		this.productVirtual = productVirtual;
	}

	/**
	 * Return the value associated with the column: products_date_added
	 */
	public java.util.Date getProductDateAdded() {
		if (productDateAdded == null) {
			return new java.util.Date();
		} else {
			return productDateAdded;
		}
	}

	/**
	 * Set the value related to the column: products_date_added
	 * 
	 * @param productDateAdded
	 *            the products_date_added value
	 */
	public void setProductDateAdded(java.util.Date productDateAdded) {

		this.productDateAdded = productDateAdded;
	}

	/**
	 * Return the value associated with the column: products_last_modified
	 */
	public java.util.Date getProductLastModified() {

		if (productDateAdded == null) {
			return new java.util.Date();
		} else {
			return productDateAdded;
		}

	}

	/**
	 * Set the value related to the column: products_last_modified
	 * 
	 * @param productLastModified
	 *            the products_last_modified value
	 */
	public void setProductLastModified(java.util.Date productLastModified) {
		this.productLastModified = productLastModified;
	}

	/**
	 * Return the value associated with the column: products_date_available
	 */
	public java.util.Date getProductDateAvailable() {
		if (productDateAvailable == null) {
			return new java.util.Date();
		} else {
			return productDateAvailable;
		}
	}

	/**
	 * Set the value related to the column: products_date_available
	 * 
	 * @param productDateAvailable
	 *            the products_date_available value
	 */
	public void setProductDateAvailable(java.util.Date productDateAvailable) {
		this.productDateAvailable = productDateAvailable;
	}

	/**
	 * Return the value associated with the column: products_weight
	 */
	public java.math.BigDecimal getProductWeight() {
		return productWeight;
	}

	/**
	 * Set the value related to the column: products_weight
	 * 
	 * @param productWeight
	 *            the products_weight value
	 */
	public void setProductWeight(java.math.BigDecimal productWeight) {
		this.productWeight = productWeight;
	}

	/**
	 * Return the value associated with the column: products_status
	 */
	public boolean isProductStatus() {
		return productStatus;
	}

	/**
	 * Set the value related to the column: products_status
	 * 
	 * @param productStatus
	 *            the products_status value
	 */
	public void setProductStatus(boolean productStatus) {
		this.productStatus = productStatus;
	}

	/**
	 * Return the value associated with the column: products_tax_class_id
	 */
	public long getProductTaxClassId() {
		return productTaxClassId;
	}

	/**
	 * Set the value related to the column: products_tax_class_id
	 * 
	 * @param productTaxClassId
	 *            the products_tax_class_id value
	 */
	public void setProductTaxClassId(long productTaxClassId) {
		this.productTaxClassId = productTaxClassId;
	}

	/**
	 * Return the value associated with the column: manufacturers_id
	 */
	public int getProductManufacturersId() {
		return productManufacturersId;
	}

	/**
	 * Set the value related to the column: manufacturers_id
	 * 
	 * @param productManufacturersId
	 *            the manufacturers_id value
	 */
	public void setProductManufacturersId(int productManufacturersId) {
		this.productManufacturersId = productManufacturersId;
	}

	/**
	 * Return the value associated with the column: products_ordered
	 */
	public int getProductOrdered() {
		return productOrdered;
	}

	/**
	 * Set the value related to the column: products_ordered
	 * 
	 * @param productOrdered
	 *            the products_ordered value
	 */
	public void setProductOrdered(int productOrdered) {
		this.productOrdered = productOrdered;
	}

	/**
	 * Return the value associated with the column: products_quantity_order_min
	 */
	public int getProductQuantityOrderMin() {
		return productQuantityOrderMin;
	}

	/**
	 * Set the value related to the column: products_quantity_order_min
	 * 
	 * @param productQuantityOrderMin
	 *            the products_quantity_order_min value
	 */
	public void setProductQuantityOrderMin(int productQuantityOrderMin) {
		this.productQuantityOrderMin = productQuantityOrderMin;
	}

	/**
	 * Return the value associated with the column:
	 * products_quantity_order_units
	 */
	public int getProductQuantityOrderUnits() {
		return productQuantityOrderUnits;
	}

	/**
	 * Set the value related to the column: products_quantity_order_units
	 * 
	 * @param productQuantityOrderUnits
	 *            the products_quantity_order_units value
	 */
	public void setProductQuantityOrderUnits(int productQuantityOrderUnits) {
		this.productQuantityOrderUnits = productQuantityOrderUnits;
	}

	/**
	 * Return the value associated with the column: product_is_free
	 */
	public boolean isProductIsFree() {
		return productIsFree;
	}

	/**
	 * Set the value related to the column: product_is_free
	 * 
	 * @param productIsFree
	 *            the product_is_free value
	 */
	public void setProductIsFree(boolean productIsFree) {
		this.productIsFree = productIsFree;
	}

	/**
	 * Return the value associated with the column: product_is_call
	 */
	public boolean isProductIsCall() {
		return productIsCall;
	}

	/**
	 * Set the value related to the column: product_is_call
	 * 
	 * @param productIsCall
	 *            the product_is_call value
	 */
	public void setProductIsCall(boolean productIsCall) {
		this.productIsCall = productIsCall;
	}

	/**
	 * Return the value associated with the column:
	 * product_is_always_free_shipping
	 */
	public boolean isProductIsAlwaysFreeShipping() {
		return productIsAlwaysFreeShipping;
	}

	/**
	 * Set the value related to the column: product_is_always_free_shipping
	 * 
	 * @param productIsAlwaysFreeShipping
	 *            the product_is_always_free_shipping value
	 */
	public void setProductIsAlwaysFreeShipping(
			boolean productIsAlwaysFreeShipping) {
		this.productIsAlwaysFreeShipping = productIsAlwaysFreeShipping;
	}

	/**
	 * Return the value associated with the column: products_quantity_order_max
	 */
	public int getProductQuantityOrderMax() {
		return productQuantityOrderMax;
	}

	/**
	 * Set the value related to the column: products_quantity_order_max
	 * 
	 * @param productQuantityOrderMax
	 *            the products_quantity_order_max value
	 */
	public void setProductQuantityOrderMax(int productQuantityOrderMax) {
		this.productQuantityOrderMax = productQuantityOrderMax;
	}

	/**
	 * Return the value associated with the column: products_sort_order
	 */
	public int getProductSortOrder() {
		return productSortOrder;
	}

	/**
	 * Set the value related to the column: products_sort_order
	 * 
	 * @param productSortOrder
	 *            the products_sort_order value
	 */
	public void setProductSortOrder(int productSortOrder) {
		this.productSortOrder = productSortOrder;
	}

	/**
	 * Return the value associated with the column: master_categories_id
	 */
	public long getMasterCategoryId() {
		return masterCategoryId;
	}

	/**
	 * Set the value related to the column: master_categories_id
	 * 
	 * @param masterCategoriesId
	 *            the master_categories_id value
	 */
	public void setMasterCategoryId(long masterCategoryId) {
		this.masterCategoryId = masterCategoryId;
	}

	/**
	 * Return the value associated with the column: merchantid
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * Set the value related to the column: merchantid
	 * 
	 * @param merchantid
	 *            the merchantid value
	 */
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Return the value associated with the column: products_length
	 */
	public java.math.BigDecimal getProductLength() {
		return productLength;
	}

	/**
	 * Set the value related to the column: products_length
	 * 
	 * @param productLength
	 *            the products_length value
	 */
	public void setProductLength(java.math.BigDecimal productLength) {
		this.productLength = productLength;
	}

	/**
	 * Return the value associated with the column: products_width
	 */
	public java.math.BigDecimal getProductWidth() {
		return productWidth;
	}

	/**
	 * Set the value related to the column: products_width
	 * 
	 * @param productWidth
	 *            the products_width value
	 */
	public void setProductWidth(java.math.BigDecimal productWidth) {
		this.productWidth = productWidth;
	}

	/**
	 * Return the value associated with the column: products_height
	 */
	public java.math.BigDecimal getProductHeight() {
		return productHeight;
	}

	/**
	 * Set the value related to the column: products_height
	 * 
	 * @param productHeight
	 *            the products_height value
	 */
	public void setProductHeight(java.math.BigDecimal productHeight) {
		this.productHeight = productHeight;
	}

	/**
	 * Return the value associated with the column: ASIN
	 */
	public java.lang.String getAsin() {
		return asin;
	}

	/**
	 * Set the value related to the column: ASIN
	 * 
	 * @param asin
	 *            the ASIN value
	 */
	public void setAsin(java.lang.String asin) {
		this.asin = asin;
	}

	/**
	 * Return the value associated with the column: UPC
	 */
	public java.lang.String getUpc() {
		return upc;
	}

	/**
	 * Set the value related to the column: UPC
	 * 
	 * @param upc
	 *            the UPC value
	 */
	public void setUpc(java.lang.String upc) {
		this.upc = upc;
	}

	/**
	 * Return the value associated with the column: SKU
	 */
	public java.lang.String getSku() {
		return sku;
	}

	/**
	 * Set the value related to the column: SKU
	 * 
	 * @param sku
	 *            the SKU value
	 */
	public void setSku(java.lang.String sku) {
		this.sku = sku;
	}

	/**
	 * Return the value associated with the column: EAN
	 */
	public java.lang.String getEan() {
		return ean;
	}

	/**
	 * Set the value related to the column: EAN
	 * 
	 * @param ean
	 *            the EAN value
	 */
	public void setEan(java.lang.String ean) {
		this.ean = ean;
	}

	/**
	 * Return the value associated with the column: ISBN
	 */
	public java.lang.String getIsbn() {
		return isbn;
	}

	/**
	 * Set the value related to the column: ISBN
	 * 
	 * @param isbn
	 *            the ISBN value
	 */
	public void setIsbn(java.lang.String isbn) {
		this.isbn = isbn;
	}

	/**
	 * Return the value associated with the column: products_image_large
	 */

	public java.lang.String getProductImageLarge() {
		return productImageLarge;
	}

	/**
	 * Set the value related to the column: products_image_large
	 * 
	 * @param productImageLarge
	 *            the products_image_large value
	 */
	public void setProductImageLarge(java.lang.String productImageLarge) {
		this.productImageLarge = productImageLarge;
	}

	/**
	 * Return the value associated with the column: Descriptions
	 */
	public java.util.Set<com.salesmanager.core.entity.catalog.ProductDescription> getDescriptions() {
		return descriptions;
	}

	/**
	 * Set the value related to the column: Descriptions
	 * 
	 * @param descriptions
	 *            the Descriptions value
	 */
	public void setDescriptions(
			java.util.Set<com.salesmanager.core.entity.catalog.ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public void addToDescriptions(
			com.salesmanager.core.entity.catalog.ProductDescription productDescription) {
		if (null == getDescriptions())
			setDescriptions(new java.util.TreeSet<com.salesmanager.core.entity.catalog.ProductDescription>());
		getDescriptions().add(productDescription);
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.catalog.Product))
			return false;
		else {
			com.salesmanager.core.entity.catalog.Product product = (com.salesmanager.core.entity.catalog.Product) obj;
			return (this.getProductId() == product.getProductId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getProductId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public java.util.Set<com.salesmanager.core.entity.catalog.ProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(
			java.util.Set<com.salesmanager.core.entity.catalog.ProductAttribute> attributes) {
		this.attributes = attributes;
	}

	public java.util.Set<com.salesmanager.core.entity.catalog.ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(
			java.util.Set<com.salesmanager.core.entity.catalog.ProductPrice> prices) {
		this.prices = prices;
	}

	public java.util.Set<com.salesmanager.core.entity.catalog.Special> getSpecials() {
		return specials;
	}

	public Special getSpecial() {

		Special sp = null;

		if (this.specials != null && this.specials.size() > 0) {
			Special[] spArray = (Special[]) specials
					.toArray(new Special[specials.size()]);
			if (spArray != null && spArray.length > 0) {
				sp = spArray[0];
			}
		}

		return sp;
	}

	public void setSpecials(java.util.Set<Special> specials) {
		this.specials = specials;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setLocale(Locale locale, String currency) {
		this.locale = locale;
		this.currency = currency;
	}

	public String getFormatHTMLProductPrice() {
		return ProductUtil
				.formatHTMLProductPrice(locale, currency, this, false, false);
	}
	
	public String getFormatHTMLShortProductPrice() {
		return ProductUtil
				.formatHTMLProductPrice(locale, currency, this, false, true);
	}

	public BigDecimal getPrice() {
		return ProductUtil.determinePrice(this, this.locale, this.currency);
	}

	public boolean isAvailable() {
		if (this.getProductDateAvailable().before(new Date())
				&& this.productStatus && this.productQuantity > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getExternalLinkDownload() {
		
		String downloadLink = this.getProductExternalDl();
		
		if (this.getDescriptions() != null
				&& this.getDescriptions().size() > 0) {
			ProductDescription[] descArray = (ProductDescription[]) this
					.getDescriptions().toArray(
							new ProductDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				downloadLink = descArray[0].getProductExternalDl();
			}
		}
		
		return downloadLink;
		
	}
	


	public String getLargeImagePath() {
		return FileUtil.getLargeProductImagePath(this.getMerchantId(), this
				.getProductImage());
	}

	public String getSmallImagePath() {
		return FileUtil.getSmallProductImagePath(this.getMerchantId(), this
				.getProductImage());
	}

	public String getProductImagePath() {
		return FileUtil.getProductImagePath(this.getMerchantId(), this
				.getProductImage());
	}

	public boolean isProductImageCrop() {
		return productImageCrop;
	}

	public void setProductImageCrop(boolean productImageCrop) {
		this.productImageCrop = productImageCrop;
	}

	public boolean isDiscount() {
		return ProductUtil.hasDiscount(this);
	}

	public String getProductExternalDl() {
		return productExternalDl;
	}

	public void setProductExternalDl(String productExternalDl) {
		this.productExternalDl = productExternalDl;
	}

	public java.lang.String getProductImage1() {
		return productImage1;
	}

	public void setProductImage1(java.lang.String productImage1) {
		this.productImage1 = productImage1;
	}

	public java.lang.String getProductImage2() {
		return productImage2;
	}

	public void setProductImage2(java.lang.String productImage2) {
		this.productImage2 = productImage2;
	}

	public java.lang.String getProductImage3() {
		return productImage3;
	}

	public void setProductImage3(java.lang.String productImage3) {
		this.productImage3 = productImage3;
	}

	public java.lang.String getProductImage4() {
		return productImage4;
	}

	public void setProductImage4(java.lang.String productImage4) {
		this.productImage4 = productImage4;
	}

	public boolean getAdditionalImages() {
		
		if(!StringUtils.isBlank(this.getProductImage1())) {
			return true;
		}
		if(!StringUtils.isBlank(this.getProductImage2())) {
			return true;
		}
		if(!StringUtils.isBlank(this.getProductImage3())) {
			return true;
		}
		if(!StringUtils.isBlank(this.getProductImage4())) {
			return true;
		}
		
		return false;
		
	}
	public List<String> getImagesPath() {
		
		List returnList = new ArrayList();
		if(!StringUtils.isBlank(this.getProductImage1())) {
			returnList.add(FileUtil.getProductImagePath(this.getMerchantId(), this
					.getProductImage1()));
		}
		if(!StringUtils.isBlank(this.getProductImage2())) {
			returnList.add(FileUtil.getProductImagePath(this.getMerchantId(), this
					.getProductImage2()));
		}
		if(!StringUtils.isBlank(this.getProductImage3())) {
			returnList.add(FileUtil.getProductImagePath(this.getMerchantId(), this
					.getProductImage3()));
		}
		if(!StringUtils.isBlank(this.getProductImage4())) {
			returnList.add(FileUtil.getProductImagePath(this.getMerchantId(), this
					.getProductImage4()));
		}
		return returnList;
	}

	public java.math.BigDecimal getProductReviewAvg() {
		return productReviewAvg;
	}
	
	public double getProductReview() {
		if(this.getProductReviewAvg()!=null) {
			return this.getProductReviewAvg().doubleValue();
		}
		return 0;
	}
	
	public int getProductReviewRound() {
		if(this.getProductReviewAvg()!=null) {
			return new Double(this.getProductReviewAvg().doubleValue()).intValue();
		}
		return 0;
	}

	public void setProductReviewAvg(java.math.BigDecimal productReviewAvg) {
		this.productReviewAvg = productReviewAvg;
	}

	public int getProductReviewCount() {
		return productReviewCount;
	}

	public void setProductReviewCount(int productReviewCount) {
		this.productReviewCount = productReviewCount;
	}
	
}
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.salesmanager.core.util.PropertiesUtil;

/**
 * This is an object that contains data related to the products_description
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="products_description"
 */
@Indexed
public class ProductDescription implements Serializable {

	public static String REF = "ProductDescription";
	public static String PROP_PRODUCT_VIEWED = "productViewed";
	public static String PROP_PRODUCT_DESCRIPTION = "productDescription";
	public static String PROP_PRODUCT_NAME = "productName";
	public static String PROP_PRODUCT_URL = "productUrl";
	public static String PROP_PRODUCT_HIGHLIGHT = "productHighlight";
	public static String PROP_ID = "id";
	private static Logger log = Logger.getLogger(ProductDescription.class);

	private static boolean multipleMerchants = true;

	static {
		try {
			multipleMerchants = PropertiesUtil.getConfiguration().getBoolean(
					"core.multiplemerchants");
		} catch (Exception e) {
			log.warn("Error while getting property core.multiplemerchants");
		}
	}

	// constructors
	public ProductDescription() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProductDescription(
			com.salesmanager.core.entity.catalog.ProductDescriptionId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	@DocumentId
	@FieldBridge(impl = com.salesmanager.core.entity.catalog.ProductDescriptionIdPkBridge.class)
	private com.salesmanager.core.entity.catalog.ProductDescriptionId id;

	// fields
	//@Field
	@Field(index = org.hibernate.search.annotations.Index.UN_TOKENIZED, store = Store.NO)
	private java.lang.String productName;
	//@Field
	@Field(index = org.hibernate.search.annotations.Index.UN_TOKENIZED, store = Store.NO)
	private java.lang.String productDescription;

	private java.lang.String productUrl;
	private java.lang.Integer productViewed;
	private java.lang.String productHighlight;
	private String productExternalDl;

	@ContainedIn
	private Product product;

	private String seUrlSrc;
	private String seUrl;

	private String metatagTitle;
	private String metatagKeywords;
	private String metatagDescription;
	
	private String productTitle;

	public String getMetatagTitle() {
		return metatagTitle;
	}

	public void setMetatagTitle(String metatagTitle) {
		this.metatagTitle = metatagTitle;
	}

	public String getMetatagKeywords() {
		return metatagKeywords;
	}

	public void setMetatagKeywords(String metatagKeywords) {
		this.metatagKeywords = metatagKeywords;
	}

	public String getMetatagDescription() {
		return metatagDescription;
	}

	public void setMetatagDescription(String metatagDescription) {
		this.metatagDescription = metatagDescription;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public com.salesmanager.core.entity.catalog.ProductDescriptionId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(
			com.salesmanager.core.entity.catalog.ProductDescriptionId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: products_name
	 */
	public java.lang.String getProductName() {
		return productName;
	}

	/**
	 * Set the value related to the column: products_name
	 * 
	 * @param productName
	 *            the products_name value
	 */
	public void setProductName(java.lang.String productName) {
		this.productName = productName;
	}

	/**
	 * Return the value associated with the column: products_description
	 */
	public java.lang.String getProductDescription() {
		return productDescription;
	}

	/**
	 * Set the value related to the column: products_description
	 * 
	 * @param productDescription
	 *            the products_description value
	 */
	public void setProductDescription(java.lang.String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * Return the value associated with the column: products_url
	 */
	public java.lang.String getProductUrl() {
		return productUrl;
	}

	/**
	 * Set the value related to the column: products_url
	 * 
	 * @param productUrl
	 *            the products_url value
	 */
	public void setProductUrl(java.lang.String productUrl) {
		this.productUrl = productUrl;
	}

	/**
	 * Return the value associated with the column: products_viewed
	 */
	public java.lang.Integer getProductViewed() {
		return productViewed;
	}

	/**
	 * Set the value related to the column: products_viewed
	 * 
	 * @param productViewed
	 *            the products_viewed value
	 */
	public void setProductViewed(java.lang.Integer productViewed) {
		this.productViewed = productViewed;
	}

	/**
	 * Return the value associated with the column: products_highlight
	 */
	public java.lang.String getProductHighlight() {
		return productHighlight;
	}

	/**
	 * Set the value related to the column: products_highlight
	 * 
	 * @param productHighlight
	 *            the products_highlight value
	 */
	public void setProductHighlight(java.lang.String productHighlight) {
		this.productHighlight = productHighlight;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.catalog.ProductDescription))
			return false;
		else {
			com.salesmanager.core.entity.catalog.ProductDescription productDescription = (com.salesmanager.core.entity.catalog.ProductDescription) obj;
			if (null == this.getId() || null == productDescription.getId())
				return false;
			else
				return (this.getId().equals(productDescription.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSeUrl() {
		return seUrl;
	}

	public void setSeUrl(String seUrl) {
		this.seUrl = seUrl;
	}

	public String getSeUrlSrc() {
		return seUrlSrc;
	}

	public void setSeUrlSrc(String seUrlSrc) {
		this.seUrlSrc = seUrlSrc;
	}

	public String getUrl() {
		StringBuffer prod = new StringBuffer();

		if (!StringUtils.isBlank(this.getSeUrl())) {
			prod.append(this.getSeUrl());
		} else {
			prod.append(String.valueOf(this.getId().getProductId()));
		}

		/*
		 * if(this.multipleMerchants) { if(this.getProduct()!=null) {
		 * prod.append
		 * ("?merchantId=").append(this.getProduct().getMerchantId()); } }
		 */
		return prod.toString();
	}

	public String getProductExternalDl() {
		return productExternalDl;
	}

	public void setProductExternalDl(String productExternalDl) {
		this.productExternalDl = productExternalDl;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

}
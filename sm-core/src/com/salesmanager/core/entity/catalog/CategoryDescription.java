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

import com.salesmanager.core.util.PropertiesUtil;

/**
 * This is an object that contains data related to the categories_description
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="categories_description"
 */
// @Indexed
public class CategoryDescription implements Serializable {

	public static String REF = "CategoryDescription";
	public static String PROP_CATEGORY_NAME = "categoryName";
	public static String PROP_CATEGORY_DESCRIPTION = "categoryDescription";
	public static String PROP_ID = "id";
	private static Logger log = Logger.getLogger(CategoryDescription.class);

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
	public CategoryDescription() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CategoryDescription(
			com.salesmanager.core.entity.catalog.CategoryDescriptionId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private com.salesmanager.core.entity.catalog.CategoryDescriptionId id;

	// fields
	// @DocumentId
	private java.lang.String categoryName;
	private java.lang.String categoryDescription;

	private Category category;

	private String seUrlSrc;
	// @Field
	private String seUrl;

	public String getUrl() {

		StringBuffer cat = new StringBuffer();

		if (!StringUtils.isBlank(this.getSeUrl())) {
			cat.append(this.getSeUrl());
		} else {
			cat.append(String.valueOf(this.getId().getCategoryId()));
		}

		if (this.multipleMerchants) {
			if (this.getCategory() != null) {
				cat.append("?merchantId=").append(
						this.getCategory().getMerchantId());
			}
		}
		return cat.toString();
	}

	private String metatagTitle;
	private String metatagKeywords;
	private String metatagDescription;
	
	private String categoryTitle;

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
	public com.salesmanager.core.entity.catalog.CategoryDescriptionId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(
			com.salesmanager.core.entity.catalog.CategoryDescriptionId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: categories_name
	 */
	public java.lang.String getCategoryName() {
		return categoryName;
	}

	/**
	 * Set the value related to the column: categories_name
	 * 
	 * @param categoryName
	 *            the categories_name value
	 */
	public void setCategoryName(java.lang.String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * Return the value associated with the column: categories_description
	 */
	public java.lang.String getCategoryDescription() {
		return categoryDescription;
	}

	/**
	 * Set the value related to the column: categories_description
	 * 
	 * @param categoryDescription
	 *            the categories_description value
	 */
	public void setCategoryDescription(java.lang.String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String toString() {
		return super.toString();
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime
				* result
				+ ((categoryDescription == null) ? 0 : categoryDescription
						.hashCode());
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + hashCode;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((metatagDescription == null) ? 0 : metatagDescription
						.hashCode());
		result = prime * result
				+ ((metatagKeywords == null) ? 0 : metatagKeywords.hashCode());
		result = prime * result
				+ ((metatagTitle == null) ? 0 : metatagTitle.hashCode());
		result = prime * result + ((seUrl == null) ? 0 : seUrl.hashCode());
		result = prime * result
				+ ((seUrlSrc == null) ? 0 : seUrlSrc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryDescription other = (CategoryDescription) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryDescription == null) {
			if (other.categoryDescription != null)
				return false;
		} else if (!categoryDescription.equals(other.categoryDescription))
			return false;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (hashCode != other.hashCode)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (metatagDescription == null) {
			if (other.metatagDescription != null)
				return false;
		} else if (!metatagDescription.equals(other.metatagDescription))
			return false;
		if (metatagKeywords == null) {
			if (other.metatagKeywords != null)
				return false;
		} else if (!metatagKeywords.equals(other.metatagKeywords))
			return false;
		if (metatagTitle == null) {
			if (other.metatagTitle != null)
				return false;
		} else if (!metatagTitle.equals(other.metatagTitle))
			return false;
		if (seUrl == null) {
			if (other.seUrl != null)
				return false;
		} else if (!seUrl.equals(other.seUrl))
			return false;
		if (seUrlSrc == null) {
			if (other.seUrlSrc != null)
				return false;
		} else if (!seUrlSrc.equals(other.seUrlSrc))
			return false;
		return true;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

}
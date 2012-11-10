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
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * This is an object that contains data related to the categories table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="categories"
 */
// @Indexed
public class Category implements Serializable {

	public static String REF = "Category";
	public static String PROP_REF_CATEGORY_LEVEL = "refCategoryLevel";
	public static String PROP_CATEGORY_IMAGE = "categoryImage";
	public static String PROP_DATE_ADDED = "dateAdded";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_REF_CATEGORY_ID = "refCategoryId";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_PARENT_ID = "parentId";
	public static String PROP_REF_CATEGORY_NAME = "refCategoryName";
	public static String PROP_REF_CATEGORY_PARENT_ID = "refCategoryParentId";
	public static String PROP_CATEGORY_STATUS = "categoryStatus";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_CATEGORY_ID = "categoryId";
	public static String PROP_REF_EXPIRED = "refExpired";

	// constructors
	public Category() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Category(long categoryId) {
		this.setCategoryId(categoryId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	// @DocumentId
	private long categoryId;

	// fields
	private java.lang.String categoryImage;
	private long parentId;
	private java.lang.Integer sortOrder;
	private java.util.Date dateAdded;
	private java.util.Date lastModified;
	private boolean categoryStatus;
	private boolean visible;
	private int refCategoryId;
	private int refCategoryLevel;
	private java.lang.String refCategoryName;
	private java.lang.String refCategoryParentId;
	private java.lang.String refExpired;
	private int merchantId;
	private Category parent;
	// private Set<Category> subCategories;
	private Set<Category> descriptions;
	private int depth;
	// @Field
	private String lineage;

	private String name;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="categories_id"
	 */
	public long getCategoryId() {
		return categoryId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param categoryId
	 *            the new ID
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Return the value associated with the column: categories_image
	 */
	public java.lang.String getCategoryImage() {
		return categoryImage;
	}

	/**
	 * Set the value related to the column: categories_image
	 * 
	 * @param categoryImage
	 *            the categories_image value
	 */
	public void setCategoryImage(java.lang.String categoryImage) {
		this.categoryImage = categoryImage;
	}

	/**
	 * Return the value associated with the column: parent_id
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * Set the value related to the column: parent_id
	 * 
	 * @param parentId
	 *            the parent_id value
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * Return the value associated with the column: sort_order
	 */
	public java.lang.Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * Set the value related to the column: sort_order
	 * 
	 * @param sortOrder
	 *            the sort_order value
	 */
	public void setSortOrder(java.lang.Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * Return the value associated with the column: date_added
	 */
	public java.util.Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Set the value related to the column: date_added
	 * 
	 * @param dateAdded
	 *            the date_added value
	 */
	public void setDateAdded(java.util.Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Return the value associated with the column: last_modified
	 */
	public java.util.Date getLastModified() {
		return lastModified;
	}

	/**
	 * Set the value related to the column: last_modified
	 * 
	 * @param lastModified
	 *            the last_modified value
	 */
	public void setLastModified(java.util.Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Return the value associated with the column: categories_status
	 */
	public boolean isCategoryStatus() {
		return categoryStatus;
	}

	/**
	 * Set the value related to the column: categories_status
	 * 
	 * @param categoryStatus
	 *            the categories_status value
	 */
	public void setCategoryStatus(boolean categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	/**
	 * Return the value associated with the column: visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set the value related to the column: visible
	 * 
	 * @param visible
	 *            the visible value
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Return the value associated with the column: RefCategoryID
	 */
	public int getRefCategoryId() {
		return refCategoryId;
	}

	/**
	 * Set the value related to the column: RefCategoryID
	 * 
	 * @param refCategoryId
	 *            the RefCategoryID value
	 */
	public void setRefCategoryId(int refCategoryId) {
		this.refCategoryId = refCategoryId;
	}

	/**
	 * Return the value associated with the column: RefCategoryLevel
	 */
	public int getRefCategoryLevel() {
		return refCategoryLevel;
	}

	/**
	 * Set the value related to the column: RefCategoryLevel
	 * 
	 * @param refCategoryLevel
	 *            the RefCategoryLevel value
	 */
	public void setRefCategoryLevel(int refCategoryLevel) {
		this.refCategoryLevel = refCategoryLevel;
	}

	/**
	 * Return the value associated with the column: RefCategoryName
	 */
	public java.lang.String getRefCategoryName() {
		return refCategoryName;
	}

	/**
	 * Set the value related to the column: RefCategoryName
	 * 
	 * @param refCategoryName
	 *            the RefCategoryName value
	 */
	public void setRefCategoryName(java.lang.String refCategoryName) {
		this.refCategoryName = refCategoryName;
	}

	/**
	 * Return the value associated with the column: RefCategoryParentID
	 */
	public java.lang.String getRefCategoryParentId() {
		return refCategoryParentId;
	}

	/**
	 * Set the value related to the column: RefCategoryParentID
	 * 
	 * @param refCategoryParentId
	 *            the RefCategoryParentID value
	 */
	public void setRefCategoryParentId(java.lang.String refCategoryParentId) {
		this.refCategoryParentId = refCategoryParentId;
	}

	/**
	 * Return the value associated with the column: RefExpired
	 */
	public java.lang.String getRefExpired() {
		return refExpired;
	}

	/**
	 * Set the value related to the column: RefExpired
	 * 
	 * @param refExpired
	 *            the RefExpired value
	 */
	public void setRefExpired(java.lang.String refExpired) {
		this.refExpired = refExpired;
	}

	public String toString() {
		return super.toString();
	}

	public String getName() {
		if (StringUtils.isBlank(this.name)) {
			if (this.descriptions != null && this.descriptions.size() == 1) {
				CategoryDescription[] descArray = (CategoryDescription[]) this
						.getDescriptions().toArray(
								new CategoryDescription[this.getDescriptions()
										.size()]);
				if (descArray != null && descArray.length > 0) {
					this.name = descArray[0].getCategoryName();
				}
			}
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	/*
	 * public Set<Category> getSubCategories() { return subCategories; }
	 * 
	 * public void setSubCategories(Set<Category> subCategories) {
	 * this.subCategories = subCategories; }
	 */

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (categoryId ^ (categoryId >>> 32));
		result = PRIME * result + merchantId;
		result = PRIME * result + (int) (parentId ^ (parentId >>> 32));
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
		final Category other = (Category) obj;
		if (categoryId != other.categoryId)
			return false;
		if (merchantId != other.merchantId)
			return false;
		if (parentId != other.parentId)
			return false;
		return true;
	}

	public Set<Category> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<Category> descriptions) {
		this.descriptions = descriptions;
	}

	public CategoryDescription getCategoryDescription() {

		CategoryDescription desc = null;
		if (this.getDescriptions() != null && this.getDescriptions().size() > 0) {
			CategoryDescription[] descArray = (CategoryDescription[]) this
					.getDescriptions().toArray(
							new CategoryDescription[this.getDescriptions()
									.size()]);
			if (descArray != null && descArray.length > 0) {
				desc = descArray[0];
			}
		}
		return desc;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

}
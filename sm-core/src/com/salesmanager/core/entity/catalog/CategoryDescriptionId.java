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

public class CategoryDescriptionId implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private long categoryId;
	private int languageId;

	public CategoryDescriptionId() {
	}

	public CategoryDescriptionId(long categoryId, int languageId) {

		this.setCategoryId(categoryId);
		this.setLanguageId(languageId);
	}

	/**
	 * Return the value associated with the column: categories_id
	 */
	public long getCategoryId() {
		return categoryId;
	}

	/**
	 * Set the value related to the column: categories_id
	 * 
	 * @param categoryId
	 *            the categories_id value
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Return the value associated with the column: language_id
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * Set the value related to the column: language_id
	 * 
	 * @param languageId
	 *            the language_id value
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (categoryId ^ (categoryId >>> 32));
		result = PRIME * result + hashCode;
		result = PRIME * result + languageId;
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
		final CategoryDescriptionId other = (CategoryDescriptionId) obj;
		if (categoryId != other.categoryId)
			return false;
		if (hashCode != other.hashCode)
			return false;
		if (languageId != other.languageId)
			return false;
		return true;
	}

}
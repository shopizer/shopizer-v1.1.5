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
package com.salesmanager.core.entity.reference;

import java.io.Serializable;
import java.util.Locale;

import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;

/**
 * This is an object that contains data related to the languages table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 * 
 * @hibernate.class table="languages"
 */

public class Language implements Serializable, I18NEntity {

	public static String REF = "Language";
	public static String PROP_IMAGE = "image";
	public static String PROP_CODE = "code";
	public static String PROP_NAME = "name";
	public static String PROP_DIRECTORY = "directory";
	public static String PROP_LANGUAGE_ID = "languageId";
	public static String PROP_SORT_ORDER = "sortOrder";

	private Locale locale;

	// constructors
	public Language() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Language(int languageId) {
		this.setLanguageId(languageId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int languageId;

	// fields
	private java.lang.String name;
	private java.lang.String code;
	private java.lang.String image;
	private java.lang.String directory;
	private java.lang.Integer sortOrder;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="languages_id"
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param languageId
	 *            the new ID
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * 
	 * @param name
	 *            the name value
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: code
	 */
	public java.lang.String getCode() {
		return code;
	}

	/**
	 * Set the value related to the column: code
	 * 
	 * @param code
	 *            the code value
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * Return the value associated with the column: image
	 */
	public java.lang.String getImage() {
		return image;
	}

	/**
	 * Set the value related to the column: image
	 * 
	 * @param image
	 *            the image value
	 */
	public void setImage(java.lang.String image) {
		this.image = image;
	}

	/**
	 * Return the value associated with the column: directory
	 */
	public java.lang.String getDirectory() {
		return directory;
	}

	/**
	 * Set the value related to the column: directory
	 * 
	 * @param directory
	 *            the directory value
	 */
	public void setDirectory(java.lang.String directory) {
		this.directory = directory;
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

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.Language))
			return false;
		else {
			com.salesmanager.core.entity.reference.Language language = (com.salesmanager.core.entity.reference.Language) obj;
			return (this.getLanguageId() == language.getLanguageId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getLanguageId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		if (this.locale == null) {
			locale = LocaleUtil.getDefaultLocale();
		}
		LabelUtil l = LabelUtil.getInstance();
		l.setLocale(locale);
		return l.getText(locale, "label.language." + this.getCode());
	}

	public void setLocale(Locale locale) {
		// TODO Auto-generated method stub
		this.locale = locale;

	}

	public void setLocale(Locale locale, String currency) {
		// TODO Auto-generated method stub
		this.locale = locale;

	}

}
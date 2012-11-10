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
package com.salesmanager.core.entity.merchant;

import java.io.Serializable;

/**
 * This is an object that contains data related to the merchant_information
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="merchant_information"
 */

public class MerchantUserInformation implements Serializable, IMerchant {

	public static String REF = "MerchantInformation";
	public static String PROP_STATUS = "status";
	public static String PROP_TOKEN = "token";
	public static String PROP_USERLNAME = "userlname";
	public static String PROP_USERCOUNTRYCODE = "usercountrycode";
	public static String PROP_WEB_SITE = "webSite";
	public static String PROP_ADMIN_EMAIL = "adminEmail";
	public static String PROP_ADMIN_PASS = "adminPass";
	public static String PROP_USERPOSTALCODE = "userpostalcode";
	public static String PROP_MERCHANT_TMP_ID = "merchantTmpId";
	public static String PROP_USERLANG = "userlang";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_USERFNAME = "userfname";
	public static String PROP_USERPHONE = "userphone";
	public static String PROP_MERCHANTID = "merchantid";
	public static String PROP_USERCITY = "usercity";
	public static String PROP_USERADDRESS = "useraddress";
	public static String PROP_ADMIN_NAME = "adminName";
	public static String PROP_USERSTATE = "userstate";

	// constructors
	public MerchantUserInformation() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public MerchantUserInformation(int merchantTmpId) {
		this.setMerchantTmpId(merchantTmpId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int merchantTmpId;

	// fields
	private Long merchantUserId;
	private java.lang.String adminName;
	private java.lang.String adminEmail;
	private java.lang.String adminPass;
	private java.lang.String webSite;
	private java.lang.String token;
	private java.lang.String userfname;
	private java.lang.String userlname;
	private java.lang.String useraddress;
	private java.lang.String usercity;
	private java.lang.String userpostalcode;
	private String userstate;
	private int usercountrycode;
	private java.lang.String userphone;
	private java.util.Date lastModified;
	private int merchantId;
	private java.lang.String userlang;
	private int status;
	
	private String securityQuestion1;
	private String securityQuestion2;
	private String securityQuestion3;
	private String securityAnswer1;
	private String securityAnswer2;
	private String securityAnswer3;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="merchant_tmp_id"
	 */
	public int getMerchantTmpId() {
		return merchantTmpId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param merchantTmpId
	 *            the new ID
	 */
	public void setMerchantTmpId(int merchantTmpId) {
		this.merchantTmpId = merchantTmpId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: admin_name
	 */
	public java.lang.String getAdminName() {
		return adminName;
	}

	/**
	 * Set the value related to the column: admin_name
	 * 
	 * @param adminName
	 *            the admin_name value
	 */
	public void setAdminName(java.lang.String adminName) {
		this.adminName = adminName;
	}

	/**
	 * Return the value associated with the column: admin_email
	 */
	public java.lang.String getAdminEmail() {
		return adminEmail;
	}

	/**
	 * Set the value related to the column: admin_email
	 * 
	 * @param adminEmail
	 *            the admin_email value
	 */
	public void setAdminEmail(java.lang.String adminEmail) {
		this.adminEmail = adminEmail;
	}

	/**
	 * Return the value associated with the column: admin_pass
	 */
	public java.lang.String getAdminPass() {
		return adminPass;
	}

	/**
	 * Set the value related to the column: admin_pass
	 * 
	 * @param adminPass
	 *            the admin_pass value
	 */
	public void setAdminPass(java.lang.String adminPass) {
		this.adminPass = adminPass;
	}

	/**
	 * Return the value associated with the column: web_site
	 */
	public java.lang.String getWebSite() {
		return webSite;
	}

	/**
	 * Set the value related to the column: web_site
	 * 
	 * @param webSite
	 *            the web_site value
	 */
	public void setWebSite(java.lang.String webSite) {
		this.webSite = webSite;
	}

	/**
	 * Return the value associated with the column: token
	 */
	public java.lang.String getToken() {
		return token;
	}

	/**
	 * Set the value related to the column: token
	 * 
	 * @param token
	 *            the token value
	 */
	public void setToken(java.lang.String token) {
		this.token = token;
	}

	/**
	 * Return the value associated with the column: userfname
	 */
	public java.lang.String getUserfname() {
		return userfname;
	}

	/**
	 * Set the value related to the column: userfname
	 * 
	 * @param userfname
	 *            the userfname value
	 */
	public void setUserfname(java.lang.String userfname) {
		this.userfname = userfname;
	}

	/**
	 * Return the value associated with the column: userlname
	 */
	public java.lang.String getUserlname() {
		return userlname;
	}

	/**
	 * Set the value related to the column: userlname
	 * 
	 * @param userlname
	 *            the userlname value
	 */
	public void setUserlname(java.lang.String userlname) {
		this.userlname = userlname;
	}

	/**
	 * Return the value associated with the column: useraddress
	 */
	public java.lang.String getUseraddress() {
		return useraddress;
	}

	/**
	 * Set the value related to the column: useraddress
	 * 
	 * @param useraddress
	 *            the useraddress value
	 */
	public void setUseraddress(java.lang.String useraddress) {
		this.useraddress = useraddress;
	}

	/**
	 * Return the value associated with the column: usercity
	 */
	public java.lang.String getUsercity() {
		return usercity;
	}

	/**
	 * Set the value related to the column: usercity
	 * 
	 * @param usercity
	 *            the usercity value
	 */
	public void setUsercity(java.lang.String usercity) {
		this.usercity = usercity;
	}

	/**
	 * Return the value associated with the column: userpostalcode
	 */
	public java.lang.String getUserpostalcode() {
		return userpostalcode;
	}

	/**
	 * Set the value related to the column: userpostalcode
	 * 
	 * @param userpostalcode
	 *            the userpostalcode value
	 */
	public void setUserpostalcode(java.lang.String userpostalcode) {
		this.userpostalcode = userpostalcode;
	}

	/**
	 * Return the value associated with the column: userstate
	 */
	public String getUserstate() {
		return userstate;
	}

	/**
	 * Set the value related to the column: userstate
	 * 
	 * @param userstate
	 *            the userstate value
	 */
	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}

	/**
	 * Return the value associated with the column: usercountrycode
	 */
	public int getUsercountrycode() {
		return usercountrycode;
	}

	/**
	 * Set the value related to the column: usercountrycode
	 * 
	 * @param usercountrycode
	 *            the usercountrycode value
	 */
	public void setUsercountrycode(int usercountrycode) {
		this.usercountrycode = usercountrycode;
	}

	/**
	 * Return the value associated with the column: userphone
	 */
	public java.lang.String getUserphone() {
		return userphone;
	}

	/**
	 * Set the value related to the column: userphone
	 * 
	 * @param userphone
	 *            the userphone value
	 */
	public void setUserphone(java.lang.String userphone) {
		this.userphone = userphone;
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
	 * Return the value associated with the column: userlang
	 */
	public java.lang.String getUserlang() {
		return userlang;
	}

	/**
	 * Set the value related to the column: userlang
	 * 
	 * @param userlang
	 *            the userlang value
	 */
	public void setUserlang(java.lang.String userlang) {
		this.userlang = userlang;
	}

	/**
	 * Return the value associated with the column: status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Set the value related to the column: status
	 * 
	 * @param status
	 *            the status value
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.merchant.MerchantUserInformation))
			return false;
		else {
			com.salesmanager.core.entity.merchant.MerchantUserInformation merchantInformation = (com.salesmanager.core.entity.merchant.MerchantUserInformation) obj;
			return (this.getMerchantTmpId() == merchantInformation
					.getMerchantTmpId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getMerchantTmpId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

	public Long getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(Long merchantUserId) {
		this.merchantUserId = merchantUserId;
	}

	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	public String getSecurityQuestion3() {
		return securityQuestion3;
	}

	public void setSecurityQuestion3(String securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}

	public String getSecurityAnswer1() {
		return securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	public String getSecurityAnswer2() {
		return securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

	public String getSecurityAnswer3() {
		return securityAnswer3;
	}

	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}

}
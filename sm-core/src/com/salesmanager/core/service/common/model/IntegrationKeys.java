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
package com.salesmanager.core.service.common.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;

/**
 * Used for containing the shipping / payment and other integration system keys
 * 
 * @author Carl Samson
 * 
 */
public class IntegrationKeys implements ParsableConfiguration {

	private String userid;
	private String password;
	private String transactionKey;
	private String key1;
	private String key2;
	private String key3;
	private String key4;

	private Map customkeys = new HashMap();

	public Map getCustomkeys() {
		return customkeys;
	}

	public void setCustomkeys(Map customkeys) {
		this.customkeys = customkeys;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		if (!StringUtils.isBlank(key1)) {
			key1.trim();
		}
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		if (!StringUtils.isBlank(key2)) {
			key2.trim();
		}
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		if (!StringUtils.isBlank(key3)) {
			key3.trim();
		}
		this.key3 = key3;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (!StringUtils.isBlank(password)) {
			password.trim();
		}
		this.password = password;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		if (!StringUtils.isBlank(userid)) {
			userid.trim();
		}
		this.userid = userid;
	}

	public void parse(MerchantConfiguration conf) {

	}

	public String getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(String transactionKey) {
		if (!StringUtils.isBlank(transactionKey)) {
			transactionKey.trim();
		}
		this.transactionKey = transactionKey;
	}

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		if (!StringUtils.isBlank(key4)) {
			key4.trim();
		}
		this.key4 = key4;
	}

}

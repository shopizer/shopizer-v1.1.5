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
package com.salesmanager.core.service.merchant;

public class ConfigurationRequest {

	private int merchantid;
	private boolean like = false;
	private String configurationkey;

	/**
	 * This constructor is used to retreive one configuration line if like is
	 * set to false, it will match configurationkey (configuration_key). It will
	 * match many configuration lines if like is set to true.
	 * 
	 * @param merchantid
	 * @param like
	 * @param configurationkey
	 */
	public ConfigurationRequest(int merchantid, boolean like,
			String configurationkey) {
		super();
		this.merchantid = merchantid;
		this.like = like;
		this.configurationkey = configurationkey;
	}

	/**
	 * Retreives the information for a given configuration key
	 * 
	 * @param merchantid
	 * @param configurationkey
	 */
	public ConfigurationRequest(int merchantid, String configurationkey) {
		super();
		this.merchantid = merchantid;
		this.configurationkey = configurationkey;
	}

	/**
	 * Retreives the information for a given merchantId
	 * 
	 * @param merchantid
	 */
	public ConfigurationRequest(int merchantid) {
		super();
		this.merchantid = merchantid;
	}

	public int getMerchantid() {
		return merchantid;
	}

	public String getConfigurationkey() {
		return configurationkey;
	}

	public boolean isLike() {
		return like;
	}

	public void setMerchantid(int merchantid) {
		this.merchantid = merchantid;
	}

	public void setConfigurationkey(String configurationkey) {
		this.configurationkey = configurationkey;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

}

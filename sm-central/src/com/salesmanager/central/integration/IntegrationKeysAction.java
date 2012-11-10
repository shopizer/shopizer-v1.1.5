/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.integration;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.invoice.InvoiceDetailsAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;





public class IntegrationKeysAction extends BaseAction {
	
	
	private String googleapi;
	private String analytics;
/*	private String fbkey;
	private String fbsecret;*/
	
	private Logger log = Logger.getLogger(IntegrationKeysAction.class);
	
	
	public String displayPage() throws Exception {
		
		super.setPageTitle("label.menu.function.INTKEYO01");
		
		
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		Context ctx = super.getContext();
		
		// get analytics
		ConfigurationRequest req = new ConfigurationRequest(
				ctx.getMerchantid(), ConfigurationConstants.G_API);
		ConfigurationResponse resp = mservice.getConfiguration(req);

		MerchantConfiguration googleCode = resp
				.getMerchantConfiguration(ConfigurationConstants.G_API);
		
		if (googleCode != null) {
			analytics = googleCode.getConfigurationValue();
			googleapi = googleCode.getConfigurationValue1();
		}
		
/*		req = new ConfigurationRequest(
				ctx.getMerchantid(), ConfigurationConstants.FB_API);
		resp = mservice.getConfiguration(req);
		
		MerchantConfiguration fbCode = resp
		.getMerchantConfiguration(ConfigurationConstants.FB_API);
		
		if (fbCode != null) {
			fbkey = fbCode.getConfigurationValue();
			fbsecret = fbCode.getConfigurationValue1();
		}*/
		
		
		return SUCCESS;
		
	}
	
	public String editConfigurationKeys() throws Exception {
		
		super.setPageTitle("label.menu.function.INTKEYO01");
		
		Context ctx = super.getContext();
		
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		// get analytics
		ConfigurationRequest req = new ConfigurationRequest(
				ctx.getMerchantid(), ConfigurationConstants.G_API);
		ConfigurationResponse resp = mservice.getConfiguration(req);

		MerchantConfiguration googleCode = resp
				.getMerchantConfiguration(ConfigurationConstants.G_API);
		
		// google
		if (!StringUtils.isBlank(analytics) || !StringUtils.isBlank(googleapi)) {
			if (googleCode == null) {
				googleCode = new MerchantConfiguration();
				googleCode
						.setConfigurationKey(ConfigurationConstants.G_API);
				googleCode.setMerchantId(ctx.getMerchantid());
				googleCode.setDateAdded(new Date());
				googleCode.setLastModified(new Date());

			}
			if (!StringUtils.isBlank(analytics)) {
				googleCode.setConfigurationValue(analytics);
			} else {
				googleCode.setConfigurationValue("");
			}

			if (!StringUtils.isBlank(googleapi)) {
				googleCode.setConfigurationValue1(googleapi);
				ctx.setGcode(googleapi);
			} else {
				googleCode.setConfigurationValue1("");
			}

			mservice.saveOrUpdateMerchantConfiguration(googleCode);
		} else if (StringUtils.isBlank(analytics)
				|| StringUtils.isBlank(googleapi)) {

			
			if(StringUtils.isBlank(analytics) && StringUtils.isBlank(googleapi)) {
				
				if (googleCode != null) {
					ctx.setGcode(null);
					mservice.deleteMerchantConfiguration(googleCode);
				}
				
			} else {
				
				if (googleCode != null) {
					if(StringUtils.isBlank(googleapi)) {
						ctx.setGcode(null);
						googleCode.setConfigurationValue1("");
					}
					if(StringUtils.isBlank(analytics)) {
						ctx.setGcode(null);
						googleCode.setConfigurationValue("");
					}
					googleCode.setLastModified(new Date());
					mservice.saveOrUpdateMerchantConfiguration(googleCode);
				}
				
			}
			

		}
		
/*		
		req = new ConfigurationRequest(
				ctx.getMerchantid(), ConfigurationConstants.FB_API);
		resp = mservice.getConfiguration(req);
		
		MerchantConfiguration fbCode = resp
		.getMerchantConfiguration(ConfigurationConstants.FB_API);
		
		
		// fb
		if (!StringUtils.isBlank(fbkey) || !StringUtils.isBlank(fbsecret)) {
			if (fbCode == null) {
				fbCode = new MerchantConfiguration();
				fbCode
						.setConfigurationKey(ConfigurationConstants.FB_API);
				fbCode.setMerchantId(ctx.getMerchantid());
				fbCode.setDateAdded(new Date());
				fbCode.setLastModified(new Date());

			}
			if (!StringUtils.isBlank(fbkey)) {
				fbCode.setConfigurationValue(fbkey);
			} else {
				fbCode.setConfigurationValue("");
			}

			if (!StringUtils.isBlank(fbsecret)) {
				fbCode.setConfigurationValue1(fbsecret);
			} else {
				fbCode.setConfigurationValue1("");
			}
			mservice.saveOrUpdateMerchantConfiguration(fbCode);
		} else if (StringUtils.isBlank(fbkey)
				|| StringUtils.isBlank(fbsecret)) {
			
			
			if(StringUtils.isBlank(fbkey) && StringUtils.isBlank(fbsecret)) {
				
				if (fbCode != null) {
					mservice.deleteMerchantConfiguration(fbCode);
				}
				
			} else {
				
				if (fbCode != null) {
					if(StringUtils.isBlank(fbkey)) {
						googleCode.setConfigurationValue("");
					}
					if(StringUtils.isBlank(fbsecret)) {
						googleCode.setConfigurationValue("");
					}
					fbCode.setLastModified(new Date());
					mservice.saveOrUpdateMerchantConfiguration(fbCode);
				}
				
			}

		}*/
		
		super.setSuccessMessage();
		
		return SUCCESS;
	}


	public String getGoogleapi() {
		return googleapi;
	}


	public void setGoogleapi(String googleapi) {
		this.googleapi = googleapi;
	}


	public String getAnalytics() {
		return analytics;
	}


	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}

/*
	public String getFbkey() {
		return fbkey;
	}


	public void setFbkey(String fbkey) {
		this.fbkey = fbkey;
	}


	public String getFbsecret() {
		return fbsecret;
	}


	public void setFbsecret(String fbsecret) {
		this.fbsecret = fbsecret;
	}*/

}

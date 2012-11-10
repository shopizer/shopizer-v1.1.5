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
package com.salesmanager.core.util;

import java.util.Map;

import javax.activation.DataSource;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;

public interface EmailUtil {

	public Map prepareEmailContext(
			MerchantStore profile, String lang) throws Exception;

	public void send(String to, String subject, Map entries) throws Exception;

	/**
	 * public void send(String to, String subject, String text, DataSource
	 * attachment, String attachmentFileName) throws Exception;
	 **/
	public void setEmailTemplate(String template);

}

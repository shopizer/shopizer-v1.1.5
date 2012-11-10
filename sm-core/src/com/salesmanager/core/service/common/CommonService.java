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
package com.salesmanager.core.service.common;

import java.util.Collection;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.common.impl.ModuleManagerImpl;
import com.salesmanager.core.service.common.impl.ServicesUtil;
import com.salesmanager.core.util.EmailUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
public class CommonService {

	private static Logger log = Logger.getLogger(CommonService.class);

	public Collection getModules(String countryIsoCode, int serviceCode)
			throws Exception {

		return ModuleManagerImpl.getModuleService(countryIsoCode, serviceCode);

	}

	public CoreModuleService getModule(String countryIsoCode, String moduleName)
			throws Exception {

		return ServicesUtil.getModule(countryIsoCode, moduleName);

	}

	/**
	 * Sends HTML emails. Requires a sendto valid email address, a subject, the
	 * originator's merchant id, a map that contains key-value pairs that will
	 * be parsed in the HTML template, as well as the html template file to be
	 * used
	 * 
	 * @param sendto
	 * @param subject
	 * @param merchantid
	 * @param keyvalueparseableelements
	 * @param emailtemplatename
	 * @throws Exception
	 */
	@Transactional
	public void sendHtmlEmail(String sendto, String subject,
			MerchantStore profile,
			Map keyvalueparseableelements, String emailtemplatename, String lang)
			throws Exception {

		try {

			EmailUtil emailhelper = (EmailUtil) SpringUtil
					.getBean("htmlEmailSender");
			emailhelper.setEmailTemplate(emailtemplatename);

			Map contour = emailhelper.prepareEmailContext(profile, lang);

			contour.putAll(keyvalueparseableelements);

			emailhelper.send(sendto, subject, contour);

		} finally {

		}

	}

}

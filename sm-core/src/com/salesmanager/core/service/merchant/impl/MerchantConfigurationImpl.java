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
package com.salesmanager.core.service.merchant.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.common.model.ConfigurableModule;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantConfigurationDao;
import com.salesmanager.core.util.SpringUtil;

@Component
public class MerchantConfigurationImpl {

	private Logger log = Logger.getLogger(MerchantConfigurationImpl.class);

	@Autowired
	private IMerchantConfigurationDao merchantConfigurationDao;

	@Transactional
	public ConfigurationResponse getConfigurationVO(String moduleName,
			int merchantId) throws Exception {

		Collection coll = merchantConfigurationDao.findByModule(moduleName,
				merchantId);
		ConfigurationResponse vo = new ConfigurationResponse();

		if (coll != null) {

			Iterator it = coll.iterator();
			while (it.hasNext()) {
				MerchantConfiguration conf = (MerchantConfiguration) it.next();
				// Class clz = ModuleManagerImpl.getClass(moduleName);
				ConfigurableModule module = (ConfigurableModule) SpringUtil
						.getBean(moduleName);
				if (module != null) {
					// ConfigurableModule mod =
					// (ConfigurableModule)clz.newInstance();
					module.getConfiguration(conf, vo);
					continue;
				} else {
					log
							.warn("No implementation found for module "
									+ moduleName);
				}
			}

		}

		return vo;

	}

	@Transactional
	public ConfigurationResponse getConfigurationVO(ConfigurationRequest request)
			throws Exception {

		List configs;

		if (request.isLike()) {
			configs = merchantConfigurationDao.findListByLike(request
					.getConfigurationkey(), request.getMerchantid());
		} else {
			if (!StringUtils.isBlank(request.getConfigurationkey())) {
				configs = merchantConfigurationDao.findListByKey(request
						.getConfigurationkey(), request.getMerchantid());
			} else {
				configs = merchantConfigurationDao.findListMerchantId(request
						.getMerchantid());
			}
		}

		ConfigurationResponse vo = new ConfigurationResponse();
		vo.setConfigurationkey(request.getConfigurationkey());

		if (configs != null) {

			Iterator it = configs.iterator();
			while (it.hasNext()) {
				MerchantConfiguration c = (MerchantConfiguration) it.next();
				String key = c.getConfigurationKey();

				String module = c.getConfigurationModule();
				if (module != null && !module.trim().equals("")) {
					// Class clz = ModuleManagerImpl.getClass(module);
					ConfigurableModule mod = (ConfigurableModule) SpringUtil
							.getBean(module);
					if (mod != null) {
						// ConfigurableModule mod =
						// (ConfigurableModule)clz.newInstance();
						mod.getConfiguration(c, vo);
						continue;
					} else {
						log
								.warn("No implementation found for module "
										+ module);
					}
				} else {
					vo.addMerchantConfiguration(c);
				}
			}
		}

		return vo;

	}

	@Transactional
	public ConfigurationResponse getConfigurationVOByModule(
			ConfigurationRequest request, String moduleName) throws Exception {

		Collection configs;

		configs = merchantConfigurationDao.findByModule(moduleName, request
				.getMerchantid());

		ConfigurationResponse vo = new ConfigurationResponse();
		vo.setConfigurationkey(request.getConfigurationkey());

		if (configs != null) {

			Iterator it = configs.iterator();
			while (it.hasNext()) {
				MerchantConfiguration c = (MerchantConfiguration) it.next();
				String key = c.getConfigurationKey();

				String module = c.getConfigurationModule();
				if (module != null && !module.trim().equals("")) {
					// Class clz = ModuleManagerImpl.getClass(module);
					ConfigurableModule mod = (ConfigurableModule) SpringUtil
							.getBean(module);
					if (mod != null) {
						// ConfigurableModule mod =
						// (ConfigurableModule)clz.newInstance();
						mod.getConfiguration(c, vo);
						continue;
					} else {
						log
								.warn("No implementation found for module "
										+ module);
					}
				} else {
					vo.addMerchantConfiguration(c);
				}
			}
		}

		return vo;

	}

}

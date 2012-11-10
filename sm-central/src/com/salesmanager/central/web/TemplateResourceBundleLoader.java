/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.web;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;

public class TemplateResourceBundleLoader extends
		javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static Logger log = Logger
			.getLogger(TemplateResourceBundleLoader.class);

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.init();
	}

	public void init() throws ServletException {

		try {

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Collection coll = rservice.getCoreModules(
					CatalogConstants.STORE_FRONT_TEMPLATES_CODE,
					Constants.ALLCOUNTRY_ISOCODE);

			if (coll == null || coll.size() == 0) {
				log.warn("No template bundle found");
			} else {
				Iterator i = coll.iterator();
				while (i.hasNext()) {
					CoreModuleService cms = (CoreModuleService) i.next();
					String fileName = cms.getCoreModuleName();
					log.info("Loading messages from catalog-" + fileName);
					LocalizedTextUtil.addDefaultResourceBundle(
							fileName);

				}
			}

		} catch (Exception e) {
			log.error(e);
		}

	}

}

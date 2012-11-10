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
package com.salesmanager.central.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.system.SystemService;

/**
 * Servlet implementation class for Servlet: FunctionLoaderServlet
 * 
 */
public class ReferenceLoaderServlet extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ReferenceLoaderServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void init() throws ServletException {

		try {

			Logger.getLogger(ReferenceLoaderServlet.class).debug(
					"********* SPRING INIT **********");
			ReferenceService ref = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Logger.getLogger(ReferenceLoaderServlet.class).debug(
					"********* SPRING LOADED **********");


			SystemService sservice = (SystemService) ServiceFactory
					.getService(ServiceFactory.SystemService);

			Collection groups = sservice.getCentralGroups();

			Collection associations = sservice
					.getCentralRegistrationAssociations();

			Collection functions = sservice.getCentralFunctions();

			MenuFactory factory = MenuFactory.getInstance();
			factory.setGroups(groups, associations);
			factory.setFunctions(functions, associations);
			factory.setFunctionsByFunctionCode(functions);


			/**
			 * Load core cache
			 */
			com.salesmanager.core.service.cache.RefCache corecache = com.salesmanager.core.service.cache.RefCache
					.getInstance();


			CatalogService catalogService = new CatalogService();
			catalogService.loadCategoriesCache();

		} catch (Exception e) {
			Logger.getLogger(ReferenceLoaderServlet.class).error(e);
		}
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
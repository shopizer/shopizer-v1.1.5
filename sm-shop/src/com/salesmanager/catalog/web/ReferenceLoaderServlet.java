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
package com.salesmanager.catalog.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;

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

		// Session session = null;

		try {

			Logger.getLogger(ReferenceLoaderServlet.class).debug(
					"********* SPRING INIT **********");
			ReferenceService ref = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Logger.getLogger(ReferenceLoaderServlet.class).debug(
					"********* SPRING LOADED **********");

			/**
			 * Load core cache
			 */
			com.salesmanager.core.service.cache.RefCache corecache = com.salesmanager.core.service.cache.RefCache
					.getInstance();
			corecache.createCache();
			/***********************************/

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
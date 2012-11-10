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
package com.salesmanager.catalog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.util.www.SalesManagerInterceptor;

public class URLInterceptor extends SalesManagerInterceptor {

	@Override
	protected String baseIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub

		/**
		 * This interceptor is used for urls invoked using SEO type urls The
		 * system is configured to support /category/category-name
		 * /product/product-name /pagelink/page-name
		 */

		// get the product from the path
		String path = StringUtils.removeStart(req.getRequestURI(), req
				.getContextPath());

		/**
		 * should have left /category/<PRODUCT-URL>?request parameters or should
		 * have left /pagelink/<PAGE-URL>?request parameters or should have left
		 * /product/<PRODUCT-URL>?request parameters keep after second / and
		 * before ?
		 */

		// remove /product or /category or /page
		String pathnocontext = path.substring(1);// remove /

		// should now have product/<PRODUCT-ID>?request parameters or
		// category/<CATEGORY-ID>?request parameters or pagelink/<PAGE-ID>
		String[] parameters = pathnocontext.split("/");

		// should now have
		// parameters[0] = product or category or pagelink
		// parameters[1] = <PRODUCT-ID>?parameters

		if (parameters == null || parameters.length == 1) {
			throw new Exception("Invalid parameters in request " + path);
		}

		String id = parameters[1];
		
		if(id.contains(".action")) {
			id = id.substring(0,id.indexOf(".action"));
		}

		SalesManagerBaseAction action = ((SalesManagerBaseAction) invoke
				.getAction());

		action.setRequestedEntityId(id);

		return null;

	}

}

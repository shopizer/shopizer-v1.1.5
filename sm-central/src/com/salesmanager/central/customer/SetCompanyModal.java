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
package com.salesmanager.central.customer;

import javax.servlet.http.HttpServletRequest;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * Class used by DWR for storing selection from modal form
 * 
 * @author Administrator
 * 
 */
public class SetCompanyModal {

	private void setCompanyName(String name) {

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();
		req.getSession().setAttribute("customercompany", name);

	}

	private String getCompanyName() {

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();

		if (req.getSession().getAttribute("customercompany") != null) {
			return (String) req.getSession().getAttribute("customercompany");
		} else {
			return "";
		}

	}

}

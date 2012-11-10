/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public abstract class RoleInterceptor implements Interceptor {
	
	private Logger log = Logger
	.getLogger(RoleInterceptor.class);


	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public String intercept(ActionInvocation invoke) throws Exception {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) ServletActionContext
		.getRequest();
		HttpServletResponse resp = (HttpServletResponse) ServletActionContext
		.getResponse();
	
		try {
			
			HttpSession session = req.getSession();
			Principal p = (Principal) session.getAttribute("PRINCIPAL");
			
			if(!isUserInRole(p, req, resp)) {
				MessageUtil.addErrorMessage(req, LabelUtil
						.getInstance().getText("messages.authorization"));
					return "AUTHORIZATIONEXCEPTION";
			}
			
			return invoke.invoke();
		} catch (Exception e) {
		

			log.error(e);
			MessageUtil.addErrorMessage(req, LabelUtil
					.getInstance().getText("errors.technical") + " " + e.getMessage());
			return "GENERICERROR";
		}
	}
	
	protected abstract boolean isUserInRole(Principal principal, HttpServletRequest req, HttpServletResponse resp);

}

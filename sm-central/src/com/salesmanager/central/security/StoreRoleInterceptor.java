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

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.shipping.ShippingModuleActionInterceptor;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class StoreRoleInterceptor extends RoleInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8642268816260222840L;
	

	protected boolean isUserInRole(Principal principal, HttpServletRequest req, HttpServletResponse resp) {
		
		return com.salesmanager.central.util.SecurityUtil.isUserInRole(req, "store");
	}



}

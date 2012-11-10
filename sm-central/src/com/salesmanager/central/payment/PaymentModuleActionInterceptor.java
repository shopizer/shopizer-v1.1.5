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
package com.salesmanager.central.payment;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.salesmanager.central.util.ValidationException;

public class PaymentModuleActionInterceptor implements Interceptor {

	private Logger log = Logger.getLogger(PaymentModuleActionInterceptor.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	/**
	 * For having access to the error message
	 */
	public String intercept(ActionInvocation invoke) throws Exception {
		// TODO Auto-generated method stub

		try {
			return invoke.invoke();
		} catch (Exception e) {
			if (e instanceof ValidationException) {// do nothing

			} else {
				log.error(e);
				ActionSupport action = (ActionSupport) invoke.getAction();
				action.addActionError(action.getText("errors.technical") + " "
						+ e.getMessage());
			}
			return com.opensymphony.xwork2.Action.SUCCESS;
		}

	}

}

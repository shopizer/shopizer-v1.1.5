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
package com.salesmanager.core.module.impl.application.logon;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class CustomerLoginCallBackHandler implements CallbackHandler {

	private String userName;
	private String password;
	private int merchantId;

	public CustomerLoginCallBackHandler(String userName, String password,
			int merchantId) {
		this.userName = userName;
		this.password = password;
		this.merchantId = merchantId;
	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (Callback callBack : callbacks) {
			if (callBack instanceof NameCallback) {
				((NameCallback) callBack).setName(userName);
			}
			if (callBack instanceof PasswordCallback) {
				((PasswordCallback) callBack).setPassword(password
						.toCharArray());
			}
			if (callBack instanceof TextInputCallback) {
				((TextInputCallback) callBack).setText(String
						.valueOf(merchantId));
			}
		}
	}

}

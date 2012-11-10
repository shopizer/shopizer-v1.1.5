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

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

public class SalesManagerJAASConfiguration extends Configuration {

	AppConfigurationEntry entry = null;

	protected SalesManagerJAASConfiguration(String module) {
		AppConfigurationEntry entry = new AppConfigurationEntry(module,
				AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
				new HashMap());
		this.entry = entry;
		super.setConfiguration(this);
	}

	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String arg0) {
		// TODO Auto-generated method stub
		AppConfigurationEntry[] entries = new AppConfigurationEntry[1];
		entries[0] = entry;
		return entries;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}

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
package com.salesmanager.core.module.impl.application.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.module.model.application.PasswordGeneratorModule;
import com.salesmanager.core.util.PropertiesUtil;

/**
 * Simple module for generating passwords
 * 
 * @author Administrator
 * 
 */
public class SimplePasswordGeneratorModule implements PasswordGeneratorModule {

	/*
	 * Set of valid characters
	 */
	protected static char[] goodChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
			'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private static Configuration config = PropertiesUtil.getConfiguration();
	private static Logger log = Logger
			.getLogger(SimplePasswordGeneratorModule.class);
	private static java.util.Random r = new java.util.Random();

	private static int MINLENGTH = 8;

	static {
		try {
			MINLENGTH = config
					.getInt("core.application.module.passwordgenerator.length");
		} catch (Exception e) {
			log
					.error("Cannot find integer property core.application.module.passwordgenerator.length");
		}
	}

	public String generatePassword() throws Exception {
		// TODO Auto-generated method stub
		return getPassword();
	}

	/* Generate a Password object with a random password. */
	private String getPassword() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < MINLENGTH; i++) {
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}

}

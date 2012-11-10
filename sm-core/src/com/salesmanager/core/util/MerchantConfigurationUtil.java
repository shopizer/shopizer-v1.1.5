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
package com.salesmanager.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;

public class MerchantConfigurationUtil {

	/**
	 * Strips all delimiters
	 * 
	 * @param configurationLine
	 * @return
	 */
	public static Collection getConfigurationList(String configurationLine,
			String delimiter) {

		List returnlist = new ArrayList();
		StringTokenizer st = new StringTokenizer(configurationLine, delimiter);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			returnlist.add(token);
		}
		return returnlist;
	}

	/**
	 * Build a line with delimiter
	 * 
	 * @param configs
	 * @param delimiter
	 * @return
	 */
	public static String buildConfigurationLine(Collection<String> configs,
			String delimiter) {
		StringBuffer keyLine = new StringBuffer();

		if (configs != null && configs.size() > 0) {
			int count = 1;
			Iterator i = configs.iterator();
			while (i.hasNext()) {
				String s = (String) i.next();
				keyLine.append(s);
				if (count < configs.size()) {
					keyLine.append(delimiter);
				}
				count++;
			}
		}

		return keyLine.toString();

	}

	public static IntegrationProperties getIntegrationProperties(
			String configurationValue, String delimiter) {
		if (configurationValue == null)
			return new IntegrationProperties();
		StringTokenizer st = new StringTokenizer(configurationValue, delimiter);
		int i = 1;
		IntegrationProperties keys = new IntegrationProperties();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			if (i == 1) {
				keys.setProperties1(value);
			} else if (i == 2) {
				keys.setProperties2(value);
			} else if (i == 3) {
				keys.setProperties3(value);
			} else if (i == 4) {
				keys.setProperties4(value);
			} else {
				keys.setProperties5(value);
			}

			i++;
		}
		return keys;
	}
	
	
	
	public static IntegrationKeys getIntegrationKeys(String configvalue, String delimiter)
	throws Exception {
		if (configvalue == null)
			return new IntegrationKeys();
		StringTokenizer st = new StringTokenizer(configvalue, delimiter);
		int i = 1;
		int j = 1;
		IntegrationKeys keys = new IntegrationKeys();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();

					if (i == 1) {
				// decrypt
				keys.setUserid(value);
			} else if (i == 2) {
				// decrypt
				keys.setPassword(value);
			} else if (i == 3) {
				// decrypt
				keys.setTransactionKey(value);
			} else {
				if (j == 1) {
					keys.setKey1(value);
				} else if (j == 2) {
					keys.setKey2(value);
				} else if (j == 3) {
					keys.setKey3(value);
				}
				j++;
			}
			i++;
		}
		return keys;
}
	

	public static String getConfigurationValue(Collection<String> values,
			String delimiter) {
		if (values == null || values.size() == 0) {
			return "";
		}
		int count = 1;
		Iterator i = values.iterator();
		StringBuffer b = new StringBuffer();
		while (i.hasNext()) {
			String value = (String) i.next();
			b.append(value);
			if (values.size() > count) {
				b.append(delimiter);
			}
			count++;
		}
		return b.toString();
	}

	public static String getConfigurationValue(IntegrationProperties keys,
			String delimiter) {

		if (StringUtils.isBlank(keys.getProperties1())
				&& StringUtils.isBlank(keys.getProperties2())
				&& StringUtils.isBlank(keys.getProperties3())
				&& StringUtils.isBlank(keys.getProperties4())
				&& StringUtils.isBlank(keys.getProperties5())) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		b.append(keys.getProperties1());
		if (!StringUtils.isBlank(keys.getProperties2())) {
			b.append(delimiter);
			b.append(keys.getProperties2());
			if (!StringUtils.isBlank(keys.getProperties3())) {
				b.append(delimiter);
				b.append(keys.getProperties3());
				if (!StringUtils.isBlank(keys.getProperties4())) {
					b.append(delimiter);
					b.append(keys.getProperties4());
					if (!StringUtils.isBlank(keys.getProperties5())) {
						b.append(delimiter);
						b.append(keys.getProperties5());
					}
				}
			}
		}

		return b.toString();

	}
}

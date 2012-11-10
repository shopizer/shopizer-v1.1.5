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
package com.salesmanager.core.module.impl.integration.shipping;

import java.util.StringTokenizer;

import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;

public class ShippingUtil {

	public static IntegrationKeys getKeys(String keyline) {

		// 1->userid 2->password i->key
		StringTokenizer st = new StringTokenizer(keyline, ";");
		int i = 1;
		int j = 1;
		IntegrationKeys keys = new IntegrationKeys();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			if (i == 1) {
				keys.setUserid(value);
			} else if (i == 2) {
				keys.setPassword(value);
			} else {
				if (j == 1) {
					keys.setKey1(value);
				} else if (j == 2) {
					keys.setKey2(value);
				} else if (j == 3) {
					keys.setKey3(value);
				} else if (j == 4) {
					keys.setKey4(value);
				}
				j++;
			}
			i++;
		}
		return keys;

	}

	public static IntegrationProperties getProperties(String propsline) {

		// 1->userid 2->password i->key
		StringTokenizer st = new StringTokenizer(propsline, ";");
		int i = 1;
		int j = 1;
		IntegrationProperties props = new IntegrationProperties();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			if (i == 1) {
				props.setProperties1(value);
			} else if (i == 2) {
				props.setProperties2(value);
			} else if (i == 3) {
				props.setProperties3(value);
			} else if (i == 4) {
				props.setProperties4(value);
			}

			i++;
		}
		return props;

	}

}

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
package com.salesmanager.checkout.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.payment.PaymentMethod;

public class PaymentUtil {

	private static Logger log = Logger.getLogger(PaymentUtil.class);

	public static Map<String, PaymentMethod> getPaymentMethods(int merchantId,
			Locale locale) throws Exception {

		Map payments = com.salesmanager.core.util.PaymentUtil
				.getPaymentMethods(merchantId, locale);

		Map returnMap = new HashMap();
		// now, change credit card payment
		Iterator i = payments.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			PaymentMethod pm = (PaymentMethod) payments.get(key);
			if (pm.getType() == 1) {
				key = "GATEWAY";
			}
			returnMap.put(key, pm);
		}

		return returnMap;

	}

}

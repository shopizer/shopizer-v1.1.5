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

import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.system.SystemService;

public class LogMerchantUtil {

	public static void log(int merchantid, String message) {

		SystemService systemService = (SystemService) ServiceFactory
				.getService(ServiceFactory.SystemService);
		
		
		if(message.length()>255) {
			message = message.substring(0,255);
		}
		
		systemService.logServiceMessage(merchantid, message);
	}

}

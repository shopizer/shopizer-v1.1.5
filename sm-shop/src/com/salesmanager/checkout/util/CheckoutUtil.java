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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutUtil {

	public static boolean isValid(String regExp, String value) {
		boolean isPatternMatched = false;
		if (value == null) {
			value = "";
		}
		Pattern compilePattern = Pattern.compile(regExp);
		Matcher matcherPhone = compilePattern.matcher(value);
		isPatternMatched = (matcherPhone.matches()) ? true : false;
		return isPatternMatched;
	}
}

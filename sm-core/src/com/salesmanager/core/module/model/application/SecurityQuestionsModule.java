/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Jun 1, 2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.module.model.application;

import java.util.Locale;
import java.util.Map;

import com.salesmanager.core.entity.merchant.MerchantUserInformation;

public interface SecurityQuestionsModule {
	
	/**
	 * Returns a Map with security question id and security question (text format)
	 * @param locale
	 * @return
	 */
	public Map<Integer,String> getSecurityQuestions(Locale locale);
	
	/*
	 * Validates questions
	 */
	public boolean validateSecurityQuestions(MerchantUserInformation userInformation, Map<Integer,Integer> userQuestions, Locale locale);

	
	/**
	 * Returns question label based on the id
	 * @param questionId
	 * @param locale
	 * @return
	 */
	public String getQuestionText(int questionId, Locale locale);
}

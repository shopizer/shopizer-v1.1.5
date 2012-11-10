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
package com.salesmanager.central.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains promotion codes and registration codes for a giving url for granting
 * the access to a given page. Used from AuthFilter class
 * 
 * @author Carl Samson
 * 
 */
public class AuthorizationCodes {

	private String url;

	private List registrationCode = new ArrayList();
	private List promotionCode = new ArrayList();

	public AuthorizationCodes(String url) {
		this.url = url;
	}

	public void addRegistrationCode(String code) {
		registrationCode.add(code);
	}

	public void addPromotionCode(String code) {
		promotionCode.add(code);
	}

	public boolean containsRegistrationCode(String code) {
		return registrationCode.contains(code);
	}

	public boolean containsPromotionCode(String code) {
		return promotionCode.contains(code);
	}

}

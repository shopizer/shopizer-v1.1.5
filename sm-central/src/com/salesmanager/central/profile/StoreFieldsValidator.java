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
package com.salesmanager.central.profile;

import java.util.List;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class StoreFieldsValidator extends FieldValidatorSupport {

	public void validate(Object arg0) throws ValidationException {
		// TODO Auto-generated method stub
		String fieldName = getFieldName();
		if (arg0 instanceof StoreAction) {

			StoreAction saction = (StoreAction) arg0;
			List languages = saction.getSupportedLanguages();
			if (languages == null || languages.size() < 1) {
				addFieldError(fieldName, arg0);
			}
		}
	}

}

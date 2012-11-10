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
package com.salesmanager.central.tax;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.salesmanager.central.profile.Context;
import com.salesmanager.core.constants.TaxConstants;
import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.entity.tax.TaxRateDescription;
import com.salesmanager.core.entity.tax.TaxRateDescriptionId;
import com.salesmanager.core.entity.tax.TaxRateDescriptionTaxTemplate;
import com.salesmanager.core.entity.tax.TaxRateDescriptionTaxTemplateId;
import com.salesmanager.core.entity.tax.TaxRateTaxTemplate;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.tax.TaxService;

public class TaxTemplateHelper {

	private static void persistTaxLines(int merchantId, int countryId,
			int zoneId, int schemeId) throws Exception {

		TaxService taxService = (TaxService) ServiceFactory
				.getService(ServiceFactory.TaxService);

		taxService.createTaxRates(schemeId, merchantId, countryId, zoneId);



	}

	public static void createCATaxLines(Context context) throws Exception {
		persistTaxLines(context.getMerchantid(), context.getCountryid(),
				context.getZoneid(), TaxConstants.CA_SCHEME);

	}

	public static void createUSTaxLines(Context context) throws Exception {
		persistTaxLines(context.getMerchantid(), context.getCountryid(),
				context.getZoneid(), TaxConstants.US_SCHEME);

	}

	public static void createEUTaxLines(Context context) throws Exception {
		persistTaxLines(context.getMerchantid(), context.getCountryid(),
				context.getZoneid(), TaxConstants.EU_SCHEME);
	}

}

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
package com.salesmanager.central;

import java.util.ArrayList;
import java.util.Collection;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class CountrySelectBaseAction extends BaseAction {

	private static final long serialVersionUID = -7565733895354076459L;
	private Collection<Country> countries;// drop down
	private Collection<Zone> zones = new ArrayList();
	private String zoneText;
	private String formState;

	protected void prepareSelections(int defaultCountry) {

		countries = RefCache.getAllcountriesmap(
				LanguageUtil.getLanguageNumberCode(super.getLocale()
						.getLanguage())).values();

		zones = RefCache.getFilterdByCountryZones(defaultCountry, LanguageUtil
				.getLanguageNumberCode(super.getLocale().getLanguage()));

	}

	protected void prepareSelections() {

		int defaultCountry = PropertiesUtil.getConfiguration().getInt(
				"core.system.defaultcountryid", Constants.US_COUNTRY_ID);
		countries = RefCache.getAllcountriesmap(
				LanguageUtil.getLanguageNumberCode(super.getLocale()
						.getLanguage())).values();

		zones = RefCache.getFilterdByCountryZones(defaultCountry, LanguageUtil
				.getLanguageNumberCode(super.getLocale().getLanguage()));

	}

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public Collection<Zone> getZones() {
		return zones;
	}

	public void setZones(Collection<Zone> zones) {
		this.zones = zones;
	}

	public String getZoneText() {
		return zoneText;
	}

	public void setZoneText(String zoneText) {
		this.zoneText = zoneText;
	}

	public String getFormState() {
		return formState;
	}

	public void setFormState(String formState) {
		this.formState = formState;
	}

}

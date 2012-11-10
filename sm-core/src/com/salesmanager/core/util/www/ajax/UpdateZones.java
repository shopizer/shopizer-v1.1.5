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
package com.salesmanager.core.util.www.ajax;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.PropertiesUtil;

public class UpdateZones {

	private static Logger log = Logger.getLogger(UpdateZones.class);
	private static Configuration conf = PropertiesUtil.getConfiguration();

	private static int defaultcountryid = 223;

	static {

		try {
			defaultcountryid = conf.getInt("core.system.defaultcountryid");
		} catch (Exception e) {
			log.error("Problem parsing default countryid");
		}

	}

	@SuppressWarnings("unchecked")
	public Zone[] getZones(String countryId, int languageId) {
		int country = defaultcountryid;
		try {
			country = Integer.parseInt(countryId);
		} catch (Exception e) {
			log.error(e);
		}
		Collection<Zone> c = RefCache.getFilterdByCountryZones(country,
				languageId);

		if (c != null) {
			Zone[] z = new Zone[c.size()];
			Zone[] znarray = (Zone[]) c.toArray(z);
			return znarray;
		} else {
			Zone[] z = new Zone[1];
			Zone zone = new Zone();
			zone.setZoneCountryId(country);
			zone.setZoneName("N/A");
			zone.setZoneId(0);
			z[0] = zone;
			return z;
		}
	}

	public Zone[] updateZones(String countryid, int languageid) {

		RefCache cache = RefCache.getInstance();

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();

		int country = defaultcountryid;

		try {
			country = Integer.parseInt(countryid);
		} catch (Exception e) {
			log.error(e);
		}

		req.getSession().setAttribute("COUNTRY", country);

		ReferenceService service = new ReferenceService();
		// Collection c = service.getZonesByCountry(country, req);
		Collection c = RefCache.getFilterdByCountryZones(country, languageid);

		if (c != null) {
			Zone[] z = new Zone[c.size()];
			Zone[] znarray = (Zone[]) c.toArray(z);
			return znarray;
		} else {
			Zone[] z = new Zone[1];
			Zone zone = new Zone();
			zone.setZoneCountryId(country);
			zone.setZoneName("N/A");
			zone.setZoneId(0);
			z[0] = zone;
			return z;
		}

	}

}

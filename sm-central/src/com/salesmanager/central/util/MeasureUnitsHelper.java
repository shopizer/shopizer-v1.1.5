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
package com.salesmanager.central.util;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.service.cache.RefCache;

public class MeasureUnitsHelper {

	public static String displaySizeUnitSymbol(HttpServletRequest req) {

		Context ctx = (Context) req.getAttribute(ProfileConstants.context);

		String unit = ctx.getSizeunit();
		if (unit == null) {
			unit = "CM";
		}
		Map mapunits = (Map) RefCache.getSizeunits();

		// Locale locale = req.getLocale();
		String description;
		com.salesmanager.core.entity.reference.CentralMeasureUnits cmu = (com.salesmanager.core.entity.reference.CentralMeasureUnits) mapunits
				.get(unit);
		cmu.setLocale(req.getLocale());

		if (cmu == null) {
			description = "";
		} else {

			description = cmu.getDescription();
		}

		return description;

	}

	public static String displayWeightUnitSymbol(HttpServletRequest req) {

		Context ctx = (Context) req.getSession().getAttribute(
				ProfileConstants.context);

		String unit = ctx.getWeightunit();
		if (unit == null) {
			unit = "LB";
		}
		Map mapunits = (Map) RefCache.getWeightunits();

		Locale locale = req.getLocale();
		String description;
		CentralMeasureUnits cmu = (CentralMeasureUnits) mapunits.get(unit);

		if (cmu == null) {
			description = "";
		} else {

			description = cmu.getDescription();
		}

		return description;

	}

}

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
package com.salesmanager.central.catalog;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;

public class UpdateOptionsValues {

	private static Logger log = Logger.getLogger(UpdateOptionsValues.class);

	public ProductOptionValueDisplay[] updateOptionsValues(long productOptionId) {

		try {

			HttpServletRequest req = WebContextFactory.get()
					.getHttpServletRequest();

			Context ctx = (Context) req.getSession().getAttribute(
					ProfileConstants.context);

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			ProductOption optionWithValues = cservice
					.getProductOptionWithValues(productOptionId);

			if (optionWithValues != null) {

				if (optionWithValues.getProductOptionType() == ProductConstants.PRODUCT_OPTION_TYPE_TEXT) {
					ProductOptionValueDisplay[] display = new ProductOptionValueDisplay[1];
					ProductOptionValueDisplay d = new ProductOptionValueDisplay();
					d.setOptionType(ProductConstants.PRODUCT_OPTION_TYPE_TEXT);
					display[0] = d;
					return display;
				}

				Set values = optionWithValues.getValues();
				if (values != null) {

					int size = values.size();

					ProductOptionValueDisplay[] display = new ProductOptionValueDisplay[size];

					int count = 0;

					Iterator viter = values.iterator();
					while (viter.hasNext()) {
						ProductOptionValue pov = (ProductOptionValue) viter
								.next();
						ProductOptionValueDisplay povd = new ProductOptionValueDisplay();
						povd.setProductOptionValueId(pov
								.getProductOptionValueId());
						povd.setProductOptionValueName(String.valueOf(pov
								.getProductOptionValueId()));
						Set descs = pov.getDescriptions();
						if (descs != null) {
							Iterator descsiter = descs.iterator();
							while (descsiter.hasNext()) {
								ProductOptionValueDescription povdesc = (ProductOptionValueDescription) descsiter
										.next();
								ProductOptionValueDescriptionId id = povdesc
										.getId();
								if (id.getLanguageId() == LanguageUtil
										.getLanguageNumberCode(ctx.getLang())) {
									povd.setProductOptionValueName(povdesc
											.getProductOptionValueName());
									break;
								}
							}
						}
						display[count] = povd;
						count++;
					}

					return display;

				} else {
					return buildDefaultOptions();
				}

			} else {
				return buildDefaultOptions();
			}

		} catch (Exception e) {
			log.error(e);
			return buildDefaultOptions();
		}

	}

	private ProductOptionValueDisplay[] buildDefaultOptions() {
		ProductOptionValueDisplay display = new ProductOptionValueDisplay();
		display.setProductOptionValueId(-1);
		display.setProductOptionValueName("");
		ProductOptionValueDisplay[] disp = new ProductOptionValueDisplay[1];
		disp[0] = display;
		return disp;
	}

}

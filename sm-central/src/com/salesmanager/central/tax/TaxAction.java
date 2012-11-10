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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.CountrySelectBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.tax.TaxClass;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public abstract class TaxAction extends CountrySelectBaseAction implements
		Preparable {

	protected final static String SCHEME = "S";
	protected final static String TAX_BASIS = "T";

	private int schemeid = 0;
	private String taxbasis = "";
	private String taxclass = "";

	private String descen = "";
	private String descfr = "";

	private Collection taxlist;
	private Map taxmap;

	private Collection<Language> languages;// used in the page as an index
	protected Map<Integer, Integer> reflanguages = new HashMap();// reference
																	// count -
																	// languageId

	// descriptions
	private List<String> descriptions = new ArrayList<String>();

	private int countryId;

	private Logger log = Logger.getLogger(TaxAction.class);

	protected abstract void setup() throws Exception;

	public void prepare() {

		try {

			setupTax();

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			super.prepareSelections(ctx.getCountryid());

			MerchantStore mstore = service.getMerchantStore(merchantid);

			if (mstore == null) {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"errors.profile.storenotcreated"));
			} else {

				Map languagesMap = mstore.getGetSupportedLanguages();

				languages = languagesMap.values();// collection reverse the map

				super.getServletRequest().setAttribute("languages", languages);

				// int count = languagesMap.size()-1;
				int count = 0;
				Iterator langit = languagesMap.keySet().iterator();
				while (langit.hasNext()) {
					Integer langid = (Integer) langit.next();
					Language lang = (Language) languagesMap.get(langid);
					reflanguages.put(count, langid);
					count++;
				}

			}

			setup();

		} catch (Exception e) {
			log.error(e);
		}

	}

	protected Map gatherParameters() throws Exception {

		String scheme = super.getServletRequest().getParameter("SCHEMEID");

		if (scheme == null)
			throw new Exception("gatherParameters() Did not received scheme");

		String taxbasis = super.getServletRequest().getParameter("taxbasis");

		if (taxbasis == null)
			throw new Exception("gatherParameters() Did not received taxbasis");

		Map p = new HashMap();
		p.put(SCHEME, scheme);
		p.put(TAX_BASIS, taxbasis);

		int schid = new Integer(scheme);
		this.schemeid = schid;
		this.taxbasis = taxbasis;

		super.getServletRequest().setAttribute("SCHEMEID", schid);
		super.getServletRequest().setAttribute("taxbasis", taxbasis);

		return p;

	}

	/**
	 * -- ALWAYS INVOKED -- Get tax configured for a given merchantid
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setupTax() throws Exception {

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			// set basic information
			this.setCountryId(ctx.getCountryid());
			super.getServletRequest().getSession().setAttribute("COUNTRY",
					ctx.getCountryid());

			TaxService taxService = (TaxService) ServiceFactory
					.getService(ServiceFactory.TaxService);

			taxlist = taxService.getTaxRates(merchantid);

			if (taxlist == null) {
				taxlist = new ArrayList();
			}

			// iterate and get tax class ids and name
			Map classtaxesid = new TreeMap();

			List txscl = taxService.getTaxClasses(merchantid);

			if (txscl != null) {
				Iterator it = txscl.iterator();
				while (it.hasNext()) {
					TaxClass x = (TaxClass) it.next();
					classtaxesid.put(String.valueOf(x.getTaxClassId()), x
							.getTaxClassTitle());
				}
			}

			classtaxesid.put("1", "Tax");

			super.getServletRequest().setAttribute("taxclassmap", classtaxesid);
			super.getServletRequest().setAttribute("taxlist", taxlist);

			this.setTaxmap(classtaxesid);

			return SUCCESS;

		} catch (HibernateException e) {
			log.error(e);
			return ERROR;
		}

	}

	public int getSchemeid() {
		return schemeid;
	}

	public void setSchemeid(int schemeid) {
		this.schemeid = schemeid;
	}

	public String getTaxbasis() {
		return taxbasis;
	}

	public void setTaxbasis(String taxbasis) {
		this.taxbasis = taxbasis;
	}

	public String getTaxclass() {
		return taxclass;
	}

	public void setTaxclass(String taxclass) {
		this.taxclass = taxclass;
	}

	public String getDescen() {
		return descen;
	}

	public void setDescen(String descen) {
		this.descen = descen;
	}

	public String getDescfr() {
		return descfr;
	}

	public void setDescfr(String descfr) {
		this.descfr = descfr;
	}

	public Collection getTaxlist() {
		return taxlist;
	}

	public void setTaxlist(Collection taxlist) {
		this.taxlist = taxlist;
	}

	public Map getTaxmap() {
		return taxmap;
	}

	public void setTaxmap(Map taxmap) {
		this.taxmap = taxmap;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}

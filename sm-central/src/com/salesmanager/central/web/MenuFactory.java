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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.entity.functions.CentralFunctionRegistration;
import com.salesmanager.central.entity.functions.CentralGroupRegistration;
import com.salesmanager.central.util.CacheFactory;
import com.salesmanager.core.entity.system.CentralFunction;
import com.salesmanager.core.entity.system.CentralGroup;
import com.salesmanager.core.entity.system.CentralRegistrationAssociation;

public class MenuFactory {

	private static MenuFactory menuFactory = null;
	private Map groups;
	private Map functions;
	private Map<String, CentralFunction> functionsByFunctionCode;

	public Map<String, CentralFunction> getFunctionsByFunctionCode() {
		return functionsByFunctionCode;
	}

	public void setFunctionsByFunctionCode(Collection functions) {

		if (functions != null) {
			// functionsByFunctionCode = functions;
			functionsByFunctionCode = new TreeMap();
			Iterator i = functions.iterator();
			while (i.hasNext()) {
				CentralFunction cf = (CentralFunction) i.next();
				functionsByFunctionCode.put(cf.getCentralFunctionCode(), cf);
			}
		}

	}

	private MenuFactory() {
		CacheFactory cfactory = CacheFactory.getInstance();
		try {
			groups = cfactory.createCacheMap("groups");
			functions = cfactory.createCacheMap("functions");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MenuFactory getInstance() {
		if (menuFactory == null) {
			menuFactory = new MenuFactory();
		}
		return menuFactory;
	}

	private synchronized List getFunctionsList(int registrationcode,
			String group) {

		CacheFactory cfactory = CacheFactory.getInstance();
		List functionslist = (List) functions
				.get(new Integer(registrationcode));
		// Logger.getLogger(MenuFactory.class).debug("*** ANALYZE F LIST " +
		// registrationcode + " SIZE " + functionslist.size());
		Iterator i = functionslist.iterator();
		try {
			List rfunctionlist = cfactory.createCacheList("functions"
					+ registrationcode + group);
			while (i.hasNext()) {
				CentralFunctionRegistration function = (CentralFunctionRegistration) i
						.next();
				int code = function.getMerchantRegistrationDefCode();
				String groupcode = function.getCentralGroupCode();

				if (!StringUtils.isBlank(groupcode) && registrationcode == code
						&& groupcode.equalsIgnoreCase(group)) {
					rfunctionlist.add(function);
				}
			}
			return rfunctionlist;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public List getFunctions(int registrationcode, String group) {
		CacheFactory cfactory = CacheFactory.getInstance();
		// Logger.getLogger(MenuFactory.class).debug("*** ANALYZE F " +
		// registrationcode + " " + group);
		if (cfactory.containsCache("functions" + registrationcode + group)) {
			// Logger.getLogger(MenuFactory.class).debug("*** ANALYZE F - GOT FROM CACHE");
			return cfactory
					.getCacheList("functions" + registrationcode + group);
		}
		List functionslist = getFunctionsList(registrationcode, group);
		return functionslist;

	}

	public List getGroups(int registrationcode) {
		List grouplist = (List) groups.get(new Integer(registrationcode));

		if (grouplist == null)
			grouplist = new ArrayList();

		return grouplist;
	}

	/**
	 * Set groups in a map per registration code
	 * 
	 * @param grouplist
	 */
	public void setGroups(Collection groupsColl, Collection registrationList) {

		if (registrationList == null || registrationList.size() == 0
				|| groupsColl == null || groupsColl.size() == 0)
			return;

		groups = new HashMap();

		// Iterator i = registrationList.iterator();
		Iterator i = groupsColl.iterator();
		while (i.hasNext()) {
			// CentralRegistrationAssociation association =
			// (CentralRegistrationAssociation)i.next();
			CentralGroup gr = (CentralGroup) i.next();

			Iterator regIterator = registrationList.iterator();
			while (regIterator.hasNext()) {
				CentralRegistrationAssociation association = (CentralRegistrationAssociation) regIterator
						.next();
				if (!StringUtils.isBlank(association.getCentralGroupCode())
						&& association.getCentralGroupCode().equals(
								gr.getCentralGroupCode())) {

					int code = association.getMerchantRegistrationDefCode();
					Integer akey = new Integer(code);

					CentralGroupRegistration registration = new CentralGroupRegistration();
					registration.setCentralGroupCode(association
							.getCentralGroupCode());
					registration.setMerchantRegistrationDefCode(association
							.getMerchantRegistrationDefCode());
					registration.setPromotionCode(association
							.getPromotionCode());

					registration.setCentralGroupDescription(gr
							.getCentralGroupDescription());
					registration.setCentralGroupNewUntil(gr
							.getCentralGroupNewUntil());
					registration.setCentralGroupNew(gr.isCentralGroupNew());
					registration.setCentralGroupPosition(gr
							.getCentralGroupPosition());
					registration.setCentralGroupVisible(gr
							.isCentralGroupVisible());

					// Logger.getLogger(MenuFactory.class).debug("*** CHECKING KEY "
					// + akey);
					if (!groups.containsKey(akey)) {
						// Logger.getLogger(MenuFactory.class).debug("*** KEY "
						// + akey + " does not exixt");
						List newlist = new ArrayList();
						// Logger.getLogger(MenuFactory.class).debug("***WILL ADD to key "
						// + akey + group.getCentralGroupCode() + " desc " +
						// group.getCentralGroupDescription());
						newlist.add(registration);
						groups.put(akey, newlist);
					} else {
						// Logger.getLogger(MenuFactory.class).debug("*** KEY "
						// + akey + " exixt");
						List thelist = (List) groups.get(akey);
						// Logger.getLogger(MenuFactory.class).debug("***WILL ADD to key "
						// + akey + group.getCentralGroupCode() + " desc " +
						// group.getCentralGroupDescription());
						thelist.add(registration);
					}

				}
			}

		}

		// order groups

		// receive group map and registrationassociation

		// iterate through registration get group
		// create CentralGroupRegistration
		// store in map

		// iterate groups
		// get CentralGroupRegistration

		/*
		 * groups = new HashMap(); if(grouplist==null) return;
		 * 
		 * //List keys = new ArrayList(); Iterator i = grouplist.iterator();
		 * 
		 * //Logger.getLogger(MenuFactory.class).debug("*** GROUP LIST SIZE " +
		 * grouplist.size());
		 * 
		 * while(i.hasNext()) { CentralGroupRegistration group =
		 * (CentralGroupRegistration)i.next(); int code =
		 * group.getMerchantRegistrationDefCode(); Integer akey = new
		 * Integer(code);
		 * //Logger.getLogger(MenuFactory.class).debug("*** CHECKING KEY " +
		 * akey); if(!groups.containsKey(akey)) {
		 * //Logger.getLogger(MenuFactory.class).debug("*** KEY " + akey +
		 * " does not exixt"); List newlist = new ArrayList();
		 * //Logger.getLogger(MenuFactory.class).debug("***WILL ADD to key " +
		 * akey + group.getCentralGroupCode() + " desc " +
		 * group.getCentralGroupDescription()); newlist.add(group);
		 * groups.put(akey, newlist); } else {
		 * //Logger.getLogger(MenuFactory.class).debug("*** KEY " + akey +
		 * " exixt"); List thelist = (List)groups.get(akey);
		 * //Logger.getLogger(MenuFactory.class).debug("***WILL ADD to key " +
		 * akey + group.getCentralGroupCode() + " desc " +
		 * group.getCentralGroupDescription()); thelist.add(group); } }
		 */
	}

	/**
	 * Set functions based on registration code
	 * 
	 * @param functions
	 */
	public void setFunctions(Collection functionsColl,
			Collection registrationList) {

		if (registrationList == null || registrationList.size() == 0)
			return;

		functions = new HashMap();

		try {
			CacheFactory cfactory = CacheFactory.getInstance();

			cfactory.removeCache("functionsurl");
			Map functionsurl = cfactory.createCacheMap("functionsurl");

			// Iterator i = registrationList.iterator();
			Iterator i = functionsColl.iterator();
			while (i.hasNext()) {
				// CentralRegistrationAssociation association =
				// (CentralRegistrationAssociation)i.next();

				CentralFunction f = (CentralFunction) i.next();

				Iterator regIterator = registrationList.iterator();
				while (regIterator.hasNext()) {
					CentralRegistrationAssociation association = (CentralRegistrationAssociation) regIterator
							.next();
					if (!StringUtils.isBlank(association
							.getCentralFunctionCode())
							&& association.getCentralFunctionCode().equals(
									f.getCentralFunctionCode())) {

						CentralFunctionRegistration registration = new CentralFunctionRegistration();

						registration.setCentralFunctionCode(association
								.getCentralFunctionCode());
						registration.setMerchantRegistrationDefCode(association
								.getMerchantRegistrationDefCode());
						registration.setPromotionCode(association
								.getPromotionCode());

						registration.setCentralFunctionDescription(f
								.getCentralFunctionDescription());
						registration.setCentralFunctionNewUntil(f
								.getCentralFunctionNewUntil());
						registration.setCentralFunctionNew(f
								.isCentralFunctionNew());
						registration.setCentralFunctionPosition(f
								.getCentralFunctionPosition());
						registration.setCentralFunctionVisible(f
								.isCentralFunctionVisible());
						registration.setCentralFunctionUrl(f
								.getCentralFunctionUrl());
						registration.setCentralGroupCode(f
								.getCentralGroupCode());
						registration.setRole(f.getRole());

						// CentralFunctionRegistration function =
						// (CentralFunctionRegistration)i.next();
						int code = registration
								.getMerchantRegistrationDefCode();
						int pcode = registration.getPromotionCode();
						String url = registration.getCentralFunctionUrl();

						AuthorizationCodes codes = (AuthorizationCodes) functionsurl
								.get(url);
						if (codes == null) {
							codes = new AuthorizationCodes(f
									.getCentralFunctionUrl());
							functionsurl.put(url, codes);
						}
						if (!codes.containsRegistrationCode(String
								.valueOf(code))) {
							codes.addRegistrationCode(String.valueOf(code));

						}
						if (pcode > 0) {
							if (!codes.containsPromotionCode(String
									.valueOf(pcode))) {
								codes.addPromotionCode(String.valueOf(pcode));

							}
						}
						Integer akey = new Integer(code);
						if (!functions.containsKey(akey)) {
							List newlist = new ArrayList();
							newlist.add(registration);
							functions.put(akey, newlist);
						} else {
							List thelist = (List) functions.get(akey);
							thelist.add(registration);
						}

					}

				}

			}

		} catch (Exception e) {
			Logger.getLogger(MenuFactory.class).error(e);
		}
	}

	public Map getFunctions() {
		if (functions == null) {
			functions = new HashMap();
		}
		return functions;
	}

}

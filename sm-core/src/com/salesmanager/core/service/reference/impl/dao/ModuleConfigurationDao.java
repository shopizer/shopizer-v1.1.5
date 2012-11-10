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
package com.salesmanager.core.service.reference.impl.dao;

// Generated Jul 11, 2008 8:31:33 AM by Hibernate Tools 3.2.0.b9

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.reference.ModuleConfiguration;

/**
 * Home object for domain model class ModuleConfiguration.
 * 
 * @see com.salesmanager.core.entity.reference.ModuleConfiguration
 * @author Hibernate Tools
 */
@Repository
public class ModuleConfigurationDao extends HibernateDaoSupport implements
		IModuleConfigurationDao { // implements IMerchantConfigurationDao {

	private static final Log log = LogFactory
			.getLog(ModuleConfigurationDao.class);

	@Autowired
	public ModuleConfigurationDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.IModuleConfigurationDao#
	 * persist(com.salesmanager.core.entity.reference.ModuleConfiguration)
	 */
	public void persist(ModuleConfiguration transientInstance) {

		try {
			super.getHibernateTemplate().persist(transientInstance);

		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.IModuleConfigurationDao#
	 * delete(com.salesmanager.core.entity.reference.ModuleConfiguration)
	 */
	public void delete(ModuleConfiguration persistentInstance) {

		try {
			super.getHibernateTemplate().delete(persistentInstance);

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Collection<ModuleConfiguration> findByConfigurationModuleAndCountryCode(
			String configurationModule, String countryIsoCode) {
		try {

			List countryList = new ArrayList();
			countryList.add(countryIsoCode);
			countryList.add(Constants.ALLCOUNTRY_ISOCODE);

			DetachedCriteria crit = DetachedCriteria
					.forClass(ModuleConfiguration.class);
			crit.add(Expression.in("id.countryIsoCode2", countryList));
			crit.add(Expression.eq("id.configurationModule",
					configurationModule));

			Collection list = this.getHibernateTemplate().findByCriteria(crit);


			List countrySpecificList = new ArrayList();
			Iterator i = list.iterator();
			while (i.hasNext()) {
				ModuleConfiguration cms = (ModuleConfiguration) i.next();
				if (cms.getId().getCountryIsoCode2().equals(countryIsoCode)) {

					countrySpecificList.add(cms);
				}
			}

			if (countrySpecificList.size() > 0) {
				return countrySpecificList;
			} else {
				return list;
			}

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ModuleConfiguration> findByConfigurationKeyAndCountryCode(
			String configurationKey, String countryIsoCode) {
		try {

			List countryList = new ArrayList();
			countryList.add(countryIsoCode);
			countryList.add(Constants.ALLCOUNTRY_ISOCODE);


			DetachedCriteria crit = DetachedCriteria
					.forClass(ModuleConfiguration.class);
			crit.add(Expression.in("id.countryIsoCode2", countryList));
			crit.add(Expression.eq("id.configurationKey", configurationKey));

			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			List countrySpecificList = new ArrayList();
			Iterator i = result.iterator();
			while (i.hasNext()) {
				ModuleConfiguration cms = (ModuleConfiguration) i.next();
				if (cms.getId().getCountryIsoCode2().equals(countryIsoCode)) {

					countrySpecificList.add(cms);
				}
			}

			if (countrySpecificList.size() > 0) {
				return countrySpecificList;
			} else {
				return result;
			}

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	public Collection<ModuleConfiguration> findByModuleIds(
			List<String> ids) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select m from ModuleConfiguration m where m.id.configurationModule in (:sIds)")
					.setParameterList("sIds", ids).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.IModuleConfigurationDao#
	 * findById(com.salesmanager.core.entity.reference.ModuleConfigurationId)
	 */

	public ModuleConfiguration findById(
			com.salesmanager.core.entity.reference.ModuleConfigurationId id) {

		try {
			ModuleConfiguration instance = (ModuleConfiguration) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.reference.ModuleConfiguration",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

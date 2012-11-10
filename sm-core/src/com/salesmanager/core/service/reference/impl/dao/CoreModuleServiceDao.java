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

// Generated Nov 8, 2008 9:09:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.reference.CoreModuleService;

/**
 * Home object for domain model class CoreModuleService.
 * 
 * @author Hibernate Tools
 */
@Repository
public class CoreModuleServiceDao extends HibernateDaoSupport implements
		ICoreModuleServiceDao {

	@Autowired
	public CoreModuleServiceDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.ICoreModuleServiceDao
	 * #persist(com.salesmanager.core.entity.reference.CoreModuleService)
	 */
	public void persist(CoreModuleService transientInstance) {

		try {
			this.getHibernateTemplate().persist(transientInstance);

		} catch (RuntimeException re) {

			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.ICoreModuleServiceDao
	 * #saveOrUpdate(com.salesmanager.core.test.CoreModulesServices)
	 */
	public void saveOrUpdate(CoreModuleService instance) {

		try {
			this.getHibernateTemplate().saveOrUpdate(instance);

		} catch (RuntimeException re) {

			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.ICoreModuleServiceDao
	 * #delete(com.salesmanager.core.entity.reference.CoreModuleService)
	 */
	public void delete(CoreModuleService persistentInstance) {

		try {
			this.getHibernateTemplate().delete(persistentInstance);

		} catch (RuntimeException re) {

			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.ICoreModuleServiceDao
	 * #merge(com.salesmanager.core.entity.reference.CoreModuleService)
	 */
	public CoreModuleService merge(CoreModuleService detachedInstance) {

		try {
			CoreModuleService result = (CoreModuleService) this
					.getHibernateTemplate().merge(detachedInstance);

			return result;
		} catch (RuntimeException re) {

			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.ICoreModuleServiceDao
	 * #findByServiceTypeAndSubTypeByRegion(int, int, java.lang.String)
	 */
	public Collection<CoreModuleService> findByServiceTypeAndSubTypeByRegion(
			int type, int subType, String region) {

		try {

			List countryList = new ArrayList();
			countryList.add(region);
			countryList.add(Constants.ALLCOUNTRY_ISOCODE);

			DetachedCriteria crit = DetachedCriteria
					.forClass(CoreModuleService.class);
			crit.add(Expression.in("countryIsoCode2", countryList));
			crit.add(Expression.eq("coreModuleServiceCode", type));
			crit.add(Expression.eq("coreModuleServiceSubtype", subType));
			crit.addOrder(org.hibernate.criterion.Order
					.desc("coreModuleServicePosition"));
			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			List countrySpecificList = new ArrayList();
			Iterator i = result.iterator();
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				if (cms.getCountryIsoCode2().equals(region)) {

					countrySpecificList.add(cms);
				}
			}

			if (countrySpecificList.size() > 0) {
				return countrySpecificList;
			} else {
				return result;
			}

		} catch (RuntimeException re) {
			// log.error("get failed", re);
			throw re;
		}
	}

	public Collection<CoreModuleService> getCoreModulesServices() {

		try {
			Collection list = super.getSession().createCriteria(
					CoreModuleService.class).addOrder(
					Order.asc("countryIsoCode2")).addOrder(
					Order.asc("coreModuleServiceSubtype")).addOrder(
					Order.asc("coreModuleServicePosition")).list();

			return list;

		} catch (RuntimeException re) {
			// TODO: handle exception
			throw re;
		}

	}

	public Collection<CoreModuleService> findByServiceTypeAndByRegion(int type,
			String region) {

		try {

			List countryList = new ArrayList();
			countryList.add(region);
			countryList.add(Constants.ALLCOUNTRY_ISOCODE);

			DetachedCriteria crit = DetachedCriteria
					.forClass(CoreModuleService.class);
			crit.add(Expression.in("countryIsoCode2", countryList));
			crit.add(Expression.eq("coreModuleServiceCode", type));
			crit.addOrder(org.hibernate.criterion.Order
					.desc("coreModuleServicePosition"));
			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			List countrySpecificList = new ArrayList();
			Iterator i = result.iterator();
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				if (cms.getCountryIsoCode2().equals(region)) {

					countrySpecificList.add(cms);
				}
			}

			if (countrySpecificList.size() > 0) {
				return countrySpecificList;
			} else {
				return result;
			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public CoreModuleService findByModuleAndRegion(String module, String region) {

		try {

			List countryList = new ArrayList();
			countryList.add(region);
			countryList.add(Constants.ALLCOUNTRY_ISOCODE);

			DetachedCriteria crit = DetachedCriteria
					.forClass(CoreModuleService.class);
			crit.add(Expression.in("countryIsoCode2", countryList));
			crit.add(Expression.eq("coreModuleName", module));
			crit.addOrder(org.hibernate.criterion.Order
					.desc("coreModuleServicePosition"));
			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			List countrySpecificList = new ArrayList();
			Iterator i = result.iterator();
			CoreModuleService tempCms = null;
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				if (cms.getCountryIsoCode2().equals(region)) {
					return cms;
				}
				if (cms.getCountryIsoCode2().equals(
						Constants.ALLCOUNTRY_ISOCODE)) {
					tempCms = cms;
				}
			}

			return tempCms;

		} catch (RuntimeException re) {
			throw re;
		}
	}

}

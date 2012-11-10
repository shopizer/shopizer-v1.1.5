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
package com.salesmanager.core.service.tax.impl.dao;

// Generated Sep 4, 2008 8:23:33 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.entity.tax.TaxRateTaxTemplate;

/**
 * Home object for domain model class TaxRates.
 * 
 * @see com.salesmanager.core.test.TaxRates
 * @author Hibernate Tools
 */
@Repository
public class TaxRateDao extends HibernateDaoSupport implements ITaxRateDao {

	private static final Log log = LogFactory.getLog(TaxRateDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public TaxRateDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDao#persist(com.salesmanager
	 * .core.entity.tax.TaxRate)
	 */
	public void persist(TaxRate transientInstance) {
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
	 * @seecom.salesmanager.core.service.tax.impl.ITaxRateDao#saveOrUpdate(com.
	 * salesmanager.core.entity.tax.TaxRate)
	 */
	public void saveOrUpdate(TaxRate instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDao#delete(com.salesmanager
	 * .core.entity.tax.TaxRate)
	 */
	public void delete(TaxRate persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDao#deleteAll(java.util
	 * .Collection)
	 */
	public void deleteAll(Collection<TaxRate> collection) {

		try {
			super.getHibernateTemplate().deleteAll(collection);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDao#merge(com.salesmanager
	 * .core.entity.tax.TaxRate)
	 */
	public TaxRate merge(TaxRate detachedInstance) {
		try {
			TaxRate result = (TaxRate) super.getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.salesmanager.core.service.tax.impl.ITaxRateDao#findById(int)
	 */
	public TaxRate findById(long id) {
		try {
			TaxRate instance = (TaxRate) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.tax.TaxRate", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDao#findByMerchantId(int)
	 */
	public List<TaxRate> findByMerchantId(int merchantId) {

		try {

			List list = (List) super
					.getSession()
					.createQuery(
							"select t from TaxRate t join fetch t.zoneToGeoZone z join fetch z.geoZone left join fetch t.descriptions where t.merchantId=:mId order by t.taxZoneId, t.taxPriority")
					.setInteger("mId", merchantId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<TaxRate> findByTaxClassId(long taxClassId) {

		try {

			List tx = super.getSession().createCriteria(TaxRate.class).add(
					Restrictions.eq("taxClassId", taxClassId)).list();

			return tx;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<TaxRateTaxTemplate> findBySchemeId(int schemeId) {

		try {

			List list = (List) super
					.getSession()
					.createQuery(
							"select t from TaxRateTaxTemplate t left join fetch t.descriptions join fetch t.zoneToGeoZone z join fetch z.geoZone g where g.schemeid=:sId order by g.geoZoneId")
					.setInteger("sId", schemeId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<TaxRateTaxTemplate> findByZoneCountryId(int countryId) {

		try {

			List list = (List) super
					.getSession()
					.createQuery(
							"select t from TaxRateTaxTemplate t left join fetch t.descriptions join fetch t.zoneToGeoZone z join fetch z.geoZone g where z.zoneCountryId=:cId order by g.geoZoneId")
					.setInteger("cId", countryId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<TaxRate> findByCountryIdZoneIdAndClassId(int countryId,
			int zoneId, long taxClassId, int merchantId) {

		try {

			List list = (List) super
					.getSession()
					.createQuery(
							"select t from TaxRate t join fetch t.zoneToGeoZone z join fetch z.geoZone where t.merchantId=:mId and t.taxClassId=:tId and z.zoneCountryId=:cId and z.zoneId=:zId order by t.taxZoneId, t.taxPriority")
					.setInteger("mId", merchantId).setLong("tId", taxClassId)
					.setInteger("cId", countryId).setInteger("zId", zoneId)
					.list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<TaxRate> findByCountryId(int countryId, int merchantId) {

		try {

			List list = (List) super
					.getSession()
					.createQuery(
							"select t from TaxRate t join fetch t.zoneToGeoZone z join fetch z.geoZone where t.merchantId=:mId and z.zoneCountryId=:cId order by t.taxZoneId, t.taxPriority")
					.setInteger("mId", merchantId).setInteger("cId", countryId)
					.list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

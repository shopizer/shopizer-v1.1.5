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

// Generated Sep 4, 2008 8:23:33 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.GeoZone;

/**
 * Home object for domain model class GeoZones.
 * 
 * @see com.salesmanager.core.entity.reference.GeoZone
 * @author Hibernate Tools
 */
@Repository
public class GeoZoneDao extends HibernateDaoSupport implements IGeoZoneDao {

	private static final Log log = LogFactory.getLog(GeoZoneDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public GeoZoneDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.IGeoZoneDao#persist(com.salesmanager
	 * .core.entity.tax.GeoZone)
	 */
	public void persist(GeoZone transientInstance) {
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
	 * @seecom.salesmanager.core.service.tax.impl.IGeoZoneDao#saveOrUpdate(com.
	 * salesmanager.core.entity.tax.GeoZone)
	 */
	public void saveOrUpdate(GeoZone instance) {
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
	 * com.salesmanager.core.service.tax.impl.IGeoZoneDao#delete(com.salesmanager
	 * .core.entity.tax.GeoZone)
	 */
	public void delete(GeoZone persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<GeoZone> collection) {

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
	 * com.salesmanager.core.service.tax.impl.IGeoZoneDao#merge(com.salesmanager
	 * .core.entity.tax.GeoZone)
	 */
	public GeoZone merge(GeoZone detachedInstance) {
		try {
			GeoZone result = (GeoZone) super.getHibernateTemplate().merge(
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
	 * @see com.salesmanager.core.service.tax.impl.IGeoZoneDao#findById(int)
	 */
	public GeoZone findById(int id) {
		try {
			GeoZone instance = (GeoZone) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.tax.GeoZone", id);

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
	 * com.salesmanager.core.service.tax.impl.IGeoZoneDao#findByMerchantId(int)
	 */
	public Collection<GeoZone> findByMerchantId(int merchantid) {

		try {

			List tx = super.getSession().createCriteria(GeoZone.class).add(
					Restrictions.eq("merchantId", merchantid)).list();

			return tx;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

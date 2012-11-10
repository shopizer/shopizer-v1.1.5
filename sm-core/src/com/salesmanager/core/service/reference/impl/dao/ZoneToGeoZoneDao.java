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

import com.salesmanager.core.entity.reference.ZoneToGeoZone;

/**
 * Home object for domain model class ZonesToGeoZones.
 * 
 * @see com.salesmanager.core.entity.reference.ZoneToGeoZone
 * @author Hibernate Tools
 */
@Repository
public class ZoneToGeoZoneDao extends HibernateDaoSupport implements
		IZoneToGeoZoneDao {

	private static final Log log = LogFactory.getLog(ZoneToGeoZoneDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ZoneToGeoZoneDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#persist(com.
	 * salesmanager.core.entity.tax.ZoneToGeoZone)
	 */
	public void persist(ZoneToGeoZone transientInstance) {
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
	 * com.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#saveOrUpdate
	 * (com.salesmanager.core.entity.tax.ZoneToGeoZone)
	 */
	public void saveOrUpdate(ZoneToGeoZone instance) {

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
	 * @seecom.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#delete(com.
	 * salesmanager.core.entity.tax.ZoneToGeoZone)
	 */
	public void delete(ZoneToGeoZone persistentInstance) {
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
	 * com.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#deleteAll(java
	 * .util.Collection)
	 */
	public void deleteAll(Collection<ZoneToGeoZone> collection) {

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
	 * @seecom.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#merge(com.
	 * salesmanager.core.entity.tax.ZoneToGeoZone)
	 */
	public ZoneToGeoZone merge(ZoneToGeoZone detachedInstance) {
		try {
			ZoneToGeoZone result = (ZoneToGeoZone) super.getHibernateTemplate()
					.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#findById(int)
	 */
	public ZoneToGeoZone findById(int id) {
		try {
			ZoneToGeoZone instance = (ZoneToGeoZone) super
					.getHibernateTemplate().get(
							"com.salesmanager.core.entity.tax.ZonesToGeoZones",
							id);

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
	 * com.salesmanager.core.service.tax.impl.IZoneToGeoZoneDao#findByMerchantId
	 * (int)
	 */
	public Collection<ZoneToGeoZone> findByMerchantId(int merchantid) {

		try {

			List tx = super.getSession().createCriteria(ZoneToGeoZone.class)
					.add(Restrictions.eq("merchantId", merchantid)).list();

			return tx;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

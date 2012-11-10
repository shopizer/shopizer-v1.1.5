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
package com.salesmanager.core.service.merchant.impl.dao;

// Generated Jul 3, 2008 9:19:31 PM by Hibernate Tools 3.2.0.b9

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;

/**
 * Home object for domain model class MerchantConfiguration.
 * 
 * @see com.salesmanager.core.service.merchant.impl.MerchantConfiguration
 * @author Hibernate Tools
 */
@Repository
public class MerchantConfigurationDao extends HibernateDaoSupport implements
		IMerchantConfigurationDao {

	private static final Log log = LogFactory
			.getLog(MerchantConfigurationDao.class);

	@Autowired
	public MerchantConfigurationDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#persist
	 * (com.salesmanager.core.entity.merchant.MerchantConfiguration)
	 */
	public void persist(MerchantConfiguration transientInstance) {
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
	 * @seecom.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#
	 * saveOrUpdate(com.salesmanager.core.entity.merchant.MerchantConfiguration)
	 */
	public void saveOrUpdate(MerchantConfiguration transientInstance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(transientInstance);
		} catch (RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(
			Collection<MerchantConfiguration> transientInstances) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(transientInstances);
		} catch (RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#delete
	 * (com.salesmanager.core.entity.merchant.MerchantConfiguration)
	 */
	public void delete(MerchantConfiguration persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void delete(Collection<MerchantConfiguration> instances) {
		try {
			super.getHibernateTemplate().deleteAll(instances);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#
	 * deleteLike(java.lang.String, int)
	 */
	public void deleteLike(String like, int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.like("configurationKey", "%" + like + "%"))
					.add(Restrictions.eq("merchantId", merchantId)).list();

			if (configs != null) {
				super.getHibernateTemplate().deleteAll(configs);
			}

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteLikeModule(String like, String moduleid, int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.like("configurationKey", "%" + like + "%"))
					.add(Restrictions.eq("merchantId", merchantId)).add(
							Restrictions.eq("configurationModule", moduleid))
					.list();

			if (configs != null) {
				super.getHibernateTemplate().deleteAll(configs);
			}

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteKey(String key, int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.eq("configurationKey", key)).add(
					Restrictions.eq("merchantId", merchantId)).list();

			if (configs != null) {
				super.getHibernateTemplate().deleteAll(configs);
			}

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#
	 * findByLike(java.lang.String, int)
	 */
	public List<MerchantConfiguration> findListByLike(String like,
			int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.like("configurationKey", "%" + like + "%"))
					.add(Restrictions.eq("merchantId", merchantId)).list();

			return configs;
		} catch (RuntimeException re) {
			log.error("findListByLike failed", re);
			throw re;
		}
	}

	public List<MerchantConfiguration> findListMerchantId(int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.eq("merchantId", merchantId)).list();

			return configs;
		} catch (RuntimeException re) {
			log.error("findListMerchantId failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMasterConfigurationDao#findByKey
	 * (java.lang.String, int)
	 */
	public MerchantConfiguration findByKey(String key, int merchantId) {

		try {

			MerchantConfiguration config = (MerchantConfiguration) super
					.getSession().createCriteria(MerchantConfiguration.class)
					.add(Restrictions.eq("configurationKey", key)).add(
							Restrictions.eq("merchantId", merchantId))
					.uniqueResult();

			return config;
		} catch (RuntimeException re) {
			log.error("findByKey failed", re);
			throw re;
		}
	}

	public List<MerchantConfiguration> findListByKey(String key, int merchantId) {

		try {

			List configs = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.eq("configurationKey", key)).add(
					Restrictions.eq("merchantId", merchantId)).list();

			return configs;
		} catch (RuntimeException re) {
			log.error("findListByKey failed", re);
			throw re;
		}
	}

	public Collection<MerchantConfiguration> findByModule(String moduleName,
			int merchantId) {
		try {
			List list = super.getSession().createCriteria(
					MerchantConfiguration.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("configurationModule", moduleName)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

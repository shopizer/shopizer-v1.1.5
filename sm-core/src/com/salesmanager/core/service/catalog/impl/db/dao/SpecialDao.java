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
package com.salesmanager.core.service.catalog.impl.db.dao;

// Generated Aug 19, 2008 8:26:20 AM by Hibernate Tools 3.2.0.beta8

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.Special;

/**
 * Home object for domain model class Specials.
 * 
 * @see com.salesmanager.core.test.Specials
 * @author Hibernate Tools
 */
@Repository
public class SpecialDao extends HibernateDaoSupport implements ISpecialDao {

	private static final Log log = LogFactory.getLog(SpecialDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public SpecialDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.ISpecialDao#persist(com.
	 * salesmanager.core.entity.catalog.Special)
	 */
	public void persist(Special transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.ISpecialDao#saveOrUpdate(com
	 * .salesmanager.core.entity.catalog.Special)
	 */
	public void saveOrUpdate(Special instance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.ISpecialDao#delete(com.
	 * salesmanager.core.entity.catalog.Special)
	 */
	public void delete(Special persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.ISpecialDao#merge(com.salesmanager
	 * .core.entity.catalog.Special)
	 */
	public Special merge(Special detachedInstance) {
		try {
			Special result = (Special) super.getHibernateTemplate().merge(
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
	 * @see
	 * com.salesmanager.core.service.catalog.impl.ISpecialDao#findByProductId
	 * (long)
	 */
	public Special findByProductId(long productId) {
		try {
			Special special = (Special) super.getSession().createCriteria(
					Special.class).add(Restrictions.eq("productId", productId))
					.uniqueResult();
			return special;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

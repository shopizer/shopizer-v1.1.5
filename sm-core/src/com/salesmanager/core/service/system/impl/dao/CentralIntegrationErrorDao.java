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
package com.salesmanager.core.service.system.impl.dao;

// Generated Apr 29, 2010 2:03:20 PM by Hibernate Tools 3.2.4.GA

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.system.CentralIntegrationError;

/**
 * Home object for domain model class CentralIntegrationErrors.
 * 
 * @see com.salesmanager.core.test.CentralIntegrationErrors
 * @author Hibernate Tools
 */
@Repository
public class CentralIntegrationErrorDao extends HibernateDaoSupport implements
		ICentralIntegrationErrorDao {

	@Autowired
	public CentralIntegrationErrorDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private static final Log log = LogFactory
			.getLog(CentralIntegrationErrorDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.system.impl.dao.ICentralIntegrationErrorDao
	 * #persist(com.salesmanager.core.entity.system.CentralIntegrationError)
	 */
	public void persist(CentralIntegrationError transientInstance) {
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
	 * com.salesmanager.core.service.system.impl.dao.ICentralIntegrationErrorDao
	 * #
	 * saveOrUpdate(com.salesmanager.core.entity.system.CentralIntegrationError)
	 */
	public void saveOrUpdate(CentralIntegrationError instance) {
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
	 * com.salesmanager.core.service.system.impl.dao.ICentralIntegrationErrorDao
	 * #delete(com.salesmanager.core.entity.system.CentralIntegrationError)
	 */
	public void delete(CentralIntegrationError persistentInstance) {
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
	 * com.salesmanager.core.service.system.impl.dao.ICentralIntegrationErrorDao
	 * #findByMerchantId(java.lang.Integer)
	 */
	public Collection<CentralIntegrationError> findByMerchantId(
			java.lang.Integer id) {
		try {
			List list = super.getSession().createCriteria(
					CentralIntegrationError.class).add(
					Restrictions.eq("merchantid", id)).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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
package com.salesmanager.core.service.payment.impl.dao;

// Generated Apr 10, 2009 10:47:08 AM by Hibernate Tools 3.2.0.beta8

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.payment.OffsystemNotificationOrder;

/**
 * Home object for domain model class OffsystemNotificationOrders.
 * 
 * @see com.salesmanager.core.entity.payment.OffsystemNotificationOrder
 * @author Hibernate Tools
 */
@Repository
public class OffsystemNotificationOrderDao extends HibernateDaoSupport
		implements IOffsystemNotificationOrderDao {

	private static final Log log = LogFactory
			.getLog(OffsystemNotificationOrderDao.class);

	@Autowired
	public OffsystemNotificationOrderDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.system.impl.dao.IOffsystemNotificationOrderDao
	 * #persist(com.salesmanager.core.entity.system.OffsystemNotificationOrder)
	 */
	public void persist(OffsystemNotificationOrder transientInstance) {
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
	 * com.salesmanager.core.service.system.impl.dao.IOffsystemNotificationOrderDao
	 * #
	 * saveOrUpdate(com.salesmanager.core.entity.system.OffsystemNotificationOrder
	 * )
	 */
	public void saveOrUpdate(OffsystemNotificationOrder instance) {
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
	 * com.salesmanager.core.service.system.impl.dao.IOffsystemNotificationOrderDao
	 * #delete(com.salesmanager.core.entity.system.OffsystemNotificationOrder)
	 */
	public void delete(OffsystemNotificationOrder persistentInstance) {
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
	 * com.salesmanager.core.service.system.impl.dao.IOffsystemNotificationOrderDao
	 * #findById(int)
	 */
	public OffsystemNotificationOrder findById(long id) {

		try {
			OffsystemNotificationOrder instance = (OffsystemNotificationOrder) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.system.OffsystemNotificationOrder",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

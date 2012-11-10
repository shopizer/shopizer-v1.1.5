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
package com.salesmanager.core.service.order.impl.dao;

// Generated Nov 8, 2008 9:09:21 AM by Hibernate Tools 3.2.0.beta8

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderAccount;

/**
 * Home object for domain model class OrdersAccount.
 * 
 * @see com.salesmanager.core.entity.orders.OrderAccount
 * @author Hibernate Tools
 */
@Repository
public class OrderAccountDao extends HibernateDaoSupport implements
		IOrderAccountDao {

	private static final Log log = LogFactory.getLog(OrderAccountDao.class);

	@Autowired
	public OrderAccountDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountDao#persist
	 * (com.salesmanager.core.entity.orders.OrderAccount)
	 */
	public void persist(OrderAccount transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountDao#saveOrUpdate
	 * (com.salesmanager.core.entity.orders.OrderAccount)
	 */
	public void saveOrUpdate(OrderAccount instance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountDao#delete(
	 * com.salesmanager.core.entity.orders.OrderAccount)
	 */
	public void delete(OrderAccount persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountDao#findById
	 * (long)
	 */
	public OrderAccount findById(long id) {
		try {
			OrderAccount instance = (OrderAccount) super
					.getHibernateTemplate()
					.get("com.salesmanager.core.entity.orders.OrderAccount", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public OrderAccount findByOrderId(long orderId) {
		try {
			OrderAccount instance = (OrderAccount) super.getSession()
					.createCriteria(OrderAccount.class).add(
							Restrictions.eq("orderId", orderId)).uniqueResult();
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

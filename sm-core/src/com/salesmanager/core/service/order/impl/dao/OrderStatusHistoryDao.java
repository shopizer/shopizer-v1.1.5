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

// Generated Oct 1, 2008 11:18:03 AM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderStatusHistory;

/**
 * Home object for domain model class Orders.
 * 
 * @see com.salesmanager.core.test.Orders
 * @author Hibernate Tools
 */
@Repository
public class OrderStatusHistoryDao extends HibernateDaoSupport implements
		IOrderStatusHistoryDao {

	private static final Log log = LogFactory
			.getLog(OrderStatusHistoryDao.class);

	@Autowired
	public OrderStatusHistoryDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#persist
	 * (com.salesmanager.core.entity.orders.OrderStatusHistory)
	 */
	public void persist(OrderStatusHistory transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#saveOrUpdate
	 * (com.salesmanager.core.entity.orders.OrderStatusHistory)
	 */
	public void saveOrUpdate(OrderStatusHistory instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<OrderStatusHistory> coll) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(coll);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#delete
	 * (com.salesmanager.core.entity.orders.OrderStatusHistory)
	 */
	public void delete(OrderStatusHistory persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<OrderStatusHistory> coll) {
		try {
			super.getHibernateTemplate().deleteAll(coll);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#merge
	 * (com.salesmanager.core.entity.orders.OrderStatusHistory)
	 */
	public OrderStatusHistory merge(OrderStatusHistory detachedInstance) {
		try {
			OrderStatusHistory result = (OrderStatusHistory) super
					.getHibernateTemplate().merge(detachedInstance);
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
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#findById
	 * (long)
	 */
	public OrderStatusHistory findById(long id) {
		try {
			OrderStatusHistory instance = (OrderStatusHistory) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderStatusHistory",
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
	 * com.salesmanager.core.service.order.impl.IOrderStatusHistoryDao#findByOrderId
	 * (long)
	 */
	public Collection<OrderStatusHistory> findByOrderId(long orderId) {
		try {

			List status = super.getSession().createCriteria(
					OrderStatusHistory.class).add(
					Restrictions.eq("orderId", orderId)).list();

			return status;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

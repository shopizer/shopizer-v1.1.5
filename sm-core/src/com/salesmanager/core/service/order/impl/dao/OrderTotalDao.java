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

// Generated Dec 29, 2008 11:58:51 AM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderTotal;

/**
 * Home object for domain model class OrdersTotal.
 * 
 * @see com.salesmanager.core.test.OrderTotal
 * @author Hibernate Tools
 */
@Repository
public class OrderTotalDao extends HibernateDaoSupport implements
		IOrderTotalDao {

	private static final Log log = LogFactory.getLog(OrderTotalDao.class);

	@Autowired
	public OrderTotalDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderTotal#persist(com.
	 * salesmanager.core.test.OrderTotal)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#persist
	 * (com.salesmanager.core.test.OrderTotal)
	 */
	public void persist(OrderTotal transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderTotal#saveOrUpdate
	 * (com.salesmanager.core.test.OrderTotal)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#saveOrUpdate
	 * (com.salesmanager.core.test.OrderTotal)
	 */
	public void saveOrUpdate(OrderTotal instance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderTotal#saveOrUpdateAll
	 * (java.util.Collection)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#saveOrUpdateAll
	 * (java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<OrderTotal> coll) {
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
	 * @seecom.salesmanager.core.service.order.impl.dao.IOrderTotal#delete(com.
	 * salesmanager.core.test.OrderTotal)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#delete(
	 * com.salesmanager.core.test.OrderTotal)
	 */
	public void delete(OrderTotal persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderTotal#deleteAll(java
	 * .util.Collection)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#deleteAll
	 * (java.util.Collection)
	 */
	public void deleteAll(Collection<OrderTotal> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderTotal#findById(int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#findById
	 * (int)
	 */
	public OrderTotal findById(int id) {
		try {
			OrderTotal instance = (OrderTotal) super.getHibernateTemplate()
					.get("com.salesmanager.core.entity.orders.OrderTotal", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

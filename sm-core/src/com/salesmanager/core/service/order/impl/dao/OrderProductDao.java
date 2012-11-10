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

import com.salesmanager.core.entity.orders.OrderProduct;

/**
 * Home object for domain model class OrdersProducts.
 * 
 * @see com.salesmanager.core.test.OrdersProducts
 * @author Hibernate Tools
 */
@Repository
public class OrderProductDao extends HibernateDaoSupport implements
		IOrderProductDao {

	private static final Log log = LogFactory.getLog(OrderProductDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public OrderProductDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#persist
	 * (com.salesmanager.core.entity.orders.OrderProduct)
	 */
	public void persist(OrderProduct transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#saveOrUpdate
	 * (com.salesmanager.core.entity.orders.OrderProduct)
	 */
	public void saveOrUpdate(OrderProduct instance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#saveOrUpdateAll
	 * (java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<OrderProduct> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#delete(
	 * com.salesmanager.core.entity.orders.OrderProduct)
	 */
	public void delete(OrderProduct persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#deleteAll
	 * (java.util.Collection)
	 */
	public void deleteAll(Collection<OrderProduct> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductDao#findById
	 * (int)
	 */
	public OrderProduct findById(long id) {
		try {
			OrderProduct instance = (OrderProduct) super
					.getHibernateTemplate()
					.get("com.salesmanager.core.entity.orders.OrderProduct", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

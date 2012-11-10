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

// Generated Dec 29, 2008 11:38:32 AM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderProductPrice;

/**
 * Home object for domain model class OrdersProductsPrices.
 * 
 * @see com.salesmanager.core.entity.orders.OrderProductPrice
 * @author Hibernate Tools
 */
@Repository
public class OrderProductPriceDao extends HibernateDaoSupport implements
		IOrderProductPriceDao {

	private static final Log log = LogFactory
			.getLog(OrderProductPriceDao.class);

	@Autowired
	public OrderProductPriceDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPrice#persist
	 * (com.salesmanager.core.entity.orders.OrderProductPrice)
	 */
	public void persist(OrderProductPrice transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPrice#saveOrUpdate
	 * (com.salesmanager.core.entity.orders.OrderProductPrice)
	 */
	public void saveOrUpdate(OrderProductPrice instance) {
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
	 * @seecom.salesmanager.core.service.order.impl.dao.IOrderProductPrice#
	 * saveOrUpdateAll(java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<OrderProductPrice> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPrice#delete
	 * (com.salesmanager.core.entity.orders.OrderProductPrice)
	 */
	public void delete(OrderProductPrice persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPrice#deleteAll
	 * (java.util.Collection)
	 */
	public void deleteAll(Collection<OrderProductPrice> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPrice#findById
	 * (int)
	 */
	public OrderProductPrice findById(int id) {
		try {
			OrderProductPrice instance = (OrderProductPrice) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderProductPrice",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

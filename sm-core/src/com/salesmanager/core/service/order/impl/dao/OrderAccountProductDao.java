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

// Generated Jan 7, 2009 9:29:01 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderAccountProduct;

/**
 * Home object for domain model class OrdersAccountProducts.
 * 
 * @see com.salesmanager.core.entity.orders.OrderAccountProduct
 * @author Hibernate Tools
 */
@Repository
public class OrderAccountProductDao extends HibernateDaoSupport implements
		IOrderAccountProductDao {

	private static final Log log = LogFactory
			.getLog(OrderAccountProductDao.class);

	@Autowired
	public OrderAccountProductDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * persist(com.salesmanager.core.entity.orders.OrderAccountProduct)
	 */
	public void persist(OrderAccountProduct transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * saveOrUpdate(com.salesmanager.core.entity.orders.OrderAccountProduct)
	 */
	public void saveOrUpdate(OrderAccountProduct instance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * saveOrUpdateAll(java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<OrderAccountProduct> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * delete(com.salesmanager.core.entity.orders.OrderAccountProduct)
	 */
	public void delete(OrderAccountProduct persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<OrderAccountProduct> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao#
	 * findById(long)
	 */
	public OrderAccountProduct findById(long id) {
		try {
			OrderAccountProduct instance = (OrderAccountProduct) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderAccountProduct",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

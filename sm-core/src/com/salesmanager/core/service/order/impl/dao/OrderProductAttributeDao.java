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

import com.salesmanager.core.entity.orders.OrderProductAttribute;

/**
 * Home object for domain model class OrdersProductsAttributes.
 * 
 * @see com.salesmanager.core.test.OrdersProductsAttributes
 * @author Hibernate Tools
 */
@Repository
public class OrderProductAttributeDao extends HibernateDaoSupport implements
		IOrderProductAttributeDao {

	private static final Log log = LogFactory
			.getLog(OrderProductAttributeDao.class);

	@Autowired
	public OrderProductAttributeDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#persist
	 * (com.salesmanager.core.entity.orders.OrderProductAttribute)
	 */
	public void persist(OrderProductAttribute transientInstance) {

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
	 * @seecom.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#
	 * saveOrUpdate(com.salesmanager.core.entity.orders.OrderProductAttribute)
	 */
	public void saveOrUpdate(OrderProductAttribute instance) {
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
	 * @seecom.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#
	 * saveOrUpdateAll(java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<OrderProductAttribute> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#delete
	 * (com.salesmanager.core.entity.orders.OrderProductAttribute)
	 */
	public void delete(OrderProductAttribute persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#deleteAll
	 * (java.util.Collection)
	 */
	public void deleteAll(Collection<OrderProductAttribute> coll) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductAttribute#findById
	 * (int)
	 */
	public OrderProductAttribute findById(int id) {
		try {
			OrderProductAttribute instance = (OrderProductAttribute) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderProductAttribute",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

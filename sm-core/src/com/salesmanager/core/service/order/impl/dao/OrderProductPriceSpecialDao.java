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

// Generated Mar 8, 2009 8:57:18 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderProductPriceSpecial;

/**
 * Home object for domain model class OrdersProductsPricesSpecials.
 * 
 * @see com.salesmanager.core.entity.orders.OrderProductPriceSpecial
 * @author Hibernate Tools
 */
@Repository
public class OrderProductPriceSpecialDao extends HibernateDaoSupport implements
		IOrderProductPriceSpecialDao {

	private static final Log log = LogFactory
			.getLog(OrderProductPriceSpecialDao.class);

	@Autowired
	public OrderProductPriceSpecialDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPriceSpecialDao
	 * #persist(com.salesmanager.core.entity.orders.OrderProductPriceSpecial)
	 */
	public void persist(OrderProductPriceSpecial transientInstance) {
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPriceSpecialDao
	 * #
	 * saveOrUpdate(com.salesmanager.core.entity.orders.OrderProductPriceSpecial
	 * )
	 */
	public void saveOrUpdate(OrderProductPriceSpecial instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<OrderProductPriceSpecial> coll) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(coll);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<OrderProductPriceSpecial> coll) {
		try {
			super.getHibernateTemplate().deleteAll(coll);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPriceSpecialDao
	 * #delete(com.salesmanager.core.entity.orders.OrderProductPriceSpecial)
	 */
	public void delete(OrderProductPriceSpecial persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OrderProductPriceSpecial findById(long id) {
		try {
			OrderProductPriceSpecial instance = (OrderProductPriceSpecial) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderProductPriceSpecial",
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
	 * com.salesmanager.core.service.order.impl.dao.IOrderProductPriceSpecialDao
	 * #findById(long)
	 */
	public void deleteByOrderProductPriceIds(List ids) {

		try {

			List list = super.getSession().createCriteria(
					OrderProductPriceSpecial.class).add(
					Restrictions.in("orderProductPrice", ids)).list();

			this.deleteAll(list);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}

}

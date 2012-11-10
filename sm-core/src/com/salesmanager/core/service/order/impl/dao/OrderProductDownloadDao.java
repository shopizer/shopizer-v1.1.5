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

import com.salesmanager.core.entity.orders.OrderProductDownload;

/**
 * Home object for domain model class OrdersProductsDownload.
 * 
 * @see com.salesmanager.core.test.OrderProductDownload
 * @author Hibernate Tools
 */
@Repository
public class OrderProductDownloadDao extends HibernateDaoSupport implements
		IOrderProductDownloadDao {

	private static final Log log = LogFactory
			.getLog(OrderProductDownloadDao.class);

	@Autowired
	public OrderProductDownloadDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IOrderProductDownloadDao#persist
	 * (com.salesmanager.core.entity.orders.OrderProductDownload)
	 */
	public void persist(OrderProductDownload transientInstance) {
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
	 * @seecom.salesmanager.core.service.order.impl.IOrderProductDownloadDao#
	 * saveOrUpdate(com.salesmanager.core.entity.orders.OrderProductDownload)
	 */
	public void saveOrUpdate(OrderProductDownload instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<OrderProductDownload> coll) {
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
	 * com.salesmanager.core.service.order.impl.IOrderProductDownloadDao#delete
	 * (com.salesmanager.core.entity.orders.OrderProductDownload)
	 */
	public void delete(OrderProductDownload persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<OrderProductDownload> coll) {
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
	 * com.salesmanager.core.service.order.impl.IOrderProductDownloadDao#merge
	 * (com.salesmanager.core.entity.orders.OrderProductDownload)
	 */
	public OrderProductDownload merge(OrderProductDownload detachedInstance) {
		try {
			OrderProductDownload result = (OrderProductDownload) super
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
	 * com.salesmanager.core.service.order.impl.IOrderProductDownloadDao#findById
	 * (int)
	 */
	public OrderProductDownload findById(long id) {
		try {
			OrderProductDownload instance = (OrderProductDownload) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.orders.OrderProductDownload",
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
	 * @seecom.salesmanager.core.service.order.impl.IOrderProductDownloadDao#
	 * findByOrderId(int)
	 */
	public List<OrderProductDownload> findByOrderId(long id) {
		try {

			List downloads = super.getSession().createCriteria(
					OrderProductDownload.class).add(
					Restrictions.eq("orderId", id)).list();

			return downloads;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;

/**
 * Home object for domain model class Orders.
 * 
 * @see com.salesmanager.core.test.Orders
 * @author Hibernate Tools
 */
@Repository
public class OrderDao extends HibernateDaoSupport implements IOrderDao {

	private static final Log log = LogFactory.getLog(OrderDao.class);

	@Autowired
	public OrderDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IOrderDao#persist(com.salesmanager
	 * .core.entity.orders.Order)
	 */
	public void persist(Order transientInstance) {
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
	 * @seecom.salesmanager.core.service.order.impl.IOrderDao#saveOrUpdate(com.
	 * salesmanager.core.entity.orders.Order)
	 */
	public void saveOrUpdate(Order instance) {
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
	 * com.salesmanager.core.service.order.impl.IOrderDao#delete(com.salesmanager
	 * .core.entity.orders.Order)
	 */
	public void delete(Order persistentInstance) {
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
	 * com.salesmanager.core.service.order.impl.IOrderDao#merge(com.salesmanager
	 * .core.entity.orders.Order)
	 */
	public Order merge(Order detachedInstance) {
		try {
			Order result = (Order) super.getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Order createRawOrder(long orderId) {
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();

			session.createSQLQuery(
					"INSERT INTO orders(orders_id) values (" + orderId + ")")
					.executeUpdate();

			Order result = findById(orderId);
			return result;
		} catch (Exception re) {
			log.error("merge failed", re);
			throw new RuntimeException(re);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.salesmanager.core.service.order.impl.IOrderDao#findById(int)
	 */
	public Order findById(long id) {
		try {
			Order instance = (Order) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.orders.Order", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Order> findOrdersByCustomer(long customerId) {

		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("customerId", customerId)).addOrder(
				org.hibernate.criterion.Order.desc("orderId"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();

	}

	public List<Order> findOrdersByMerchant(int merchantId) {

		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("merchantId", merchantId)).addOrder(
				org.hibernate.criterion.Order.desc("orderId"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<Order> findInvoicesByCustomer(long customerId) {
		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("customerId", customerId)).add(
				Restrictions.eq("channel", OrderConstants.INVOICE_CHANNEL))
				.addOrder(org.hibernate.criterion.Order.desc("orderId"));

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public Collection<Order> findInvoicesByCustomerAndStartDate(
			long customerId, Date startDate) {
		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("customerId", customerId)).add(
				Restrictions.eq("channel", OrderConstants.INVOICE_CHANNEL))
				.add(Restrictions.ge("datePurchased", startDate)).addOrder(
						org.hibernate.criterion.Order.desc("orderId"));

		return criteria.list();

	}

	public SearchOrderResponse searchInvoice(SearchOrdersCriteria searchCriteria) {

		Criteria criteria = super
				.getSession()
				.createCriteria(Order.class)
				.add(
						Restrictions.eq("merchantId", searchCriteria
								.getMerchantId()))
				.add(Restrictions.eq("channel", OrderConstants.INVOICE_CHANNEL))
				.add(
						Restrictions.eq("orderStatus",
								OrderConstants.STATUSINVOICED)).addOrder(
						org.hibernate.criterion.Order.desc("orderId"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		StringBuffer q = new StringBuffer();

		q.append(" select o from Order o where o.merchantId=:mId");
		q.append(" and channel=:channel and orderStatus=:status");

		if (searchCriteria != null) {

			if (!StringUtils.isBlank(searchCriteria.getCustomerName())) {
				q.append(" and o.customerName like %:cName%");
			}

			if (searchCriteria.getOrderId() != -1) {
				q.append(" and o.orderId=:oId");
			}

			if (searchCriteria.getEdate() != null
					|| searchCriteria.getSdate() != null) {
				if (searchCriteria.getSdate() != null) {
					q.append(" and o.datePurchased > :sDate");
				} else {
					q.append(" and o.datePurchased > :sDate");
				}
				if (searchCriteria.getEdate() != null) {
					q.append(" and o.datePurchased < :eDate");
				} else {
					q.append(" and o.datePurchased < :eDate");
				}
			}
		}
		q.append(" order by o.orderId desc");

		Query c = super.getSession().createQuery(q.toString());
		c.setInteger("channel", OrderConstants.INVOICE_CHANNEL);
		c.setInteger("status", OrderConstants.STATUSINVOICED);
		c.setInteger("mId", searchCriteria.getMerchantId());

		if (searchCriteria != null) {

			if (!StringUtils.isBlank(searchCriteria.getCustomerName())) {
				criteria.add(Restrictions.like("customerName", "%"
						+ searchCriteria.getCustomerName() + "%"));
				c.setString("cName", "%" + searchCriteria.getCustomerName()
						+ "%");
			}

			if (searchCriteria.getOrderId() != -1) {
				criteria.add(Restrictions.eq("orderId", searchCriteria
						.getOrderId()));
				c.setLong("oId", searchCriteria.getOrderId());
			}

			if (searchCriteria.getEdate() != null
					|| searchCriteria.getSdate() != null) {
				if (searchCriteria.getSdate() != null) {
					criteria.add(Restrictions.ge("datePurchased",
							searchCriteria.getSdate()));
					c.setDate("sDate", searchCriteria.getSdate());
				} else {
					criteria.add(Restrictions.ge("datePurchased", DateUtils
							.addDays(new Date(), -1)));
					c.setDate("sDate", DateUtils.addDays(new Date(), -1));
				}
				if (searchCriteria.getEdate() != null) {
					criteria.add(Restrictions.le("datePurchased",
							searchCriteria.getEdate()));
					c.setDate("eDate", searchCriteria.getEdate());
				} else {
					criteria.add(Restrictions.ge("datePurchased", DateUtils
							.addDays(new Date(), +1)));
					c.setDate("eDate", DateUtils.addDays(new Date(), +1));
				}
			}
		}

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();

		criteria.setProjection(null);

		int max = searchCriteria.getQuantity();

		List list = null;
		if (max != -1 && count > 0) {
			list = c.setMaxResults(searchCriteria.getUpperLimit(count))
					.setFirstResult(searchCriteria.getLowerLimit()).list();
		} else {
			list = c.list();
		}

		SearchOrderResponse response = new SearchOrderResponse();
		response.setCount(count);
		response.setOrders(list);

		return response;

	}

	public SearchOrderResponse searchOrder(SearchOrdersCriteria searchCriteria) {

		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("merchantId", searchCriteria.getMerchantId()))
				.add(Restrictions.eq("channel", OrderConstants.ONLINE_CHANNEL))
				.addOrder(org.hibernate.criterion.Order.desc("orderId"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		StringBuffer q = new StringBuffer();

		q.append(" select o from Order o where o.merchantId=:mId");
		q.append(" and channel=:channel");

		if (searchCriteria != null) {

			if (!StringUtils.isBlank(searchCriteria.getCustomerName())) {
				q.append(" and o.customerName like :cName");
			}

			if (searchCriteria.getOrderId() != -1) {
				q.append(" and o.orderId= :oId");
			}

			if (searchCriteria.getEdate() != null
					|| searchCriteria.getSdate() != null) {
				if (searchCriteria.getSdate() != null) {
					q.append(" and o.datePurchased > :sDate");
				} else {
					q.append(" and o.datePurchased > :sDate");
				}
				if (searchCriteria.getEdate() != null) {
					q.append(" and o.datePurchased < :eDate");
				} else {
					q.append(" and o.datePurchased < :eDate");
				}
			}
		}
		q.append(" order by o.orderId desc");

		Query c = super.getSession().createQuery(q.toString());
		c.setInteger("channel", OrderConstants.ONLINE_CHANNEL);
		c.setInteger("mId", searchCriteria.getMerchantId());

		if (searchCriteria != null) {

			if (!StringUtils.isBlank(searchCriteria.getCustomerName())) {
				criteria.add(Restrictions.like("customerName", "%"
						+ searchCriteria.getCustomerName() + "%"));
				c.setString("cName", "%" + searchCriteria.getCustomerName()
						+ "%");
			}

			if (searchCriteria.getOrderId() != -1) {
				criteria.add(Restrictions.eq("orderId", searchCriteria
						.getOrderId()));
				c.setLong("oId", searchCriteria.getOrderId());
			}

			if (searchCriteria.getEdate() != null
					|| searchCriteria.getSdate() != null) {
				if (searchCriteria.getSdate() != null) {
					criteria.add(Restrictions.ge("datePurchased",
							searchCriteria.getSdate()));
					c.setDate("sDate", searchCriteria.getSdate());
				} else {
					criteria.add(Restrictions.ge("datePurchased", DateUtils
							.addDays(new Date(), -1)));
					c.setDate("sDate", DateUtils.addDays(new Date(), -1));
				}
				if (searchCriteria.getEdate() != null) {
					criteria.add(Restrictions.le("datePurchased",
							searchCriteria.getEdate()));
					c.setDate("eDate", searchCriteria.getEdate());
				} else {
					criteria.add(Restrictions.ge("datePurchased", DateUtils
							.addDays(new Date(), +1)));
					c.setDate("eDate", DateUtils.addDays(new Date(), +1));
				}
			}
		}

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();

		criteria.setProjection(null);

		int max = searchCriteria.getQuantity();

		List list = null;
		if (max != -1 && count > 0) {
			c.setMaxResults(searchCriteria.getUpperLimit(count));
			c.setFirstResult(searchCriteria.getLowerLimit());
			list = c.list();
		} else {
			list = c.list();
		}

		SearchOrderResponse response = new SearchOrderResponse();
		response.setCount(count);
		response.setOrders(list);

		return response;

	}

	public SearchOrderResponse searchOrderByCustomer(
			SearchOrdersCriteria searchCriteria) {

		Criteria criteria = super.getSession().createCriteria(Order.class).add(
				Restrictions.eq("customerId", searchCriteria.getCustomerId()))
				.add(
						Restrictions.eq("merchantId", searchCriteria
								.getMerchantId())).add(
						Restrictions.eq("channel",
								OrderConstants.ONLINE_CHANNEL)).addOrder(
						org.hibernate.criterion.Order.desc("orderId"));



		StringBuffer q = new StringBuffer();

		q.append(" select o from Order o where o.merchantId=:mId");
		q.append(" and channel=:channel");

		q.append(" and o.customerId = :cId");
		q.append(" and o.merchantId = :mId");
		q.append(" order by o.orderId desc");

		Query query = super.getSession().createQuery(q.toString());
		query.setInteger("channel", OrderConstants.ONLINE_CHANNEL);
		query.setInteger("mId", searchCriteria.getMerchantId());
		query.setLong("cId", searchCriteria.getCustomerId());
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();

		criteria.setProjection(null);

		int max = searchCriteria.getQuantity();

		List list = null;
		if (max != -1 && count > 0) {
			query.setMaxResults(searchCriteria.getUpperLimit(count));
			query.setFirstResult(searchCriteria.getLowerLimit());
			list = query.list();
		} else {
			list = query.list();
		}

		SearchOrderResponse response = new SearchOrderResponse();
		response.setCount(count);
		response.setOrders(list);

		return response;

	}

}

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
package com.salesmanager.core.service.customer.impl.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerBasket;
import com.salesmanager.core.entity.customer.CustomerBasketAttribute;
import com.salesmanager.core.entity.customer.SearchCustomerCriteria;
import com.salesmanager.core.entity.customer.SearchCustomerResponse;

@Repository
public class CustomerDao extends HibernateDaoSupport implements ICustomerDao {

	private static final Log log = LogFactory.getLog(CustomerDao.class);

	@Autowired
	public CustomerDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.customer.impl.ICustomerDao#saveShoppingCart
	 * (com.salesmanager.core.entity.customer.CustomerBasket)
	 */
	public void saveShoppingCart(CustomerBasket transientInstance) {
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
	 * @seecom.salesmanager.core.service.customer.impl.ICustomerDao#
	 * saveShoppingCartAttributes
	 * (com.salesmanager.core.entity.customer.CustomerBasketAttribute)
	 */
	public void saveShoppingCartAttributes(
			CustomerBasketAttribute transientInstance) {
		try {
			super.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void persist(Customer transientInstance) {
		try {
			super.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void saveOrUptade(Customer instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public Customer merge(Customer detachedInstance) {
		try {
			Customer result = (Customer) super.getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void delete(Customer persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<Customer> customers) {
		try {
			super.getHibernateTemplate().deleteAll(customers);
		} catch (RuntimeException re) {
			log.error("deleteAll failed", re);
			throw re;
		}
	}

	public Customer findById(long id) {
		try {
			Customer instance = (Customer) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.customer.Customer", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Customer> findByMerchantId(int merchantId) {
		try {
			List list = super.getSession().createCriteria(Customer.class).add(
					Restrictions.eq("merchantId", merchantId)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Customer findByUserNameAndPassword(String userName, String password) {
		try {

			Customer customer = (Customer) super
					.getSession()
					.createQuery(
							"select c from Customer c where c.customerNick=:cId and c.customerPassword=:pId")
					.setString("cId", userName).setString("pId", password)
					.uniqueResult();

			return customer;

		} catch (Exception e) {
			log.error("get failed", e);
			throw new RuntimeException(e);
		}
	}

	public Customer findByUserNameAndPasswordByMerchantId(String userName,
			String password, int merchantId) {
		try {
			Customer customer = (Customer) super.getSession().createCriteria(
					Customer.class).add(
					Restrictions.eq("customerNick", userName)).add(
					Restrictions.eq("customerPassword", password)).add(
					Restrictions.eq("merchantId", merchantId)).uniqueResult();
			return customer;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Customer> findByCompanyName(String companyName,
			int merchantId) {
		try {
			List l = super.getSession().createCriteria(Customer.class).add(
					Restrictions.eq("customerCompany", companyName)).add(
					Restrictions.eq("merchantId", merchantId)).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Customer> findCustomersHavingCompany(int merchantId) {
		try {
			List l = super.getSession().createCriteria(Customer.class).add(
					Restrictions.isNotNull("customerCompany")).add(
					(Restrictions.eq("merchantId", merchantId))).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<String> findUniqueCompanyName(int merchantId) {

		try {

			Query q = super
					.getSession()
					.createSQLQuery(
							"select distinct c.customers_company from customers c where c.merchantId=:p and c.customers_company IS NOT NULL order by c.customers_company asc")
					.addScalar("customers_company", Hibernate.STRING)
					.setParameter("p", merchantId);

			List entries = q.list();

			return entries;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public Customer findCustomerbyEmail(final String email) {

		Customer c = (Customer) super.getSession().createCriteria(
				Customer.class).add(
				Restrictions.eq("customerEmailAddress", email)).uniqueResult();

		return c;
	}

	public SearchCustomerResponse findCustomers(
			SearchCustomerCriteria searchCriteria) {

		Criteria criteria = super.getSession().createCriteria(Customer.class)
				.add(
						Restrictions.eq("merchantId", searchCriteria
								.getMerchantId())).addOrder(
						org.hibernate.criterion.Order.desc("customerLastname"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		Criteria query = super.getSession().createCriteria(Customer.class).add(
				Restrictions.eq("merchantId", searchCriteria.getMerchantId()))
				.addOrder(
						org.hibernate.criterion.Order.desc("customerLastname"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (searchCriteria != null) {

			if (!StringUtils.isBlank(searchCriteria.getCustomerName())) {
				criteria.add(Restrictions.like("customerLastname", "%"
						+ searchCriteria.getCustomerName() + "%"));
				query.add(Restrictions.like("customerLastname", "%"
						+ searchCriteria.getCustomerName() + "%"));
			}

			if (!StringUtils.isBlank(searchCriteria.getEmail())) {
				criteria.add(Restrictions.like("customerEmailAddress", "%"
						+ searchCriteria.getEmail() + "%"));
				query.add(Restrictions.like("customerEmailAddress", "%"
						+ searchCriteria.getEmail() + "%"));
			}

			if (!StringUtils.isBlank(searchCriteria.getCompanyName())) {
				criteria.add(Restrictions.like("customerCompany", "%"
						+ searchCriteria.getCompanyName() + "%"));
				query.add(Restrictions.like("customerCompany", "%"
						+ searchCriteria.getCompanyName() + "%"));
			}

		}

		criteria.setProjection(Projections.rowCount());
		Integer count = (Integer) criteria.uniqueResult();

		criteria.setProjection(null);

		int max = searchCriteria.getQuantity();

		List list = null;
		if (count > 0) {
			query.setMaxResults(searchCriteria.getUpperLimit(count));
			query.setFirstResult(searchCriteria.getLowerLimit());
		}

		list = query.list();

		SearchCustomerResponse response = new SearchCustomerResponse();
		response.setCount(count);
		response.setCustomers(list);

		return response;

	}

	public Customer findCustomerbyUserName(final String userName,
			final int merchantId) {

		Customer c = (Customer) super.getSession().createCriteria(
				Customer.class).add(Restrictions.eq("customerNick", userName))
				.add(Restrictions.eq("merchantId", merchantId)).uniqueResult();

		return c;
	}

}

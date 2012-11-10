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

// Generated Jul 1, 2008 10:06:12 PM by Hibernate Tools 3.2.0.b9

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.customer.CustomerBasketAttribute;

/**
 * Home object for domain model class CustomersBasketAttributes.
 * 
 * @see com.salesmanager.core.dao.CustomersBasketAttributes
 * @author Hibernate Tools
 */
@Repository
public class CustomerBasketAttributeDao extends HibernateDaoSupport implements
		ICustomerBasketAttributeDao {

	private static final Log log = LogFactory
			.getLog(CustomerBasketAttributeDao.class);

	@Autowired
	public CustomerBasketAttributeDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void persist(CustomerBasketAttribute transientInstance) {
		log.debug("persisting CustomersBasketAttributes instance");
		try {
			super.getHibernateTemplate().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void saveOrUpdate(CustomerBasketAttribute instance) {
		log.debug("attaching dirty CustomersBasketAttributes instance");
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CustomerBasketAttribute persistentInstance) {
		log.debug("deleting CustomerBasketAttributes instance");
		try {
			super.getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerBasketAttribute findById(int id) {
		log.debug("getting CustomerBasketAttributes instance with id: " + id);
		try {
			CustomerBasketAttribute instance = (CustomerBasketAttribute) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.customer.CustomerBasketAttribute",
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerBasketAttribute instance) {
		log.debug("finding CustomersBasketAttributes instance by example");
		try {
			List results = getSession()
					.createCriteria(
							"com.salesmanager.core.entity.customer.CustomerBasketAttribute")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}

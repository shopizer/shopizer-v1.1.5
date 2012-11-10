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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.customer.CustomerBasket;

/**
 * Home object for domain model class CustomersBasket.
 * 
 * @see com.salesmanager.core.dao.CustomersBasket
 * @author Hibernate Tools
 */
@Repository
public class CustomerBasketDao extends HibernateDaoSupport implements
		ICustomerBasketDao {

	private static final Log log = LogFactory.getLog(CustomerBasketDao.class);

	@Autowired
	public CustomerBasketDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void persist(CustomerBasket transientInstance) {
		log.debug("persisting CustomerBasket instance");
		try {
			// getSession().persist(transientInstance);
			super.getHibernateTemplate().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void saveOrUpdate(CustomerBasket instance) {
		log.debug("attaching dirty CustomersBasket instance");
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CustomerBasket persistentInstance) {
		log.debug("deleting CustomersBasket instance");
		try {
			super.getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerBasket findById(int id) {
		log.debug("getting CustomersBasket instance with id: " + id);
		try {
			CustomerBasket instance = (CustomerBasket) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.customer.CustomersBasket",
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

}

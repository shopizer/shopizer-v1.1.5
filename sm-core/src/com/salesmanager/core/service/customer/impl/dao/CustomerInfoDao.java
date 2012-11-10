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

// Generated Mar 8, 2009 10:16:41 PM by Hibernate Tools 3.2.0.beta8

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.customer.CustomerInfo;

/**
 * Home object for domain model class CustomersInfo.
 * 
 * @see com.salesmanager.core.entity.customer.CustomerInfo
 * @author Hibernate Tools
 */
@Repository
public class CustomerInfoDao extends HibernateDaoSupport implements
		ICustomerInfoDao {

	private static final Log log = LogFactory.getLog(CustomerInfoDao.class);

	@Autowired
	public CustomerInfoDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.customer.impl.dao.ICustomerInfoDao#persist
	 * (com.salesmanager.core.entity.customer.CustomerInfo)
	 */
	public void persist(CustomerInfo transientInstance) {
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
	 * com.salesmanager.core.service.customer.impl.dao.ICustomerInfoDao#saveOrUpdate
	 * (com.salesmanager.core.entity.customer.CustomerInfo)
	 */
	public void saveOrUpdate(CustomerInfo instance) {
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
	 * com.salesmanager.core.service.customer.impl.dao.ICustomerInfoDao#delete
	 * (com.salesmanager.core.entity.customer.CustomerInfo)
	 */
	public void delete(CustomerInfo persistentInstance) {
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
	 * com.salesmanager.core.service.customer.impl.dao.ICustomerInfoDao#findById
	 * (int)
	 */
	public CustomerInfo findById(long id) {
		try {
			CustomerInfo instance = (CustomerInfo) super.getHibernateTemplate()
					.get("com.salesmanager.core.entity.customer.CustomerInfo",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

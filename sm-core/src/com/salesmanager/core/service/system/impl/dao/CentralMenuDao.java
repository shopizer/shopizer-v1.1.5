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
package com.salesmanager.core.service.system.impl.dao;

// Generated Nov 11, 2009 9:19:11 AM by Hibernate Tools 3.2.4.GA

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.system.CentralFunction;
import com.salesmanager.core.entity.system.CentralGroup;
import com.salesmanager.core.entity.system.CentralRegistrationAssociation;

/**
 * Home object for domain model class CentralRegistrationAssociation.
 * 
 * @see com.salesmanager.core.entity.system.CentralRegistrationAssociation
 * @author Hibernate Tools
 */
@Repository
public class CentralMenuDao extends HibernateDaoSupport implements
		ICentralMenuDao {

	private static final Log log = LogFactory.getLog(CentralMenuDao.class);

	@Autowired
	public CentralMenuDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.system.impl.dao.ICentralMenuDao#save(com
	 * .salesmanager.core.entity.system.CentralRegistrationAssociation)
	 */
	public void save(CentralRegistrationAssociation transientInstance) {
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
	 * com.salesmanager.core.service.system.impl.dao.ICentralMenuDao#saveOrUpdate
	 * (com.salesmanager.core.entity.system.CentralRegistrationAssociation)
	 */
	public void saveOrUpdate(CentralRegistrationAssociation instance) {
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
	 * com.salesmanager.core.service.system.impl.dao.ICentralMenuDao#delete(
	 * com.salesmanager.core.entity.system.CentralRegistrationAssociation)
	 */
	public void delete(CentralRegistrationAssociation persistentInstance) {

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
	 * com.salesmanager.core.service.system.impl.dao.ICentralMenuDao#findById
	 * (java.lang.Integer)
	 */
	public CentralRegistrationAssociation findById(java.lang.Integer id) {

		try {
			CentralRegistrationAssociation instance = (CentralRegistrationAssociation) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.system.CentralRegistrationAssociation",
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
	 * @seecom.salesmanager.core.service.system.impl.dao.ICentralMenuDao#
	 * loadAllCentralRegistrationAssociation()
	 */
	public Collection<CentralRegistrationAssociation> loadAllCentralRegistrationAssociation() {

		try {
			List list = super.getSession().createCriteria(
					CentralRegistrationAssociation.class).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.system.impl.dao.ICentralMenuDao#
	 * loadAllCentralFunction()
	 */
	public Collection<CentralFunction> loadAllCentralFunction() {

		try {
			List list = super.getSession()
					.createCriteria(CentralFunction.class).add(
							Restrictions.eq("centralFunctionVisible",
									new Boolean(true))).addOrder(
							org.hibernate.criterion.Order
									.asc("centralFunctionPosition")).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.system.impl.dao.ICentralMenuDao#
	 * loadAllCentralGroup()
	 */
	public Collection<CentralGroup> loadAllCentralGroup() {

		try {
			List list = super.getSession().createCriteria(CentralGroup.class)
					.add(
							Restrictions.eq("centralGroupVisible", new Boolean(
									true))).addOrder(
							org.hibernate.criterion.Order
									.asc("centralGroupPosition")).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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
package com.salesmanager.core.service.merchant.impl.dao;

// Generated Jul 4, 2009 10:54:16 AM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantUserRole;

/**
 * Home object for domain model class MerchantUserRole.
 * 
 * @see com.salesmanager.core.test.MerchantUserRole
 * @author Hibernate Tools
 */

@Repository
public class MerchantUserRoleDao extends HibernateDaoSupport implements
		IMerchantUserRoleDao {

	private static final Log log = LogFactory.getLog(MerchantUserRoleDao.class);


	@Autowired
	public MerchantUserRoleDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao#
	 * save(com.salesmanager.core.entity.merchant.MerchantUserRole)
	 */
	public void save(MerchantUserRole transientInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao#
	 * saveOrUpdate(com.salesmanager.core.entity.merchant.MerchantUserRole)
	 */
	public void saveOrUpdate(MerchantUserRole instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void saveOrUpdateAll(Collection<MerchantUserRole> instances) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(instances);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao#
	 * delete(com.salesmanager.core.entity.merchant.MerchantUserRole)
	 */
	public void delete(MerchantUserRole persistentInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao#
	 * findByUserName(java.lang.String)
	 */
	public Collection<MerchantUserRole> findByUserName(String userName) {
		try {

			Query c = super
					.getSession()
					.createQuery(
							"select r from MerchantUserRole r where adminName = :adminName");
			c.setString("adminName", userName);

			return c.list();

			/*
			 * Collection<MerchantUserRole> lst = super.getSession()
			 * .createCriteria(MerchantUserRole.class)
			 * .add(Restrictions.eq("adminName", userName)) .list();
			 */

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDao#
	 * deleteByUserName(java.lang.String)
	 */
	public void deleteByUserName(String userName) {
		try {
			Collection<MerchantUserRole> lst = super.getSession()
					.createCriteria(MerchantUserRole.class).add(
							Restrictions.eq("adminName", userName)).list();

			if (lst != null) {

				super.getHibernateTemplate().deleteAll(lst);

			}

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

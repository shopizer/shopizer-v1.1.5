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

// Generated Jul 31, 2008 1:26:06 PM by Hibernate Tools 3.2.0.b9

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantUserInformation;

/**
 * Home object for domain model class MerchantUserInformation.
 * 
 * @see com.salesmanager.core.service.merchant.impl.MerchantUserInformation
 * @author Hibernate Tools
 */
@Repository
public class MerchantUserInformationDao extends HibernateDaoSupport implements
		IMerchantUserInformationDao {

	private static final Log log = LogFactory
			.getLog(MerchantUserInformationDao.class);

	@Autowired
	public MerchantUserInformationDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #persist(com.salesmanager.core.entity.merchant.MerchantUserInformation)
	 */
	public void persist(MerchantUserInformation transientInstance) {

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
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #delete(com.salesmanager.core.entity.merchant.MerchantUserInformation)
	 */
	public void delete(MerchantUserInformation persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	

	public void deleteAll(Collection<MerchantUserInformation> persistentInstances) {
		
		try {
			super.getHibernateTemplate().deleteAll(persistentInstances);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #merge(com.salesmanager.core.entity.merchant.MerchantUserInformation)
	 */
	public void saveOrUpdate(MerchantUserInformation instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #findById(int)
	 */
	public MerchantUserInformation findById(long id) {
		try {
			MerchantUserInformation instance = (MerchantUserInformation) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.merchant.MerchantUserInformation",
							new Long(id));

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<MerchantUserInformation> findByMerchantId(int merchantId) {
		try {
			List instances = super
					.getSession().createCriteria(MerchantUserInformation.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.list();

			return instances;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public MerchantUserInformation findByAdminEmail(String email) {
		List<MerchantUserInformation> userInfo = getHibernateTemplate()
				.findByNamedParam(
						"from MerchantUserInformation info where info.adminEmail = :email",
						"email", email);
		return (userInfo == null || userInfo.isEmpty()) ? null : userInfo
				.get(0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #findByUserName(java.lang.String)
	 */
	public MerchantUserInformation findByUserName(String name) {

		try {

			MerchantUserInformation instance = (MerchantUserInformation) super
					.getSession().createCriteria(MerchantUserInformation.class)
					.add(Restrictions.eq("adminName", name)).uniqueResult();

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
	 * com.salesmanager.core.service.merchant.impl.IMerchantUserInformationDao
	 * #findByUserNameAndPassword(java.lang.String, java.lang.String)
	 */
	public MerchantUserInformation findByUserNameAndPassword(String name,
			String password) {

		try {

			MerchantUserInformation instance = (MerchantUserInformation) super
					.getSession().createCriteria(MerchantUserInformation.class)
					.add(Restrictions.eq("adminName", name)).add(
							Restrictions.eq("adminPass", password))
					.uniqueResult();

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

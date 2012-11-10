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

// Generated Jul 29, 2008 2:56:55 PM by Hibernate Tools 3.2.0.b9

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantRegistration;

@Repository
public class MerchantRegistrationDao extends HibernateDaoSupport implements
		IMerchantRegistrationDao {

	private static final Log log = LogFactory
			.getLog(MerchantRegistrationDao.class);

	@Autowired
	public MerchantRegistrationDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantRegistrationDao#
	 * persist(com.salesmanager.core.entity.merchant.MerchantRegistration)
	 */
	public void persist(MerchantRegistration transientInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantRegistrationDao#
	 * delete(com.salesmanager.core.entity.merchant.MerchantRegistration)
	 */
	public void delete(MerchantRegistration persistentInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantRegistrationDao#
	 * merge(com.salesmanager.core.entity.merchant.MerchantRegistration)
	 */
	public MerchantRegistration merge(MerchantRegistration detachedInstance) {
		try {
			MerchantRegistration result = (MerchantRegistration) super
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantRegistrationDao#
	 * findByMerchantId(int)
	 */
	public MerchantRegistration findByMerchantId(int merchantid) {
		try {
			MerchantRegistration instance = (MerchantRegistration) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.merchant.MerchantRegistration",
							merchantid);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

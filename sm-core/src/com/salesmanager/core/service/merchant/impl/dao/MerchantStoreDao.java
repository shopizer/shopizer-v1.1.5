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

// Generated Aug 7, 2008 11:34:44 PM by Hibernate Tools 3.2.0.beta8

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantStore;

/**
 * Home object for domain model class MerchantStore.
 * 
 * @see com.salesmanager.core.test.MerchantStore
 * @author Hibernate Tools
 */
@Repository
public class MerchantStoreDao extends HibernateDaoSupport implements
		IMerchantStoreDao {

	private static final Log log = LogFactory.getLog(MerchantStoreDao.class);

	@Autowired
	public MerchantStoreDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.merchant.impl.IMerchantStore#persist(com
	 * .salesmanager.core.test.MerchantStore)
	 */
	public void persist(MerchantStore transientInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantStore#saveOrUpdate
	 * (com.salesmanager.core.test.MerchantStore)
	 */
	public void saveOrUpdate(MerchantStore instance) {
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantStore#delete(com
	 * .salesmanager.core.test.MerchantStore)
	 */
	public void delete(MerchantStore persistentInstance) {
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
	 * com.salesmanager.core.service.merchant.impl.IMerchantStore#merge(com.
	 * salesmanager.core.test.MerchantStore)
	 */
	public MerchantStore merge(MerchantStore detachedInstance) {
		try {
			MerchantStore result = (MerchantStore) super.getHibernateTemplate()
					.merge(detachedInstance);

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
	 * com.salesmanager.core.service.merchant.impl.IMerchantStore#findByMerchantId
	 * (int)
	 */
	public MerchantStore findByMerchantId(int id) {

		try {
			MerchantStore instance = (MerchantStore) super
					.getHibernateTemplate()
					.get("com.salesmanager.core.entity.merchant.MerchantStore",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MerchantStore> loadAll() {
		return getHibernateTemplate().loadAll(MerchantStore.class);
	}

}

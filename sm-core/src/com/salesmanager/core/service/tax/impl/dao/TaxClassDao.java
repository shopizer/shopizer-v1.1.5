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
package com.salesmanager.core.service.tax.impl.dao;

// Generated Aug 7, 2008 11:34:44 PM by Hibernate Tools 3.2.0.beta8

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.tax.TaxClass;

/**
 * Home object for domain model class TaxClass.
 * 
 * @see com.salesmanager.core.service.tax.impl.TaxClass
 * @author Hibernate Tools
 */
@Repository
public class TaxClassDao extends HibernateDaoSupport implements ITaxClassDao {

	private static final Log log = LogFactory.getLog(TaxClassDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public TaxClassDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxClassDao#persist(com.salesmanager
	 * .core.service.tax.impl.TaxClass)
	 */
	public void persist(TaxClass transientInstance) {
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
	 * com.salesmanager.core.service.tax.impl.ITaxClassDao#saveOrUpdate(com.
	 * salesmanager.core.service.tax.impl.TaxClass)
	 */
	public void saveOrUpdate(TaxClass instance) {
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
	 * com.salesmanager.core.service.tax.impl.ITaxClassDao#delete(com.salesmanager
	 * .core.service.tax.impl.TaxClass)
	 */
	public void delete(TaxClass persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<TaxClass> collection) {

		try {
			super.getHibernateTemplate().deleteAll(collection);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxClassDao#merge(com.salesmanager
	 * .core.service.tax.impl.TaxClass)
	 */
	public TaxClass merge(TaxClass detachedInstance) {
		try {
			TaxClass result = (TaxClass) super.getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.salesmanager.core.service.tax.impl.ITaxClassDao#findById(int)
	 */
	public TaxClass findById(long id) {

		try {
			TaxClass instance = (TaxClass) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.tax.TaxClass", id);

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
	 * com.salesmanager.core.service.tax.impl.ITaxClassDao#findByMerchantId(int)
	 */
	public List<TaxClass> findByMerchantId(int merchantid) {

		try {

			List values = new ArrayList();
			values.add(0);
			values.add(merchantid);
			List tx = super.getSession().createCriteria(TaxClass.class).add(
					Restrictions.in("merchantId", values)).list();

			return tx;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<TaxClass> findByOwnerMerchantId(int merchantid) {

		try {

			List tx = super.getSession().createCriteria(TaxClass.class).add(
					Restrictions.eq("merchantId", merchantid)).list();

			return tx;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

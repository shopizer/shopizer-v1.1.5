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

// Generated Sep 4, 2008 8:23:33 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.tax.TaxRateDescription;

/**
 * Home object for domain model class TaxRatesDescription.
 * 
 * @see com.salesmanager.core.entity.tax.TaxRateDescription
 * @author Hibernate Tools
 */
@Repository
public class TaxRateDescriptionDao extends HibernateDaoSupport implements
		ITaxRateDescriptionDao {

	private static final Log log = LogFactory
			.getLog(TaxRateDescriptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public TaxRateDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#persist
	 * (com.salesmanager.core.entity.tax.TaxRateDescription)
	 */
	public void persist(TaxRateDescription transientInstance) {
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
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#saveOrUpdate
	 * (com.salesmanager.core.entity.tax.TaxRateDescription)
	 */
	public void saveOrUpdate(TaxRateDescription instance) {
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
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#delete(
	 * com.salesmanager.core.entity.tax.TaxRateDescription)
	 */
	public void delete(TaxRateDescription persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<TaxRateDescription> collection) {

		try {
			super.getHibernateTemplate().saveOrUpdateAll(collection);
		} catch (RuntimeException re) {
			log.error("bulk save failed", re);
			throw re;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#deleteAll
	 * (java.util.Collection)
	 */
	public void deleteAll(Collection<TaxRateDescription> collection) {

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
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#merge(com
	 * .salesmanager.core.entity.tax.TaxRateDescription)
	 */
	public TaxRateDescription merge(TaxRateDescription detachedInstance) {
		try {
			TaxRateDescription result = (TaxRateDescription) super
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
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#findById
	 * (com.salesmanager.core.entity.tax.TaxRateDescriptionId)
	 */
	public TaxRateDescription findById(
			com.salesmanager.core.entity.tax.TaxRateDescriptionId id) {
		try {
			TaxRateDescription instance = (TaxRateDescription) super
					.getHibernateTemplate()
					.get("com.salesmanager.core.entity.tax.TaxRateDescription",
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
	 * @see
	 * com.salesmanager.core.service.tax.impl.ITaxRateDescriptionDao#findByTaxRateId
	 * (long)
	 */
	public Set<TaxRateDescription> findByTaxRateId(long id) {
		try {
			List descriptions = super.getSession().createCriteria(
					TaxRateDescription.class).add(
					Restrictions.eq("id.taxRateId", id)).list();
			HashSet set = new HashSet();
			set.addAll(descriptions);
			return set;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

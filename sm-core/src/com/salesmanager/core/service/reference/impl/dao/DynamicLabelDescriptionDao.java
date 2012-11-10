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
package com.salesmanager.core.service.reference.impl.dao;

// Generated May 25, 2009 12:08:24 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.DynamicLabelDescription;

/**
 * Home object for domain model class DynamicLabelDescription.
 * 
 * @see com.salesmanager.core.entity.reference.DynamicLabelDescription
 * @author Hibernate Tools
 */
@Repository
public class DynamicLabelDescriptionDao extends HibernateDaoSupport implements
		IDynamicLabelDescriptionDao {

	private static final Log log = LogFactory
			.getLog(DynamicLabelDescriptionDao.class);

	@Autowired
	public DynamicLabelDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao
	 * #persist(com.salesmanager.core.entity.reference.DynamicLabelDescription)
	 */
	public void persist(DynamicLabelDescription transientInstance) {
		try {
			this.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao
	 * #
	 * saveOrUpdate(com.salesmanager.core.entity.reference.DynamicLabelDescription
	 * )
	 */
	public void saveOrUpdate(DynamicLabelDescription instance) {
		try {
			this.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<DynamicLabelDescription> coll) {
		try {
			this.getHibernateTemplate().saveOrUpdateAll(coll);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao
	 * #delete(com.salesmanager.core.entity.reference.DynamicLabelDescription)
	 */
	public void delete(DynamicLabelDescription persistentInstance) {
		try {
			this.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<DynamicLabelDescription> coll) {
		try {
			this.getHibernateTemplate().deleteAll(coll);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao
	 * #merge(com.salesmanager.core.entity.reference.DynamicLabelDescription)
	 */
	public DynamicLabelDescription merge(
			DynamicLabelDescription detachedInstance) {
		try {
			DynamicLabelDescription result = (DynamicLabelDescription) this
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
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDescriptionDao
	 * #
	 * findById(com.salesmanager.core.entity.reference.DynamicLabelDescriptionId
	 * )
	 */
	public DynamicLabelDescription findById(
			com.salesmanager.core.entity.reference.DynamicLabelDescriptionId id) {
		try {
			DynamicLabelDescription instance = (DynamicLabelDescription) this
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.reference.DynamicLabelDescription",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public DynamicLabelDescription findByMerchantIdSectionIdAndSectionId(
			int merchantId, long sectionId, int languageId) {
		try {
			return (DynamicLabelDescription) super.getSession().createCriteria(
					DynamicLabelDescription.class).add(
					Restrictions.eq("id.languageId", languageId)).add(
					Restrictions.eq("dynamicLabel.merchantId", merchantId))
					.add(Restrictions.eq("dynamicLabel.sectionId", sectionId))
					.uniqueResult();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

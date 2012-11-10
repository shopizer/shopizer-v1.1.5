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
package com.salesmanager.core.service.catalog.impl.db.dao;

// Generated Nov 11, 2009 9:19:11 AM by Hibernate Tools 3.2.4.GA

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ReviewDescription;

/**
 * Home object for domain model class ReviewsDescription.
 * 
 * @see com.salesmanager.core.entity.catalog.ReviewDescription
 * @author Hibernate Tools
 */
@Repository
public class ReviewDescriptionDao extends HibernateDaoSupport implements
		IReviewDescriptionDao {

	private static final Log log = LogFactory
			.getLog(ReviewDescriptionDao.class);

	@Autowired
	public ReviewDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * persist(com.salesmanager.core.entity.catalog.ReviewDescription)
	 */
	public void persist(ReviewDescription transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * saveOrUpdate(com.salesmanager.core.entity.catalog.ReviewDescription)
	 */
	public void saveOrUpdate(ReviewDescription instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * saveOrUpdateAll(java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<ReviewDescription> coll) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(coll);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * delete(com.salesmanager.core.entity.catalog.ReviewDescription)
	 */
	public void delete(ReviewDescription persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<ReviewDescription> coll) {
		try {
			super.getHibernateTemplate().deleteAll(coll);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * findById(com.salesmanager.core.entity.catalog.ReviewDescriptionId)
	 */
	public ReviewDescription findById(
			com.salesmanager.core.entity.catalog.ReviewDescriptionId id) {
		try {
			ReviewDescription instance = (ReviewDescription) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ReviewsDescription",
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * findById(long)
	 */
	public Collection<ReviewDescription> findById(long id) {
		try {
			List l = super.getHibernateTemplate().find(
					"com.salesmanager.core.entity.catalog.ReviewsDescription",
					id);

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

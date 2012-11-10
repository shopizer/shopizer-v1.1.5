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

// Generated Nov 11, 2009 10:40:38 AM by Hibernate Tools 3.2.4.GA

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.entity.common.Counter;

/**
 * Home object for domain model class Reviews.
 * 
 * @see com.salesmanager.core.test.Reviews
 * @author Hibernate Tools
 */
@Repository
public class ReviewDao extends HibernateDaoSupport implements IReviewDao {

	private static final Log log = LogFactory.getLog(ReviewDao.class);

	@Autowired
	public ReviewDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#persist(com
	 * .salesmanager.core.entity.catalog.Review)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * persist(com.salesmanager.core.entity.catalog.Review)
	 */
	public void persist(Review transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.Review)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * saveOrUpdate(com.salesmanager.core.entity.catalog.Review)
	 */
	public void saveOrUpdate(Review instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#delete(com.
	 * salesmanager.core.entity.catalog.Review)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * delete(com.salesmanager.core.entity.catalog.Review)
	 */
	public void delete(Review persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#deleteAll(java
	 * .util.Collection)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<Review> coll) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#findByCustomerId
	 * (long, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * findByCustomerId(long, int)
	 */
	public Collection<Review> findByCustomerId(long id, int languageId) {
		try {
			Query q = super
					.getSession()
					.createQuery(
							"select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.customerId=:cId and s.id.languageId=:lId order by r.reviewId desc")
					.setLong("cId", id).setInteger("lId", languageId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			;
			return q.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#findByProductId
	 * (long, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * findByProductId(long, int)
	 */
	public Collection<Review> findByProductId(long id, int languageId) {
		try {
			Query q = super
					.getSession()
					.createQuery(
							"select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.productId=:pId and s.id.languageId=:lId")
					.setLong("pId", id).setInteger("lId", languageId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			;
			return q.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Review> findByProductId(long id) {
		try {
			Query q = super
					.getSession()
					.createQuery(
							"select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.productId=:pId order by r.reviewId desc")
					.setLong("pId", id);
			return q.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public SearchReviewResponse searchByProductId(SearchReviewCriteria criteria) {
		try {
			// Query q =
			// super.getSession().createQuery("select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.productId=:pId and s.id.languageId=:lId order by r.reviewId desc")
			Query q = super
					.getSession()
					.createQuery(
							"select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.productId=:pId order by r.reviewId desc")
					.setLong("pId", criteria.getProductId())
					// .setInteger("lId", criteria.getLanguageId());
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Criteria c = super.getSession().createCriteria(Review.class).add(
					Restrictions.eq("productId", criteria.getProductId()))
					.addOrder(org.hibernate.criterion.Order.desc("reviewId"));

			c.setProjection(Projections.rowCount());
			Integer count = (Integer) c.uniqueResult();

			c.setProjection(null);

			int max = criteria.getQuantity();
			/*
			 * if(count<criteria.getQuantity()) {
			 * if(criteria.getStartindex()==1) { max = count; } else { max =
			 * count - criteria.getStartindex(); } }
			 */

			List list = null;
			if (count > 0) {
				q.setMaxResults(criteria.getUpperLimit(count));
				q.setFirstResult(criteria.getLowerLimit());
			}

			list = q.list();

			SearchReviewResponse response = new SearchReviewResponse();
			response.setCount(count);
			response.setReviews(list);

			return response;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public SearchReviewResponse searchByCustomerId(SearchReviewCriteria criteria) {
		try {
			// Query q =
			// super.getSession().createQuery("select r from Review r left join fetch r.descriptions s join fetch r.customer c where r.customerId=:cId and s.id.languageId=:lId order by r.reviewId desc")
			Query q = super
					.getSession()
					.createQuery(
							"select r from Review r join fetch r.customer c where r.customerId=:cId order by r.reviewId desc")
					.setLong("cId", criteria.getCustomerId());
			// .setInteger("lId", criteria.getLanguageId());

			Criteria c = super.getSession().createCriteria(Review.class).add(
					Restrictions.eq("customerId", criteria.getCustomerId()))
					.addOrder(org.hibernate.criterion.Order.desc("reviewId"));

			c.setProjection(Projections.rowCount());
			Integer count = (Integer) c.uniqueResult();

			c.setProjection(null);

			List list = null;
			if (count > 0) {
				q.setMaxResults(criteria.getUpperLimit(count));
				q.setFirstResult(criteria.getLowerLimit());
			}

			list = q.list();

			SearchReviewResponse response = new SearchReviewResponse();
			response.setCount(count);
			response.setReviews(list);

			return response;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDao#findById(long)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IReviewDescriptionDao#
	 * findById(long)
	 */
	public Review findById(long id) {
		try {
			Review instance = (Review) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.catalog.Review", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Counter countAverageRatingByProduct(long productId) {
		try {
			Criteria c = super.getSession().createCriteria(Review.class).add(
					Restrictions.eq("productId", productId));

			c.setProjection(Projections.projectionList().add(
					Projections.avg("reviewRating"))
					.add(Projections.rowCount()));

			List resp = c.list();

			Counter counter = new Counter();
			if (resp != null && resp.size() > 0) {
				Iterator i = resp.iterator();
				while (i.hasNext()) {
					Object[] o = (Object[]) i.next();
					counter.setAverage(((Double) o[0]));
					counter.setCount((Integer) o[1]);
				}
			}

			return counter;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

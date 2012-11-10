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

// Generated Aug 7, 2008 11:34:44 PM by Hibernate Tools 3.2.0.beta8

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;

/**
 * Home object for domain model class CategoryDescription.
 * 
 * @see com.salesmanager.core.test.CategoryDescription
 * @author Hibernate Tools
 */
@Repository
public class CategoryDescriptionDao extends HibernateDaoSupport implements
		ICategoryDescriptionDao {

	private static final Log log = LogFactory
			.getLog(CategoryDescriptionDao.class);

	@Autowired
	public CategoryDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.ICategoryDescriptionDao#persist
	 * (com.salesmanager.core.entity.catalog.CategoryDescription)
	 */
	public void persist(CategoryDescription transientInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.ICategoryDescriptionDao#
	 * saveOrUpdate(com.salesmanager.core.entity.catalog.CategoryDescription)
	 */
	public void saveOrUpdate(CategoryDescription instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<CategoryDescription> instances) {
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
	 * com.salesmanager.core.service.catalog.impl.ICategoryDescriptionDao#delete
	 * (com.salesmanager.core.entity.catalog.CategoryDescription)
	 */
	public void delete(CategoryDescription persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.ICategoryDescriptionDao#merge
	 * (com.salesmanager.core.entity.catalog.CategoryDescription)
	 */
	public CategoryDescription merge(CategoryDescription detachedInstance) {
		try {
			CategoryDescription result = (CategoryDescription) super
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
	 * com.salesmanager.core.service.catalog.impl.ICategoryDescriptionDao#findById
	 * (com.salesmanager.core.entity.catalog.CategoryDescriptionId)
	 */
	public CategoryDescription findById(CategoryDescriptionId id) {
		try {
			CategoryDescription instance = (CategoryDescription) super
					.getHibernateTemplate().get(
							"com.salesmanager.core.entity.CategoryDescription",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteCategoriesDescriptions(
			Collection<CategoryDescription> descriptions) {

		try {
			super.getHibernateTemplate().deleteAll(descriptions);
		} catch (RuntimeException e) {
			log.error(e);
			throw e;

		}

	}

	public Collection<CategoryDescription> findByCategoryIds(
			Collection<Long> categoryIds) {

		try {

			if (categoryIds == null)
				return null;

			DetachedCriteria crit = DetachedCriteria
					.forClass(CategoryDescription.class);
			crit.add(Expression.in("id.categoryId", categoryIds));
			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			return result;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public List<CategoryDescription> findByCategoryId(long id) {

		try {
			List descriptions = super.getSession().createCriteria(
					CategoryDescription.class).add(
					Restrictions.eq("id.categoryId", id)).list();

			return descriptions;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CategoryDescription> findByLanguageId(int languageId) {

		try {

			Criteria descriptions = super.getSession().createCriteria(
					CategoryDescription.class).add(
					Restrictions.eq("id.languageId", languageId)).addOrder(
					Order.asc("categoryName")).setResultTransformer(
					Criteria.DISTINCT_ROOT_ENTITY);

			List list = descriptions.list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CategoryDescription> findByMerchantIdandLanguageId(
			int merchantId, int languageId) {

		try {

			Collection ids = new ArrayList();
			ids.add(Constants.GLOBAL_MERCHANT_ID);
			ids.add(merchantId);

			Criteria descriptions = super.getSession().createCriteria(
					CategoryDescription.class).add(
					Restrictions.eq("id.languageId", languageId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Criteria category = descriptions.createCriteria("category").add(
					Expression.in("merchantId", ids)).addOrder(
					Order.asc("sortOrder"));

			List list = descriptions.list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public CategoryDescription findByMerchantIdAndCategoryIdAndLanguageId(
			int merchantId, long categoryId, int languageId) {

		try {

			Collection ids = new ArrayList();
			ids.add(Constants.GLOBAL_MERCHANT_ID);
			ids.add(merchantId);

			Criteria descriptions = super.getSession().createCriteria(
					CategoryDescription.class).add(
					Restrictions.eq("id.languageId", languageId)).add(
					Restrictions.eq("id.categoryId", categoryId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Criteria category = descriptions.createCriteria("category").add(
					Expression.in("merchantId", ids)).addOrder(
					Order.asc("sortOrder"));

			CategoryDescription description = (CategoryDescription) descriptions
					.uniqueResult();

			return description;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CategoryDescription> findByParentCategoryIDMerchantIdandLanguageId(
			int merchantId, long parentCategoryId, int languageId) {

		try {

			Collection ids = new ArrayList();
			ids.add(Constants.GLOBAL_MERCHANT_ID);
			ids.add(merchantId);

			Criteria descriptions = super.getSession().createCriteria(
					CategoryDescription.class).add(
					Restrictions.eq("id.languageId", languageId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Criteria category = descriptions.createCriteria("category").add(
					Expression.in("merchantId", ids)).add(
					Restrictions.eq("parentId", parentCategoryId)).addOrder(
					Order.asc("sortOrder"));

			List list = descriptions.list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

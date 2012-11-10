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

// Generated Aug 7, 2008 10:59:18 AM by Hibernate Tools 3.2.0.b9

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Category;

/**
 * Home object for domain model class Category.
 * 
 * @see com.salesmanager.core.service.catalog.impl.Category
 * @author Hibernate Tools
 */
@Repository
public class CategoryDao extends HibernateDaoSupport implements ICategoryDao {

	private static final Log log = LogFactory.getLog(CategoryDao.class);

	@Autowired
	public CategoryDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.ICategoryDao#persist(com.
	 * salesmanager.core.entity.catalog.Category)
	 */
	public void persist(Category transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.ICategoryDao#saveOrUpdate(
	 * com.salesmanager.core.entity.catalog.Category)
	 */
	public void saveOrUpdate(Category instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<Category> instances) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(instances);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void save(final Category instance) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Connection con = session.connection();
				PreparedStatement ps = con
						.prepareStatement("insert into categories(categories_id,categories_image,parent_id,"
								+ "sort_order,date_added,last_modified,categories_status,visible,RefCategoryID,"
								+ "RefCategoryLevel,RefCategoryName,RefCategoryParentID,RefExpired,merchantid,depth,"
								+ "lineage) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				ps.setLong(1, instance.getCategoryId());
				ps.setString(2, instance.getCategoryImage());
				ps.setLong(3, instance.getParentId());
				ps.setInt(4, (instance.getSortOrder() == null ? 0 : instance
						.getSortOrder()));
				ps.setDate(5, new java.sql.Date(instance.getDateAdded()
						.getTime()));
				ps.setDate(6, new java.sql.Date(instance.getLastModified()
						.getTime()));
				ps.setBoolean(7, instance.isCategoryStatus());
				ps.setBoolean(8, instance.isVisible());
				ps.setLong(9, instance.getRefCategoryId());
				ps.setInt(10, instance.getRefCategoryLevel());
				ps.setString(11, instance.getRefCategoryName());
				ps.setString(12, instance.getRefCategoryParentId());
				ps.setString(13, instance.getRefExpired());
				ps.setLong(14, instance.getMerchantId());
				ps.setInt(15, instance.getDepth());
				ps.setString(16, instance.getLineage());
				return ps.executeUpdate();
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.ICategoryDao#delete(com.
	 * salesmanager.core.entity.catalog.Category)
	 */
	public void delete(Category persistentInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.ICategoryDao#merge(com.
	 * salesmanager.core.entity.catalog.Category)
	 */
	public Category merge(Category detachedInstance) {
		try {
			Category result = (Category) super.getHibernateTemplate().merge(
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
	 * @see
	 * com.salesmanager.core.service.catalog.impl.ICategoryDao#findById(int)
	 */
	public Category findById(long id) {
		try {

			return (Category) super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s join fetch c.parent where c.categoryId=:cId")
					.setLong("cId", id).uniqueResult();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteCategories(Collection<Category> categories) {

		try {
			super.getHibernateTemplate().deleteAll(categories);
		} catch (RuntimeException e) {
			log.error(e);
			throw e;

		}

	}

	public Collection<Category> findByCategoryIds(Collection<Long> categoryIds) {

		try {

			if (categoryIds == null)
				return null;

			DetachedCriteria crit = DetachedCriteria.forClass(Category.class);
			crit.add(Expression.in("categoryId", categoryIds));
			Collection result = this.getHibernateTemplate()
					.findByCriteria(crit);

			return result;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.ICategoryDao#findByMerchantId
	 * (int)
	 */
	public List<Category> findByMerchantId(int merchantid) {
		try {
			List values = new ArrayList();
			values.add(Constants.GLOBAL_MERCHANT_ID);
			values.add(merchantid);
			List list = super.getSession().createCriteria(Category.class).add(
					Restrictions.in("merchantId", values)).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Category> findByMerchantIdAndLineage(int merchantId,
			String lineage) {
		try {
			List list = super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s join fetch c.parent where c.merchantId=:mId and c.lineage like :lin order by c.lineage, c.sortOrder")
					.setInteger("mId", merchantId).setString(
							"lin",
							new StringBuffer().append(lineage).append("%")
									.toString()).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Category> findByMerchantIdAndLanguageIdAndLineage(
			int merchantId, int languageId, String lineage) {
		try {

			List merchants = new ArrayList();
			merchants.add(Constants.GLOBAL_MERCHANT_ID);
			merchants.add(merchantId);

			List list = super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s join fetch c.parent where c.merchantId in (:mId) and s.id.languageId=:lId and c.lineage like :lin order by c.lineage, c.sortOrder")
					.setParameterList("mId", merchants).setInteger("lId",
							languageId).setString(
							"lin",
							new StringBuffer().append(lineage).append("%")
									.toString()).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Category> findByMerchantIdAndLanguageId(int merchantId,
			int languageId) {
		try {

			List merchants = new ArrayList();
			merchants.add(Constants.GLOBAL_MERCHANT_ID);
			merchants.add(merchantId);

			List list = super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s join fetch c.parent where c.merchantId in (:mId) and s.id.languageId=:lId order by c.lineage, c.sortOrder")
					.setParameterList("mId", merchants).setInteger("lId",
							languageId).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Category> findByMerchantIdAndLanguage(int merchantId,
			int language) {
		try {

			Collection ids = new ArrayList();
			ids.add(Constants.GLOBAL_MERCHANT_ID);
			ids.add(merchantId);

			Criteria query = super.getSession().createCriteria(
					com.salesmanager.core.entity.catalog.Product.class).add(
					Expression.in("merchantId", ids)).setResultTransformer(
					Criteria.DISTINCT_ROOT_ENTITY);

			Criteria descCriteria = query.createCriteria("descriptions");
			descCriteria.add(Restrictions.eq("id.languageId", language));

			List list = query.list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Category> findSubCategories(long categoryId) {
		try {

			List list = super.getSession().createCriteria(Category.class).add(
					Restrictions.eq("parentId", categoryId))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Category> findSubCategoriesByLang(int merchantId,
			long categoryId, int languageId) {
		try {

			List merchants = new ArrayList();
			merchants.add(Constants.GLOBAL_MERCHANT_ID);
			merchants.add(merchantId);

			List list = super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s join fetch c.parent where c.merchantId in (:mId) and c.parentId=:pId and s.id.languageId=:lId order by c.lineage, c.sortOrder")
					.setParameterList("mId", merchants).setInteger("lId",
							languageId).setLong("pId", categoryId).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Category findCategoryByMerchantIdAndSeoURLAndByLang(int merchantId,
			String seUrl, int languageId) {

		try {

			Category c = null;

			List list = super
					.getSession()
					.createQuery(
							"select c from Category c left join fetch c.descriptions s where c.merchantId=:mId and s.seUrl=:sText and s.id.languageId=:lId order by c.sortOrder")
					.setInteger("mId", merchantId).setString("sText", seUrl)
					.setInteger("lId", languageId).list();

			if (list != null && list.size() > 0) {
				c = (Category) list.get(0);// get first item
			}

			return c;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

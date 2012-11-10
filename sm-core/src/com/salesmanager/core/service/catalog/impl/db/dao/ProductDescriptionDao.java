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

// Generated Aug 19, 2008 8:26:20 AM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductDescriptionId;

/**
 * Home object for domain model class ProductsDescription.
 * 
 * @see com.salesmanager.core.test.ProductsDescription
 * @author Hibernate Tools
 */
@Repository
public class ProductDescriptionDao extends HibernateDaoSupport implements
		IProductDescriptionDao {

	private static final Log log = LogFactory
			.getLog(ProductDescriptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductDescriptionDao#persist
	 * (com.salesmanager.core.entity.catalog.ProductDescription)
	 */
	public void persist(ProductDescription transientInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.IProductDescriptionDao#
	 * saveOrUpdate(com.salesmanager.core.entity.catalog.ProductDescription)
	 */
	public void saveOrUpdate(ProductDescription instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductDescriptionDao#delete
	 * (com.salesmanager.core.entity.catalog.ProductDescription)
	 */
	public void delete(ProductDescription persistentInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.IProductDescriptionDao#
	 * deleteProductDescriptions(java.util.Collection)
	 */
	public void deleteProductDescriptions(
			Collection<ProductDescription> descriptions) {

		try {
			super.getHibernateTemplate().deleteAll(descriptions);
		} catch (RuntimeException e) {
			log.error(e);
			throw e;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductDescriptionDao#merge
	 * (com.salesmanager.core.entity.catalog.ProductDescription)
	 */
	public ProductDescription merge(ProductDescription detachedInstance) {
		try {
			ProductDescription result = (ProductDescription) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductDescriptionDao#findById
	 * (com.salesmanager.core.entity.catalog.ProductDescriptionId)
	 */
	public ProductDescription findById(ProductDescriptionId id) {
		try {
			ProductDescription instance = (ProductDescription) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductDescription",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductDescription> findByMerchantIdAndCategoryId(
			int merchantId, long categoryId, int languageId) {
		try {

			Query q = super
					.getSession()
					.createQuery(
							"from ProductDescription d inner join fetch d.product prod where d.id.languageId=:l and prod.merchantId = :m and prod.masterCategoryId = :c");
			q.setParameter("m", merchantId);
			q.setParameter("c", categoryId);
			q.setParameter("l", languageId);
			return q.list();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductDescription> findByMerchantIdAndCategoriesId(
			int merchantId, List<Long> categorieId, int languageId) {
		try {

			StringBuffer qBuffer = new StringBuffer();
			String query = "from ProductDescription d inner join fetch d.product prod where d.id.languageId=:l and prod.merchantId = :m and prod.masterCategoryId in";
			qBuffer.append(query);
			qBuffer.append("(");
			Iterator cIterator = categorieId.iterator();
			int i = 1;
			while (cIterator.hasNext()) {
				Long id = (Long) cIterator.next();
				qBuffer.append(id);
				if (i < categorieId.size()) {
					qBuffer.append(",");
				}
				i++;
			}
			qBuffer.append(")");
			Query q = super.getSession().createQuery(qBuffer.toString());
			q.setParameter("m", merchantId);
			q.setParameter("l", languageId);
			return q.list();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductDescription> findByProductsId(
			Collection<Long> ids, int languageId) {
		try {
			return super.getSession().createCriteria(ProductDescription.class)
					.add(Restrictions.in("id.productId", ids)).add(
							Restrictions.eq("id.languageId", languageId))
					.list();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ProductDescription findByProductId(long id, int languageId) {
		try {
			return (ProductDescription) super.getSession().createCriteria(
					ProductDescription.class).add(
					Restrictions.eq("id.productId", id)).add(
					Restrictions.eq("id.languageId", languageId))
					.uniqueResult();

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Set<ProductDescription> findByProductId(long id) {
		try {
			List descriptions = super.getSession().createCriteria(
					ProductDescription.class).add(
					Restrictions.eq("id.productId", id)).list();
			HashSet set = new HashSet();
			set.addAll(descriptions);
			return set;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

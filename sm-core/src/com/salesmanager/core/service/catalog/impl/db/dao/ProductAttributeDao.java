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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductAttribute;

/**
 * Home object for domain model class ProductsAttributes.
 * 
 * @see com.salesmanager.core.test.ProductsAttributes
 * @author Hibernate Tools
 */
@Repository
public class ProductAttributeDao extends HibernateDaoSupport implements
		IProductAttributeDao {

	private static final Log log = LogFactory.getLog(ProductAttributeDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductAttributeDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDao#persist
	 * (com.salesmanager.core.entity.catalog.ProductAttribute)
	 */
	public void persist(ProductAttribute transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDao#saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.ProductAttribute)
	 */
	public void saveOrUpdate(ProductAttribute instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDao#delete
	 * (com.salesmanager.core.entity.catalog.ProductAttribute)
	 */
	public void delete(ProductAttribute persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<ProductAttribute> persistentInstances) {
		try {
			super.getHibernateTemplate().deleteAll(persistentInstances);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDao#merge
	 * (com.salesmanager.core.entity.catalog.ProductAttribute)
	 */
	public ProductAttribute merge(ProductAttribute detachedInstance) {
		try {
			ProductAttribute result = (ProductAttribute) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDao#findById
	 * (long)
	 */
	public ProductAttribute findById(long id) {
		try {
			/**
			 * ProductAttribute instance =
			 * (ProductAttribute)super.getHibernateTemplate()
			 * .get("com.salesmanager.core.entity.catalog.ProductAttribute",
			 * id);
			 **/

			/**
			 * if(instance!=null) {
			 * Hibernate.initialize(instance.getProductOption());
			 * Hibernate.initialize(instance.getProductOptionValue()); }
			 **/

			ProductAttribute instance = (ProductAttribute) super.getSession()
					.createCriteria(ProductAttribute.class).add(
							Restrictions.eq("productAttributeId", id))
					.setFetchMode("productOption", FetchMode.JOIN)
					.setFetchMode("productOptionValue", FetchMode.JOIN)
					.uniqueResult();

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ProductAttribute findById(long id, int languageId) {
		try {

			ProductAttribute instance = (ProductAttribute) super
					.getSession()
					.createQuery(
							"select a from ProductAttribute a left join fetch a.productOption o left join fetch o.descriptions od left join fetch a.productOptionValue v left join fetch v.descriptions vd where a.productAttributeId=:pId and od.id.languageId=:lId and vd.id.languageId=:lId order by a.optionId, a.productOptionSortOrder")
					.setLong("pId", id).setInteger("lId", languageId)
					.uniqueResult();

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.IProductAttributeDao#
	 * findByProductId(long)
	 */
	public Collection<ProductAttribute> findByProductId(long id) {
		try {

			List list = super.getSession().createCriteria(
					ProductAttribute.class).add(
					Restrictions.eq("productId", id)).addOrder(
					Order.asc("optionId")).addOrder(
					Order.asc("productOptionSortOrder")).list();

			// List list =
			// super.getSession().createQuery("select a from ProductAttribute a left join fetch a.productOption o left join fetch o.descriptions od left join fetch a.productOptionValue v left join fetch v.descriptions vd where a.productId=:pId order by a.optionId, a.productOptionSortOrder")
			// .setLong("pId", id)
			// .list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductAttribute> findAttributesByProductId(long id,
			int languageId) {
		try {
			List list = super
					.getSession()
					.createQuery(
							"select a from ProductAttribute a left join fetch a.productOption o left join fetch o.descriptions od left join fetch a.productOptionValue v left join fetch v.descriptions vd where a.productId=:pId and od.id.languageId=:lId and vd.id.languageId=:lId  order by a.optionId, a.productOptionSortOrder")
					.setLong("pId", id).setInteger("lId", languageId).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductAttribute> findAttributesByIds(List ids,
			int languageId) {
		try {
			List list = super
					.getSession()
					.createQuery(
							"select a from ProductAttribute a left join fetch a.productOption o left join fetch o.descriptions od left join fetch a.productOptionValue v left join fetch v.descriptions vd where a.productAttributeId in (:pId) and od.id.languageId=:lId and vd.id.languageId=:lId order by a.optionId, a.productOptionSortOrder")
					.setParameterList("pId", ids)

					.setInteger("lId", languageId).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ProductAttribute findByProductIdAndOptionValueId(long productId,
			long productOptionValueId) {
		try {
			ProductAttribute attr = (ProductAttribute) super.getSession()
					.createCriteria(ProductAttribute.class).add(
							Restrictions.eq("productId", productId)).add(
							Restrictions.eq("optionValueId",
									productOptionValueId)).setFetchMode(
							"productOption", FetchMode.JOIN).setFetchMode(
							"productOptionValue", FetchMode.JOIN)
					.uniqueResult();

			return attr;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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

// Generated Sep 21, 2008 5:20:57 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValueDescriptionId;

/**
 * Home object for domain model class ProductsOptionsValuesDescription.
 * 
 * @see com.salesmanager.core.test.ProductsOptionsValuesDescription
 * @author Hibernate Tools
 */
@Repository
public class ProductOptionValueDescriptionDao extends HibernateDaoSupport
		implements IProductOptionValueDescriptionDao {

	private static final Log log = LogFactory
			.getLog(ProductOptionValueDescriptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductOptionValueDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #
	 * persist(com.salesmanager.core.entity.catalog.ProductOptionValueDescription
	 * )
	 */
	public void persist(ProductOptionValueDescription transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #attachDirty(com.salesmanager.core.entity.catalog.
	 * ProductOptionValueDescription)
	 */
	public void saveOrUpdate(ProductOptionValueDescription instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(
			Collection<ProductOptionValueDescription> collection) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(collection);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #
	 * delete(com.salesmanager.core.entity.catalog.ProductOptionValueDescription
	 * )
	 */
	public void delete(ProductOptionValueDescription persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<ProductOptionValueDescription> collection) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #
	 * merge(com.salesmanager.core.entity.catalog.ProductOptionValueDescription)
	 */
	public ProductOptionValueDescription merge(
			ProductOptionValueDescription detachedInstance) {
		try {
			ProductOptionValueDescription result = (ProductOptionValueDescription) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDescriptionDao
	 * #
	 * findById(com.salesmanager.core.entity.catalog.ProductOptionValueDescriptionId
	 * )
	 */
	public ProductOptionValueDescription findById(
			ProductOptionValueDescriptionId id) {

		try {
			ProductOptionValueDescription instance = (ProductOptionValueDescription) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductOptionValueDescription",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductOptionValueDescription> findByProductOptionValueId(
			long id) {

		try {

			List list = super.getSession().createCriteria(
					ProductOptionValue.class).add(
					Restrictions.eq("id.productOptionValueId", id)).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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

// Generated Sep 17, 2008 4:47:05 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductOptionDescription;

/**
 * Home object for domain model class ProductsOptionsDescription.
 * 
 * @see com.salesmanager.core.entity.catalog.ProductOptionDescription
 * @author Hibernate Tools
 */
@Repository
public class ProductOptionDescriptionDao extends HibernateDaoSupport implements
		IProductOptionDescriptionDao {

	private static final Log log = LogFactory
			.getLog(ProductOptionDescriptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductOptionDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #persist(com.salesmanager.core.entity.catalog.ProductOptionDescription)
	 */
	public void persist(ProductOptionDescription transientInstance) {
		try {
			super.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(
			Collection<ProductOptionDescription> descriptions) {

		try {
			super.getHibernateTemplate().saveOrUpdateAll(descriptions);
		} catch (RuntimeException re) {
			log.error("insert failed", re);
			throw re;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.ProductOptionDescription)
	 */
	public void saveOrUpdate(ProductOptionDescription instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #delete(com.salesmanager.core.entity.catalog.ProductOptionDescription)
	 */
	public void delete(ProductOptionDescription persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<ProductOptionDescription> entries) {
		try {
			super.getHibernateTemplate().deleteAll(entries);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #merge(com.salesmanager.core.entity.catalog.ProductOptionDescription)
	 */
	public ProductOptionDescription merge(
			ProductOptionDescription detachedInstance) {
		try {
			ProductOptionDescription result = (ProductOptionDescription) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #findById
	 * (com.salesmanager.core.entity.catalog.ProductOptionDescriptionId)
	 */
	public ProductOptionDescription findById(
			com.salesmanager.core.entity.catalog.ProductOptionDescriptionId id) {
		try {
			ProductOptionDescription instance = (ProductOptionDescription) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductOptionDescription",
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDescriptionDao
	 * #findByMerchantId(int)
	 */
	public Collection<ProductOptionDescription> findByMerchantId(int merchantId) {
		try {
			List list = super.getSession().createCriteria(
					ProductOptionDescription.class).add(
					Restrictions.eq("merchantId", merchantId)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

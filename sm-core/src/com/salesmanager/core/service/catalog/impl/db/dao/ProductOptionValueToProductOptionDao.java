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

import com.salesmanager.core.entity.catalog.ProductOptionValueToProductOption;

/**
 * Home object for domain model class ProductsOptionsValuesToProductsOptions.
 * 
 * @see com.salesmanager.core.entity.catalog.ProductOptionValueToProductOption
 * @author Hibernate Tools
 */
@Repository
public class ProductOptionValueToProductOptionDao extends HibernateDaoSupport
		implements IProductOptionValueToProductOptionDao {

	private static final Log log = LogFactory
			.getLog(ProductOptionValueToProductOptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductOptionValueToProductOptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.
	 * IProductOptionValueToProductOptionDao
	 * #persist(com.salesmanager.core.entity
	 * .catalog.ProductOptionValueToProductOption)
	 */
	public void persist(ProductOptionValueToProductOption transientInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.
	 * IProductOptionValueToProductOptionDao
	 * #saveOrUpdate(com.salesmanager.core.entity
	 * .catalog.ProductOptionValueToProductOption)
	 */
	public void saveOrUpdate(ProductOptionValueToProductOption instance) {

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
	 * @seecom.salesmanager.core.service.catalog.impl.
	 * IProductOptionValueToProductOptionDao
	 * #delete(com.salesmanager.core.entity.
	 * catalog.ProductOptionValueToProductOption)
	 */
	public void delete(ProductOptionValueToProductOption persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(
			Collection<ProductOptionValueToProductOption> collection) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.
	 * IProductOptionValueToProductOptionDao
	 * #merge(com.salesmanager.core.entity.catalog
	 * .ProductOptionValueToProductOption)
	 */
	public ProductOptionValueToProductOption merge(
			ProductOptionValueToProductOption detachedInstance) {
		try {
			ProductOptionValueToProductOption result = (ProductOptionValueToProductOption) super
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
	 * @seecom.salesmanager.core.service.catalog.impl.
	 * IProductOptionValueToProductOptionDao
	 * #findById(com.salesmanager.core.entity
	 * .catalog.ProductOptionValueToProductOptionId)
	 */
	public ProductOptionValueToProductOption findById(
			com.salesmanager.core.entity.catalog.ProductOptionValueToProductOptionId id) {

		try {
			ProductOptionValueToProductOption instance = (ProductOptionValueToProductOption) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.catalog.entity.ProductOptionValueToProductOption",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductOptionValueToProductOption> findByIdProductOptionId(
			long productOptionId) {
		try {

			List options = super.getSession().createCriteria(
					ProductOptionValueToProductOption.class).add(
					Restrictions.eq("id.productOptionId", productOptionId))
					.list();

			return options;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductOptionValueToProductOption> findByIdProductOptionValueId(
			long productOptionValueId) {
		try {

			List options = super.getSession().createCriteria(
					ProductOptionValueToProductOption.class).add(
					Restrictions.eq("id.productOptionValueId",
							productOptionValueId)).list();

			return options;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

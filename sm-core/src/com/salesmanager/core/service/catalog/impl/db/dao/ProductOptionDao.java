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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.reference.ProductOptionType;

/**
 * Home object for domain model class ProductsOptions.
 * 
 * @see com.salesmanager.core.test.ProductsOptions
 * @author Hibernate Tools
 */
@Repository
public class ProductOptionDao extends HibernateDaoSupport implements
		IProductOptionDao {

	private static final Log log = LogFactory.getLog(ProductOptionDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductOptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#persist(
	 * com.salesmanager.core.test.ProductsOptions)
	 */
	public void persist(ProductOption transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#saveOrUpdate
	 * (com.salesmanager.core.test.ProductsOptions)
	 */
	public void saveOrUpdate(ProductOption instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#delete(com
	 * .salesmanager.core.test.ProductsOptions)
	 */
	public void delete(ProductOption persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#merge(com
	 * .salesmanager.core.test.ProductsOptions)
	 */
	public ProductOption merge(ProductOption detachedInstance) {
		try {
			ProductOption result = (ProductOption) super.getHibernateTemplate()
					.merge(detachedInstance);
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#findById
	 * (int)
	 */
	public ProductOption findById(long id) {
		try {
			ProductOption instance = (ProductOption) super
					.getHibernateTemplate()
					.get("com.salesmanager.core.entity.catalog.ProductOption",
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionDao#findByMerchantId
	 * (int)
	 */
	public Collection<ProductOption> findByMerchantId(int merchantId) {

		try {

			List values = new ArrayList();
			values.add(0);
			values.add(merchantId);
			List list = super.getSession().createCriteria(ProductOption.class)
					.add(Restrictions.in("merchantId", values)).addOrder(
							Order.asc("productOptionSortOrder"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					// .setFetchMode("descriptions",FetchMode.JOIN)
					.list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ProductOption findOptionsValuesByProductOptionId(long productOptionId) {
		try {

			ProductOption option = (ProductOption) super.getSession()
					.createCriteria(ProductOption.class)
					.add(Restrictions.eq("productOptionId", productOptionId))
					.addOrder(Order.asc("productOptionSortOrder"))
					.setFetchMode("descriptions", FetchMode.JOIN)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.uniqueResult();

			if (option != null) {
				Hibernate.initialize(option.getValues());
			}
			return option;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductOptionType> findAllProductOptionTypes() {

		try {

			List types = super.getSession().createQuery(
					"from ProductOptionType p").list();
			return types;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductOptionValue;

/**
 * Home object for domain model class ProductsOptionsValues.
 * 
 * @see com.salesmanager.core.test.ProductsOptionsValues
 * @author Hibernate Tools
 */
@Repository
public class ProductOptionValueDao extends HibernateDaoSupport implements
		IProductOptionValueDao {

	private static final Log log = LogFactory
			.getLog(ProductOptionValueDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductOptionValueDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDao#persist
	 * (com.salesmanager.core.entity.catalog.ProductOptionValue)
	 */
	public void persist(ProductOptionValue transientInstance) {
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
	 * @seecom.salesmanager.core.service.catalog.impl.IProductOptionValueDao#
	 * saveOrUpdate(com.salesmanager.core.entity.catalog.ProductOptionValue)
	 */
	public void saveOrUpdate(ProductOptionValue instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDao#delete
	 * (com.salesmanager.core.entity.catalog.ProductOptionValue)
	 */
	public void delete(ProductOptionValue persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<ProductOptionValue> collection) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDao#merge
	 * (com.salesmanager.core.entity.catalog.ProductOptionValue)
	 */
	public ProductOptionValue merge(ProductOptionValue detachedInstance) {
		try {
			ProductOptionValue result = (ProductOptionValue) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductOptionValueDao#findById
	 * (long)
	 */
	public ProductOptionValue findById(long id) {
		try {
			ProductOptionValue instance = (ProductOptionValue) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductOptionValue",
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
	 * @seecom.salesmanager.core.service.catalog.impl.IProductOptionValueDao#
	 * findByProductOptionId(long)
	 */
	public Collection<ProductOptionValue> findByMerchantId(int merchantId) {

		try {

			List values = new ArrayList();
			values.add(0);
			values.add(merchantId);
			List list = super.getSession().createCriteria(
					ProductOptionValue.class).add(
					Restrictions.in("merchantId", values)).addOrder(
					Order.asc("productOptionValueSortOrder"))
			// .setProjection(Projections.groupProperty(""))
					.list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}

}

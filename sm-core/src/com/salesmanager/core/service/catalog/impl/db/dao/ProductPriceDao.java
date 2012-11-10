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

// Generated Nov 5, 2008 10:22:36 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductPrice;

/**
 * Home object for domain model class ProductsPrice.
 * 
 * @see com.salesmanager.core.entity.catalog.ProductPrice
 * @author Hibernate Tools
 */
@Repository
public class ProductPriceDao extends HibernateDaoSupport implements
		IProductPriceDao {

	private static final Log log = LogFactory.getLog(ProductPriceDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductPriceDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDao#persist
	 * (com.salesmanager.core.entity.catalog.ProductPrice)
	 */
	public void persist(ProductPrice transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDao#saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.ProductPrice)
	 */
	public void saveOrUpdate(ProductPrice instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDao#delete
	 * (com.salesmanager.core.entity.catalog.ProductPrice)
	 */
	public void delete(ProductPrice persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<ProductPrice> coll) {
		try {
			super.getHibernateTemplate().deleteAll(coll);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<ProductPrice> coll) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(coll);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDao#merge
	 * (com.salesmanager.core.entity.catalog.ProductPrice)
	 */
	public ProductPrice merge(ProductPrice detachedInstance) {
		try {
			ProductPrice result = (ProductPrice) super.getHibernateTemplate()
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDao#findById
	 * (int)
	 */
	public ProductPrice findById(long id) {
		try {
			ProductPrice instance = (ProductPrice) super.getHibernateTemplate()
					.get("com.salesmanager.core.entity.catalog.ProductPrice",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

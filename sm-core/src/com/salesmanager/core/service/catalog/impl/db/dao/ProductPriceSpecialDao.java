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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductPriceSpecial;

/**
 * Home object for domain model class ProductsPriceSpecials.
 * 
 * @see com.salesmanager.core.entity.catalog.ProductPriceSpecial
 * @author Hibernate Tools
 */
@Repository
public class ProductPriceSpecialDao extends HibernateDaoSupport implements
		IProductPriceSpecialDao {

	private static final Log log = LogFactory
			.getLog(ProductPriceSpecialDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductPriceSpecialDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceSpecialDao
	 * #persist(com.salesmanager.core.entity.catalog.ProductPriceSpecial)
	 */
	public void persist(ProductPriceSpecial transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceSpecialDao
	 * #saveOrUpdate(com.salesmanager.core.entity.catalog.ProductPriceSpecial)
	 */
	public void saveOrUpdate(ProductPriceSpecial instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceSpecialDao
	 * #delete(com.salesmanager.core.entity.catalog.ProductPriceSpecial)
	 */
	public void delete(ProductPriceSpecial persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceSpecialDao
	 * #merge(com.salesmanager.core.entity.catalog.ProductPriceSpecial)
	 */
	public ProductPriceSpecial merge(ProductPriceSpecial detachedInstance) {
		try {
			ProductPriceSpecial result = (ProductPriceSpecial) super
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceSpecialDao
	 * #findById(int)
	 */
	public ProductPriceSpecial findByProductPriceId(long id) {
		try {
			ProductPriceSpecial instance = (ProductPriceSpecial) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductPriceSpecial",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

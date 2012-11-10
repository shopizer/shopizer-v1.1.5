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

// Generated May 19, 2010 2:04:20 PM by Hibernate Tools 3.2.4.GA

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductPriceDescription;

/**
 * Home object for domain model class ProductsPriceDescription.
 * 
 * @see com.salesmanager.core.test.ProductsPriceDescription
 * @author Hibernate Tools
 */
@Repository
public class ProductPriceDescriptionDao extends HibernateDaoSupport implements
		IProductPriceDescriptionDao {

	private static final Log log = LogFactory
			.getLog(ProductPriceDescriptionDao.class);

	@Autowired
	public ProductPriceDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDescription
	 * #persist(com.salesmanager.core.entity.catalog.ProductPriceDescription)
	 */
	public void persist(ProductPriceDescription transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDescription
	 * #saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.ProductPriceDescription)
	 */
	public void saveOrUpdate(ProductPriceDescription instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDescription
	 * #saveOrUpdateAll(java.util.Collection)
	 */
	public void saveOrUpdateAll(Collection<ProductPriceDescription> instance) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDescription
	 * #delete(com.salesmanager.core.entity.catalog.ProductPriceDescription)
	 */
	public void delete(ProductPriceDescription persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductPriceDescription
	 * #deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection<ProductPriceDescription> persistentInstance) {
		try {
			super.getHibernateTemplate().deleteAll(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

}

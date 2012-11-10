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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductAttributeDownload;

/**
 * Home object for domain model class ProductsAttributesDownload.
 * 
 * @see com.salesmanager.core.test.ProductsAttributesDownload
 * @author Hibernate Tools
 */
@Repository
public class ProductAttributeDownloadDao extends HibernateDaoSupport implements
		IProductAttributeDownloadDao {

	private static final Log log = LogFactory
			.getLog(ProductAttributeDownloadDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public ProductAttributeDownloadDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDownloadDao
	 * #persist(com.salesmanager.core.entity.catalog.ProductAttributeDownload)
	 */
	public void persist(ProductAttributeDownload transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDownloadDao
	 * #saveOrUpdate
	 * (com.salesmanager.core.entity.catalog.ProductAttributeDownload)
	 */
	public void saveOrUpdate(ProductAttributeDownload instance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDownloadDao
	 * #delete(com.salesmanager.core.entity.catalog.ProductAttributeDownload)
	 */
	public void delete(ProductAttributeDownload persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDownloadDao
	 * #merge(com.salesmanager.core.entity.catalog.ProductAttributeDownload)
	 */
	public ProductAttributeDownload merge(
			ProductAttributeDownload detachedInstance) {
		try {
			ProductAttributeDownload result = (ProductAttributeDownload) super
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
	 * com.salesmanager.core.service.catalog.impl.IProductAttributeDownloadDao
	 * #findById(int)
	 */
	public ProductAttributeDownload findById(long id) {
		try {
			ProductAttributeDownload instance = (ProductAttributeDownload) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductAttributeDownload",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

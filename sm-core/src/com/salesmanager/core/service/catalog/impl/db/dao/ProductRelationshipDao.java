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

// Generated Oct 4, 2009 7:13:58 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.catalog.ProductRelationship;

/**
 * Home object for domain model class ProductRelationship.
 * 
 * @see com.salesmanager.core.entity.catalog.ProductRelationship
 * @author Hibernate Tools
 */
@Repository
public class ProductRelationshipDao extends HibernateDaoSupport implements
		IProductRelationshipDao {

	private static final Log log = LogFactory
			.getLog(ProductRelationshipDao.class);

	@Autowired
	public ProductRelationshipDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.dao.IProductRelationshipDao
	 * #persist(com.salesmanager.core.entity.catalog.ProductRelationship)
	 */
	public void persist(ProductRelationship transientInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductRelationshipDao
	 * #saveOrUpdate(com.salesmanager.core.entity.catalog.ProductRelationship)
	 */
	public void saveOrUpdate(ProductRelationship instance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductRelationshipDao
	 * #delete(com.salesmanager.core.entity.catalog.ProductRelationship)
	 */
	public void delete(ProductRelationship persistentInstance) {
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
	 * com.salesmanager.core.service.catalog.impl.dao.IProductRelationshipDao
	 * #findById(int)
	 */
	public ProductRelationship findById(int id) {
		try {
			ProductRelationship instance = (ProductRelationship) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.catalog.ProductRelationship",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ProductRelationship findRelationshipLine(long productId,
			long relatedProductId, int merchantId, int relationType) {
		try {
			ProductRelationship pr = (ProductRelationship) super.getSession()
					.createCriteria(ProductRelationship.class).add(
							Restrictions.eq("productId", productId)).add(
							Restrictions.eq("relatedProductId",
									relatedProductId)).add(
							Restrictions.eq("merchantId", merchantId)).add(
							Restrictions.eq("relationshipType", relationType))
					.uniqueResult();

			return pr;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductRelationship> findByMerchantIdAndRelationTypeId(
			int merchantId, int relationType) {
		try {
			List list = super.getSession().createCriteria(
					ProductRelationship.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("relationshipType", relationType)).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<ProductRelationship> findByProductIdAndMerchantIdAndRelationTypeId(
			long productId, int merchantId, int relationType) {
		try {
			List list = super.getSession().createCriteria(
					ProductRelationship.class).add(
					Restrictions.eq("relatedProductId", productId)).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("relationshipType", relationType)).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

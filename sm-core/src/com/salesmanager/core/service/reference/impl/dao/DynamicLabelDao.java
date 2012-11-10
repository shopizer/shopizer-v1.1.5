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
package com.salesmanager.core.service.reference.impl.dao;

// Generated May 25, 2009 12:08:24 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.DynamicLabel;

/**
 * Home object for domain model class DynamicLabel.
 * 
 * @see com.salesmanager.core.entity.reference.DynamicLabel
 * @author Hibernate Tools
 */
@Repository
public class DynamicLabelDao extends HibernateDaoSupport implements
		IDynamicLabelDao {

	private static final Log log = LogFactory.getLog(DynamicLabelDao.class);

	@Autowired
	public DynamicLabelDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#persist
	 * (com.salesmanager.core.entity.reference.DynamicLabel)
	 */
	public void persist(DynamicLabel transientInstance) {

		try {
			this.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#
	 * saveOrUpdate(com.salesmanager.core.entity.reference.DynamicLabel)
	 */
	public void saveOrUpdate(DynamicLabel instance) {

		try {
			this.getHibernateTemplate().saveOrUpdate(instance);

		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<DynamicLabel> coll) {

		try {
			this.getHibernateTemplate().saveOrUpdateAll(coll);

		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#delete
	 * (com.salesmanager.core.entity.reference.DynamicLabel)
	 */
	public void delete(DynamicLabel persistentInstance) {

		try {
			this.getHibernateTemplate().delete(persistentInstance);

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteAll(Collection<DynamicLabel> labels) {

		try {
			this.getHibernateTemplate().deleteAll(labels);

		} catch (RuntimeException re) {
			log.error("deleteAll failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#merge
	 * (com.salesmanager.core.entity.reference.DynamicLabel)
	 */
	public DynamicLabel merge(DynamicLabel detachedInstance) {

		try {
			DynamicLabel result = (DynamicLabel) this.getHibernateTemplate()
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
	 * com.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#findById
	 * (int)
	 */
	public DynamicLabel findById(long id) {

		try {
			DynamicLabel instance = (DynamicLabel) this.getHibernateTemplate()
					.get("com.salesmanager.core.entity.reference.DynamicLabel",
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
	 * @seecom.salesmanager.core.service.reference.impl.dao.IDynamicLabelDao#
	 * findByMerchantIdAndSectionId(int, long)
	 */
	public Collection<DynamicLabel> findByMerchantIdAndSectionId(
			int merchantId, int sectionId) {

		try {
			List list = super.getSession().createCriteria(DynamicLabel.class)
					.add(Restrictions.eq("merchantId", merchantId)).add(
							Restrictions.eq("sectionId", sectionId)).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<DynamicLabel> findByMerchantId(int merchantId) {

		try {
			Collection list = super.getSession().createCriteria(
					DynamicLabel.class).add(
					Restrictions.eq("merchantId", merchantId)).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<DynamicLabel> findByMerchantIdAndLanguageId(
			int merchantId, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId order by d.sortOrder")
					.setInteger("mId", merchantId)
					.setInteger("lId", languageId).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Collection<DynamicLabel> findByMerchantIdAnsSectionIdsAndLanguageId(
			int merchantId, List<Integer> sections, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and d.sectionId in (:sIds) order by d.sectionId, d.sortOrder")
					.setInteger("mId", merchantId)
					.setParameterList("sIds", sections)
					.setInteger("lId", languageId).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Collection<DynamicLabel> findByMerchantIdAndLabelIdAndLanguageId(
			int merchantId, List<Long> ids, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and d.dynamicLabelId in (:sIds) order by d.sortOrder")
					.setInteger("mId", merchantId)
					.setParameterList("sIds", ids)
					.setInteger("lId", languageId).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Collection<DynamicLabel> findByMerchantIdAndTitleAndLanguageId(
			int merchantId, List<String> ids, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and d.title in (:sTitles) order by d.sortOrder")
					.setInteger("mId", merchantId)
					.setParameterList("sTitles", ids)
					.setInteger("lId", languageId).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<DynamicLabel> findByMerchantIdAndSectionIdAndLanguageId(
			int merchantId, int sectionId, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and d.sectionId=:sId order by d.sectionId, d.sortOrder")
					.setInteger("mId", merchantId)
					.setInteger("lId", languageId).setInteger("sId", sectionId)
					.list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	public DynamicLabel findByMerchantIdAndTitleAndLanguageId(
			int merchantId, String title, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and d.title=:tId order by d.sectionId, d.sortOrder")
					.setInteger("mId", merchantId)
					.setInteger("lId", languageId)
					.setString("tId", title)
					.list();

			if(l!=null && l.size()>0) {
				return (DynamicLabel)l.get(0);
			}
			
			else return null;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public DynamicLabel findByMerchantIdAndSeUrlAndLanguageId(int merchantId,
			String url, int languageId) {

		try {

			List l = super
					.getSession()
					.createQuery(
							"select d from DynamicLabel d left join fetch d.descriptions s where d.merchantId=:mId and s.id.languageId=:lId and s.seUrl=:sUrl")
					.setInteger("mId", merchantId)
					.setInteger("lId", languageId).setString("sUrl", url)
					.list();

			if (l.size() > 0) {
				return (DynamicLabel) l.get(0);
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

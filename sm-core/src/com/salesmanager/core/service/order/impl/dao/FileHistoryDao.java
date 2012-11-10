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
package com.salesmanager.core.service.order.impl.dao;

// Generated Aug 19, 2008 8:26:20 AM by Hibernate Tools 3.2.0.beta8

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.FileHistory;
import com.salesmanager.core.entity.orders.FileHistoryId;

/**
 * Home object for domain model class FilesHistory.
 * 
 * @see com.salesmanager.core.test.FilesHistory
 * @author Hibernate Tools
 */
@Repository
public class FileHistoryDao extends HibernateDaoSupport implements
		IFileHistoryDao {

	private static final Log log = LogFactory.getLog(FileHistoryDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	@Autowired
	public FileHistoryDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.order.impl.IFileHistoryDao#persist(com.
	 * salesmanager.core.entity.orders.FileHistory)
	 */
	public void persist(FileHistory transientInstance) {

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
	 * com.salesmanager.core.service.order.impl.IFileHistoryDao#saveOrUpdate
	 * (com.salesmanager.core.entity.orders.FileHistory)
	 */
	public void saveOrUpdate(FileHistory instance) {
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
	 * @seecom.salesmanager.core.service.order.impl.IFileHistoryDao#delete(com.
	 * salesmanager.core.entity.orders.FileHistory)
	 */
	public void delete(FileHistory persistentInstance) {
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
	 * @seecom.salesmanager.core.service.order.impl.IFileHistoryDao#merge(com.
	 * salesmanager.core.entity.orders.FileHistory)
	 */
	public FileHistory merge(FileHistory detachedInstance) {
		try {
			FileHistory result = (FileHistory) super.getHibernateTemplate()
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
	 * com.salesmanager.core.service.order.impl.IFileHistoryDao#findById(com
	 * .salesmanager.core.test.FilesHistoryId)
	 */
	public FileHistory findById(FileHistoryId id) {
		try {
			FileHistory instance = (FileHistory) super.getHibernateTemplate()
					.get("com.salesmanager.core.entity.orders.FileHistory", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

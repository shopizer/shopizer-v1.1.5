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

// Generated Apr 29, 2010 2:03:20 PM by Hibernate Tools 3.2.4.GA

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.salesmanager.core.entity.reference.ZoneDescription;

/**
 * Home object for domain model class ZonesDescription.
 * 
 * @see com.salesmanager.core.test.ZonesDescription
 * @author Hibernate Tools
 */
public class ZoneDescriptionDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(ZoneDescriptionDao.class);

	@Autowired
	public ZoneDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public ZoneDescription findById(
			com.salesmanager.core.entity.reference.ZoneDescriptionId id) {
		log.debug("getting ZonesDescription instance with id: " + id);
		try {
			ZoneDescription instance = (ZoneDescription) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.reference.ZoneDescription",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

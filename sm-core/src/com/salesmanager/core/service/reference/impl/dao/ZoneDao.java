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
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.Zone;

/**
 * Home object for domain model class Zones.
 * 
 * @see com.salesmanager.core.test.Zones
 * @author Hibernate Tools
 */
@Repository
public class ZoneDao extends HibernateDaoSupport implements IZoneDao {

	private static final Log log = LogFactory.getLog(ZoneDao.class);

	@Autowired
	public ZoneDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IZoneDao#findById(java
	 * .lang.Integer)
	 */
	public Zone findById(java.lang.Integer id) {
		log.debug("getting Zones instance with id: " + id);
		try {
			Zone instance = (Zone) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.reference.Zone", id);
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
	 * com.salesmanager.core.service.reference.impl.dao.IZoneDao#findByName(
	 * java.lang.String, int)
	 */
	public Zone findByName(String name, int languageId) {

		try {

			Zone zone = (Zone) super
					.getSession()
					.createQuery(
							"select z from Zone z left join fetch z.Descriptions s where s.zoneName=:zName and s.id.languageId=:lId")
					.setString("zName", name).setInteger("lId", languageId)
					.uniqueResult();

			return zone;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.reference.impl.dao.IZoneDao#findByCode(
	 * java.lang.String, int)
	 */
	public Zone findByCode(String code, int languageId) {

		try {

			Zone z = (Zone) super
					.getSession()
					.createQuery(
							"select z from Zone z left join fetch z.Descriptions s where s.id.languageId=:lId and z.zoneCode=:zId")
					.setString("zId", code).setInteger("lId", languageId)
					.uniqueResult();

			return z;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

}

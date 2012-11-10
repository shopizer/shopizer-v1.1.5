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

// Generated Nov 11, 2009 9:19:11 AM by Hibernate Tools 3.2.4.GA

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;

/**
 * Home object for domain model class CountriesDescription.
 * 
 * @see com.salesmanager.core.test.CountriesDescription
 * @author Hibernate Tools
 */
@Repository
public class CountryDescriptionDao extends HibernateDaoSupport implements
		ICountryDescriptionDao {

	private static final Log log = LogFactory
			.getLog(CountryDescriptionDao.class);

	@Autowired
	public CountryDescriptionDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void persist(CountryDescription transientInstance) {

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
	 * com.salesmanager.core.service.reference.impl.dao.ICountryDescriptionDao
	 * #saveOrUpdate(com.salesmanager.core.entity.reference.CountryDescription)
	 */
	public void saveOrUpdate(CountryDescription instance) {

		try {
			super.getHibernateTemplate().saveOrUpdate(instance);

		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CountryDescription persistentInstance) {

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
	 * com.salesmanager.core.service.reference.impl.dao.ICountryDescriptionDao
	 * #findByIsoCode(java.lang.String, int)
	 */
	public CountryDescription findByIsoCode(String code, int languageId) {

		try {

			Country ct = (Country) super
					.getSession()
					.createQuery(
							"select c from Country c left join fetch c.Descriptions s where c.countryIsoCode2=:cId and s.id.languageId=:lId")
					.setString("cId", code).setInteger("lId", languageId)
					.uniqueResult();

			CountryDescription desc = null;

			if (ct != null) {
				CountryDescription[] descArray = (CountryDescription[]) ct
						.getDescriptions().toArray(
								new CountryDescription[ct.getDescriptions()
										.size()]);
				if (descArray != null && descArray.length > 0) {
					desc = descArray[0];
				}
			}

			return desc;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CountryDescription findByCountryName(String name, int languageId) {

		try {

			CountryDescription desc = (CountryDescription) super
					.getSession()
					.createQuery(
							"select c from CountryDescription c where c.countryName=:cName and c.id.languageId=:lId")
					.setString("cName", name).setInteger("lId", languageId)
					.uniqueResult();

			return desc;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CountryDescription findByCountryId(int countryId, int languageId) {

		try {

			Country ct = (Country) super
					.getSession()
					.createQuery(
							"select c from Country c left join fetch c.Descriptions s where c.countryId=:cId and s.id.languageId=:lId")
					.setInteger("cId", countryId).setInteger("lId", languageId)
					.uniqueResult();

			CountryDescription desc = null;

			if (ct != null) {
				CountryDescription[] descArray = (CountryDescription[]) ct
						.getDescriptions().toArray(
								new CountryDescription[ct.getDescriptions()
										.size()]);
				if (descArray != null && descArray.length > 0) {
					desc = descArray[0];
				}
			}

			return desc;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

}

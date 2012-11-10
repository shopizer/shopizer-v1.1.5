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

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.Country;

/**
 * Home object for domain model class Countries.
 * 
 * @see com.salesmanager.core.test.Countries
 * @author Hibernate Tools
 */
@Repository
public class CountryDao extends HibernateDaoSupport implements ICountryDao {

	private static final Log log = LogFactory.getLog(CountryDao.class);

	@Autowired
	public CountryDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void persist(Country transientInstance) {

		try {
			super.getHibernateTemplate().persist(transientInstance);

		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void saveOrUpdate(Country instance) {

		try {
			super.getHibernateTemplate().saveOrUpdate(instance);

		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Country persistentInstance) {

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
	 * com.salesmanager.core.service.reference.impl.dao.ICountryDao#getCountries
	 * ()
	 */
	public Collection<Country> getCountries() {

		try {

			List list = super.getSession().createCriteria(Country.class)
					.addOrder(Order.asc("countryId")).list();

			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Country findByName(String name, int languageId) {

		try {

			Country ct = (Country) super
					.getSession()
					.createQuery(
							"select c from Country c left join fetch c.Descriptions s where s.countryName=:cName and s.id.languageId=:lId")
					.setString("cName", name).setInteger("lId", languageId)
					.uniqueResult();

			return ct;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Country findByIsoCode(String code) {

		try {

			Country ct = (Country) super.getSession().createQuery(
					"select c from Country where c.countryIsoCode2=:cId")
					.setString("cId", code).uniqueResult();

			return ct;

		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

}

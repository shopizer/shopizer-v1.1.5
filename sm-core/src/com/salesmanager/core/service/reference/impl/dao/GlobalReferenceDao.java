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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.reference.CentralCountryStatus;
import com.salesmanager.core.entity.reference.CentralCreditCard;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.reference.ProductType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.catalog.impl.db.dao.CategoryDao;

@Repository
public class GlobalReferenceDao extends HibernateDaoSupport implements
		IGlobalReferenceDao {

	private static final Log log = LogFactory.getLog(CategoryDao.class);

	@Autowired
	public GlobalReferenceDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.dao.ICatalogReferenceDao#
	 * getProductTypes()
	 */
	public Collection<ProductType> getProductTypes() {

		try {

			List list = super.getSession().createCriteria(ProductType.class)
					.list();

			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<Zone> getZones() {

		try {

			List list = super.getSession().createCriteria(Zone.class).addOrder(
					Order.asc("zoneCountryId")).addOrder(Order.asc("zoneCode"))
					.list();

			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<CentralCountryStatus> getCountryStatus() {

		try {

			List list = super.getSession().createCriteria(
					CentralCountryStatus.class).list();
			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<OrderStatus> getOrderStatus() {

		try {

			List list = super.getSession().createCriteria(OrderStatus.class)
					.list();
			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<Currency> getCurrencies() {

		try {

			List list = super.getSession().createCriteria(Currency.class).add(
					Restrictions.eq("supported", Boolean.TRUE)).addOrder(
					Order.asc("currencyId")).list();
			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<CentralMeasureUnits> getMeasureUnits() {

		try {

			List list = super.getSession().createCriteria(
					CentralMeasureUnits.class).list();
			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Collection<Language> getLanguages() {

		try {

			List list = super.getSession().createCriteria(Language.class)
					.list();
			return list;

		} catch (RuntimeException e) {
			log.error("get failed", e);
			throw e;
		}

	}

	public Map getSupportedCreditCards() {

		/**
		 * Credit cards
		 */
		List list = super.getSession().createCriteria(CentralCreditCard.class)
				.addOrder(Order.asc("centralCreditCardPosition")).list();
		// List cc = (List)session.createQuery(
		// "from CentralCreditCard c order by c.centralCreditCardPosition asc").list();
		Map supportedCreditCards = new HashMap();
		if (list != null && list.size() > 0) {
			Iterator ccit = list.iterator();
			while (ccit.hasNext()) {
				CentralCreditCard ccd = (CentralCreditCard) ccit.next();
				supportedCreditCards.put(ccd.getCentralCreditCardId(), ccd);
			}
		}

		return supportedCreditCards;

	}

}

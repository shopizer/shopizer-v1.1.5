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
package com.salesmanager.core.service.payment.impl.dao;

// Generated May 25, 2009 12:08:24 PM by Hibernate Tools 3.2.0.beta8

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;

/**
 * Home object for domain model class MerchantPaymentGatewayTrx.
 * 
 * @see com.salesmanager.core.test.MerchantPaymentGatewayTrx
 * @author Hibernate Tools
 */
@Repository
public class MerchantPaymentGatewayTrxDao extends HibernateDaoSupport implements
		IMerchantPaymentGatewayTrxDao {

	private static final Log log = LogFactory
			.getLog(MerchantPaymentGatewayTrxDao.class);

	@Autowired
	public MerchantPaymentGatewayTrxDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.payment.impl.dao.IMerchantPaymentGatewayTrxDao
	 * #persist(com.salesmanager.core.test.MerchantPaymentGatewayTrx)
	 */
	public void persist(MerchantPaymentGatewayTrx transientInstance) {
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
	 * com.salesmanager.core.service.payment.impl.dao.IMerchantPaymentGatewayTrxDao
	 * #saveOrUpdate(com.salesmanager.core.test.MerchantPaymentGatewayTrx)
	 */
	public void saveOrUpdate(MerchantPaymentGatewayTrx instance) {
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
	 * com.salesmanager.core.service.payment.impl.dao.IMerchantPaymentGatewayTrxDao
	 * #delete(com.salesmanager.core.test.MerchantPaymentGatewayTrx)
	 */
	public void delete(MerchantPaymentGatewayTrx persistentInstance) {
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
	 * com.salesmanager.core.service.payment.impl.dao.IMerchantPaymentGatewayTrxDao
	 * #findById(int)
	 */
	public MerchantPaymentGatewayTrx findById(int id) {
		try {
			MerchantPaymentGatewayTrx instance = (MerchantPaymentGatewayTrx) super
					.getHibernateTemplate()
					.get(
							"com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx",
							id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<MerchantPaymentGatewayTrx> findByMerchantIdAndOrderId(
			int merchantId, long orderId) {
		try {
			List trxs = (List) super
					.getSession()
					.createQuery(
							"from MerchantPaymentGatewayTrx where merchantid = :p and orderId = :p1")
					.setInteger("p", merchantId).setLong("p1", orderId).list();

			return trxs;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

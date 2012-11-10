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
package com.salesmanager.core.service.merchant.impl.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.MerchantId;

@Repository
public class MerchantIdDao extends HibernateDaoSupport implements
		IMerchantIdDao {

	private static final Log log = LogFactory.getLog(MerchantIdDao.class);

	@Autowired
	public MerchantIdDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public Integer saveMerchantId(MerchantId merchantId) {
		return (Integer) getHibernateTemplate().save(merchantId);
	}

	public MerchantId findById(int merchantId) {
		try {
			MerchantId instance = (MerchantId) super.getHibernateTemplate()
					.get("com.salesmanager.core.entity.reference.MerchantId",
							merchantId);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void delete(MerchantId merchantId) {
		getHibernateTemplate().delete(merchantId);
	}

	@SuppressWarnings("unchecked")
	public List<MerchantId> loadAll() {
		return getHibernateTemplate().loadAll(MerchantId.class);
	}

}

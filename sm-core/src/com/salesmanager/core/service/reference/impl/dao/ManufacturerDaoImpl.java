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

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.reference.Manufacturers;
import com.salesmanager.core.entity.reference.ManufacturersInfo;

@Repository
public class ManufacturerDaoImpl extends HibernateDaoSupport implements
		IManufacturerDao {

	@Autowired
	public ManufacturerDaoImpl(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void saveOrUpdateManufacturers(Manufacturers manufacturers) {
		getHibernateTemplate().saveOrUpdate(manufacturers);
	}

	public void saveOrUpdateManufacturersInfo(ManufacturersInfo manuInfo) {
		getHibernateTemplate().saveOrUpdate(manuInfo);
	}

}

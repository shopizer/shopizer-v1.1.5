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
package com.salesmanager.core.service.system;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.entity.system.CentralFunction;
import com.salesmanager.core.entity.system.CentralGroup;
import com.salesmanager.core.entity.system.CentralIntegrationError;
import com.salesmanager.core.entity.system.CentralRegistrationAssociation;
import com.salesmanager.core.service.system.impl.dao.ICentralIntegrationErrorDao;
import com.salesmanager.core.service.system.impl.dao.ICentralMenuDao;
import com.salesmanager.core.service.system.impl.dao.ISystemDao;

/**
 * Retreives modules and information related to Modules
 * 
 * @author Administrator
 * 
 */
@Service
public class SystemService {

	private static Logger log = Logger.getLogger(SystemService.class);

	@Autowired
	private ISystemDao systemDao;

	@Autowired
	private ICentralMenuDao centralMenuDao;

	@Autowired
	private ICentralIntegrationErrorDao centralIntegrationErrorDao;

	@Transactional
	public long getNextOrderIdSequence() throws Exception {
		return systemDao.incrementOrderIdCounter();
	}

	@Transactional
	public Collection<CentralRegistrationAssociation> getCentralRegistrationAssociations() {
		return centralMenuDao.loadAllCentralRegistrationAssociation();
	}

	@Transactional
	public Collection<CentralFunction> getCentralFunctions() {
		return centralMenuDao.loadAllCentralFunction();
	}

	@Transactional
	public Collection<CentralGroup> getCentralGroups() {
		return centralMenuDao.loadAllCentralGroup();
	}

	@Transactional
	public Collection<com.salesmanager.core.entity.system.CentralIntegrationError> getIntegrationErrors(
			int merchantid) throws Exception {
		return centralIntegrationErrorDao.findByMerchantId(merchantid);
	}

	@Transactional
	public void logServiceMessage(int merchantid, String message) {

		CentralIntegrationError error = new CentralIntegrationError();
		error.setCentralIntegrationErrorDescription(message);
		error.setDateAdded(new java.util.Date(new java.util.Date().getTime()));
		error.setMerchantid(merchantid);
		centralIntegrationErrorDao.persist(error);

	}

}

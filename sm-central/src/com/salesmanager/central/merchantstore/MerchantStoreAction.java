/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.merchantstore;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;

import com.salesmanager.central.CountrySelectBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.web.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.merchant.MerchantRegistration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantStoreHeader;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantException;
import com.salesmanager.core.service.merchant.MerchantService;

public class MerchantStoreAction extends CountrySelectBaseAction {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(MerchantStoreAction.class);
	private MerchantUserInformation merchantUserInfo;
	private MerchantRegistration merchantRegistration;
	private MerchantStore merchantStore;
	private int merchantId;
	List<MerchantStoreHeader> merchantStoreHeaderList = null;
	


	private List<Integer> merchantRegistrationDefCodes = Constants.MERCHANT_REG_DEF_CODES;

	

	
	public String saveMerchantStore() {
		
		super.setPageTitle("label.menu.group.store");
		
		try {
		
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		MerchantUserInformation mu = mservice.getMerchantUserInformation(super.getPrincipal().getRemoteUser());
		
		prepareSelections(mu.getUsercountrycode());

		if (!super.getContext().isExistingStore()) {
			return "unauthorized";
		}
		if (!isValidMerchantInfo()) {
			return INPUT;
		}

		
		
		
		//check if email already exist
			//MerchantUserInformation user = mservice.getMerchantUserInformationByAdminEmail(merchantUserInfo.getAdminEmail());
			//if(user!=null) {
			//	super.setErrorMessage("messages.emailalreadyexist");
			//	return INPUT;
			//}

			//super.prepareSelections(merchantUserInfo.getUsercountrycode());


		

			// get original store
			Context ctx = super.getContext();
			MerchantStore originalStore = mservice.getMerchantStore(ctx
					.getMerchantid());

			mservice.createNewOrSaveMerchant(originalStore, merchantUserInfo,
					merchantRegistration);
		} catch (ServiceException e) {
			if (e.getReason() == ErrorConstants.EMAIL_ALREADY_EXISTS) {
				addActionError(getText("errors.merchant.email.already.exists"));
			} else {
				super.setTechnicalMessage();
				log.error(e);
				return ERROR;
			}
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return ERROR;
		}
		if (getActionErrors().size() == 0) {
			super.setSuccessMessage();
			getServletRequest().setAttribute("savedMerchantId",String.valueOf(merchantUserInfo.getMerchantId()));
			getServletRequest().setAttribute("savedmerchantUserId",
					merchantUserInfo.getMerchantUserId());
		}
		return SUCCESS;
	}

	public String fetchMerchantStore() {
		
		super.setPageTitle("label.menu.group.store");
		
		if (!super.getContext().isExistingStore()) {
			return "unauthorized";
		}

		if (getMerchantId() != 0) {
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			try {
				merchantRegistration = mservice
						.getMerchantRegistration(getMerchantId());
				
				
				String user = super.getPrincipal().getRemoteUser();
				merchantUserInfo = mservice.getMerchantUserInformation(user);
				
				//merchantUserInfo = mservice
				//		.getMerchantUserInfo(getMerchantId());
				merchantStore = mservice.getMerchantStore(getMerchantId());

				getServletRequest().setAttribute("savedMerchantId",
						String.valueOf(merchantUserInfo.getMerchantId()));
				getServletRequest().setAttribute("savedmerchantUserId",
						merchantUserInfo.getMerchantUserId().toString());
			} catch (Exception e) {
				log.error(e);
				super.setTechnicalMessage();
				return ERROR;
			}

		} else {
			this.merchantUserInfo = new MerchantUserInformation();
			this.merchantUserInfo.setUsercountrycode(super.getContext()
					.getZoneid());
			this.merchantUserInfo.setUsercountrycode(super.getContext()
					.getCountryid());

		}

		super.prepareSelections(merchantUserInfo.getUsercountrycode());

		return SUCCESS;
	}

	public String viewMerchantStores() {
		
		super.setPageTitle("label.menu.group.store");
		
		if (!super.getContext().isExistingStore()) {
			return "unauthorized";
		}
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		merchantStoreHeaderList = mservice.getAllMerchantStores();
		return SUCCESS;
	}

	public String deleteMerchant() {
		
		super.setPageTitle("label.menu.group.store");
		
		if (!super.getContext().isExistingStore()) {
			return "unauthorized";
		}

		try {
			if (getMerchantId() != 0) {
				MerchantService mservice = (MerchantService) ServiceFactory
						.getService(ServiceFactory.MerchantService);
				mservice.deleteMerchant(getMerchantId());
				// addActionMessage(getText("message.merchant.delete.success"));
				super.setSuccessMessage();
			} else {
				addActionError(getText("errors.invalid.merchant.id"));
			}

		} catch (Exception e) {
			log.error("Can't delete merchant " + getMerchantId(), e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	protected boolean isValidMerchantInfo() {
		if (StringUtils.isBlank(merchantUserInfo.getAdminName())) {
			addActionError(getText("messages.required.merchantname"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getAdminEmail())) {
			addActionError(getText("messages.required.adminEmail"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserfname())) {
			addActionError(getText("messages.required.merchantfirstname"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserlname())) {
			addActionError(getText("messages.required.merchantlastname"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserphone())) {
			addActionError(getText("messages.required.userphone"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUseraddress())) {
			addActionError(getText("messages.required.merchantaddress"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUsercity())) {
			addActionError(getText("messages.required.merchantaddress"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserpostalcode())) {
			addActionError(getText("messages.required.userpostalcode"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserstate())) {
			addActionError(getText("messages.required.userstate"));
		}
		if (merchantUserInfo.getUsercountrycode() == 0) {
			addActionError(getText("messages.required.usercountrycode"));
		}
		if (StringUtils.isBlank(merchantUserInfo.getUserlang())) {
			addActionError(getText("messages.required.language"));
		}
		return (getActionErrors().size() == 0);
	}

	public MerchantUserInformation getMerchantUserInfo() {
		return merchantUserInfo;
	}

	public void setMerchantUserInfo(MerchantUserInformation merchantUserInfo) {
		this.merchantUserInfo = merchantUserInfo;
	}

	public MerchantRegistration getMerchantRegistration() {
		return merchantRegistration;
	}

	public void setMerchantRegistration(
			MerchantRegistration merchantRegistration) {
		this.merchantRegistration = merchantRegistration;
	}

	public List<Integer> getMerchantRegistrationDefCodes() {
		return merchantRegistrationDefCodes;
	}

	public void setMerchantRegistrationDefCodes(
			List<Integer> merchantRegistrationDefCodes) {
		this.merchantRegistrationDefCodes = merchantRegistrationDefCodes;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public List<MerchantStoreHeader> getMerchantStoreHeaderList() {
		return merchantStoreHeaderList;
	}

	public void setMerchantStoreHeaderList(
			List<MerchantStoreHeader> merchantStoreHeaderList) {
		this.merchantStoreHeaderList = merchantStoreHeaderList;
	}

	public MerchantStore getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}



}

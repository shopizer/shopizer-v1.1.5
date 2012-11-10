package com.salesmanager.central.merchantstore;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;

public class AjaxRequestAction extends BaseAction {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -2538593680759691882L;

	private Logger log = Logger.getLogger(AjaxRequestAction.class);
	
	//username validation indicator
	private boolean validUserName;
	private String adminName;
	
	public String validateUserName() {
		
		if(StringUtils.isBlank(this.getAdminName())) {
			validUserName = false;
			return SUCCESS;
		}

		
		if(this.getAdminName().length()<6) {
			validUserName = false;
			return SUCCESS;
		}
		
		try {
			
			MerchantService mservice = (MerchantService) ServiceFactory
			.getService(ServiceFactory.MerchantService);
			
			MerchantUserInformation merchantUserInformation = mservice.getMerchantUserInformation(this.getAdminName());
			if(merchantUserInformation!=null) {
				validUserName = false;
				return SUCCESS;
			}
			
			
		} catch (Exception e) {
			log.error(e);
		}
		validUserName = true;
		
		return SUCCESS;
		
		
		
	}
	
	//@JSON(name="validUserName")
	public boolean isValidUserName() {
		return validUserName;
	}

	public void setValidUserName(boolean validUserName) {
		this.validUserName = validUserName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

}

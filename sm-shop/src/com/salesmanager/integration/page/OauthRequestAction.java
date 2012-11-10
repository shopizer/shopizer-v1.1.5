package com.salesmanager.integration.page;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.util.www.BaseAction;

public class OauthRequestAction extends BaseAction {
	
	
	public String displayPage() {
		
		
		HttpSession session = super.getServletRequest().getSession();
		
		String url = (String)session.getAttribute("oAuthUrl");
		
		
		if(!StringUtils.isBlank(url)) {
			super.getServletRequest().setAttribute("url", url);
			return SUCCESS;
		} else {
			return ERROR;
		}
		
		
	}

}

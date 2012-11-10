package com.salesmanager.central.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.core.util.MessageUtil;

public class ActionInterceptor implements Interceptor {

	
	private Logger log = Logger
	.getLogger(ActionInterceptor.class);
	

	public String intercept(ActionInvocation invoke) throws Exception {
		HttpServletRequest req = (HttpServletRequest) ServletActionContext
		.getRequest();
		
		try {

			
			HttpServletResponse resp = (HttpServletResponse) ServletActionContext
					.getResponse();
			return invoke.invoke();
		} catch (Exception e) {
			if (e instanceof ValidationException) {// do nothing
				return com.opensymphony.xwork2.Action.SUCCESS;
			}
			if (e instanceof AuthorizationException) {// return to dashborad
				return "AUTHORIZATIONEXCEPTION";
			}
			log.error(e);
			
			MessageUtil.addErrorMessage(req,e.getMessage());
			
			
			return com.opensymphony.xwork2.Action.ERROR;
		}
		
	}


	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	public void init() {
		// TODO Auto-generated method stub
		
	}

}

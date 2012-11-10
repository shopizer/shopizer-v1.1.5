package com.salesmanager.integration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionInvocation;
import com.salesmanager.core.util.www.SalesManagerInterceptor;

public class PageInterceptor extends SalesManagerInterceptor {

	@Override
	protected String baseIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

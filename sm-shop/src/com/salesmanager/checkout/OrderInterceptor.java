package com.salesmanager.checkout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;

public class OrderInterceptor extends CheckoutInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		// check if session has expired
		String token = (String) ActionContext.getContext().getSession()
				.get("TOKEN");
		if (token == null) {// session expired
			ActionSupport action = (ActionSupport) invoke.getAction();
			action.addActionError(action.getText("error.sessionexpired"));
			return "GENERICERROR";
		}
		
		return null;
	}

}

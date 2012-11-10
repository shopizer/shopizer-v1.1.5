package com.salesmanager.central.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CatalogRoleInterceptor extends RoleInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457659562527890433L;

	@Override
	protected boolean isUserInRole(Principal principal, HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return com.salesmanager.central.util.SecurityUtil.isUserInRole(req, "catalog");
	}

}

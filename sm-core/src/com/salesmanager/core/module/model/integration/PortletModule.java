package com.salesmanager.core.module.model.integration;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;

public interface PortletModule {
	
	public void display(MerchantStore store, HttpServletRequest request, Locale locale, PageRequestAction action, PageExecutionContext pageContext);
	public void submit(MerchantStore store, HttpServletRequest request, Locale locale, PageRequestAction action, PageExecutionContext pageContext);
	public boolean requiresAuthorization();
}

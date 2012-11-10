package com.salesmanager.integration;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.opensymphony.xwork2.ActionInvocation;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.www.BaseActionAware;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;
import com.salesmanager.core.util.www.SalesManagerInterceptor;
import com.salesmanager.core.util.www.SalesManagerPrincipalProxy;
import com.salesmanager.core.util.www.integration.fb.FacebookIntegrationFactory;
import com.salesmanager.core.util.www.integration.fb.FacebookUser;

public class FbPageInterceptor extends SalesManagerInterceptor {



	private Logger log = Logger.getLogger(FbPageInterceptor.class);
	
	
	@Override
	protected String baseIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		
		MerchantStore store = (MerchantStore)req.getAttribute("STORE");
		
		
		HttpServletRequest request = (HttpServletRequest)req; 
		HttpServletResponse response = (HttpServletResponse)resp;    
		HttpSession session = request.getSession(true); 
		
		
		String pageAppender = "/page/";
		if(req.getRequestURI().contains("/fbPage/")) {
			pageAppender = "/fbPage/";
		}
		
		String pathnocontext = StringUtils.removeStart(req.getRequestURI(), req
				.getContextPath() + "/integration" + pageAppender);
		
		
		
		String p = pathnocontext;
		

		
		String path = "";

		//should have /fbPage/<PAGE>/
		
		//first slash [page name]
		int indexOfLastSlash = p.indexOf("/");
		if(indexOfLastSlash>0) {
			path = p.substring(0,indexOfLastSlash);
		}
		

		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		Page page = rservice.getPage(path.trim(), store.getMerchantId());
		
		if(page==null) {
			log.error("FB Page " + path.trim() + " does not exist");
			return "errorPage";
		}
		
		
		FacebookUser user = FacebookIntegrationFactory.getFacebookUser(request, page);
		
		
		if(page.getSecured() && !user.isAuthorized()) {
			
			String url = FacebookIntegrationFactory.getAuthorizationUrl(user, page);
			request.setAttribute("url", url);
			return "oauth";  
		}
		



		PageExecutionContext pageExecutionContext = new PageExecutionContext();
		pageExecutionContext.addToExecutionContext("facebookUser", user);
		

		try {

			PageRequestAction action = ((PageRequestAction) invoke
					.getAction());
			action.setExecutionContext(pageExecutionContext);
		} catch (Exception e) {
			log
					.error("The current action does not extend PageRequestAction "
							+ invoke.getAction().getClass());
		}
		
		
		return null;
	}
	
	

    


}

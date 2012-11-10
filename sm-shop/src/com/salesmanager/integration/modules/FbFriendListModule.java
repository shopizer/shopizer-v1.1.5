/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Jan 12, 2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.integration.modules;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.User;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.integration.PortletModule;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;
import com.salesmanager.core.util.www.integration.fb.FacebookUser;
import com.salesmanager.core.util.www.integration.fb.SalesManagerFacebookClient;

@Component("friendslist")
public class FbFriendListModule implements PortletModule {
	
	private Logger log = Logger.getLogger(FbFriendListModule.class);
	
	public boolean requiresAuthorization() {
		return false;
	}

	public void display(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
		
		
		try {
			
			
/*			//example on how to use facebook java graph API

			
			FacebookUser user = (FacebookUser)pageContext.getFromExecutionContext("facebookUser");
			
			String token = user.getOauth_token();

			
			FacebookClient facebookClient = new SalesManagerFacebookClient(token);
			//default is from API
			//FacebookClient facebookClient = new DefaultFacebookClient(token);


			User u = facebookClient.fetchObject("me", User.class);
			
			Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
					    
			if(myFriends!=null) {
				
				List<User> users = myFriends.getData();
				request.setAttribute("fbFriends", users);
				
			}
			
			Connection<CategorizedFacebookType> likes = 
				facebookClient.fetchConnection("me/likes", 
				CategorizedFacebookType.class);
			
			//fb_sig_profile_user
			
		    

			System.out.println(likes.hasNext());*/
			
		} catch (Exception e) {
			log.error(e);
		}
		
		


	}

	public void submit(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
		// TODO Auto-generated method stub

	}

}

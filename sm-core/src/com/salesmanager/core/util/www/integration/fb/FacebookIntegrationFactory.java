package com.salesmanager.core.util.www.integration.fb;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper; 


import com.restfb.FacebookClient;
import com.salesmanager.core.entity.reference.Page;



public class FacebookIntegrationFactory {
	
	private static Logger log = Logger.getLogger(FacebookIntegrationFactory.class);
	private static final String FACEBOOK_USER_CLIENT = "facebook.user.client";
	
	private static final String perms = "publish_stream,email,user_online_presence,user_likes";
	
	/**
	 * - Check if exist in http session
	 * - Check if authenticated
	 * @param request
	 * @param page
	 */
	public static FacebookUser getFacebookUser(HttpServletRequest request, Page page) {
		
		FacebookUser user = new FacebookUser();
		try {
			
			HttpSession session = request.getSession();
			user = (FacebookUser)session.getAttribute(FACEBOOK_USER_CLIENT);
			
			if(user==null) {
				
				user = new FacebookUser();
				user.setClientId(page.getProperty2());
				user.setApplicationSecret(page.getProperty4());
				user.setApplicationKey(page.getProperty5());
				session.setAttribute(FACEBOOK_USER_CLIENT, user);
				
			}
			
			String error_reason = request.getParameter("error_reason");
			
			if(!StringUtils.isBlank(error_reason)) {
				user.setOauth_token(null);
				user.setExpires(null);
				user.setAuthorized(false);
				return user;
			}
			
			String signed_request = request.getParameter("signed_request");
			if(!StringUtils.isBlank(signed_request)) {
				
				user.setAuthenticated(true);
					
                if (signed_request == null) 
                    throw new Exception("Invalid signature."); 


                String[] parts = signed_request.split("\\."); 
                if (parts.length != 2) 
                    throw new Exception("Invalid signature."); 


                String encSig = parts[0]; 
                String encPayload = parts[1]; 


                Base64 decoder = new Base64(true); 
                Map<String, String> data; 
                try {
                	

    				
    				String o = new String(decoder.decode(encPayload.getBytes()));
    				
    				o = o.trim();

    				
    				data = new ObjectMapper().readValue(o, HashMap.class);
    				String oauthtoken = data.get("oauth_token");
    				

    			    Iterator entries = data.entrySet().iterator(); 
    				while (entries.hasNext()) {   
    					Entry thisEntry = (Entry) entries.next();   
    					Object key = thisEntry.getKey();   
    					Object value = thisEntry.getValue();
    					log.debug("Got key " + key + " and value " + value);
    					if(key.equals("page")) {
    						if(value instanceof Map) {
    							Iterator ientries = ((Map)value).entrySet().iterator(); 
    		    				while (ientries.hasNext()) { 
    		    					Entry iEntry = (Entry) ientries.next(); 
    		    					Object ikey = iEntry.getKey();   
    		    					Object ivalue = iEntry.getValue();
    		    					log.debug("Got page key " + ikey + " and value " + ivalue + " type " + ivalue.getClass().getName());
    		    					if(ikey.equals("liked")) {
    	    								if((Boolean)ivalue==true) {
    	    									log.debug("User likes page [Boolean]");
        	    								user.setLikesPage(true);
    	    								}
    	    							break;
    		    					}
    		    				}

    						} else {
    							log.debug("value instanceof " + value.getClass().toString());
    						}
    					}
    				}
    				
    				//String expires = data.get("expires");
    				
    				//System.out.println(oauthtoken);
    				//System.out.println(expires);
    				user.setOauth_token(oauthtoken);
    				//user.setExpires(expires);
  

                } catch (Exception e) { 
                    throw new Exception("Failed to parse JSON session.", e); 
                } 

                

                try {
                	

					int idx = signed_request.indexOf(".");
					byte[] sig = new Base64(true).decode(signed_request.substring(0, idx).getBytes());
					String rawpayload = signed_request.substring(idx+1);
					

					SecretKeySpec secretKeySpec = new SecretKeySpec("93b47625ec3dcc4172fda796899ae42d".getBytes(), "HMACSHA256");
					Mac mac2 = Mac.getInstance("HMACSHA256");
					mac2.init(secretKeySpec);
					byte[] mysig = mac2.doFinal(rawpayload.getBytes());


					if (Arrays.equals(mysig, sig)) {
						user.setAuthorized(true);
					}

                    

                } catch (Exception e) { 
                    throw new Exception("Failed to perform crypt operation.", e); 
                } 


				
			}
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return user;
		
	}
	
	public static String getAuthorizationUrl(FacebookUser user, Page page) throws Exception {
		
		//facebook page url
		String url = URLEncoder.encode(page.getProperty6(), "UTF-8");
		
		StringBuffer requestUrl = new StringBuffer();
		requestUrl.append("https://graph.facebook.com/oauth/authorize?type=user_agent");
		requestUrl.append("&client_id=");
		requestUrl.append(user.getClientId());
		requestUrl.append("&redirect_uri=");
		requestUrl.append(url);
		requestUrl.append("&scope=");
		requestUrl.append(perms);
		   

		return requestUrl.toString();
		
	}
	


	
}



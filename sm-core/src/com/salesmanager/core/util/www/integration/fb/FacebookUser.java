package com.salesmanager.core.util.www.integration.fb;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class FacebookUser implements Serializable {
	
	

	private String clientId;
	private String applicationSecret;
	private String applicationKey;
	
	
	
	private boolean isAuthenticated= false;
	private boolean isAuthorized = false;
	
	
	private String user_id;
	private String oauth_token;
	private String expires;
	private String profile_id;
	
	private boolean likesPage;
	
	//algorithm
	//issued_at
	

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
	public String getApplicationSecret() {
		return applicationSecret;
	}
	public void setApplicationSecret(String applicationSecret) {
		this.applicationSecret = applicationSecret;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getOauth_token() {
		return oauth_token;
	}
	public void setOauth_token(String oauthToken) {
		oauth_token = oauthToken;
	}
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getProfile_id() {
		return profile_id;
	}
	public void setProfile_id(String profileId) {
		profile_id = profileId;
	}
	public boolean isAuthorized() {
		return isAuthorized;
	}
	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	public String getApplicationKey() {
		return applicationKey;
	}
	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}
	
	/**
	 * Determines if the user is fan of the current page
	 * @param request
	 * @return
	 */

	public boolean isLikesPage() {
		return likesPage;
	}
	public void setLikesPage(boolean likesPage) {
		this.likesPage = likesPage;
	}


}

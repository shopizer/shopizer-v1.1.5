package com.salesmanager.central.profile;

import java.io.Serializable;

public class Logon implements Serializable {
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

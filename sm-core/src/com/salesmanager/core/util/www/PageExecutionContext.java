package com.salesmanager.core.util.www;

import java.util.HashMap;
import java.util.Map;

public class PageExecutionContext {
	
	private Map infortation = new HashMap();
	
	public void addToExecutionContext(String key, Object value) {
		infortation.put(key, value);
	}
	
	public Object getFromExecutionContext(String key) {
		return infortation.get(key);
	}
	
	public Map getInternalMap() {
		return infortation;
	}
	
	public Object get(Object key) {
		return infortation.get(key);
	}

}

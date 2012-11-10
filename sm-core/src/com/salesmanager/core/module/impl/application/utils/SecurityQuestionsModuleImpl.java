package com.salesmanager.core.module.impl.application.utils;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.module.model.application.SecurityQuestionsModule;
import com.salesmanager.core.util.LabelUtil;

@Component("securityQuestions")
public class SecurityQuestionsModuleImpl implements SecurityQuestionsModule {

	//What was the color of your first car?
	//In what city were you born?
	//What was the name of your first pet?
	//What is your preferred musical genre?
	//Who was your childhood hero? 
	//In what city or town was your first job?
	//Where were you when you had your first kiss? 
	//In what city did you meet your spouse/significant other?
	//What was your childhood nickname? 




	
	public Map<Integer, String> getSecurityQuestions(Locale locale) {
		// TODO Auto-generated method stub
		
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);
		
		Map map = new TreeMap();
		map.put(1, label.getText("security.question.1"));
		map.put(2, label.getText("security.question.2"));
		map.put(3, label.getText("security.question.3"));
		map.put(4, label.getText("security.question.4"));
		map.put(5, label.getText("security.question.5"));
		map.put(6, label.getText("security.question.6"));
		map.put(7, label.getText("security.question.7"));
		map.put(8, label.getText("security.question.8"));
		map.put(9, label.getText("security.question.9"));
		
		
		
		return map;
	}

	public boolean validateSecurityQuestions(MerchantUserInformation userInformation, Map<Integer,Integer> userQuestions, Locale locale) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getQuestionText(int questionId, Locale locale) {
		
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);
		
		return label.getText("security.question." + questionId);
		
	}

}

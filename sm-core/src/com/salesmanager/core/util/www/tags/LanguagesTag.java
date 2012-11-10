package com.salesmanager.core.util.www.tags;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.UrlUtil;

public class LanguagesTag extends SimpleTagSupport {
	
	private Logger log = Logger.getLogger(LanguagesTag.class);
	
	private String delimiter;
	
	
	public void doTag() throws JspException, IOException {
		
		HttpServletRequest request = ((HttpServletRequest) ((PageContext) getJspContext())
				.getRequest());
		Locale locale = (Locale) request.getAttribute("LOCALE");
		
		try {
			
			String url = new StringBuffer().append("<a href=\"").append(UrlUtil.getUnsecuredDomain(request)).append(request.getContextPath()).append("/passthrough/changeLanguage.action?request_locale=").toString();
			//<s:property value="code" />"><s:property value="getText('label.language.' + ${language.code})" /></a>

			MerchantStore store = (MerchantStore)request.getAttribute("STORE");
			
			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(locale);
			
			int i = 1;
			
			Collection languages = store.getLanguages();
			if(languages != null && languages.size()>1) {
				
				if(languages.size()<2) {
					getJspContext().setAttribute("languageUrl", "");
					getJspBody().invoke(null);
					return;
				}
				
				for(Object o : languages) {
					
					Language l = (Language)o;
					
					String text = "label.language." + l.getCode();
					
					StringBuffer sb = new StringBuffer();
					sb.append(url).append(l.getCode())
					.append("\">").append(label.getText(text))
					.append("</a>");
					
					if(i<languages.size() && !StringUtils.isBlank(this.getDelimiter())) {
						
						sb.append(this.getDelimiter());
						
					}
					
					
					getJspContext().setAttribute("languageUrl", sb.toString());
					getJspBody().invoke(null);
					i++;
					
				}
				
				
			}
			
		} catch (Exception e) {
			log.error(e);
			// TODO: handle exception
		}
		
		
	}


	public String getDelimiter() {
		return delimiter;
	}


	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}

package com.salesmanager.core.util.www.tags;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.UrlUtil;


public class ProductImageTag extends TagSupport {
	
	private Logger log = Logger.getLogger(ProductImageTag.class);
	
	private Product product;
	private String source;
	private int resizeratio;
	private String id;
	private String cssClass;
	private boolean addSchemeHostAndPort = false;


	public int doStartTag() throws JspException {
		try {



			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			HttpSession session = request.getSession();
			
			Locale locale = (Locale) request.getAttribute("LOCALE");
			
			String imagePath = null;
			
			if("smallImage".equals(this.getSource())) {
				imagePath = FileUtil.getSmallProductImagePath(this.getProduct().getMerchantId(), this
						.getProduct().getProductImage());
			} else {
				imagePath = FileUtil.getLargeProductImagePath(this.getProduct().getMerchantId(), this
						.getProduct().getProductImage());
			}
			
			if(addSchemeHostAndPort) {
				imagePath = UrlUtil.getUnsecuredDomain(request) + imagePath;
			}
			
			//get configuration
			MerchantStore store = (MerchantStore)session.getAttribute("STORE");
			
			Map configurations = (Map)session.getAttribute("STORECONFIGURATION");
			
			if(configurations==null) {
				
				ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
				Map storeConfiguration = rservice.getModuleConfigurationsKeyValue(
				store.getTemplateModule(), store.getCountry());
				if (storeConfiguration != null) {
					session.setAttribute("STORECONFIGURATION",
							storeConfiguration);
				} else {
					configurations = new HashMap();
					Configuration conf = PropertiesUtil.getConfiguration();
					configurations.put("largeimagewidth", conf.getString("core.product.config.large.image.width"));
					configurations.put("largeimageheight", conf.getString("core.product.config.large.image.height"));
					configurations.put("smallimagewidth", conf.getString("core.product.config.small.image.width"));
					configurations.put("smallimageheight", conf.getString("core.product.config.small.image.height"));
				}
				
			}
			
			
			String simageWidth = null;
			String simageHeight = null;
			
			if("smallImage".equals(this.getSource())) {
				simageWidth = (String)configurations.get("smallimagewidth");
				simageHeight = (String)configurations.get("smallimageheight");
			} else {
				simageWidth = (String)configurations.get("largeimagewidth");
				simageHeight = (String)configurations.get("largeimageheight");
			}

			
			StringBuilder imageTag = new StringBuilder();
			imageTag.append("<img src=\"").append(imagePath).append("\"");
			
			if(this.getResizeratio()>0) {
				
				int imageWidth = 0;
				int imageHeight = 0;
				
				try {
					if("smallImage".equals(this.getSource())) {//or largeImage
						
						imageWidth = Integer.parseInt((String)configurations.get("smallimagewidth"));
						imageHeight = Integer.parseInt((String)configurations.get("smallimageheight"));
					} else {
						imageWidth = Integer.parseInt((String)configurations.get("largeimagewidth"));
						imageHeight = Integer.parseInt((String)configurations.get("largeimageheight"));	
					}
					
					imageWidth = imageWidth * this.getResizeratio()/100;
					imageHeight = imageHeight * this.getResizeratio()/100;
					
					simageWidth = String.valueOf(imageWidth);
					simageHeight = String.valueOf(imageHeight);
		
				} catch (Exception e) {
					log.error("Error formating image size " + e);
				}
				
			}
			
			imageTag.append(" border=\"0\" width=\"").append(simageWidth).append("\"");
			imageTag.append(" border=\"0\" height=\"").append(simageHeight).append("\"");
			
			if(!StringUtils.isBlank(this.getId())) {
				imageTag.append(" id=\"").append(this.getId()).append("\"");
			}
			
			if(!StringUtils.isBlank(this.getCssClass())) {
				imageTag.append(" class=\"").append(this.getCssClass()).append("\"");
			}
			
			imageTag.append(">");

			pageContext.getOut().print(imageTag.toString());


			
		} catch (Exception ex) {
			log.error(ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getResizeratio() {
		return resizeratio;
	}

	public void setResizeratio(int resizeratio) {
		this.resizeratio = resizeratio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public boolean isAddSchemeHostAndPort() {
		return addSchemeHostAndPort;
	}

	public void setAddSchemeHostAndPort(boolean addSchemeHostAndPort) {
		this.addSchemeHostAndPort = addSchemeHostAndPort;
	}



}

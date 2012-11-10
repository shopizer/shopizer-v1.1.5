package com.salesmanager.core.entity.common;

import com.salesmanager.core.entity.merchant.MerchantStore;

public class ReportHeader {
	
	
	private MerchantStore store;
	private String searchReportCriteria;
	private String merchantStoreLogo;

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public String getSearchReportCriteria() {
		return searchReportCriteria;
	}

	public void setSearchReportCriteria(String searchReportCriteria) {
		this.searchReportCriteria = searchReportCriteria;
	}

	public String getMerchantStoreLogo() {
		return merchantStoreLogo;
	}

	public void setMerchantStoreLogo(String merchantStoreLogo) {
		this.merchantStoreLogo = merchantStoreLogo;
	}

}

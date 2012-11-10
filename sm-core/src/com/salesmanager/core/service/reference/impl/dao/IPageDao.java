package com.salesmanager.core.service.reference.impl.dao;

import com.salesmanager.core.entity.reference.Page;

public interface IPageDao {

	public abstract void persist(Page transientInstance);

	public abstract void saveOrUpdate(Page instance);

	public abstract void delete(Page persistentInstance);

	public abstract Page findById(long id);
	
	public Page getPage(String title, int merchantId);
	
	public Page getPage(long pageId, int merchantId);

}
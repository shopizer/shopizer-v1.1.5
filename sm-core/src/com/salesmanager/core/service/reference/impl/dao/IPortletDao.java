package com.salesmanager.core.service.reference.impl.dao;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.entity.reference.Portlet;

public interface IPortletDao {

	public void persist(Portlet transientInstance);

	public void saveOrUpdate(Portlet instance);

	public void delete(Portlet persistentInstance);

	public Portlet findById(long id);
	
	public Collection<Portlet> getPortlets(long pageId, String columnId, int merchantId);
	
	public void deleteAll(Collection<Portlet> instances);
	
	public void saveOrUpdateAll(Collection<Portlet> instances);
	
	public Collection<Portlet> getDynamicLabels(List<Long> ids, int merchantId);
	
	public Collection<Portlet> getPortlets(long pageId, int merchantId);

}
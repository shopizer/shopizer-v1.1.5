package com.salesmanager.core.service.merchant.impl.dao;

import java.util.Collection;

import com.salesmanager.core.entity.merchant.MerchantUserRoleDef;

public interface IMerchantUserRoleDefDao {

	public abstract Collection<MerchantUserRoleDef> findAll();

}
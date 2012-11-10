package com.salesmanager.core.service.merchant.impl.dao;
// Generated Oct 27, 2010 10:12:37 PM by Hibernate Tools 3.2.4.GA


import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.entity.merchant.MerchantUserRoleDef;
import com.salesmanager.core.entity.reference.ProductType;

/**
 * Home object for domain model class MerchantUserRoleDef.
 * @see com.salesmanager.core.test.MerchantUserRoleDef
 * @author Hibernate Tools
 */
@Repository
public class MerchantUserRoleDefDao extends HibernateDaoSupport implements IMerchantUserRoleDefDao{

    private static final Log log = LogFactory.getLog(MerchantUserRoleDefDao.class);

	@Autowired
	public MerchantUserRoleDefDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.merchant.impl.dao.IMerchantUserRoleDefDao#findAll()
	 */
    public Collection<MerchantUserRoleDef> findAll() {

        try {

            Collection l = super.getSession().createCriteria(MerchantUserRoleDef.class)
			.list();
            
            return l;

        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

}


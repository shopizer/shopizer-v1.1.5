package com.salesmanager.core.service.reference.impl.dao;
// Generated Oct 28, 2010 6:11:59 PM by Hibernate Tools 3.2.4.GA


import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.salesmanager.core.entity.reference.Page;

/**
 * Home object for domain model class Page.
 * @see com.salesmanager.core.test.Page
 * @author Hibernate Tools
 */
@Repository
public class PageDao extends HibernateDaoSupport implements IPageDao {

    private static final Log log = LogFactory.getLog(PageDao.class);

	@Autowired
	public PageDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPageDao#persist(com.salesmanager.core.entity.reference.Page)
	 */
    public void persist(Page transientInstance) {

        try {
        	super.getHibernateTemplate().persist(transientInstance);
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPageDao#saveOrUpdate(com.salesmanager.core.entity.reference.Page)
	 */
    public void saveOrUpdate(Page instance) {
        try {
        	super.getHibernateTemplate().saveOrUpdate(instance);
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPageDao#delete(com.salesmanager.core.entity.reference.Page)
	 */
    public void delete(Page persistentInstance) {
        try {
        	super.getHibernateTemplate().delete(persistentInstance);
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    

    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPageDao#findById(long)
	 */
    public Page findById(long id) {

        try {
            Page instance = (Page) super.getHibernateTemplate()
                    .get("com.salesmanager.core.entity.reference.Page", id);
  
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public Page getPage(long pageId, int merchantId) {
        try {
        	Page page = (Page)super.getSession().createCriteria(
        			Page.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("pageId", pageId))
					.uniqueResult();
        	
        	return page;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public Page getPage(String title, int merchantId) {
        try {

        	
       	Page page = (Page)super.getSession().createCriteria(
        			Page.class).add(
					Restrictions.eq("merchantId", merchantId))
					.add(
					Restrictions.eq("title", title))
					.uniqueResult();
        	
        	return page;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    

}


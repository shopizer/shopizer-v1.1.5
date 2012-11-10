package com.salesmanager.core.service.reference.impl.dao;
// Generated Oct 28, 2010 6:11:59 PM by Hibernate Tools 3.2.4.GA


import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.salesmanager.core.entity.reference.Portlet;

/**
 * Home object for domain model class Portlet.
 * @see com.salesmanager.core.test.Portlet
 * @author Hibernate Tools
 */
@Repository
public class PortletDao extends HibernateDaoSupport implements IPortletDao {

    private static final Log log = LogFactory.getLog(PortletDao.class);

	@Autowired
	public PortletDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPortletDao#persist(com.salesmanager.core.entity.reference.Portlet)
	 */
    public void persist(Portlet transientInstance) {
        try {
        	super.getHibernateTemplate().persist(transientInstance);
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPortletDao#saveOrUpdate(com.salesmanager.core.entity.reference.Portlet)
	 */
    public void saveOrUpdate(Portlet instance) {
        try {
        	super.getHibernateTemplate().saveOrUpdate(instance);
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void saveOrUpdateAll(Collection<Portlet> instances) {
        try {
        	super.getHibernateTemplate().saveOrUpdateAll(instances);
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    

    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPortletDao#delete(com.salesmanager.core.entity.reference.Portlet)
	 */
    public void delete(Portlet persistentInstance) {
        try {
        	super.getHibernateTemplate().delete(persistentInstance);
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    
    public void deleteAll(Collection<Portlet> instances) {
        try {
        	super.getHibernateTemplate().deleteAll(instances);
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    

    
    /* (non-Javadoc)
	 * @see com.salesmanager.core.service.reference.impl.dao.IPortletDao#findById(int)
	 */
    public Portlet findById( long id) {
        try {
            Portlet instance = (Portlet) super.getHibernateTemplate()
                    .get("com.salesmanager.core.entity.reference.Portlet", id);
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public Collection<Portlet> getPortlets(long pageId, String columnId, int merchantId) {
        try {
        	List portlets = super.getSession().createCriteria(
					Portlet.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("page", pageId)).add(
					Restrictions.eq("columnId", columnId))
					.addOrder(Order.asc("sortOrder"))
					.list();
        	
        	return portlets;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public Collection<Portlet> getPortlets(long pageId, int merchantId) {
        try {
        	List portlets = super.getSession().createCriteria(
					Portlet.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("page", pageId))
					.addOrder(Order.asc("sortOrder"))
					.list();
        	
        	return portlets;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public Collection<Portlet> getDynamicLabels(List<Long> ids, int merchantId) {
    	
		try {

			List l = super
					.getSession()
					.createQuery(
							"select p from Portlet p where p.merchantId=:mId and p.labelId in (:lIds) order by sortOrder")
					.setInteger("mId", merchantId)
					.setParameterList("lIds", ids).list();

			return l;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
    	
    	
    }
    

}


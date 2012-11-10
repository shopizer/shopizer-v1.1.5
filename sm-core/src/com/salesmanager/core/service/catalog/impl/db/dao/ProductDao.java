/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.catalog.impl.db.dao;

// Generated Aug 7, 2008 11:34:44 PM by Hibernate Tools 3.2.0.beta8

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductDescriptionId;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;

/**
 * Home object for domain model class Products.
 * 
 * @see com.salesmanager.core.test.Products
 * @author Hibernate Tools
 */
@Repository
public class ProductDao extends HibernateDaoSupport implements IProductDao {

	private static final Log log = LogFactory.getLog(ProductDao.class);

	@Autowired
	public ProductDao(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.IProductDao#persist(com.
	 * salesmanager.core.entity.catalog.Product)
	 */
	public void persist(Product transientInstance) {
		try {
			super.getHibernateTemplate().persist(transientInstance);
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductDao#saveOrUpdate(com
	 * .salesmanager.core.entity.catalog.Product)
	 */
	public void saveOrUpdate(Product instance) {
		try {
			super.getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void saveOrUpdateAll(Collection<Product> products) {
		try {
			super.getHibernateTemplate().saveOrUpdateAll(products);
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.salesmanager.core.service.catalog.impl.IProductDao#delete(com.
	 * salesmanager.core.entity.catalog.Product)
	 */
	public void delete(Product persistentInstance) {
		try {
			super.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductDao#merge(com.salesmanager
	 * .core.entity.catalog.Product)
	 */
	public Product merge(Product detachedInstance) {
		try {
			Product result = (Product) super.getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.salesmanager.core.service.catalog.impl.IProductDao#findById(int)
	 */
	public Product findById(long id) {
		try {
			Product instance = (Product) super.getHibernateTemplate().get(
					"com.salesmanager.core.entity.catalog.Product", id);

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Product findById(long id, int languageId) {
		try {
			Product instance = (Product) super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.productId =:pId and s.id.languageId=:lId order by p.productSortOrder")
					.setLong("pId", id).setInteger("lId", languageId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.uniqueResult();

			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findByIds(Collection<Long> ids) {
		try {
			List list = super.getSession().createCriteria(Product.class).add(
					Expression.in("productId", ids)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.salesmanager.core.service.catalog.impl.IProductDao#findByMerchantId
	 * (int)
	 */
	public Collection<Product> findByMerchantId(int merchantid) {
		try {
			List list = super.getSession().createCriteria(Product.class).add(
					Restrictions.eq("merchantId", merchantid))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findByMerchantIdAndCategoryId(int merchantId,
			long categoryId) {
		try {
			List list = super.getSession().createCriteria(Product.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Restrictions.eq("masterCategoryId", categoryId)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findByMerchantIdAndCategories(int merchantId,
			Collection<Long> categoryIds) {
		try {
			List list = super.getSession().createCriteria(Product.class).add(
					Restrictions.eq("merchantId", merchantId)).add(
					Expression.in("masterCategoryId", categoryIds)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findProductsByProductsIdAndLanguageId(
			List<Long> productIds, int languageId) {

		try {

			List list = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.productId in(:pId) and s.id.languageId=:lId order by p.productSortOrder")
					.setParameterList("pId", productIds).setInteger("lId",
							languageId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findAvailableProductsByProductsIdAndLanguageId(
			List<Long> productIds, int languageId) {

		try {

			List list = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.productId in(:pId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable<=:dt order by p.productSortOrder")
					.setParameterList("pId", productIds).setInteger("lId",
							languageId).setDate("dt", new Date())
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	private SearchProductResponse findAvailableProductsByProductsIdAndLanguageId(
			SearchProductCriteria criteria, List<Long> productIds,
			int languageId) {

		try {

			/*
			 * List list =super.getSession().createQuery(
			 * "select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.productId in(:pId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable<=:dt order by p.productSortOrder"
			 * ) .setParameterList("pId", productIds) .setInteger("lId",
			 * languageId) .setDate("dt", new Date())
			 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) .list();
			 * 
			 * return list;
			 */

			SearchProductResponse response = new SearchProductResponse();

			if (productIds == null || productIds.size() == 0) {
				response.setCount(0);
				response.setProducts(new ArrayList());
				return response;
			}

			/** count total values **/
			Query c = super
					.getSession()
					.createQuery(
							"select count(p) from Product p left join p.descriptions s where p.productId in(:pId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable<=:dt order by p.productSortOrder");
			// Query c =
			// super.getSession().createQuery("select count(p) from Product p left join p.descriptions s where p.merchantId=:mId and p.masterCategoryId in(:cId) and s.id.languageId=:lId");
			c.setParameterList("pId", productIds).setInteger("lId", languageId)
					.setDate("dt", new Date());

			int count = ((Number) c.uniqueResult()).intValue();
			response.setCount(count);

			Query q = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.productId in(:pId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable<=:dt order by p.productSortOrder");
			q.setParameterList("pId", productIds).setInteger("lId", languageId)
					.setDate("dt", new Date()).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY);

			if (count > 0) {
				q.setFirstResult(criteria.getLowerLimit());
				q.setMaxResults(criteria.getUpperLimit(count));
			}

			List l = q.list();

			response.setProducts(l);

			return response;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findProductByCategoryIdAndMerchantIdAndLanguageId(
			long categoryId, int merchantId, int languageId) {

		try {

			List list = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.merchantId=:mId and p.masterCategoryId=:cId and s.id.languageId=:lId order by p.productSortOrder")
					.setInteger("mId", merchantId).setLong("cId", categoryId)
					.setInteger("lId", languageId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void updateProductListAvailability(boolean available,
			int merchantId, List<Long> ids) {

		try {

			Query q = super
					.getSession()
					.createQuery(
							"update Product p set p.productDateAvailable=:pda where p.merchantId=:mId and p.productId in(:pId)")
					.setParameterList("pId", ids).setInteger("mId", merchantId);

			if (!available) {

				Calendar c1 = Calendar.getInstance();
				c1.add(Calendar.YEAR, 100);
				q.setDate("pda", c1.getTime());

			} else {
				q.setDate("pda", new Date(new Date().getTime()));
			}

			q.executeUpdate();

		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}

	}

	public Collection<Product> findProductsByCategoriesIdAndMerchantIdAndLanguageId(
			List<Long> categoryIds, int merchantId, int languageId) {

		try {

			List list = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.merchantId=:mId and p.masterCategoryId in(:cId) and s.id.languageId=:lId order by p.productSortOrder")
					.setInteger("mId", merchantId).setParameterList("cId",
							categoryIds).setInteger("lId", languageId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Collection<Product> findByMerchantIdAndLanguageId(int merchantId, int languageId) {
		
		try {

			List list = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.merchantId=:mId and s.id.languageId=:lId order by p.productSortOrder")
					.setInteger("mId", merchantId)
					.setInteger("lId", languageId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			return list;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}	
		
	}

	/**
	 * Catalog category page
	 */
	public SearchProductResponse findProductsByAvailabilityCategoriesIdAndMerchantIdAndLanguageId(
			SearchProductCriteria criteria) {

		if (criteria == null || criteria.getCategoryList() == null) {
			throw new RuntimeException(
					"Invalid criteria, requires merchantId, languageId, categoryLis");
		}

		try {

			SearchProductResponse response = new SearchProductResponse();

			/** count total values **/
			Query c = super
					.getSession()
					.createQuery(
							"select count(p) from Product p left join p.descriptions s where p.merchantId=:mId and p.masterCategoryId in(:cId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable <= :dt");
			c.setInteger("mId", criteria.getMerchantId()).setParameterList(
					"cId", criteria.getCategoryList())
					.setDate("dt", new Date()).setInteger("lId",
							criteria.getLanguageId());
			int count = ((Number) c.uniqueResult()).intValue();
			response.setCount(count);

			Query q = super
					.getSession()
					.createQuery(
							"select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rp left join fetch r.special x where p.merchantId=:mId and p.masterCategoryId in(:cId) and s.id.languageId=:lId and p.productDateAvailable is not null and p.productDateAvailable <= :dt order by p.productSortOrder")
					// Query q =
					// super.getSession().createQuery("select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.special x where p.merchantId=:mId and p.masterCategoryId in(:cId) and s.id.languageId=:lId order by p.productSortOrder")
					.setInteger("mId", criteria.getMerchantId())
					.setParameterList("cId", criteria.getCategoryList())
					.setInteger("lId", criteria.getLanguageId()).setDate("dt",
							new Date());

			if (count > 0) {
				q.setFirstResult(criteria.getLowerLimit());
				q.setMaxResults(criteria.getUpperLimit(count));
			}

			List list = q.list();

			response.setProducts(list);

			return response;

			/*
			 * if(max!=-1 && count >0) {
			 * c.setMaxResults(searchCriteria.getUpperLimit(count));
			 * c.setFirstResult(searchCriteria.getLowerLimit()); list =
			 * query.list(); } else { list = query.list(); }
			 */

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Collection<Product> findByTaxClassId(long taxclassId) {
		try {
			List list = super.getSession().createCriteria(Product.class).add(
					Restrictions.eq("productTaxClassId", taxclassId)).list();
			return list;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public int countProduct(int merchantId) {

		try {
			Criteria criteria = super.getSession().createCriteria(
					com.salesmanager.core.entity.catalog.Product.class).add(
					Restrictions.eq("merchantId", merchantId));

			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer) criteria.uniqueResult();

			return count;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}

	public SearchProductResponse searchProduct(
			SearchProductCriteria searchCriteria) {

		try {

			Criteria criteria = super.getSession().createCriteria(
					com.salesmanager.core.entity.catalog.Product.class).add(
					Restrictions.eq("merchantId", searchCriteria
							.getMerchantId()));
			// .setFetchMode("descriptions", FetchMode.JOIN);

			// select p from Product p left join fetch p.descriptions s left
			// join fetch p.specials y left join fetch p.prices r left join
			// fetch r.priceDescriptions rd left join fetch r.special x where
			// p.merchantId=:mId and p.masterCategoryId in(:cId) and
			// s.id.languageId=:lId order by p.productSortOrder
			StringBuffer q = new StringBuffer();
			q
					.append("select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rd left join fetch r.special x where p.merchantId=:mId and s.id.languageId=:lId");

			List inlist = null;
			if (searchCriteria.getCategoryid() != -1) {

				// only products in categories
				CatalogService cservice = (CatalogService) ServiceFactory
						.getService(ServiceFactory.CatalogService);
				Map categories = cservice.getCategoriesByLang(searchCriteria
						.getMerchantId(), LanguageUtil
						.getLanguageStringCode(searchCriteria.getLanguageId()));
				Category c = (Category) categories.get(searchCriteria
						.getCategoryid());
				// StringBuffer inlist = null;
				// if(c.getParentId()==0) {
				// List subcategs =
				// cservice.getCategoriesIdPerSubCategories(LanguageUtil.getLanguageStringCode(searchCriteria.getLanguageId()),c);
				List subcategs = cservice.findSubCategories(c.getCategoryId());
				if (subcategs != null && subcategs.size() > 0) {
					inlist = new ArrayList();
					inlist.add(searchCriteria.getCategoryid());
					Iterator it = subcategs.iterator();
					while (it.hasNext()) {
						Category category = (Category) it.next();
						long cid = category.getCategoryId();
						inlist.add(cid);
					}
				}
				// }
			}

			if (searchCriteria != null) {

				if (inlist != null) {
					q.append(" and p.masterCategoryId in(:mcIds)");
				} else if (searchCriteria.getCategoryid() != -1
						&& inlist == null) {
					q.append(" and p.masterCategoryId = :mcId");
				}

				if (searchCriteria.getVisible() != SearchProductCriteria.VISIBLEALL) {// visibility

					if (searchCriteria.getVisible() == SearchProductCriteria.VISIBLETRUE) {

						q.append(" and p.productDateAvailable <= :dt");

					} else {

						q.append(" and p.productDateAvailable > :dt");

					}
				}
				if (searchCriteria.getStatus() != SearchProductCriteria.STATUSALL) {// availability

					q.append(" and p.productStatus = :st");
				}
				if (!StringUtils.isBlank(searchCriteria.getDescription())) {

					q.append(" and s.productName like :pName");
					// q.append(" and s.productName like '%lounge%'");

				}

			}

			/*
			 * Criteria query = super.getSession()
			 * .createCriteria(com.salesmanager
			 * .core.entity.catalog.Product.class)
			 * .add(Restrictions.eq("merchantId",
			 * searchCriteria.getMerchantId())) //.setFetchMode("descriptions",
			 * FetchMode.JOIN)
			 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			 */

			Query c = super.getSession().createQuery(q.toString());
			c.setInteger("mId", searchCriteria.getMerchantId());
			c.setInteger("lId", searchCriteria.getLanguageId());

			/*
			 * Criteria descCriteria = criteria.createCriteria("descriptions")
			 * .add(Restrictions.eq("id.languageId",
			 * searchCriteria.getLanguageId()));
			 * 
			 * Criteria descCriteriaQ = query.createCriteria("descriptions")
			 * .add(Restrictions.eq("id.languageId",
			 * searchCriteria.getLanguageId()));
			 */

			if (searchCriteria != null) {

				if (inlist != null) {

					criteria.add(Restrictions.in("masterCategoryId", inlist));
					// query.add(Restrictions.in("masterCategoryId", inlist));
					c.setParameterList("mcIds", inlist);

				} else if (searchCriteria.getCategoryid() != -1
						&& inlist == null) {

					criteria.add(Restrictions.eq("masterCategoryId",
							searchCriteria.getCategoryid()));
					// query.add(Restrictions.eq("masterCategoryId",
					// searchCriteria.getCategoryid()));
					c.setLong("mcId", searchCriteria.getCategoryid());

					// wherecategory =
					// " and products_view.master_categories_id IN " +
					// inlist.toString();
				}

			}

			if (searchCriteria.getVisible() != SearchProductCriteria.VISIBLEALL) {// visibility

				if (searchCriteria.getVisible() == SearchProductCriteria.VISIBLETRUE) {
					// wherevisible =
					// " and products_view.products_date_available IS NOT NULL and products_view.products_date_available < now()";

					criteria.add(Expression.isNotNull("productDateAvailable"));
					criteria
							.add(Expression.lt("productDateAvailable",
									new java.util.Date(new java.util.Date()
											.getTime())));
					// query.add(Expression.isNotNull("productDateAvailable"));
					// query.add(Expression.lt("productDateAvailable",new
					// java.util.Date(new java.util.Date().getTime())));

					c.setParameter("dt", new Date());

				} else {
					// wherevisible =
					// " and (products_view.products_date_available IS NULL or products_view.products_date_available > now())";
					criteria.add(Restrictions.or(Expression
							.isNull("productDateAvailable"), Expression.gt(
							"productDateAvailable", new java.util.Date(
									new java.util.Date().getTime()))));
					// query.add(Restrictions.or(Expression.isNull("productDateAvailable"),Expression.gt("productDateAvailable",new
					// java.util.Date(new java.util.Date().getTime()))));

					c.setParameter("dt", new Date());

				}
			}
			if (searchCriteria.getStatus() != SearchProductCriteria.STATUSALL) {// availability
				criteria
						.add(Restrictions
								.eq(
										"productStatus",
										searchCriteria.getStatus() == SearchProductCriteria.STATUSINSTOCK ? new Boolean(
												"true")
												: new Boolean("false")));
				// query.add(Restrictions.eq("productStatus",
				// searchCriteria.getStatus()==SearchProductCriteria.STATUSINSTOCK?new
				// Boolean("true"):new Boolean("false")));
				// wherestatus = " and products_view.products_status=" +
				// criteria.getStatus();
				c
						.setBoolean(
								"st",
								searchCriteria.getStatus() == SearchProductCriteria.STATUSINSTOCK ? new Boolean(
										"true")
										: new Boolean("false"));
			}
			if (!StringUtils.isBlank(searchCriteria.getDescription())) {
				criteria.createAlias("descriptions", "description").add(
						Restrictions.eq("description.id.languageId",
								searchCriteria.getLanguageId())).add(
						Restrictions.like("description.productName", "%"
								+ searchCriteria.getDescription() + "%"));
				// query.createAlias("descriptions",
				// "description").add(Restrictions.eq("description.id.languageId",
				// searchCriteria.getLanguageId())).add(Restrictions.like("description.productName",
				// "%"+searchCriteria.getDescription()+"%"));
				// descCriteria.add((Restrictions.like("productName",
				// "%"+searchCriteria.getDescription()+"%")));
				// descCriteriaQ.add((Restrictions.like("productName",
				// "%"+searchCriteria.getDescription()+"%")));
				// wherename = " and products_description.products_name LIKE '%"
				// + criteria.getName() + "%'";
				c.setString("pName", "%" + searchCriteria.getDescription()
						+ "%");

			}

			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer) criteria.uniqueResult();

			criteria.setProjection(null);

			int max = searchCriteria.getQuantity();

			/*
			 * List list = query
			 * .setMaxResults(searchCriteria.getUpperLimit(count))
			 * .setFirstResult(searchCriteria.getLowerLimit()).list();
			 */

			List list = null;
			// .setMaxResults(searchCriteria.getUpperLimit(count))
			// .setFirstResult(searchCriteria.getLowerLimit()).list();

			if (max != -1 && count > 0) {
				c.setMaxResults(searchCriteria.getUpperLimit(count));
				c.setFirstResult(searchCriteria.getLowerLimit());
				list = c.list();
			} else {
				list = c.list();
			}

			/*
			 * //set short name if(list != null) { Iterator i = list.iterator();
			 * while(i.hasNext()) { com.salesmanager.core.entity.catalog.Product
			 * v = (com.salesmanager.core.entity.catalog.Product)i.next();
			 * v.setName(v.getDescription()); Set descs = v.getDescriptions();
			 * String shortName = ""; if(descs!=null) { Iterator di =
			 * descs.iterator(); while(di.hasNext()) { ProductDescription pd =
			 * (ProductDescription)di.next(); ProductDescriptionId id =
			 * pd.getId();
			 * if(id.getLanguageId()==searchCriteria.getLanguageId()) {
			 * shortName = pd.getProductName(); break; } } }
			 * v.setName(shortName); } }
			 */

			SearchProductResponse response = new SearchProductResponse();
			response.setCount(count);

			response.setProducts(list);

			return response;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	
	

	/**
	 * Catalog (shop) Search
	 */
	public SearchProductResponse findProductsByDescription(
			SearchProductCriteria criteria) {

		try {

			FullTextSession ftSession = Search.createFullTextSession(super
					.getSession());

			org.apache.lucene.queryParser.QueryParser parser = new QueryParser(
					"descriptions.productDescription", new StandardAnalyzer());

			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer
					// @todo, all descriptions per language are flat in the same
					// fields
					// .append("id:").append(criteria.getLanguageId()).append(".languageId AND ")
					.append("merchantId:")
					.append(criteria.getMerchantId())
					// .append(" AND productStatus:")
					// .append(1)

					.append(" AND ").append("descriptions.productDescription:")
					.append(criteria.getDescription()).append(
							" OR descriptions.productName:").append(
							criteria.getDescription());

			org.apache.lucene.search.Query luceneQuery = parser
					.parse(queryBuffer.toString());

			// org.hibernate.Query query = ftSession.createFullTextQuery(
			// luceneQuery );
			org.hibernate.search.FullTextQuery query = ftSession
					.createFullTextQuery(luceneQuery);

			int count = query.getResultSize();

			query = ftSession.createFullTextQuery(luceneQuery);

			int max = criteria.getQuantity();

			if (max != -1 && count > 0) {
				query.setMaxResults(criteria.getUpperLimit(count));
				query.setFirstResult(criteria.getLowerLimit());
			}

			Collection<Product> results = query.list();

			/*
			 * List products = new ArrayList(); if(results!=null &&
			 * results.size()>0) { for(Object o : results) { Product p =
			 * (Product)o; products.add(p.getProductId()); } }
			 */

			// SearchProductResponse response =
			// this.findAvailableProductsByProductsIdAndLanguageId(criteria,
			// products, criteria.getLanguageId());
			SearchProductResponse response = new SearchProductResponse();
			response.setCount(count);
			response.setProducts(results);

			/*
			 * SearchProductResponse response = new SearchProductResponse();
			 * //response.setProducts(results); response.setProducts(entities);
			 * response.setCount(count)
			 */
			
			//ftSession.close();

			return response;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Product findProductByMerchantIdAndSeoURLAndByLang(int merchantId,
			String seUrl, int languageId) {

		try {

			String query = "select p from Product p left join fetch p.descriptions s left join fetch p.specials y left join fetch p.prices r left join fetch r.priceDescriptions rp left join fetch r.special x where p.merchantId=:mId and s.id.languageId=:lId and s.seUrl=:sText";

			Product p = (Product) super.getSession().createQuery(query)
					.setInteger("mId", merchantId).setString("sText", seUrl)
					.setInteger("lId", languageId).setResultTransformer(
							Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();

			return p;

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

}

/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.orders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.PageBaseAction;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.entity.common.ReportHeader;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.OrderReport;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class OrderListAction extends PageBaseAction {

	private Logger log = Logger.getLogger(OrderListAction.class);
	private String cardtext;

	private Collection orders = new ArrayList();

	static Configuration config = PropertiesHelper.getConfiguration();

	private static int ordersize = 20;

	private SearchOrdersCriteria criteria = null;
	
	private InputStream inputStream;
	
	private String orderId = "";

	static {

		ordersize = config.getInt("central.orderlist.maxsize", 20);

	}

	private void buildCriteria() throws Exception {

		criteria = new SearchOrdersCriteria();

		DateUtil dh = new DateUtil();
		dh.processPostedDates(super.getServletRequest());

		criteria.setSdate(dh.getStartDate());
		criteria.setEdate(dh.getEndDate());

		if (dh.getStartDate() != null) {
			super.getServletRequest().setAttribute("sdate",
					DateUtil.formatDate(dh.getStartDate()));
		}
		if (dh.getEndDate() != null) {
			super.getServletRequest().setAttribute("edate",
					DateUtil.formatDate(dh.getEndDate()));
		}

		String customername = super.getServletRequest().getParameter(
				"customername");

		if (customername != null && !customername.trim().equals("")) {
			criteria.setCustomerName(customername);
			super.getServletRequest()
					.setAttribute("customername", customername);
		}

		criteria.setMerchantId(super.getContext().getMerchantid());

	}
	
	/**
	 * Creates report
	 * @return
	 */
	public String createReportByCriteria() {
		
		// START DATE - END DATE

		try {

			
			this.buildCriteria();
			
			
			if (!StringUtils.isBlank(this.getOrderId())) {
				long oid = -1;
				try {
					oid = Long.parseLong(this.getOrderId());

				} catch (NumberFormatException nfe) {
					log.error(nfe);
				}
				criteria.resetCriteria();
				criteria.setOrderId(oid);
			}

			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(super.getLocale());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			SearchOrderResponse response = oservice.searchOrders(criteria);

			orders = response.getOrders();
			
			LocaleUtil.setLocaleToEntityCollection(orders, super.getLocale());
			
			
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(super.getContext().getMerchantid());
			store.setLocale(super.getLocale());
			
			
			ReportHeader reportHeader = new ReportHeader();
			reportHeader.setStore(store);
			
			OrderReport report = new OrderReport();
			report.setOrders(orders);
			
			if (!StringUtils.isEmpty(store.getStorelogo())) {
				//String path = PropertiesUtil.getConfiguration().getString(
				//		"core.branding.cart.filefolder");
				String path = FileUtil.getBrandingFilePath();
				path = path + "/" + store.getMerchantId() + "/header/"
						+ store.getStorelogo();
				reportHeader.setMerchantStoreLogo(path);
			}
						
			//Build search string
			/**
			 * startDate (may be null)
			 * endDate (may be null)
			 * customerName (may be null)
			 */
			StringBuffer reportCriteriaText = new StringBuffer();
			reportCriteriaText.append(label.getText("label.order.searchreport.title"));
			
			
			

			if (!StringUtils.isBlank(this.getOrderId())) {
				long oid = -1;
				try {
					oid = Long.parseLong(this.getOrderId());

				} catch (NumberFormatException nfe) {
					log.error(nfe);
				}
				criteria.resetCriteria();
				criteria.setOrderId(oid);
				reportCriteriaText.append(" ").append(label.getText("label.order.orderid")).append(" ").append(oid);
			} else {
			
				if(criteria.getSdate()!=null) {
					reportCriteriaText.append(" ").append(label.getText("label.generic.startdate")).append(" ").append(criteria.getStartDateString());
				}
				if(criteria.getEdate()!=null) {
					reportCriteriaText.append(" ").append(label.getText("label.generic.enddate")).append(" ").append(criteria.getEndDateString());
				}
				if(!StringUtils.isBlank(criteria.getCustomerName())) {
					reportCriteriaText.append(" ").append(label.getText("label.customer.name")).append(" ").append(criteria.getCustomerName());
				}
			
			}
			
			reportHeader .setSearchReportCriteria(reportCriteriaText.toString());
			
			report.setReportHeader(reportHeader);
			
			OrderService orderService = (OrderService)ServiceFactory.getService(ServiceFactory.OrderService);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			orderService.prepareOrderListReport(report, super.getLocale(), os);
			
			inputStream = new ByteArrayInputStream(os.toByteArray());
			
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			
			return INPUT;
		}
		
	}

	/**
	 * Search header functions
	 * 
	 * @return
	 * @throws Exception
	 */
	public String searchByCriteria() throws Exception {

		// START DATE - END DATE

		try {

			super.getServletRequest().getSession().removeAttribute("ITEMCOUNT");
			this.buildCriteria();

			//String orderid = super.getServletRequest().getParameter("orderid");

			if (!StringUtils.isBlank(this.getOrderId())) {
				long oid = -1;
				try {
					oid = Long.parseLong(this.getOrderId());

				} catch (NumberFormatException nfe) {
					log.error(nfe);
				}
				criteria.resetCriteria();
				criteria.setOrderId(oid);

			}

			this.setSize(ordersize);
			super.setPageStartNumber();

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			SearchOrderResponse response = oservice.searchOrders(criteria);

			orders = response.getOrders();
			super.setListingCount(response.getCount());
			super.setRealCount(response.getOrders().size());
			super.setPageElements();

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}
		return SUCCESS;
	}

	/**
	 * Page entry point
	 * 
	 * @return
	 * @throws Exception
	 */

	private String getOrdersList() throws Exception {
		try {
			
			super.setPageTitle("label.order.orderlist.title");
			this.buildCriteria();

			// override start date & end date with page navigation criteria submission
			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate = null;
			Date eDate = null;
			try {
				if (super.getServletRequest().getParameter("navstartdate") != null) {
					if (criteria.getSdate() == null) {
						sDate = myDateFormat.parse(super.getServletRequest()
								.getParameter("navstartdate"));
					}
				}
				if (super.getServletRequest().getParameter("navenddate") != null) {
					if (criteria.getEdate() == null) {
						eDate = myDateFormat.parse(super.getServletRequest()
								.getParameter("navenddate"));
					}
				}
				criteria.setSdate(sDate);
				criteria.setEdate(eDate);
			} catch (Exception e) {
				log.error(e);
			}

			this.setSize(ordersize);
			super.setPageStartNumber();

			criteria.setQuantity(ordersize);
			criteria.setStartindex(super.getPageStartIndex());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			SearchOrderResponse response = oservice.searchOrders(criteria);

			orders = response.getOrders();

			super.setListingCount(response.getCount());
			super.setRealCount(response.getOrders().size());
			super.setPageElements();

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;

	}

	public String displayOrderList() throws Exception {

		super.getServletRequest().getSession().removeAttribute("ITEMCOUNT");
		return this.getOrdersList();

	}

	public String displayOrderListPage() throws Exception {
		return this.getOrdersList();
	}

	public String getCardtext() {
		return cardtext;
	}

	public void setCardtext(String cardtext) {
		this.cardtext = cardtext;
	}

	public Collection getOrders() {
		return orders;
	}

	public SearchOrdersCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SearchOrdersCriteria criteria) {
		this.criteria = criteria;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}

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
package com.salesmanager.central.invoice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.PageBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LocaleUtil;

public class InvoiceListAction extends PageBaseAction implements Preparable {

	private Logger log = Logger.getLogger(InvoiceListAction.class);

	private static Configuration config = PropertiesHelper.getConfiguration();
	private Collection invoices;

	private SearchOrdersCriteria criteria;
	private String invoiceId;

	private static int invoicesize = 20;

	static {

		invoicesize = config.getInt("central.invoicelist.maxsize", 20);

	}

	public String resetInvoiceList() {
		this.criteria = null;
		super.getServletRequest().removeAttribute("sdate");
		super.getServletRequest().removeAttribute("edate");
		return displayInvoiceList();

	}

	public String displayInvoiceList() {

		// for page navigation
		String sstartindex = super.getServletRequest().getParameter(
				"startindex");

		try {

			DateUtil dh = new DateUtil();
			dh.processPostedDates(super.getServletRequest());

			if (criteria == null) {
				criteria = new SearchOrdersCriteria();
			}

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

			if (!StringUtils.isBlank(this.getInvoiceId())) {

				try {
					long invId = Long.parseLong(this.getInvoiceId());
					criteria.setOrderId(invId);
				} catch (Exception e) {
					log.error("Cannot parse invoiceId " + this.getInvoiceId());
				}

			}

			int startindex = 0;
			if (sstartindex != null) {
				try {
					startindex = Integer.parseInt(sstartindex);
				} catch (Exception e) {
					log
							.error("Did not received the index for page iterator, will reset to 0");
				}
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();


			this.setSize(invoicesize);
			this.getCriteria().setQuantity(invoicesize);
			this.getCriteria().setMerchantId(ctx.getMerchantid());
			this.getCriteria().setStartindex(this.getPageStartIndex());
			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			super.setPageStartNumber();
			SearchOrderResponse resp = oservice.searchInvoices(this
					.getCriteria());

			if (resp != null) {
				invoices = resp.getOrders();
				super.setListingCount(resp.getCount());
				super.setRealCount(resp.getOrders().size());
				super.setPageElements();
			}

			LocaleUtil.setLocaleToEntityCollection(invoices, super.getLocale());

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public Collection getInvoices() {
		return invoices;
	}

	public void setInvoices(Collection invoices) {
		this.invoices = invoices;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public SearchOrdersCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SearchOrdersCriteria criteria) {
		this.criteria = criteria;
	}

	public void prepare() throws Exception {
		super.setPageTitle("label.invoice.invoicelist.title");
		
	}

}

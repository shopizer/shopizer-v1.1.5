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
package com.salesmanager.core.util.www.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.module.model.application.ProductFileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.SpringUtil;

public class OrderProductDownloadTag extends TagSupport {

	private OrderProductDownload productDownload;

	private static Logger log = Logger.getLogger(OrderProductDownloadTag.class);

	public int doStartTag() throws JspException {
		try {

			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			if (this.getProductDownload() != null) {

				// print product url
				ProductFileModule dfm = (ProductFileModule) SpringUtil
						.getBean("productfile");
				if (dfm == null) {
					log.error("no module defines for bean productfile");
				} else {

					OrderService oservice = (OrderService) ServiceFactory
							.getService(ServiceFactory.OrderService);
					Order o = oservice.getOrder(this.getProductDownload()
							.getOrderId());

					String url = dfm.getFileUrl(o.getMerchantId(), this
							.getProductDownload().getOrderProductDownloadId());

					if (url != null) {
						pageContext.getOut().print(url);
					}

				}

			}

		} catch (Exception ex) {
			throw new JspTagException("LabelTag: " + ex.getMessage());
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public OrderProductDownload getProductDownload() {
		return productDownload;
	}

	public void setProductDownload(OrderProductDownload productDownload) {
		this.productDownload = productDownload;
	}

}

/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.checkout.files;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.CoreException;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.module.model.application.DownloadFileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderException;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class FilesAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(FilesAction.class);

	private Collection downloadFiles;

	private InputStream inputStream;
	private String fileName;

	private String fileMessage;

	private String orderId = null;

	private Order order = null;

	public InputStream getInputStream() {
		return inputStream;
	}

	public String accessUrl() {

		try {

			// parse file download request
			String fileId = getServletRequest().getParameter("fileId");
			String lang = getServletRequest().getParameter("lang");

			if (StringUtils.isBlank(fileId)) {
				List msg = new ArrayList();
				msg.add(getText("error.downloadurl.invalid"));
				super.setActionErrors(msg);
				return "GENERICERROR";
			}

			Map fileInfo = FileUtil.getUrlTokens(fileId);

			String order = (String) fileInfo.get("ID");
			String date = (String) fileInfo.get("DATE");

			orderId = order;

			// Compare the date
			Date today = new Date();
			DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = null;

			dt = d.parse(date);

			if (dt.before(new Date(today.getTime()))) {
				// expired
				CoreException excpt = new CoreException(
						ErrorConstants.DELAY_EXPIRED);
				throw excpt;
			}

		} catch (Exception e) {

			if (e instanceof CoreException) {
				String message = getText("error.downloadurl.expired");
				this.setFileMessage(message);
				return "DELAYEXPIRED";
			} else {
				log.error(e);
				super.setTechnicalMessage();
				return "GENERICERROR";
			}

		}

		return SUCCESS;

	}

	public String viewFiles() {

		try {

			if (this.getOrderId() == null) {
				super.setTechnicalMessage();
				return "GENERICERROR";
			}

			// need MerchantStore, create a Locale

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			order = oservice.getOrder(Long.parseLong(this.getOrderId()));

			Set products = order.getOrderProducts();
			Iterator i = products.iterator();
			Map productsMap = new HashMap();
			List opList = new ArrayList();
			while (i.hasNext()) {
				OrderProduct op = (OrderProduct) i.next();
				productsMap.put(op.getOrderProductId(), op);
				opList.add(op);
			}

			// MerchantStore
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(order
					.getMerchantId());
			SessionUtil.setMerchantStore(store, getServletRequest());
			getServletRequest().setAttribute("MERCHANT_STORE", store);

			Set st1 = order.getOrderProducts();

			if (st1 != null && st1.size() > 0) {
				Iterator opit = st1.iterator();
				while (opit.hasNext()) {
					OrderProduct op = (OrderProduct) opit.next();

					if (op.getDownloads() != null
							&& op.getDownloads().size() > 0) {
						// check if download expired or downloadcount==0

						Set opdSet = op.getDownloads();

						downloadFiles = opdSet;

						if (downloadFiles != null && downloadFiles.size() > 0) {
							Iterator dfIterator = downloadFiles.iterator();
							while (dfIterator.hasNext()) {
								OrderProductDownload opd = (OrderProductDownload) dfIterator
										.next();
								OrderProduct opp = (OrderProduct) productsMap
										.get(opd.getOrderProductId());
								if (opp != null) {
									opd.setProductName(opp.getProductName());
								} else {
									opd.setProductName(opd
											.getOrderProductFilename());
								}
							}
						}

					}

				}
			}

		} catch (Exception e) {
			if (e instanceof CoreException) {
				String message = getText("error.downloadurl.expired");
				this.setFileMessage(message);
				return "DELAYEXPIRED";
			} else {
				log.error(e);
				super.setTechnicalMessage();
				return "GENERICERROR";
			}
		}

		return SUCCESS;

	}

	public String getFile() {

		try {

			String mod = getServletRequest().getParameter("mod");
			String fileid = getServletRequest().getParameter("fileId");
			if (fileid == null) {
				log.error("fileId is null !!!");
			} else {

				DownloadFileModule module = (DownloadFileModule) SpringUtil
						.getBean(mod);

				if (mod == null) {
					log.error("Mod is null from the URL");
					super.setTechnicalMessage();
					return "GENERICERROR";
				}

				inputStream = module.getFileInputStream(getServletRequest());
				fileName = module.getFileName();

			}

		} catch (Exception e) {
			if (e instanceof OrderException) {
				String message = getText("label.order.download.maximumdownloadreached");
				this.setFileMessage(message);
				return "MAXIMUMDOWNLOADREACHED";
			} else {
				log.error(e);
				super.setTechnicalMessage();
				return "GENERICERROR";
			}
		}

		return SUCCESS;

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileMessage() {
		return fileMessage;
	}

	public void setFileMessage(String fileMessage) {
		this.fileMessage = fileMessage;
	}

	public Collection getDownloadFiles() {
		return downloadFiles;
	}

	public void setDownloadFiles(Collection downloadFiles) {
		this.downloadFiles = downloadFiles;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}

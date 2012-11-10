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
package com.salesmanager.core.entity.orders;

import java.io.Serializable;

/**
 * This is an object that contains data related to the orders_products_download
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="orders_products_download"
 */

public class OrderProductDownload implements Serializable {

	public static String REF = "OrderProductDownload";
	public static String PROP_ORDER_PRODUCT_DOWNLOAD_ID = "orderProductDownloadId";
	public static String PROP_ORDER_PRODUCT_FILENAME = "orderProductFilename";
	public static String PROP_ORDER_PRODUCT_ID = "orderProductId";
	public static String PROP_DOWNLOAD_MAXDAYS = "downloadMaxdays";
	public static String PROP_DOWNLOAD_COUNT = "downloadCount";
	public static String PROP_ORDER_ID = "orderId";

	// constructors
	public OrderProductDownload() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderProductDownload(long orderProductsDownloadId) {
		this.setOrderProductDownloadId(orderProductsDownloadId);
		initialize();
	}

	protected void initialize() {
	}

	// primary key
	private long orderProductDownloadId;

	// fields
	private long orderId;
	private long orderProductId;
	private java.lang.String orderProductFilename;
	private int downloadMaxdays;
	private int downloadCount;
	private long fileId;// productAttribteId

	private String productName;// transiant name

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned"
	 *               column="orders_products_download_id"
	 */
	public long getOrderProductDownloadId() {
		return orderProductDownloadId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param orderProductsDownloadId
	 *            the new ID
	 */
	public void setOrderProductDownloadId(long orderProductDownloadId) {
		this.orderProductDownloadId = orderProductDownloadId;

	}

	/**
	 * Return the value associated with the column: orders_id
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * Set the value related to the column: orders_id
	 * 
	 * @param orderId
	 *            the orders_id value
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	/**
	 * Return the value associated with the column: orders_products_id
	 */
	public long getOrderProductId() {
		return orderProductId;
	}

	/**
	 * Set the value related to the column: orders_products_id
	 * 
	 * @param orderProductsId
	 *            the orders_products_id value
	 */
	public void setOrderProductId(long orderProductId) {
		this.orderProductId = orderProductId;
	}

	/**
	 * Return the value associated with the column: orders_products_filename
	 */
	public java.lang.String getOrderProductFilename() {
		return orderProductFilename;
	}

	/**
	 * Set the value related to the column: orders_products_filename
	 * 
	 * @param orderProductsFilename
	 *            the orders_products_filename value
	 */
	public void setOrderProductFilename(java.lang.String orderProductFilename) {
		this.orderProductFilename = orderProductFilename;
	}

	/**
	 * Return the value associated with the column: download_maxdays
	 */
	public int getDownloadMaxdays() {
		return downloadMaxdays;
	}

	/**
	 * Set the value related to the column: download_maxdays
	 * 
	 * @param downloadMaxdays
	 *            the download_maxdays value
	 */
	public void setDownloadMaxdays(int downloadMaxdays) {
		this.downloadMaxdays = downloadMaxdays;
	}

	/**
	 * Return the value associated with the column: download_count
	 */
	public int getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Set the value related to the column: download_count
	 * 
	 * @param downloadCount
	 *            the download_count value
	 */
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + downloadCount;
		result = PRIME * result + downloadMaxdays;
		result = PRIME * result + (int) (fileId ^ (fileId >>> 32));
		result = PRIME * result + (int) (orderId ^ (orderId >>> 32));
		result = PRIME
				* result
				+ (int) (orderProductDownloadId ^ (orderProductDownloadId >>> 32));
		result = PRIME
				* result
				+ ((orderProductFilename == null) ? 0 : orderProductFilename
						.hashCode());
		result = PRIME * result
				+ (int) (orderProductId ^ (orderProductId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final OrderProductDownload other = (OrderProductDownload) obj;
		if (downloadCount != other.downloadCount)
			return false;
		if (downloadMaxdays != other.downloadMaxdays)
			return false;
		if (fileId != other.fileId)
			return false;
		if (orderId != other.orderId)
			return false;
		if (orderProductDownloadId != other.orderProductDownloadId)
			return false;
		if (orderProductFilename == null) {
			if (other.orderProductFilename != null)
				return false;
		} else if (!orderProductFilename.equals(other.orderProductFilename))
			return false;
		if (orderProductId != other.orderProductId)
			return false;
		return true;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
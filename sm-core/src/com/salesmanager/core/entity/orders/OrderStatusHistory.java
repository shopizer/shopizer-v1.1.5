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

import com.salesmanager.core.util.DateUtil;

/**
 * This is an object that contains data related to the orders_status_history
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="orders_status_history"
 */

public class OrderStatusHistory implements Serializable {

	public static String REF = "OrderStatusHistory";
	public static String PROP_CUSTOMER_NOTIFIED = "customerNotified";
	public static String PROP_COMMENTS = "comments";
	public static String PROP_ORDER_STATUS_ID = "orderStatusId";
	public static String PROP_ORDER_STATUS_HISTORY_ID = "orderStatusHistoryId";
	public static String PROP_DATE_ADDED = "dateAdded";
	public static String PROP_ORDER_ID = "orderId";

	// constructors
	public OrderStatusHistory() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderStatusHistory(long orderStatusHistoryId) {
		this.setOrderStatusHistoryId(orderStatusHistoryId);
		initialize();
	}

	protected void initialize() {
		dateAdded = DateUtil.getDate();
		comments = "";
		customerNotified = 0;

	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private long orderStatusHistoryId;

	// fields
	private long orderId;
	private int orderStatusId;
	private java.util.Date dateAdded;
	private java.lang.Integer customerNotified;
	private java.lang.String comments;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned"
	 *               column="orders_status_history_id"
	 */
	public long getOrderStatusHistoryId() {
		return orderStatusHistoryId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param orderStatusHistoryId
	 *            the new ID
	 */
	public void setOrderStatusHistoryId(long orderStatusHistoryId) {
		this.orderStatusHistoryId = orderStatusHistoryId;
		this.hashCode = Integer.MIN_VALUE;
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
	 * Return the value associated with the column: orders_status_id
	 */
	public int getOrderStatusId() {
		return orderStatusId;
	}

	/**
	 * Set the value related to the column: orders_status_id
	 * 
	 * @param orderStatusId
	 *            the orders_status_id value
	 */
	public void setOrderStatusId(int orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	/**
	 * Return the value associated with the column: date_added
	 */
	public java.util.Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Set the value related to the column: date_added
	 * 
	 * @param dateAdded
	 *            the date_added value
	 */
	public void setDateAdded(java.util.Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Return the value associated with the column: customer_notified
	 */
	public java.lang.Integer getCustomerNotified() {
		return customerNotified;
	}

	/**
	 * Set the value related to the column: customer_notified
	 * 
	 * @param customerNotified
	 *            the customer_notified value
	 */
	public void setCustomerNotified(java.lang.Integer customerNotified) {
		this.customerNotified = customerNotified;
	}

	/**
	 * Return the value associated with the column: comments
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Set the value related to the column: comments
	 * 
	 * @param comments
	 *            the comments value
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.orders.OrderStatusHistory))
			return false;
		else {
			com.salesmanager.core.entity.orders.OrderStatusHistory orderStatusHistory = (com.salesmanager.core.entity.orders.OrderStatusHistory) obj;
			return (this.getOrderStatusHistoryId() == orderStatusHistory
					.getOrderStatusHistoryId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getOrderStatusHistoryId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}
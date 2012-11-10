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
 * This is an object that contains data related to the orders_status table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="orders_status"
 */

public class OrderStatus implements Serializable {

	public static String REF = "OrderStatus";
	public static String PROP_ORDER_STATUS_NAME = "orderStatusName";
	public static String PROP_ID = "id";

	// constructors
	public OrderStatus() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderStatus(com.salesmanager.core.entity.orders.OrderStatusId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private com.salesmanager.core.entity.orders.OrderStatusId id;

	// fields
	private java.lang.String orderStatusName;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public com.salesmanager.core.entity.orders.OrderStatusId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(com.salesmanager.core.entity.orders.OrderStatusId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: orders_status_name
	 */
	public java.lang.String getOrderStatusName() {
		return orderStatusName;
	}

	/**
	 * Set the value related to the column: orders_status_name
	 * 
	 * @param orderStatusName
	 *            the orders_status_name value
	 */
	public void setOrderStatusName(java.lang.String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.orders.OrderStatus))
			return false;
		else {
			com.salesmanager.core.entity.orders.OrderStatus orderStatus = (com.salesmanager.core.entity.orders.OrderStatus) obj;
			if (null == this.getId() || null == orderStatus.getId())
				return false;
			else
				return (this.getId().equals(orderStatus.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}
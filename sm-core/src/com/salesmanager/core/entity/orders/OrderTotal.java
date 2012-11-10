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
 * This is an object that contains data related to the orders_total table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="orders_total"
 */

public class OrderTotal implements Serializable {

	public static String REF = "OrderTotal";
	public static String PROP_VALUE = "value";
	public static String PROP_ORDER_TOTAL_ID = "orderTotalId";
	public static String PROP_MODULE_ = "module";
	public static String PROP_TEXT = "text";
	public static String PROP_TITLE = "title";
	public static String PROP_ORDER_ID = "orderId";
	public static String PROP_SORT_ORDER = "sortOrder";

	// constructors
	public OrderTotal() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public OrderTotal(long orderTotalId) {
		this.setOrderTotalId(orderTotalId);
		initialize();
	}

	protected void initialize() {
	}

	// primary key
	private long orderTotalId;

	// fields
	private long orderId;
	private java.lang.String title;
	private java.lang.String text;
	private java.math.BigDecimal value;
	private java.lang.String module;
	private int sortOrder;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="orders_total_id"
	 */
	public long getOrderTotalId() {
		return orderTotalId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param orderTotalId
	 *            the new ID
	 */
	public void setOrderTotalId(long orderTotalId) {
		this.orderTotalId = orderTotalId;
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
	 * Return the value associated with the column: title
	 */
	public java.lang.String getTitle() {
		return title;
	}

	/**
	 * Set the value related to the column: title
	 * 
	 * @param title
	 *            the title value
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * Return the value associated with the column: text
	 */
	public java.lang.String getText() {
		return text;
	}

	/**
	 * Set the value related to the column: text
	 * 
	 * @param text
	 *            the text value
	 */
	public void setText(java.lang.String text) {
		this.text = text;
	}

	/**
	 * Return the value associated with the column: value
	 */
	public java.math.BigDecimal getValue() {
		return value;
	}

	/**
	 * Set the value related to the column: value
	 * 
	 * @param value
	 *            the value value
	 */
	public void setValue(java.math.BigDecimal value) {
		this.value = value;
	}

	/**
	 * Return the value associated with the column: class
	 */
	public java.lang.String getModule() {
		return module;
	}

	/**
	 * Set the value related to the column: class
	 * 
	 * @param class_
	 *            the class value
	 */
	public void setModule(java.lang.String module) {
		this.module = module;
	}

	/**
	 * Return the value associated with the column: sort_order
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * Set the value related to the column: sort_order
	 * 
	 * @param sortOrder
	 *            the sort_order value
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((module == null) ? 0 : module.hashCode());
		result = PRIME * result + (int) (orderId ^ (orderId >>> 32));
		result = PRIME * result + (int) (orderTotalId ^ (orderTotalId >>> 32));
		result = PRIME * result + sortOrder;
		result = PRIME * result + ((text == null) ? 0 : text.hashCode());
		result = PRIME * result + ((title == null) ? 0 : title.hashCode());
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
		final OrderTotal other = (OrderTotal) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (orderId != other.orderId)
			return false;
		if (orderTotalId != other.orderTotalId)
			return false;
		if (sortOrder != other.sortOrder)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
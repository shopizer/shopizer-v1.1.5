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
package com.salesmanager.core.entity.reference;

import java.io.Serializable;

/**
 * This is an object that contains data related to the central_credit_cards
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="central_credit_cards"
 */

public class CentralCreditCard implements Serializable {

	public static String REF = "CentralCreditCard";
	public static String PROP_LAST_MODIFIED = "lastModified";
	public static String PROP_CENTRAL_CREDIT_CARD_DESCRIPTION = "centralCreditCardDescription";
	public static String PROP_CENTRAL_CREDIT_CARD_ID = "centralCreditCardId";
	public static String PROP_CENTRAL_CREDIT_CARD_CODE = "centralCreditCardCode";
	public static String PROP_CENTRAL_CREDIT_CARD_VISIBLE = "centralCreditCardVisible";
	public static String PROP_CENTRAL_CREDIT_CARD_POSITION = "centralCreditCardPosition";

	// constructors
	public CentralCreditCard() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public CentralCreditCard(int centralCreditCardId) {
		this.setCentralCreditCardId(centralCreditCardId);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private int centralCreditCardId;

	// fields
	private java.lang.String centralCreditCardCode;
	private java.lang.String centralCreditCardDescription;
	private byte centralCreditCardPosition;
	private boolean centralCreditCardVisible;
	private java.util.Date lastModified;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="central_credit_cards_id"
	 */
	public int getCentralCreditCardId() {
		return centralCreditCardId;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param centralCreditCardId
	 *            the new ID
	 */
	public void setCentralCreditCardId(int centralCreditCardId) {
		this.centralCreditCardId = centralCreditCardId;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: central_credit_cards_code
	 */
	public java.lang.String getCentralCreditCardCode() {
		return centralCreditCardCode;
	}

	/**
	 * Set the value related to the column: central_credit_cards_code
	 * 
	 * @param centralCreditCardCode
	 *            the central_credit_cards_code value
	 */
	public void setCentralCreditCardCode(java.lang.String centralCreditCardCode) {
		this.centralCreditCardCode = centralCreditCardCode;
	}

	/**
	 * Return the value associated with the column:
	 * central_credit_cards_description
	 */
	public java.lang.String getCentralCreditCardDescription() {
		return centralCreditCardDescription;
	}

	/**
	 * Set the value related to the column: central_credit_cards_description
	 * 
	 * @param centralCreditCardDescription
	 *            the central_credit_cards_description value
	 */
	public void setCentralCreditCardDescription(
			java.lang.String centralCreditCardDescription) {
		this.centralCreditCardDescription = centralCreditCardDescription;
	}

	/**
	 * Return the value associated with the column:
	 * central_credit_cards_position
	 */
	public byte getCentralCreditCardPosition() {
		return centralCreditCardPosition;
	}

	/**
	 * Set the value related to the column: central_credit_cards_position
	 * 
	 * @param centralCreditCardPosition
	 *            the central_credit_cards_position value
	 */
	public void setCentralCreditCardPosition(byte centralCreditCardPosition) {
		this.centralCreditCardPosition = centralCreditCardPosition;
	}

	/**
	 * Return the value associated with the column: central_credit_cards_visible
	 */
	public boolean isCentralCreditCardVisible() {
		return centralCreditCardVisible;
	}

	/**
	 * Set the value related to the column: central_credit_cards_visible
	 * 
	 * @param centralCreditCardVisible
	 *            the central_credit_cards_visible value
	 */
	public void setCentralCreditCardVisible(boolean centralCreditCardVisible) {
		this.centralCreditCardVisible = centralCreditCardVisible;
	}

	/**
	 * Return the value associated with the column: last_modified
	 */
	public java.util.Date getLastModified() {
		return lastModified;
	}

	/**
	 * Set the value related to the column: last_modified
	 * 
	 * @param lastModified
	 *            the last_modified value
	 */
	public void setLastModified(java.util.Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.salesmanager.core.entity.reference.CentralCreditCard))
			return false;
		else {
			com.salesmanager.core.entity.reference.CentralCreditCard centralCreditCard = (com.salesmanager.core.entity.reference.CentralCreditCard) obj;
			return (this.getCentralCreditCardId() == centralCreditCard
					.getCentralCreditCardId());
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			return (int) this.getCentralCreditCardId();
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.shipping.ShippingInformation;

/**
 * Object used in the shopping cart page
 * 
 * @author Administrator
 * 
 */
public class OrderTotalSummary implements java.io.Serializable {

	private boolean validationError;
	private boolean shipping;// is there any shipping applicable
	private String errorMessage;// for usage in the shopping cart / invoice
	private String currency = null;

	private OrderProduct[] orderProducts;// product lines

	private ShippingInformation shippingLine;

	private Collection<OrderTotalLine> recursiveAmounts;
	private Collection<OrderTotalLine> otherDueNowAmounts;
	//private Collection<OrderTotalLine> orderTotalAmounts;
	private Collection<OrderTotalLine> taxAmounts;
	private Collection<OrderTotalLine> dueNowCredits;
	private Collection<OrderTotalLine> recursiveCredits;

	private BigDecimal oneTimeSubTotal = null;// due now sub total
	private BigDecimal recursiveSubTotal = null;// upcoming recursive subtotals
	private BigDecimal taxTotal = null;
	private BigDecimal total = null;
	private BigDecimal shippingTotal = null;// contains shipping and handling

	/** used in invoice / shopping cart for display with currency **/
	private String oneTimeSubTotalText = null;
	private String recursiveSubTotalText = null;
	private String totalText = null;
	private String shippingTotalText = null;

	public OrderTotalSummary(String currency) {
		recursiveSubTotal = new BigDecimal("0");
		//recursiveSubTotal.setScale(2, BigDecimal.ROUND_DOWN);
		oneTimeSubTotal = new BigDecimal("0");
		//oneTimeSubTotal.setScale(2, BigDecimal.ROUND_DOWN);
		total = new BigDecimal("0");
		//total.setScale(2, BigDecimal.ROUND_DOWN);
		shippingTotal = new BigDecimal("0");
		//shippingTotal.setScale(2, BigDecimal.ROUND_DOWN);
		taxTotal = new BigDecimal("0");
		//taxTotal.setScale(2, BigDecimal.ROUND_DOWN);
		this.currency = currency;
	}

	public OrderProduct[] getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(OrderProduct[] orderProducts) {
		this.orderProducts = orderProducts;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isValidationError() {
		return validationError;
	}

	public void setValidationError(boolean validationError) {
		this.validationError = validationError;
	}

	public BigDecimal getOneTimeSubTotal() {
		return oneTimeSubTotal;
	}

	public void setOneTimeSubTotal(BigDecimal oneTimeSubTotal) {
		this.oneTimeSubTotal = oneTimeSubTotal;
	}

	public BigDecimal getRecursiveSubTotal() {
		return recursiveSubTotal;
	}

	public void setRecursiveSubTotal(BigDecimal recursiveSubTotal) {
		this.recursiveSubTotal = recursiveSubTotal;
	}

	public OrderTotalLine[] getRecursiveAmounts() {
		if (recursiveAmounts != null) {
			OrderTotalLine[] sArray = new OrderTotalLine[recursiveAmounts
					.size()];
			OrderTotalLine[] o = (OrderTotalLine[]) recursiveAmounts
					.toArray(sArray);
			return o;
		} else {
			return null;
		}
	}

	public void addRecursivePrice(OrderTotalLine recursivePriceLine) {
		if (recursiveAmounts == null) {
			recursiveAmounts = new ArrayList();
		}
		recursiveAmounts.add(recursivePriceLine);
	}

	public OrderTotalLine[] getOtherDueNowAmounts() {
		if (otherDueNowAmounts != null) {
			OrderTotalLine[] sArray = new OrderTotalLine[otherDueNowAmounts
					.size()];
			OrderTotalLine[] o = (OrderTotalLine[]) otherDueNowAmounts
					.toArray(sArray);
			return o;
		} else {
			return null;
		}
	}

	public void addOtherDueNowPrice(OrderTotalLine oneTimePriceLine) {
		if (otherDueNowAmounts == null) {
			otherDueNowAmounts = new ArrayList();
		}
		otherDueNowAmounts.add(oneTimePriceLine);
	}

/*	public OrderTotalLine[] getOrderTotalAmounts() {
		if (orderTotalAmounts != null) {
			OrderTotalLine[] sArray = new OrderTotalLine[orderTotalAmounts
					.size()];
			OrderTotalLine[] o = (OrderTotalLine[]) orderTotalAmounts
					.toArray(sArray);
			return o;
		} else {
			return null;
		}
	}*/

	//public void addOrderTotalPrice(OrderTotalLine orderTotalLine) {
	//	if (orderTotalAmounts == null) {
	//		orderTotalAmounts = new ArrayList();
	//	}
	//	orderTotalAmounts.add(orderTotalLine);
	//}

	public OrderTotalLine[] getTaxAmounts() {
		if (taxAmounts != null) {
			OrderTotalLine[] sArray = new OrderTotalLine[taxAmounts.size()];
			OrderTotalLine[] o = (OrderTotalLine[]) taxAmounts
					.toArray(sArray);
			return o;
		} else {
			return null;
		}
	}

	public void addTaxPrice(OrderTotalLine taxLine) {
		if (taxAmounts == null) {
			taxAmounts = new ArrayList();
		}
		taxAmounts.add(taxLine);
	}

	public String getOneTimeSubTotalText() {
		return oneTimeSubTotalText;
	}

	public void setOneTimeSubTotalText(String oneTimeSubTotalText) {
		this.oneTimeSubTotalText = oneTimeSubTotalText;
	}

	public String getRecursiveSubTotalText() {
		return recursiveSubTotalText;
	}

	public void setRecursiveSubTotalText(String recursiveSubTotalText) {
		this.recursiveSubTotalText = recursiveSubTotalText;
	}

	public BigDecimal getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getTotalText() {
		StringBuffer totalTextBuffer = new StringBuffer();
		totalTextBuffer.append(totalText);
		if(!StringUtils.isBlank(this.getCurrency())) {
			totalTextBuffer.append(" ").append(this.getCurrency());
		}
		return totalTextBuffer.toString();
	}

	public void setTotalText(String totalText) {
		this.totalText = totalText;
	}

	public boolean isShipping() {
		return shipping;
	}

	public void setShipping(boolean shipping) {
		this.shipping = shipping;
	}

	public ShippingInformation getShippingLine() {
		return shippingLine;
	}

	public void setShippingLine(ShippingInformation shippingLine) {
		this.shippingLine = shippingLine;
	}

	public BigDecimal getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(BigDecimal shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public String getShippingTotalText() {
		return shippingTotalText;
	}

	public void setShippingTotalText(String shippingTotalText) {
		this.shippingTotalText = shippingTotalText;
	}

	public Collection<OrderTotalLine> getDueNowCredits() {
		return dueNowCredits;
	}

	public void addDueNowCredits(OrderTotalLine dueNowCredit) {
		if (dueNowCredits == null) {
			dueNowCredits = new ArrayList();
		}
		dueNowCredits.add(dueNowCredit);
	}

	public Collection<OrderTotalLine> getRecursiveCredits() {
		return recursiveCredits;
	}

	public void addRecursiveCredits(OrderTotalLine recursiveCredit) {
		if (recursiveCredits == null) {
			recursiveCredits = new ArrayList();
		}
		recursiveCredits.add(recursiveCredit);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}

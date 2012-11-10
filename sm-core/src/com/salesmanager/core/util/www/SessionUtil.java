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
package com.salesmanager.core.util.www;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.entity.orders.ShoppingCartProduct;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.shipping.ShippingInformation;

public class SessionUtil {

	public static final String MERCHANT_STORE_SESSION_ATTR = "STORE";
	public static final String ORDER_PRODUCT_LIST_SESSION_ATTR = "ORDER_PRODUCT_LIST";

	private static Logger log = Logger.getLogger(SessionUtil.class);

	public static Map createSavedOrderProducts(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Map cartLines = new HashMap();
		session.setAttribute("ORDERPRODUCTS", cartLines);
		return cartLines;

	}

	/**
	 * Stores the shopping cart product in the HttpSession 1- Check if the
	 * product is already in the session using the lineId
	 * 
	 */
	public static void addOrderTotalLine(OrderProduct product, String lineId,
			HttpServletRequest request) throws Exception {

		// check if the product is already in the HttpSession

		HttpSession session = request.getSession();

		Map cartLines = (Map) session.getAttribute("ORDERPRODUCTS");

		if (cartLines == null) {
			cartLines = createSavedOrderProducts(request);
		}

		product.setLineId(Integer.parseInt(lineId));

		cartLines.put(lineId, product);

		/**
		 * Map opaMap = (Map)session.getAttribute("ORDERPODUCTATTRIBUTES");
		 * 
		 * if(opaMap==null) { opaMap = new HashMap();
		 * session.setAttribute("ORDERPODUCTATTRIBUTES", opaMap);
		 * 
		 * }
		 **/

	}

	public static void resetCart(HttpServletRequest request) throws Exception {

		// request.getSession().removeAttribute("ORDER");//and payment method
		// are not removed
		// request.getSession().removeAttribute("CUSTOMER");
		request.getSession().removeAttribute("ORDERPRODUCTS");
		request.getSession().removeAttribute("ORDERPODUCTATTRIBUTES");
		request.getSession().removeAttribute("SHIPPINGMETHODS");
		request.getSession().removeAttribute("SHIPPINGINFORMATION");
		request.getSession().removeAttribute("TOTALS");
		request.getSession().removeAttribute("ORDER_PRODUCT_LIST");
		request.getSession().removeAttribute("CARTLINE");
		request.getSession().removeAttribute("TOTALS");
		request.getSession().removeAttribute("HASPAYMENT");
		request.getSession().removeAttribute("MERCHANT_STORE_SESSION_ATTR");
		request.getSession().removeAttribute("LOGGEDINCUSTOMER");
		request.getSession().removeAttribute("STATUSHISTORY");
		request.getSession().removeAttribute("CART");

	}

	public static void cleanCart(HttpServletRequest request) throws Exception {

		request.getSession().removeAttribute("ORDER");
		// request.getSession().removeAttribute("CUSTOMER");
		request.getSession().removeAttribute("ORDERPRODUCTS");
		request.getSession().removeAttribute("ORDERPODUCTATTRIBUTES");
		request.getSession().removeAttribute("SHIPPINGMETHODS");
		request.getSession().removeAttribute("SHIPPINGINFORMATION");
		request.getSession().removeAttribute("TOTALS");
		request.getSession().removeAttribute("ORDER_PRODUCT_LIST");
		request.getSession().removeAttribute("CARTLINE");
		request.getSession().removeAttribute("TOTALS");
		request.getSession().removeAttribute("PAYMENTMETHOD");
		request.getSession().removeAttribute("HASPAYMENT");
		request.getSession().removeAttribute("MERCHANT_STORE_SESSION_ATTR");
		request.getSession().removeAttribute("LOGGEDINCUSTOMER");
		request.getSession().removeAttribute("STATUSHISTORY");
		request.getSession().removeAttribute("COMITED");
		request.getSession().removeAttribute("TOKEN");

	}

	public static void setToken(HttpServletRequest request) {
		request.getSession().setAttribute("TOKEN", "TOKEN");
	}

	public static OrderProduct resetProduct(OrderProduct original,
			long productId, String lineId, HttpServletRequest request)
			throws Exception {

		HttpSession session = request.getSession();

		Map cartLines = (Map) session.getAttribute("ORDERPRODUCTS");

		if (cartLines == null) {
			throw new Exception(
					"No OrderProduct exixt yet, cannot assign attributes");
		}

		OrderProduct scp = (OrderProduct) cartLines.get(lineId);

		if (scp == null) {
			throw new Exception("No OrderProduct exixt for lineId " + lineId);
		}

		original.setProductQuantity(scp.getProductQuantity());

		cartLines.put(lineId, original);

		Map opaMap = (Map) session.getAttribute("ORDERPODUCTATTRIBUTES");

		if (opaMap != null) {
			opaMap.remove(lineId);
		}

		return original;

	}

	public static void setMiniShoppingCart(ShoppingCart cart,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("CART", cart);
	}

	public static ShoppingCart getMiniShoppingCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (ShoppingCart) session.getAttribute("CART");
	}
	
	public static void removeMiniShoppingCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("CART");
	}

	public static void removeOrderTotalLine(String lineId,
			HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();

		Map products = (Map) session.getAttribute("ORDERPRODUCTS");
		if (products != null) {
			products.remove(lineId);

		}

		ShoppingCart cart = SessionUtil.getMiniShoppingCart(request);
		if (cart != null) {

		}

		Map opaMap = (Map) session.getAttribute("ORDERPODUCTATTRIBUTES");

		if (opaMap != null) {
			opaMap.remove(lineId);
		}

	}

	public static void removeShippingInformation(HttpServletRequest request)
			throws Exception {

		HttpSession session = request.getSession();

		session.removeAttribute("SHIPPINGINFORMATION");
		session.removeAttribute("SHIPPINGMETHODS");

	}

	public static Map getOrderProducts(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Map) session.getAttribute("ORDERPRODUCTS");
	}

	public static Customer getCustomer(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Customer) session.getAttribute("CUSTOMER");
	}

	public static Map getOrderProductAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Map) session.getAttribute("ORDERPODUCTATTRIBUTES");
	}

	public static void setCustomer(Customer customer, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("CUSTOMER", customer);
	}

	public static Order getOrder(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Order) session.getAttribute("ORDER");
	}

	public static void setOrder(Order order, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("ORDER", order);
	}

	public static void setMerchantStore(MerchantStore store,
			HttpServletRequest request) {
		request.getSession().setAttribute(MERCHANT_STORE_SESSION_ATTR, store);
	}

	public static MerchantStore getMerchantStore(HttpServletRequest request) {
		return (MerchantStore) request.getSession().getAttribute(
				MERCHANT_STORE_SESSION_ATTR);
	}

	public static Map getShippingMethods(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Map) session.getAttribute("SHIPPINGMETHODS");
	}

	public static void setShippingMethods(Map shippingMethods,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("SHIPPINGMETHODS", shippingMethods);
	}

	public static ShippingInformation getShippingInformation(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (ShippingInformation) session
				.getAttribute("SHIPPINGINFORMATION");
	}

	public static void setShippingInformation(
			ShippingInformation shippingInformation, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("SHIPPINGINFORMATION", shippingInformation);
	}

	public static void setHasShipping(boolean shippingState,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("HASSHIPPING", new Boolean(shippingState));
	}

	public static boolean getIsShipping(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean hasShipping = (Boolean) session.getAttribute("HASSHIPPING");
		if (hasShipping != null) {
			return hasShipping.booleanValue();
		} else {
			return false;
		}
	}

	public static void addOrderProduct(OrderProduct product,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map orderProducts = getOrderProducts(request);
		String line = "0";
		if (orderProducts != null && orderProducts.size() > 0) {
			// get the current line
			String currentLine = (String) request.getSession().getAttribute(
					"CARTLINE");
			if (currentLine != null) {
				try {
					int iline = Integer.parseInt(currentLine);
					iline = iline + 1;
					line = String.valueOf(iline);
					request.getSession().setAttribute("CARTLINE", line);

				} catch (Exception e) {
					log.error("Cannot set cartline ", e);
				}
			}

		} else {
			orderProducts = new HashMap();
			int iline = Integer.parseInt(line);
			iline = iline + 1;
			line = String.valueOf(iline);
			request.getSession().setAttribute("CARTLINE", line);

		}

		ShoppingCart cart = SessionUtil.getMiniShoppingCart(request);
		if (cart != null) {
			Collection prds = cart.getProducts();
			if (prds != null) {
				Iterator iprd = prds.iterator();
				while (iprd.hasNext()) {
					ShoppingCartProduct scp = (ShoppingCartProduct) iprd.next();
					if (scp.getProductId() == product.getProductId()) {
						scp.setMainCartLine(line);
						break;
					}
				}
			}
		}

		product.setLineId(Integer.valueOf(line));
		orderProducts.put(line, product);
		session.setAttribute("ORDERPRODUCTS", orderProducts);

		request.getSession().setAttribute("CARTLINE", line);

	}

	public static void setOrderTotals(Collection totals,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("TOTALS", totals);
	}

	public static Collection getOrderTotals(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (Collection) session.getAttribute("TOTALS");
	}

	public static void setPaymentMethod(PaymentMethod method,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("PAYMENTMETHOD", method);
	}

	public static PaymentMethod getPaymentMethod(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (PaymentMethod) session.getAttribute("PAYMENTMETHOD");
	}

	public static void setHasPayment(boolean hasPayment,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("HASPAYMENT", hasPayment);
	}

	// public static void setLoggedInCustomer(HttpServletRequest
	// request,Customer customer) {
	// HttpSession session = request.getSession();
	// session.setAttribute("LOGGEDINCUSTOMER",customer);
	// }

	// public static Customer getLoggedInCustomer(HttpServletRequest request) {
	// HttpSession session = request.getSession();
	// return (Customer)session.getAttribute("LOGGEDINCUSTOMER");
	// }

	public static boolean isHasPayment(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean bPayment = (Boolean) session.getAttribute("HASPAYMENT");
		if (bPayment == null) {
			bPayment = true;
		}
		return bPayment;
	}

	public static void setOrderTotalSummary(OrderTotalSummary summary,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("ORDERSUMMARY", summary);
	}

	public static OrderTotalSummary getOrderTotalSummary(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (OrderTotalSummary) session.getAttribute("ORDERSUMMARY");
	}

	public static void setOrderStatusHistory(OrderStatusHistory history,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("STATUSHISTORY", history);
	}

	public static OrderStatusHistory getOrderStatusHistory(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (OrderStatusHistory) session.getAttribute("STATUSHISTORY");
	}

	public static void setComited(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("COMITED", "TRUE");
	}

	public static boolean isComited(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("COMITED") != null) {
			return true;
		} else {
			return false;
		}
	}

}
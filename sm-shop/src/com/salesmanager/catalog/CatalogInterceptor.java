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
package com.salesmanager.catalog;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.salesmanager.checkout.util.MiniShoppingCartUtil;
import com.salesmanager.common.ShopInterceptor;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MiniShoppingCartSerializationUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.StringUtil;
import com.salesmanager.core.util.www.BaseActionAware;
import com.salesmanager.core.util.www.SessionUtil;

/**
 * Information on the store, request parameters, Locale
 * 
 * @author Carl Samson
 * 
 */
public class CatalogInterceptor extends ShopInterceptor {

	private static Logger log = Logger.getLogger(CatalogInterceptor.class);

	@Override
	protected String doIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		/** remove profile url **/
		req.getSession().removeAttribute("profileUrl");
		
				
		
		
		/** synchronize mini shopping cart**/
		
		//get http session shopping cart
		ShoppingCart cart = SessionUtil.getMiniShoppingCart(req);
		MerchantStore mStore = SessionUtil.getMerchantStore(req);
		
		if(cart==null) {//synch only when the cart is null or empty
		
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if(cookie.getName().equals(CatalogConstants.SKU_COOKIE + mStore.getMerchantId())) {
						

							Locale locale = LocaleUtil.getLocale(req);
							
							String cookieValue = StringUtil.unescape(cookie.getValue());
							
							ShoppingCart sc = MiniShoppingCartSerializationUtil.deserializeJSON(cookieValue, mStore, locale);
							if(sc!=null) {
							
								MiniShoppingCartUtil.calculateTotal(sc,mStore);
								SessionUtil.setMiniShoppingCart(sc, req);
								
							} else {//expire cookie
								cookie.setValue(null);
								cookie.setMaxAge(0);
								resp.addCookie(cookie);
							}
					}
				}
			}
		
		}
		

		return null;

	}

}

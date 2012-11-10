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
import java.util.Collection;

public class ShoppingCart implements Serializable {

	private String errorMessage;

	private String total;

	private int quantity = 0;
	
	private String jsonShoppingCart;



	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	private Collection<ShoppingCartProduct> products;

	public Collection<ShoppingCartProduct> getProducts() {
		return products;
	}

	public void setProducts(Collection<ShoppingCartProduct> products) {
		this.products = products;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getJsonShoppingCart() {
		return jsonShoppingCart;
	}

	public void setJsonShoppingCart(String jsonShoppingCart) {
		this.jsonShoppingCart = jsonShoppingCart;
	}

}

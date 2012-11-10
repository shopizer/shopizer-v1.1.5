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
package com.salesmanager.core.service.catalog;

public class CatalogException extends Exception {

	private static final long serialVersionUID = 1L;
	private int reason = -1;

	public CatalogException(Throwable t) {
		super(t);
	}

	public CatalogException(String message, Throwable t) {
		super(message, t);
	}

	public CatalogException(String message, int reason) {
		this(message);
		this.reason = reason;
	}

	public CatalogException(String message) {
		super(message);
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}
}

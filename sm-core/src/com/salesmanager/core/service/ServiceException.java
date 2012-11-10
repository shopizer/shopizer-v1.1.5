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
package com.salesmanager.core.service;

public class ServiceException extends Exception {

	private int reason = -1;

	public ServiceException(Throwable t) {
		super(t);
	}

	public ServiceException(String message, Throwable t) {
		super(message, t);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, int reason) {
		super(message);
		this.reason = reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public int getReason() {
		return reason;
	}

}

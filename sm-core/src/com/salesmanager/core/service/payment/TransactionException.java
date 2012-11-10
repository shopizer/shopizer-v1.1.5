/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-3 Sep, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.payment;

public class TransactionException extends Exception{
	private String reason = "01";

	private String errorcode = "";

	private int errorType = 0;
	public TransactionException(String ex) {
		super(ex);
	}

	public TransactionException(Throwable t) {
		super(t);
	}

	public TransactionException(String ex,Throwable t) {
		super(ex,t);
	}

	public TransactionException(String message,String code) {
		super(message);
		this.errorcode = code;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}


}

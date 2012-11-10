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
package com.salesmanager.core.service.payment.impl;

public class PaymentProperties {

	private String properties1;
	private String properties2;
	private String properties3;
	private String properties4;

	public PaymentProperties() {
		this.properties1 = "1";// Production
		this.properties2 = "1";
		this.properties3 = "1";
		this.properties4 = "0";
	}

	public String getProperties1() {
		return properties1;
	}

	public void setProperties1(String properties1) {
		this.properties1 = properties1;
	}

	public String getProperties2() {
		return properties2;
	}

	public void setProperties2(String properties2) {
		this.properties2 = properties2;
	}

	public String getProperties3() {
		return properties3;
	}

	public void setProperties3(String properties3) {
		this.properties3 = properties3;
	}

	public String getProperties4() {
		return properties4;
	}

	public void setProperties4(String properties4) {
		this.properties4 = properties4;
	}

	public String toLine() {
		return new StringBuffer().append(properties1).append(";").append(
				properties2).append(";").append(properties3).append(";")
				.append(properties4).toString();
	}

}

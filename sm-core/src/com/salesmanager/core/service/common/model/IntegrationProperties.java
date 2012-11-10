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
package com.salesmanager.core.service.common.model;

import org.apache.commons.lang.StringUtils;

public class IntegrationProperties {

	private String properties1;
	private String properties2;
	private String properties3;
	private String properties4;
	private String properties5;

	public IntegrationProperties() {
		this.properties1 = "1";// Generally used for transaction type
		this.properties2 = "1";// generally used for environment
		this.properties3 = "1";
		this.properties4 = "0";
		this.properties5 = "0";
	}

	public String getProperties1() {
		return properties1;
	}

	public void setProperties1(String properties1) {
		if (!StringUtils.isBlank(properties1)) {
			properties1.trim();
		}
		this.properties1 = properties1;
	}

	public String getProperties2() {
		return properties2;
	}

	public void setProperties2(String properties2) {
		if (!StringUtils.isBlank(properties2)) {
			properties2.trim();
		}
		this.properties2 = properties2;
	}

	public String getProperties3() {
		return properties3;
	}

	public void setProperties3(String properties3) {
		if (!StringUtils.isBlank(properties3)) {
			properties3.trim();
		}
		this.properties3 = properties3;
	}

	public String getProperties4() {
		return properties4;
	}

	public void setProperties4(String properties4) {
		if (!StringUtils.isBlank(properties4)) {
			properties4.trim();
		}
		this.properties4 = properties4;
	}

	public String toLine() {
		return new StringBuffer().append(properties1).append(";").append(
				properties2).append(";").append(properties3).append(";")
				.append(properties4).toString();
	}

	public String getProperties5() {
		return properties5;
	}

	public void setProperties5(String properties5) {
		if (!StringUtils.isBlank(properties5)) {
			properties5.trim();
		}
		this.properties5 = properties5;
	}

}

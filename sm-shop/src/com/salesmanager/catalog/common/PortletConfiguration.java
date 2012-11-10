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

package com.salesmanager.catalog.common;

import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;

public class PortletConfiguration {

	private String moduleName;
	private boolean configurable = false;
	private boolean custom = false;
	private int position = LabelConstants.LABEL_POSITION_RIGHT;// default to
																// right
	private String content;

	private MerchantConfiguration merchantConfiguration;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isConfigurable() {
		return configurable;
	}

	public void setConfigurable(boolean configurable) {
		this.configurable = configurable;
	}

	public MerchantConfiguration getMerchantConfiguration() {
		return merchantConfiguration;
	}

	public void setMerchantConfiguration(
			MerchantConfiguration merchantConfiguration) {
		this.merchantConfiguration = merchantConfiguration;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

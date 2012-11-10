/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.catalog;

public class ProductOptionValueDisplay {

	private long productOptionValueId;
	private String productOptionValueName;

	private int optionType = -1;

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public long getProductOptionValueId() {
		return productOptionValueId;
	}

	public void setProductOptionValueId(long productOptionValueId) {
		this.productOptionValueId = productOptionValueId;
	}

	public String getProductOptionValueName() {
		return productOptionValueName;
	}

	public void setProductOptionValueName(String productOptionValueName) {
		this.productOptionValueName = productOptionValueName;
	}

}

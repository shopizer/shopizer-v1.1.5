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
package com.salesmanager.core.entity.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductOptionDescriptor implements Serializable {

	private String name;
	private long optionId;

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int optionType;

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public List getValues() {
		return values;
	}

	public void setValues(List values) {
		this.values = values;
	}

	public long getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(long defaultOption) {
		this.defaultOption = defaultOption;
	}

	private List values = new ArrayList();
	private long defaultOption;

	public void addValue(ProductAttribute pa) {
		values.add(pa);
	}

}

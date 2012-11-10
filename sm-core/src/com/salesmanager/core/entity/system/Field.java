/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Dec 7, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.entity.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Field implements Serializable {
	
	private String type;
	private String label;
	private String name;
	private String fieldValue;
	
	private final static String FIELD_TYPE_TEXT = "text";
	private final static String FIELD_TYPE_RADIO = "radio";
	private final static String FIELD_TYPE_SELECT = "select";
	private final static String FIELD_TYPE_CHECKBOX = "checkbox";
	
	private List<FieldOption> options = new ArrayList();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public List<FieldOption> getOptions() {
		return options;
	}

	public void setOptions(List<FieldOption> options) {
		this.options = options;
	}
	
	public void addFieldOption(FieldOption option) {
		this.options.add(option);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	

}

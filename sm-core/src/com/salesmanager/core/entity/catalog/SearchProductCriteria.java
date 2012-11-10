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

import java.util.List;

import com.salesmanager.core.entity.common.SearchCriteria;

public class SearchProductCriteria extends SearchCriteria {

	public final static int VISIBLEALL = 2;
	public final static int VISIBLETRUE = 1;
	public final static int VISIBLEFALSE = 0;

	public final static int STATUSALL = 2;
	public final static int STATUSINSTOCK = 1;
	public final static int STATUSOUTSTOCK = 0;

	// search criteria used by admin tool
	private long categoryid = -1;
	private int visible = VISIBLEALL;
	private String description = null;
	private int status = STATUSALL;

	// search criteria used by catalog
	private List categoryList;

	public List getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List categoryList) {
		this.categoryList = categoryList;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

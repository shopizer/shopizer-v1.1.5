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

package com.salesmanager.catalog.category;

import java.io.Serializable;
import java.util.Collection;

import com.salesmanager.core.entity.catalog.Category;

public class CategoryList implements Serializable {

	private static final long serialVersionUID = 5195446516455045249L;
	private Collection<Category> categories;
	private Collection categoryIds;

	public Collection<Category> getCategories() {
		return categories;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}

	public Collection getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Collection categoryIds) {
		this.categoryIds = categoryIds;
	}

}

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class ProductDescriptionIdPkBridge implements TwoWayFieldBridge {

	public Object get(String str, Document document) {
		ProductDescriptionId id = new ProductDescriptionId();
		Field languageId = document.getField(str + ".languageId");
		id.setLanguageId(Integer.parseInt(languageId.stringValue()));
		Field productId = document.getField(str + ".productId");
		id.setProductId(Long.parseLong(productId.stringValue()));
		return id;
	}

	public String objectToString(Object object) {
		ProductDescriptionId id = (ProductDescriptionId) object;
		StringBuilder sb = new StringBuilder();
		sb.append(id.getLanguageId()).append(" ").append(id.getProductId());
		return sb.toString();
	}

	public void set(String name, Object value, Document document,
			Field.Store store, Field.Index index, Float boost) {
		ProductDescriptionId id = (ProductDescriptionId) value;

		Field f = new Field(name, id.getLanguageId() + ".languageId", store,
				index);
		if (boost != null) {
			f.setBoost(boost);
		}
		document.add(f);

		f = new Field(name, id.getProductId() + ".productId", store, index);
		if (boost != null) {
			f.setBoost(boost);
		}
		document.add(f);

	}

}

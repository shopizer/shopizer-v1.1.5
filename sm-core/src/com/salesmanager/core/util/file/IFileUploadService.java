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
package com.salesmanager.core.util.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.salesmanager.core.service.catalog.CatalogException;

public interface IFileUploadService {

	public Map<Integer, List<String>> uploadCategory(File csvCategoryFile,
			Integer merchantId) throws CatalogException;

	public Map<Integer, List<String>> uploadManufacturers(
			File csvManufacturersFile);

	public Map<Integer, List<String>> uploadProducts(File csvProductsFile,
			Integer merchantId);
}

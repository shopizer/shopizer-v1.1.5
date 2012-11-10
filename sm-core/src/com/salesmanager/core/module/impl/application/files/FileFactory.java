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
package com.salesmanager.core.module.impl.application.files;

import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.module.model.application.ProductFileModule;

/**
 * Factory populated by Spring
 * 
 * @author Administrator
 * 
 */
public class FileFactory {

	private FileFactory() {
	}

	private static FileFactory FileFactory = new FileFactory();

	public static FileFactory createInstance() {
		return FileFactory;
	}

	private FileModule fileUtil;
	private ProductFileModule productFileUtil;

	public FileModule getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileModule localFileUtil) {
		this.fileUtil = localFileUtil;
	}

	public ProductFileModule getProductFileUtil() {
		return productFileUtil;
	}

	public void setProductFileUtil(ProductFileModule remoteFileUtil) {
		this.productFileUtil = remoteFileUtil;
	}

}

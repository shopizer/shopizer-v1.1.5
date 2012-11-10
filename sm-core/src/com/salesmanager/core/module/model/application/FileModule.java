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
package com.salesmanager.core.module.model.application;

import java.io.File;

import com.salesmanager.core.module.impl.application.files.FileException;

/**
 * Manage upload files
 * 
 * @author Carl Samson
 * 
 */
public interface FileModule {

	public boolean deleteFile(int merchantid, File file, String folder);

	public boolean deleteFile(int merchantid, File file);

	String copyFile(int merchantid, String config, File file, String fileName,
			String contentType) throws FileException;

	/**
	 * Returns the file absolute path
	 * 
	 * @param merchantid
	 * @param config
	 * @param file
	 * @param fileName
	 * @param contentType
	 * @return String (representing the uploaded file final absolute path
	 *         destination)
	 * @throws FileException
	 */
	public String uploadFile(int merchantid, String config, File file,
			String fileName, String contentType) throws FileException;

}

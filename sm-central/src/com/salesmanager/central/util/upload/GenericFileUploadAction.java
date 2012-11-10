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
package com.salesmanager.central.util.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogException;
import com.salesmanager.core.util.file.IFileUploadService;
import com.salesmanager.core.util.file.csv.CSVConstants;

public class GenericFileUploadAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(GenericFileUploadAction.class);
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String fileUploadType;

	public String uploadCategories() {
		try {
			if (isBlank()) {
				setMessage(getText("error.upload.required"));
				return INPUT;
			}
			if (!isValidFile()) {
				setMessage(CSVConstants.INVALID_FILE_EXCEPTION_MSG);
				return ERROR;
			}
			logger.info("The content type uploaded is:" + uploadContentType);
			Map<Integer, List<String>> errorMap = null;
			IFileUploadService uploadService = (IFileUploadService) ServiceFactory
					.getService("fileUpload");
			errorMap = uploadService.uploadCategory(getUpload(),
					(Integer) getServletRequest().getSession().getAttribute(
							ProfileConstants.merchant));
			if (!errorMap.isEmpty()) {
				processErrors(errorMap);
				return ERROR;
			}
		} catch (CatalogException e) {
			logger.error("Error occurred while uploading categories", e);
			setTechnicalMessage();
		}
		setSuccessMessage();
		return SUCCESS;
	}

	public void processErrors(Map<Integer, List<String>> errorMap) {
		List<String> errorList = new ArrayList<String>();
		for (Iterator<Integer> it = errorMap.keySet().iterator(); it.hasNext();) {
			Integer lineNo = it.next();
			errorList.add("Error at Line:  " + lineNo + " "
					+ errorMap.get(lineNo).toString());
		}
		addErrorMessages(errorList);
	}

	public boolean isBlank() {
		return (upload == null || uploadFileName == null);
	}

	public String uploadProducts() {
		if (isBlank()) {
			setMessage(getText("error.upload.required"));
			return INPUT;
		}
		if (!isValidFile()) {
			setMessage(CSVConstants.INVALID_FILE_EXCEPTION_MSG);
			return ERROR;
		}
		logger.info("The content type uploaded is:" + uploadContentType);
		Map<Integer, List<String>> errorMap = null;
		IFileUploadService uploadService = (IFileUploadService) ServiceFactory
				.getService("fileUpload");
		errorMap = uploadService.uploadProducts(getUpload(),
				(Integer) getServletRequest().getSession().getAttribute(
						ProfileConstants.merchant));
		if (!errorMap.isEmpty()) {
			processErrors(errorMap);
			return ERROR;
		}
		setSuccessMessage();
		return SUCCESS;
	}

	public String uploadManufacturers() {
		if (isBlank()) {
			setMessage(getText("error.upload.required"));
			return INPUT;
		}
		if (!isValidFile()) {
			setMessage(CSVConstants.INVALID_FILE_EXCEPTION_MSG);
			return ERROR;
		}
		logger.info("The content type uploaded is:" + uploadContentType);
		Map<Integer, List<String>> errorMap = null;
		IFileUploadService uploadService = (IFileUploadService) ServiceFactory
				.getService("fileUpload");
		errorMap = uploadService.uploadManufacturers(getUpload());
		if (!errorMap.isEmpty()) {
			processErrors(errorMap);
			return ERROR;
		}
		setSuccessMessage();
		return SUCCESS;
	}

	public boolean isValidFile() {
		if (!uploadFileName.endsWith(CSVConstants.CSV_SUFFIX)) {
			return false;
		}
		return true;
	}

	public String getFileUploadType() {
		return fileUploadType;
	}

	public void setFileUploadType(String fileUploadType) {
		this.fileUploadType = fileUploadType;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
}

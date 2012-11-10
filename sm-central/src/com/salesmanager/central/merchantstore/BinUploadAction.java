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
package com.salesmanager.central.merchantstore;

import java.io.File;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.module.impl.application.files.FileException;
import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.FileUtil.ContentCategoryType;

public class BinUploadAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(BinUploadAction.class);
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String fileUploadType;
	private String deleteFilePath;
	private String type;

	public String binTreeDisplay() {
		super.setPageTitle("label.media.binmanagement");
		return SUCCESS;
	}

	public String displayFileBrowser() {
		super.setPageTitle("label.media.binmanagement");
		return SUCCESS;

	}

	public String uploadToBin() {
		
		super.setPageTitle("label.media.binmanagement");

		super.getServletRequest().setAttribute("Type", this.getType());
		if (isBlank()) {
			setErrorMessage(getText("error.upload.required"));
			return INPUT;
		}
		Context ctx = super.getContext();
		Integer merchantid = ctx.getMerchantid();
		FileModule futil = (FileModule) SpringUtil.getBean("localfile");
		ContentCategoryType contentType = FileUtil
				.getContentCategoryType(uploadContentType);
		if (ContentCategoryType.IMAGE.equals(contentType)) {

			try {
				futil.copyFile(merchantid, "core.bin.images", getUpload(),
						getUploadFileName(), getUploadContentType());
			} catch (FileException e) {
				log.error(e);
				super.setTechnicalMessage();
				return INPUT;
			}
		} else if (ContentCategoryType.FLASH.equals(contentType)) {
			try {
				futil.copyFile(merchantid, "core.bin.flash", getUpload(),
						getUploadFileName(), getUploadContentType());
			} catch (Exception e) {
				log.error(e);
				super.setTechnicalMessage();
				return INPUT;
			}
		} else if (ContentCategoryType.FILE.equals(contentType)) {
			try {
				futil.copyFile(merchantid, "core.bin.files", getUpload(),
						getUploadFileName(), getUploadContentType());
			} catch (Exception e) {
				log.error(e);
				super.setTechnicalMessage();
				return INPUT;
			}
		} else {
			setErrorMessage(getText("error.bin.upload.invalid.type"));
			return INPUT;
		}
		super.setSuccessMessage();
		return SUCCESS;
	}

	public String deleteFile() {
		
		super.setPageTitle("label.media.binmanagement");
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();
		FileModule futil = (FileModule) SpringUtil.getBean("localfile");
		futil.deleteFile(merchantid, new File(deleteFilePath));
		super.setSuccessMessage();
		return SUCCESS;
	}

	public boolean isBlank() {
		return (upload == null || uploadFileName == null);
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

	public String getFileUploadType() {
		return fileUploadType;
	}

	public void setFileUploadType(String fileUploadType) {
		this.fileUploadType = fileUploadType;
	}

	public String getDeleteFilePath() {
		return deleteFilePath;
	}

	public void setDeleteFilePath(String deleteFilePath) {
		this.deleteFilePath = deleteFilePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

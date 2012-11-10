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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.CoreException;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.orders.FileHistory;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.module.model.application.DownloadFileModule;
import com.salesmanager.core.module.model.application.ProductFileModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderException;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.PropertiesUtil;

/**
 * Implementation for managing the creation and copy of files localy on the
 * server
 * 
 * @author Carl Samson
 * 
 */
public class LocalFileImpl extends CoreFileImpl implements ProductFileModule,
		DownloadFileModule {

	private static Configuration conf = PropertiesUtil.getConfiguration();

	private static Logger log = Logger.getLogger(LocalFileImpl.class);

	private long productid;

	private String fileName;

	public void setProductId(long productid) {
		this.productid = productid;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getFileInputStream(HttpServletRequest request)
			throws Exception {

		// parse token

		String fileid = request.getParameter("fileId");

		Map fileInfo = FileUtil.getFileDownloadFileTokens(fileid);

		String fileId = (String) fileInfo.get("ID");
		String date = (String) fileInfo.get("DATE");
		String merchantId = (String) fileInfo.get("MERCHANTID");

		// Compare the date
		Date today = new Date();
		DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;

		dt = d.parse(date);

		if (dt.before(new Date(today.getTime()))) {
			// expired

			CoreException excpt = new CoreException(
					ErrorConstants.DELAY_EXPIRED);

			throw excpt;
			// String lbl =
			// LabelUtil.getInstance().getText("message.error.download.delayexpired1");

		}

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		OrderProductDownload download = oservice
				.getOrderProductDownload(new Long(fileId));

		this.setFileName(download.getOrderProductFilename());

		int maxcount = conf.getInt("core.product.file.downloadmaxcount", 5);

		// String filename = "";

		FileHistory fh = oservice.getFileHistory(Integer.parseInt(merchantId),
				download.getFileId());

		if (fh == null) {
			log
					.warn("Trying to update non existing file history [attrid "
							+ download.getFileId() + "][merchantid "
							+ merchantId + "]");
		} else {
			int downloadcount = fh.getDownloadCount();
			int newcount = downloadcount + 1;
			fh.setDownloadCount(newcount);
			oservice.saveOrUpdateFileHistory(fh);
		}

		int downloadcount = download.getDownloadCount();
		if (downloadcount == maxcount) {
			OrderException oe = new OrderException("Maximum download reached",
					ErrorConstants.MAXIMUM_ORDER_PRODUCT_DOWNLOAD_REACHED);
			throw oe;

		}

		int newcount = downloadcount + 1;
		download.setDownloadCount(newcount);
		oservice.saveOrUpdateOrderProductDownload(download);

		//String downloadPath = conf.getString("core.product.file.filefolder");
		String downloadPath = FileUtil.getDownloadFilePath();

		File file = new File(downloadPath + "/" + merchantId + "/"
				+ download.getOrderProductFilename());

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));

		return bis;
	}

	public void handleResponse(HttpServletResponse response, InputStream fis)
			throws IOException {

	}

	public String getFileUrl(int merchantId, long downloadId)
			throws FileException {

		try {
			return FileUtil.getInternalDownloadFileUrl(merchantId, downloadId);
		} catch (Exception e) {
			log.error(e);
		}
		return "";

	}

	public boolean deleteFile(int merchantid, File file, String folder) {

		if (folder != null) {
			String newFile = folder + "/" + file.getName();
			file = new File(newFile);
		}
		return deleteFile(merchantid, file);
	}

	public boolean deleteFile(int merchantid, File file) {

		try {

			boolean direxist = file.exists();

			if (direxist) {
				if (file.isDirectory()) {
					String[] children = file.list();
					for (int i = 0; i < children.length; i++) {
						boolean success = deleteFile(merchantid, new File(file,
								children[i]));
						if (!success) {
							return false;
						}
					}
				}
			}

			// The directory is now empty so delete it
			boolean delete = true;
			try {
				file.delete();
			} catch (Exception e) {
				log.warn("File " + file.getName() + " does not exist");
			}
			return delete;

		} finally {

		}
	}

	public String copyFile(int merchantid, String config, File file,
			String fileName, String contentType) throws FileException {

		try {

			String filefolder = conf.getString(config + ".filefolder");
			if (filefolder == null) {
				throw new FileException(FileException.ERROR, "Properties "
						+ config + ".filefolder not defined");
			}

			// Check if merchant directory exist
			
			StringBuffer dirPath = new StringBuffer();
			dirPath.append(FileUtil.getMediaPath());
			dirPath.append(filefolder);
			dirPath.append("/");
			dirPath.append(String.valueOf(merchantid));
			String directory = dirPath.toString();
			
			//String directory = filefolder + "/" + String.valueOf(merchantid);
			String dir = conf.getString(config + ".dirname");

			String destinationdir = directory;

			if (dir != null) {
				destinationdir = directory + "/" + dir;
			}

			// do we need to clear
			String clear = conf.getString(config + ".cleanup", "false");

			if (clear.equals("true")) {
				// will delete the directory if it exist
				this.deleteFile(merchantid, new File(destinationdir));
			}

			// check if directory/merchantid exists
			boolean exists = (new File(directory)).exists();
			if (!exists) {
				// create the directory merchant id
				File merchantdir = new File(directory);
				boolean successcreate = merchantdir.mkdir();
				if (!successcreate) {
					throw new FileException(FileException.ERROR,
							"Can't create directory " + merchantdir);
				}
			}

			// create the destination directory
			File destfile = new File(destinationdir);
			boolean exists2 = (new File(destinationdir)).exists();
			if (!exists2) {
				boolean successcreatefinal = destfile.mkdir();
				if (!successcreatefinal) {
					throw new FileException(FileException.ERROR,
							"Can't create directory " + destfile);
				}
			}

			// check if file exist in destination
			File destfile2 = new File(destfile, fileName);
			boolean destfile2exist = destfile2.exists();
			if (destfile2exist) {
				// remove it
				destfile2.delete();
			}

			// Move file to new directory
			// boolean successcopy = file.renameTo(new File(destfile,
			// file.getName()));
			boolean successcopy = file.renameTo(new File(destfile, fileName));
			if (!successcopy) {
				file.delete();
				throw new FileException(FileException.ERROR, "Can't move file "
						+ file.getName() + " to destfile");
			}

			// delete temp file
			file.delete();
			return destinationdir + "/" + fileName;

		} finally {

		}
	}

	public long getProductid() {
		return productid;
	}

	public void setProductid(long productid) {
		this.productid = productid;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}

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

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author Original : plosson on 06-janv.-2006 15:05:44 - Last modified by
 *         $Author: vde $ on $Date: 2004/11/26 22:43:57 $
 * @version 1.0 - Rev. $Revision: 1.2 $
 */
public class UploadListener implements OutputStreamListener {
	private HttpServletRequest request;
	private long delay = 0;
	private long startTime = 0;
	private int totalToRead = 0;
	private int totalBytesRead = 0;
	private int totalFiles = -1;

	public UploadListener(HttpServletRequest request, long debugDelay) {
		this.request = request;
		this.delay = debugDelay;
		totalToRead = request.getContentLength();
		this.startTime = System.currentTimeMillis();
	}

	public void start() {
		totalFiles++;
		updateUploadInfo("start");
	}

	public void bytesRead(int bytesRead) {
		totalBytesRead = totalBytesRead + bytesRead;
		updateUploadInfo("progress");

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void error(String message) {
		updateUploadInfo("error");
	}

	public void done() {
		updateUploadInfo("done");
	}

	private long getDelta() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	private void updateUploadInfo(String status) {
		long delta = (System.currentTimeMillis() - startTime) / 1000;
		request.getSession().setAttribute(
				"uploadInfo",
				new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta,
						status));
	}

}

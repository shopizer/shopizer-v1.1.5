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

import uk.ltd.getahead.dwr.WebContextFactory;

public class UploadMonitor {
	public UploadInfo getUploadInfo() {
		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();

		if (req.getSession().getAttribute("uploadInfo") != null)
			return (UploadInfo) req.getSession().getAttribute("uploadInfo");
		else
			return new UploadInfo();
	}
}

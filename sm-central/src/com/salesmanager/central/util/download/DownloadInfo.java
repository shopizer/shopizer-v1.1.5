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
package com.salesmanager.central.util.download;

public class DownloadInfo {

	private int maxcount;
	private int idcount;
	private String file;

	public DownloadInfo(int maxcount, int idcount, String file) {
		this.maxcount = maxcount;
		this.idcount = idcount;
		this.file = file;
	}

	public int getMaxcount() {
		return maxcount;
	}

	public int getIdcount() {
		return idcount;
	}

	public String getFile() {
		return file;
	}

}

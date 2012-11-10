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
package com.salesmanager.central.util;

public class FileException extends Exception {

	public final static int ERROR = 0;
	public final static int USER = 1;

	private int type = USER;

	public FileException(String message) {
		super(message);
		setType(USER);
	}

	public FileException(int type, String message) {
		super(message);
		setType(type);
	}

	public FileException(Throwable t) {
		super(t);
		setType(ERROR);
	}

	public FileException(String message, Throwable t) {
		super(message, t);
		setType(ERROR);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

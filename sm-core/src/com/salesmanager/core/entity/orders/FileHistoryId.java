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
package com.salesmanager.core.entity.orders;

import java.io.Serializable;

public class FileHistoryId implements Serializable {

	private int merchantId;
	private long fileid;

	public FileHistoryId() {
	}

	public FileHistoryId(int merchantId, long fileid) {

		this.setMerchantId(merchantId);
		this.setFileid(fileid);
	}

	/**
	 * Return the value associated with the column: merchantid
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * Set the value related to the column: merchantid
	 * 
	 * @param merchantid
	 *            the merchantid value
	 */
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Return the value associated with the column: fileid
	 */
	public long getFileid() {
		return fileid;
	}

	/**
	 * Set the value related to the column: fileid
	 * 
	 * @param fileid
	 *            the fileid value
	 */
	public void setFileid(long fileid) {
		this.fileid = fileid;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (fileid ^ (fileid >>> 32));
		result = PRIME * result + merchantId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FileHistoryId other = (FileHistoryId) obj;
		if (fileid != other.fileid)
			return false;
		if (merchantId != other.merchantId)
			return false;
		return true;
	}

}
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

/**
 * This is an object that contains data related to the files_history table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="files_history"
 */

public class FileHistory implements Serializable {

	public static String REF = "FileHistory";
	public static String PROP_DATE_DELETED = "dateDeleted";
	public static String PROP_DOWNLOAD_COUNT = "downloadCount";
	public static String PROP_DATE_ADDED = "dateAdded";
	public static String PROP_FILESIZE = "filesize";
	public static String PROP_ID = "id";
	public static String PROP_ACCOUNTED_DATE = "accountedDate";

	// constructors
	public FileHistory() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public FileHistory(FileHistoryId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private FileHistoryId id;

	// fields
	private int filesize;
	private java.util.Date dateAdded;
	private java.util.Date dateDeleted;
	private java.util.Date accountedDate;
	private int downloadCount;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public FileHistoryId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(FileHistoryId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: filesize
	 */
	public int getFilesize() {
		return filesize;
	}

	/**
	 * Set the value related to the column: filesize
	 * 
	 * @param filesize
	 *            the filesize value
	 */
	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	/**
	 * Return the value associated with the column: date_added
	 */
	public java.util.Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Set the value related to the column: date_added
	 * 
	 * @param dateAdded
	 *            the date_added value
	 */
	public void setDateAdded(java.util.Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Return the value associated with the column: date_deleted
	 */
	public java.util.Date getDateDeleted() {
		return dateDeleted;
	}

	/**
	 * Set the value related to the column: date_deleted
	 * 
	 * @param dateDeleted
	 *            the date_deleted value
	 */
	public void setDateDeleted(java.util.Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	/**
	 * Return the value associated with the column: accounted_date
	 */
	public java.util.Date getAccountedDate() {
		return accountedDate;
	}

	/**
	 * Set the value related to the column: accounted_date
	 * 
	 * @param accountedDate
	 *            the accounted_date value
	 */
	public void setAccountedDate(java.util.Date accountedDate) {
		this.accountedDate = accountedDate;
	}

	/**
	 * Return the value associated with the column: download_count
	 */
	public int getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Set the value related to the column: download_count
	 * 
	 * @param downloadCount
	 *            the download_count value
	 */
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((accountedDate == null) ? 0 : accountedDate.hashCode());
		result = PRIME * result
				+ ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = PRIME * result
				+ ((dateDeleted == null) ? 0 : dateDeleted.hashCode());
		result = PRIME * result + downloadCount;
		result = PRIME * result + filesize;
		result = PRIME * result + hashCode;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
		final FileHistory other = (FileHistory) obj;
		if (accountedDate == null) {
			if (other.accountedDate != null)
				return false;
		} else if (!accountedDate.equals(other.accountedDate))
			return false;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (dateDeleted == null) {
			if (other.dateDeleted != null)
				return false;
		} else if (!dateDeleted.equals(other.dateDeleted))
			return false;
		if (downloadCount != other.downloadCount)
			return false;
		if (filesize != other.filesize)
			return false;
		if (hashCode != other.hashCode)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
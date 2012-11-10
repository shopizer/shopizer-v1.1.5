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
package com.salesmanager.central;

public class PageBaseAction extends BaseAction {

	private int pageStartIndex = 0;// page number -- pagination
	private int pageCriteriaIndex = 0;// for criteria
	private int listingCount = 0;// total number of items
	private int realCount = 0;// total number in the current page
	private int size = 20;// default
	private int firstItem = 1;
	private int lastItem = 0;

	public int getPageStartIndex() {
		return pageStartIndex;
	}

	public void setPageStartIndex(int pageStartIndex) {
		this.pageStartIndex = pageStartIndex;
	}

	public int getListingCount() {
		return listingCount;
	}

	public void setListingCount(int listingCount) {
		this.listingCount = listingCount;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFirstItem() {
		return firstItem;
	}

	public void setFirstItem(int firstItem) {
		this.firstItem = firstItem;
	}

	public int getLastItem() {
		return lastItem;
	}

	public void setLastItem(int lastItem) {
		this.lastItem = lastItem;
	}

	protected void setPageStartNumber() {

		int start = this.getPageStartIndex();
		if (this.getPageStartIndex() == 0) {
			start = 0;
		} else {
			start = start * this.getSize();
		}
		this.setPageCriteriaIndex(start);
	}

	protected void setPageElements() {

		if (getListingCount() == 0) {
			this.setFirstItem(firstItem);
			this.setLastItem(listingCount);
		} else {
			if (this.getPageStartIndex() == 0) {
				this.setFirstItem(firstItem);
			} else {

				this.setFirstItem(this.getPageCriteriaIndex() + 1);

			}
			this.setLastItem(this.getPageCriteriaIndex() + this.getRealCount());

		}
	}

	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public int getPageCriteriaIndex() {
		return pageCriteriaIndex;
	}

	public void setPageCriteriaIndex(int pageCriteriaIndex) {
		this.pageCriteriaIndex = pageCriteriaIndex;
	}

}

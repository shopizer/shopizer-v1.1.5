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
package com.salesmanager.central.catalog;

/**
 * Used for PageIteratoQuery
 * 
 * @author Carl Samson
 * 
 */
public class ProductSearchFilterCriteria {

	public final static int VISIBLEALL = 2;
	public final static int VISIBLETRUE = 1;
	public final static int VISIBLEFALSE = 0;

	public final static int STATUSALL = 2;
	public final static int STATUSINSTOCK = 1;
	public final static int STATUSOUTSTOCK = 0;

	private long categoryid = -1;
	private int visible = VISIBLEALL;
	private String name = null;
	private int status = STATUSALL;



}

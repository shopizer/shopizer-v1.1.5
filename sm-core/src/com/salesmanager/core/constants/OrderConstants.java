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
package com.salesmanager.core.constants;

public class OrderConstants {

	public final static int STATUSBASE = 1;// pending
	public final static int STATUSPROCESSING = 2;
	public final static int STATUSDELIVERED = 3;
	public final static int STATUSREFUND = 5;
	public final static int STATUSUPDATE = 4;
	public final static int STATUSINVOICED = 20;
	public final static int STATUSINVOICESENT = 21;
	public final static int STATUSINVOICEPAID = 22;
	public final static int STATUSACCOUNTINYTERM = 100;

	public final static int ONLINE_CHANNEL = 1;
	public final static int INVOICE_CHANNEL = 2;

	public final static String OT_SHIPPING_MODULE = "ot_shipping";
	public final static String OT_TOTAL_MODULE = "ot_total";
	public final static String OT_SUBTOTAL_MODULE = "ot_subtotal";
	public final static String OT_TAX_MODULE = "ot_tax";
	public final static String OT_CREDITS = "ot_credits";
	public final static String OT_RECURING = "ot_recuring";
	public final static String OT_REFUND = "ot_refund";
	public final static String OT_OTHER_DUE_NOW = "ot_other_now";
	public final static String OT_RECURING_CREDITS = "ot_recuring_credits";

}
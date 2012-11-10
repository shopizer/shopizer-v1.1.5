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
package com.salesmanager.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class DateUtil {

	private Date startDate = new Date(new Date().getTime());
	private Date endDate = new Date(new Date().getTime());
	private Logger log = Logger.getLogger(DateUtil.class);

	
	
	/**
	 * Generates a time stamp
	 * yyyymmddhhmmss
	 * @return
	 */
	public static String generateTimeStamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
		return format.format(new Date());
	}
	
	/**
	 * yyyy-MM-dd
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDate(Date dt) {

		if (dt == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(dt);

	}

	/**
	 * yy-MMM-dd
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDateMonthString(Date dt) {

		if (dt == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
		return format.format(dt);

	}

	public static Date getDate(String date) throws Exception {
		DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return myDateFormat.parse(date);
	}

	public static Date addDaysToCurrentDate(int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, days);
		return c.getTime();

	}

	public static Date getDate() {

		return new Date(new Date().getTime());

	}

	public static String getPresentDate() {

		Date dt = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(dt.getTime()));
	}

	public static String getPresentYear() {

		Date dt = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(new Date(dt.getTime()));
	}

	public void processPostedDates(HttpServletRequest request) {
		Date dt = new Date();
		DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = null;
		Date eDate = null;
		try {
			if (request.getParameter("startdate") != null) {
				sDate = myDateFormat.parse(request.getParameter("startdate"));
			}
			if (request.getParameter("enddate") != null) {
				eDate = myDateFormat.parse(request.getParameter("enddate"));
			}
			this.startDate = sDate;
			this.endDate = eDate;
		} catch (Exception e) {
			log.error(e);
			this.startDate = new Date(dt.getTime());
			this.endDate = new Date(dt.getTime());
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}
}

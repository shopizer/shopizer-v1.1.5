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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class MessageUtil implements Serializable {

	private List messages;
	private List errormessages;

	private MessageUtil() {
		messages = new ArrayList();
	}

	public static void addNoticeMessage(HttpServletRequest req, String message) {
		req.getSession().setAttribute("SM-NOTICEMESSAGE", message);
	}

	public static void addMessage(HttpServletRequest req, String message) {
		req.getSession().setAttribute("SM-MESSAGE", message);
	}

	public static String getMessage(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("SM-MESSAGE");
	}

	public static String getFormErrorMessage(HttpServletRequest req,
			String field) {

		Map errmsgs = (Map) req.getAttribute("errfrmmsg");

		if (errmsgs != null && errmsgs.containsKey(field)) {
			String msg = (String) errmsgs.get(field);
			errmsgs.remove(field);
			return msg;
		} else {
			return null;
		}

	}

	public static void addFormErrorMessage(HttpServletRequest req,
			String field, String message) {

		Map errmsgs = (Map) req.getAttribute("errfrmmsg");
		if (errmsgs == null) {
			errmsgs = new HashMap();
			req.setAttribute("errfrmmsg", errmsgs);
		}
		errmsgs.put(field, message);

	}

	public static void addErrorMessage(HttpServletRequest req, String message) {
		req.getSession().setAttribute("SM-ERR-MESSAGE", message);
	}

	public static String getErrorMessage(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("SM-ERR-MESSAGE");
	}

	public static void addErrorMessages(HttpServletRequest req,
			List<String> messages) {
		req.getSession().setAttribute("SM-ERR-MESSAGES", messages);
	}

	public static String displayFormErrorMessage(HttpServletRequest req,
			String field) {
		String message = getFormErrorMessage(req, field);

		String printmessage = LabelUtil.getInstance().getText(req.getLocale(),
				message);

		if (message != null) {
			StringBuffer sb = new StringBuffer();
			sb
					.append("<tr errorFor='")
					.append(field)
					.append("'>")
					.append(
							"<td align='center' valign='top' colspan='2'><span class='errorMessage'>")
					.append(printmessage).append("</span></td></tr>");
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String displayFormErrorMessageNoFormating(
			HttpServletRequest req, String field) {
		String message = getFormErrorMessage(req, field);

		String printmessage = LabelUtil.getInstance().getText(req.getLocale(),
				message);

		if (message != null) {

			return printmessage;

		} else {
			return "";
		}
	}

	public static String displayMessages(HttpServletRequest req) {

		StringBuffer buffer = new StringBuffer();

		String errmessage = (String) req.getSession().getAttribute(
				"SM-ERR-MESSAGE");
		String message = (String) req.getSession().getAttribute("SM-MESSAGE");
		String noticemessage = (String) req.getSession().getAttribute(
				"SM-NOTICEMESSAGE");

		List<String> errorList = (List<String>) req.getSession().getAttribute(
				"SM-ERR-MESSAGES");

		if (noticemessage != null) {

			buffer.append("<div id=\"message\" class=\"clean-yellow\">")
					.append(noticemessage).append("</div>");

		}

		if (message != null) {

			buffer.append("<div id=\"message\" class=\"icon-ok\">").append(
					message).append("</div>");

		}

		if (errmessage != null) {

			buffer.append("<div id=\"message\" class=\"icon-error\">").append(
					errmessage).append("</div>");

		}

		if (errorList != null && !errorList.isEmpty()) {
			buffer.append("<div id=\"message\" class=\"icon-error\">");
			for (String error : errorList) {
				buffer.append(error);
				buffer.append("<br>");
			}
			buffer.append("</div>");
		}

		req.getSession().removeAttribute("SM-MESSAGE");
		req.getSession().removeAttribute("SM-ERR-MESSAGE");
		req.getSession().removeAttribute("SM-ERR-MESSAGES");
		req.getSession().removeAttribute("SM-NOTICEMESSAGE");
		return buffer.toString();

	}

	public static void resetMessages(HttpServletRequest req) {
		req.getSession().removeAttribute("SM-MESSAGE");
		req.getSession().removeAttribute("SM-ERR-MESSAGE");
		req.getSession().removeAttribute("SM-ERR-MESSAGES");
		req.getSession().removeAttribute("SM-NOTICEMESSAGE");
	}
	
	public static boolean hasMessage(HttpServletRequest req) {
		
		boolean msg = false;
		String errmessage = (String) req.getSession().getAttribute(
			"SM-ERR-MESSAGE");
		String message = (String) req.getSession().getAttribute("SM-MESSAGE");
		String noticemessage = (String) req.getSession().getAttribute(
			"SM-NOTICEMESSAGE");

		List<String> errorList = (List<String>) req.getSession().getAttribute(
			"SM-ERR-MESSAGES");
		
		if (noticemessage != null || message != null || errmessage != null || (errorList != null && !errorList.isEmpty())) {
			msg = true;
		}
		
		return msg;
		
	}
	
}

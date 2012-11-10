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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCallUtil {

	public static String invokeGetUrl(String url) throws Exception {

		String agent = "Mozilla/4.0";

		HttpURLConnection conn = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			URL postURL = new URL(url);
			conn = (HttpURLConnection) postURL.openConnection();

			// Set connection parameters. We need to perform input and output,
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("User-Agent", agent);

			conn.setRequestMethod("GET");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());

			StringBuffer responseText = new StringBuffer();
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					responseText.append(_line);
				}

			}

			return responseText.toString();

		} finally {

			if (is != null) {
				try {
					is.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

		}

	}

	public static String invokePostUrl(String url, String encodedQueryString)
			throws Exception {

		String agent = "Mozilla/4.0";

		HttpURLConnection conn = null;
		DataOutputStream output = null;
		DataInputStream in = null;
		BufferedReader is = null;
		try {
			URL postURL = new URL(url);
			conn = (HttpURLConnection) postURL.openConnection();

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", agent);

			conn.setRequestProperty("Content-Length", String
					.valueOf(encodedQueryString.length()));
			conn.setRequestMethod("POST");

			// get the output stream to POST to.
			output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(encodedQueryString);
			output.flush();

			StringBuffer responseText = new StringBuffer();
			in = new DataInputStream(conn.getInputStream());
			int rc = conn.getResponseCode();
			if (rc != -1) {
				is = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String _line = null;
				while (((_line = is.readLine()) != null)) {
					responseText.append(_line);
				}

			}

			return responseText.toString();

		} finally {

			if (is != null) {
				try {
					is.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

		}

	}

}

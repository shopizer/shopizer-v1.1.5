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

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.commons.lang.StringUtils;

public class StringUtil {

	/**
	 * Build a ; delimited line with the values contained in the list
	 * 
	 * @param list
	 * @return
	 */
	public static String buildMultipleValueLine(List list) {

		if (list != null & list.size() > 0) {

			Iterator i = list.iterator();
			StringBuffer linebuffer = new StringBuffer();
			int icount = 0;
			while (i.hasNext()) {
				String value = (String) i.next();
				linebuffer.append(value);
				if (icount < list.size() - 1)
					linebuffer.append(";");
				icount++;
			}
			return linebuffer.toString();
		} else {
			return null;
		}
	}

	public static Map parseTokenLine(String line, String delimiter) {

		BidiMap returnMap = new TreeBidiMap();

		if (StringUtils.isBlank(line) || StringUtils.isBlank(delimiter)) {
			return returnMap;
		}

		StringTokenizer st = new StringTokenizer(line, delimiter);

		int count = 0;
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			returnMap.put(value, count);
			count++;
		}

		return returnMap;

	}
	
	
	public static Map deformatUrlResponse(String payload) throws Exception {
		HashMap nvp = new HashMap();
		StringTokenizer stTok = new StringTokenizer(payload, "&");
		while (stTok.hasMoreTokens()) {
			StringTokenizer stInternalTokenizer = new StringTokenizer(stTok
					.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2) {
				String key = URLDecoder.decode(stInternalTokenizer.nextToken(),
						"UTF-8");
				String value = URLDecoder.decode(stInternalTokenizer
						.nextToken(), "UTF-8");
				nvp.put(key.toUpperCase(), value);
			}
		}
		return nvp;
	}
	
	/**
	 * Can be used to decode URL 
	 * @param s
	 * @return
	 */
	public static String unescape(String s) {
		    StringBuffer sbuf = new StringBuffer () ;
		    int l  = s.length() ;
		    int ch = -1 ;
		    int b, sumb = 0;
		    for (int i = 0, more = -1 ; i < l ; i++) {
		      /* Get next byte b from URL segment s */
		      switch (ch = s.charAt(i)) {
			case '%':
			  ch = s.charAt (++i) ;
			  int hb = (Character.isDigit ((char) ch) 
				    ? ch - '0'
				    : 10+Character.toLowerCase((char) ch) - 'a') & 0xF ;
			  ch = s.charAt (++i) ;
			  int lb = (Character.isDigit ((char) ch)
				    ? ch - '0'
				    : 10+Character.toLowerCase ((char) ch)-'a') & 0xF ;
			  b = (hb << 4) | lb ;
			  break ;
			case '+':
			  b = ' ' ;
			  break ;
			default:
			  b = ch ;
		      }
		      /* Decode byte b as UTF-8, sumb collects incomplete chars */
		      if ((b & 0xc0) == 0x80) {			// 10xxxxxx (continuation byte)
			sumb = (sumb << 6) | (b & 0x3f) ;	// Add 6 bits to sumb
			if (--more == 0) sbuf.append((char) sumb) ; // Add char to sbuf
		      } else if ((b & 0x80) == 0x00) {		// 0xxxxxxx (yields 7 bits)
			sbuf.append((char) b) ;			// Store in sbuf
		      } else if ((b & 0xe0) == 0xc0) {		// 110xxxxx (yields 5 bits)
			sumb = b & 0x1f;
			more = 1;				// Expect 1 more byte
		      } else if ((b & 0xf0) == 0xe0) {		// 1110xxxx (yields 4 bits)
			sumb = b & 0x0f;
			more = 2;				// Expect 2 more bytes
		      } else if ((b & 0xf8) == 0xf0) {		// 11110xxx (yields 3 bits)
			sumb = b & 0x07;
			more = 3;				// Expect 3 more bytes
		      } else if ((b & 0xfc) == 0xf8) {		// 111110xx (yields 2 bits)
			sumb = b & 0x03;
			more = 4;				// Expect 4 more bytes
		      } else /*if ((b & 0xfe) == 0xfc)*/ {	// 1111110x (yields 1 bit)
			sumb = b & 0x01;
			more = 5;				// Expect 5 more bytes
		      }
		      /* We don't test if the UTF-8 encoding is well-formed */
		    }
		    return sbuf.toString() ;
	}


}

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
package com.salesmanager.core.util.file.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * CSVFileReader class parses the supplied *.csv file to return a list of
 * rows,each consisting of a list of corresponding columns.
 * 
 * @author anilkumar.talla
 */
public class CSVFileReader implements CSVConstants {

	private static Pattern csvRE;

	/**
	 * CSVFileReader constructor.
	 */
	public CSVFileReader() {
		csvRE = Pattern.compile(CSV_PATTERN);
	}

	/**
	 * processCSV method takes the csv file argument and processes csv file to
	 * return a list of rows having corresponding column list.
	 * 
	 * @param csvFile
	 *            File
	 * @return List<List<String>>
	 * @throws IOException
	 */
	public List<List<String>> processCSV(File csvFile) throws IOException {
		BufferedReader reader = null;
		List<List<String>> rowList = new ArrayList<List<String>>();
		try {
			reader = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (!StringUtils.isBlank(line)) {
					List<String> columns = parseRow(line);
					rowList.add(columns);
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return rowList;
	}

	/**
	 * parseRow method parses the supplied line to split in to columns by
	 * matching the corresponding columns with regular expression.
	 * 
	 * @param line
	 *            String
	 * @return List<String>
	 */
	private List<String> parseRow(String line) {
		List<String> list = new ArrayList<String>();
		Matcher m = csvRE.matcher(line);
		// For each field
		while (m.find()) {
			String match = m.group();
			if (match == null)
				break;
			if (match.endsWith(COMMA_STR)) {
				match = match.substring(0, match.length() - 1);
			}
			if (match.startsWith(DOUBLEQUOTE_STR)) {
				match = match.substring(1, match.length() - 1);
			}
			if (match.length() == 0)
				match = null;
			list.add(match);
		}
		return list;
	}
}
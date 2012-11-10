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

public interface CSVConstants {

	/**
	 * Pattern used to match CSV's consists of three alternations: the first
	 * matches a quoted field, the second unquoted, the third a null field.
	 */
	public static final String CSV_PATTERN = "\"([^\"]+?)\",?|([^,]+),?|,";
	public static final String COMMA_STR = ",";
	public static final String DOUBLEQUOTE_STR = "\"";
	public static final String CSV_SUFFIX = ".csv";
	public static final String INVALID_FILE_EXCEPTION_MSG = "Invalid File: Must be .csv Extension";
}

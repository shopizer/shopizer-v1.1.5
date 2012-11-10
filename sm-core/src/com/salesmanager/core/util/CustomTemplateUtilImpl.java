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

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CustomTemplateUtilImpl implements CustomTemplateUtil {

	private Configuration freeMarkerConfiguration;

	public String parseContent(Map context, String templateName)
			throws Exception {

		Template textTemplate = freeMarkerConfiguration
				.getTemplate(templateName);
		StringWriter textWriter = new StringWriter();
		textTemplate.process(context, textWriter);
		String returnText = textWriter.toString();

		return returnText;

	}

	public Configuration getFreeMarkerConfiguration() {
		return freeMarkerConfiguration;
	}

	public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
		this.freeMarkerConfiguration = freeMarkerConfiguration;
	}

}

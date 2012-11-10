/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Nov 19, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.util.www.integration.fb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookException;
import com.restfb.FacebookJsonMappingException;
import com.restfb.Parameter;



/**
 * Overwrites restfb.DefaultFacebookClient
 * @author Carl Samson
 *
 */
public class SalesManagerFacebookClient extends DefaultFacebookClient {
	
	  static final String ENCODING_CHARSET = "UTF-8";
	  
	  public SalesManagerFacebookClient(String accessToken) {
		    super(accessToken, new DefaultWebRequestor(), new DefaultJsonMapper());
	  }
	
	  protected String toParameterString(Parameter... parameters)
      throws FacebookJsonMappingException {
		  
		  
		  if (!StringUtils.isBlank(accessToken))
		      parameters =
		          parametersWithAdditionalParameter(
		            Parameter.with(ACCESS_TOKEN_PARAM_NAME, accessToken), parameters);

		    parameters =
		        parametersWithAdditionalParameter(
		          Parameter.with(FORMAT_PARAM_NAME, "json"), parameters);

		    StringBuilder parameterStringBuilder = new StringBuilder();
		    boolean first = true;

		    for (Parameter parameter : parameters) {
		      if (first)
		        first = false;
		      else
		        parameterStringBuilder.append("&");
		      
	    	  String name = urlEncode(parameter.name);
	    	  String value = parameter.value;
		      if(!parameter.name.equals(ACCESS_TOKEN_PARAM_NAME)) {
		    	  name = urlEncode(parameter.name);
		    	 value = urlEncodedValueForParameterName(parameter.name, parameter.value);
		      } 

		      parameterStringBuilder.append(name);
		      parameterStringBuilder.append("=");
		      parameterStringBuilder.append(value);
		    }

		    return parameterStringBuilder.toString();
	  }
	  
	  
	  
	  static String urlEncode(String string) {
		    if (string == null)
		      return null;
		    try {
		      return URLEncoder.encode(string, ENCODING_CHARSET);
		    } catch (UnsupportedEncodingException e) {
		      throw new IllegalStateException("Platform doesn't support "
		          + ENCODING_CHARSET, e);
		    }
	  }



}

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
package com.salesmanager.core.module.model.application;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaModule {

	public BufferedImage getImageForSessionId(String sessionId,
			HttpServletRequest request);

	public boolean validateResponseForSessonId(String sessionId,
			String captchaParameter);

}

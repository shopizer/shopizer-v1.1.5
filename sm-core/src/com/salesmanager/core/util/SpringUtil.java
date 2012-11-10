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

import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

public class SpringUtil {

	private static Configuration conf = PropertiesUtil.getConfiguration();

	private BeanFactoryReference bf = null;

	private static SpringUtil instance = null;

	public static Object getBean(String name) throws RuntimeException {

		if (instance == null) {
			instance = new SpringUtil();
		}

		try {
			Object o = instance.getApplicationContext().getBean(name);
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private ApplicationContext getApplicationContext() {
		if (bf == null) {
			BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
			bf = bfl.useBeanFactory("smcore");

		}
		return (ApplicationContext) bf.getFactory();
	}

}

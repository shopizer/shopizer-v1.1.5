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

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.impl.application.CacheModuleException;

public interface CacheModule {

	public void flushAll() throws CacheModuleException;

	public void flushEntry(String key) throws CacheModuleException;

	public void flushCacheGroup(String group, MerchantStore store)
			throws CacheModuleException;

	public void flushCacheGroup(String group) throws CacheModuleException;

	public Object getFromCache(String key, MerchantStore store)
			throws CacheModuleException;

	public void putInCache(String key, Object content, String group,
			MerchantStore store) throws CacheModuleException;

}

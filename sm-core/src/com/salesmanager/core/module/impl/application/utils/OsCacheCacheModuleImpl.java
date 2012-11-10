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
package com.salesmanager.core.module.impl.application.utils;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.impl.application.CacheModuleException;
import com.salesmanager.core.module.model.application.CacheModule;

/**
 * OSCache implementation wrapper
 * 
 * @author Carl Samson
 * 
 */
public class OsCacheCacheModuleImpl implements CacheModule {

	private static GeneralCacheAdministrator admin = null;
	private static OsCacheCacheModuleImpl instance = null;

	private OsCacheCacheModuleImpl() {

	}

	public static OsCacheCacheModuleImpl getInstance() {
		if (instance == null) {
			instance = new OsCacheCacheModuleImpl();
			admin = new GeneralCacheAdministrator();
		}

		return instance;
	}

	public void flushAll() throws CacheModuleException {
		admin.flushAll();
	}

	public void flushEntry(String key) throws CacheModuleException {
		admin.flushEntry(key);
	}

	public void flushCacheGroup(String group, MerchantStore store)
			throws CacheModuleException {
		// store is ignored for now
		admin.flushGroup(group);
	}

	public void flushCacheGroup(String group) throws CacheModuleException {
		admin.flushGroup(group);
	}

	public Object getFromCache(String key, MerchantStore store)
			throws CacheModuleException {

		// @todo, refreshPeriod from configuration file
		// property to cache or not //2000 seconds

		Object o = null;

		key = key + store.getMerchantId();

		if (store != null && store.isUseCache()) {

			try {
				o = admin.getFromCache(key, 2000);
			} catch (NeedsRefreshException nre) {

				admin.cancelUpdate(key);

				/*
				 * try { // Get the value myValue =
				 * "This is the content retrieved."; // Store in the cache
				 * admin.putInCache(myKey, myValue); updated = true; } finally {
				 * if (!updated) { // It is essential that cancelUpdate is
				 * called if the // cached content could not be rebuilt
				 * admin.cancelUpdate(myKey); } }
				 */

				// throw new CacheModuleException(nre);
			} catch (Exception e) {
				admin.cancelUpdate(key);
				throw new CacheModuleException(e);
			}
		}

		return o;

	}

	public void putInCache(String key, Object content, String group,
			MerchantStore store) throws CacheModuleException {

		if (store.isUseCache()) {

			key = key + store.getMerchantId();

			boolean updated = false;

			try {
				admin.putInCache(key, content, new String[] { group });
				updated = true;
			} catch (Exception nre) {
				admin.cancelUpdate(key);
			} finally {
				if (!updated) {
					// It is essential that cancelUpdate is called if the
					// cached content could not be rebuilt
					admin.cancelUpdate(key);
				}
			}

		}

	}

}

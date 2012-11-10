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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manage the cache
 * 
 * @author Carl Samson
 * 
 */

public class CacheUtil {

	private static CacheUtil cachefactory = null;
	private static Map cachesm = Collections.synchronizedMap(new HashMap());

	private CacheUtil() {
	}

	public static CacheUtil getInstance() {
		if (cachefactory == null) {
			cachefactory = new CacheUtil();
		}
		return cachefactory;
	}

	/**
	 * Create a synchronized HashMap for caching
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Map createCacheMap(String name) {
		if (cachesm.containsKey(name)) {
			return (Map) cachesm.get(name);
		}

		Map newcache = Collections.synchronizedMap(new HashMap());
		cachesm.put(name, newcache);
		return newcache;

	}

	/**
	 * Create a synchronized List for caching
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List createCacheList(String name) {
		if (cachesm.containsKey(name)) {
			return (List) cachesm.get(name);
		}

		List newcache = Collections.synchronizedList(new ArrayList());
		cachesm.put(name, newcache);
		return newcache;

	}

	public Map getCacheMap(String name) {
		Map returnmap = (Map) cachesm.get(name);
		return returnmap;
	}

	public List getCacheList(String name) {
		List returnlist = (List) cachesm.get(name);
		return returnlist;
	}

	public boolean containsCache(String name) {
		return cachesm.containsKey(name);
	}

	public void removeCache(String name) {
		cachesm.remove(name);
	}

}

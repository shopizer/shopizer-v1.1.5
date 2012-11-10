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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductDescriptionId;
import com.salesmanager.core.entity.reference.Manufacturers;
import com.salesmanager.core.entity.reference.ManufacturersInfo;
import com.salesmanager.core.entity.reference.ManufacturersInfoId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogException;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.file.IFileUploadService;

public class CSVFileUploadImpl implements IFileUploadService {

	private static final Log log = LogFactory.getLog(CSVFileUploadImpl.class);
	private static final String INSUFF_DATA_STR = "Insufficient Data:";
	private static final String INVALID_LANGUAGE_ID = "Invalid Language Id:";
	private static final String INVALID_CATEGORY_ID = "Invalid Category Id:";
	public static final String INVALID_MANUFACTURER_ID = "Invalid Manufacturer Id:";
	public static final String INVALID_AMT_CURRENCY = "Invalid Amount or Currency:";
	public static final String UNABLE_TO_PROCESS = "Unable to Process:";
	private CatalogService catalogService;
	private CSVFileReader fileReader;

	public Map<Integer, List<String>> uploadCategory(File csvCategoryFile,
			Integer merchantId) throws CatalogException {
		Map<Integer, List<String>> errorMap = new LinkedHashMap<Integer, List<String>>();
		try {

			catalogService = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			List<List<String>> categoryRowList = fileReader
					.processCSV(csvCategoryFile);
			int lineNo = 0;
			for (List<String> row : categoryRowList) {
				// Language code|category id|category name|category
				// description|Parent Category id
				lineNo++;
				Integer languageId = parseInt(row.get(0));
				if (row.size() != 5) {
					addErrorMsg(errorMap, lineNo, INSUFF_DATA_STR + row);
					continue;
				}
				if (languageId == null || !isValidLanguage(languageId)) {
					addErrorMsg(errorMap, lineNo, INVALID_LANGUAGE_ID
							+ languageId);
					continue;
				}
				Integer categoryId = parseInt(row.get(1));
				if (categoryId == null || categoryId == 0) {
					addErrorMsg(errorMap, lineNo, INVALID_CATEGORY_ID
							+ categoryId);
					continue;
				}
				long parentCategoryId;
				if (StringUtils.isNotBlank(row.get(4))) {
					parentCategoryId = Long.valueOf(row.get(4));
				} else {
					parentCategoryId = new Long(
							CatalogConstants.ROOT_CATEGORY_ID);
				}

				Category parentCat = null;
				if (parentCategoryId != CatalogConstants.ROOT_CATEGORY_ID) {
					parentCat = catalogService.getCategory(parentCategoryId);
				}
				Category cat = new Category();
				cat.setParent(parentCat);
				cat.setParentId(parentCategoryId);
				cat.setCategoryId(categoryId);
				cat.setMerchantId(merchantId);
				cat.setLastModified(new Date());
				cat.setDateAdded(new Date());

				CategoryDescription desc = new CategoryDescription();
				desc.setCategoryDescription(row.get(3));
				desc.setCategoryName(row.get(2));
				desc.setId(new CategoryDescriptionId(categoryId, languageId));
				try {
					catalogService.uploadCategories(cat, Arrays
							.asList(new CategoryDescription[] { desc }));
				} catch (Exception e) {
					log.error("Error occurred while uploading Categories", e);
					addErrorMsg(errorMap, lineNo, UNABLE_TO_PROCESS + row);
					continue;
				}
			}
		} catch (IOException e) {
			log.error("Error occurred while uploading Categories", e);
		}
		return errorMap;
	}

	public Map<Integer, List<String>> uploadManufacturers(
			File csvManufacturersFile) {
		Map<Integer, List<String>> errorMap = new LinkedHashMap<Integer, List<String>>();
		try {
			catalogService = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			List<List<String>> manuRowList = fileReader
					.processCSV(csvManufacturersFile);
			int lineNo = 0;
			for (List<String> row : manuRowList) {
				lineNo++;
				if (row.size() != 3) {
					addErrorMsg(errorMap, lineNo, INSUFF_DATA_STR + row);
					continue;
				}
				Integer languageId = parseInt(row.get(0));
				if (languageId == null || !isValidLanguage(languageId)) {
					addErrorMsg(errorMap, lineNo, INVALID_LANGUAGE_ID
							+ languageId);
					continue;
				}
				Manufacturers manufacturers = new Manufacturers();
				Integer manufacturerId = parseInt(row.get(1));
				if (manufacturerId == null || manufacturerId == 0) {
					addErrorMsg(errorMap, lineNo, INVALID_MANUFACTURER_ID
							+ manufacturerId);
					continue;
				}
				manufacturers.setManufacturersId(manufacturerId);
				manufacturers.setManufacturersName(row.get(2));
				ManufacturersInfo manuInfo = new ManufacturersInfo();
				manuInfo.setId(new ManufacturersInfoId(manufacturerId,
						languageId));
				catalogService.saveOrUpdateManufacturers(manufacturers,
						manuInfo);
			}

		} catch (IOException e) {
			log.error("Error occurred while uploading Categories", e);
		}
		return errorMap;
	}

	public Map<Integer, List<String>> uploadProducts(File csvProductsFile,
			Integer merchantId) {
		Map<Integer, List<String>> errorMap = new LinkedHashMap<Integer, List<String>>();
		try {
			catalogService = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			List<List<String>> prodowList = fileReader
					.processCSV(csvProductsFile);
			int lineNo = 0;
			for (List<String> row : prodowList) {
				lineNo++;
				if (row.size() != 14) {
					addErrorMsg(errorMap, lineNo, INSUFF_DATA_STR + row);
					continue;
				}
				Integer languageId = parseInt(row.get(0));
				if (languageId == null || !isValidLanguage(languageId)) {
					addErrorMsg(errorMap, lineNo, INVALID_LANGUAGE_ID
							+ languageId);
					continue;
				}
				Product product = new Product();
				product.setMerchantId(merchantId);
				product.setSku(row.get(1));
				product.setProductModel(row.get(4));
				product.setProductQuantity(parseInt(row.get(5)));
				try {
					product.setProductPrice(CurrencyUtil.validateCurrency(row
							.get(6), row.get(7)));
				} catch (ValidationException e) {
					addErrorMsg(errorMap, lineNo, INVALID_AMT_CURRENCY
							+ languageId);
					continue;
				}
				product.setProductManufacturersId(parseInt(row.get(8)));
				product.setProductVirtual(Boolean.parseBoolean(row.get(9)));
				product.setProductWeight(new BigDecimal(parseInt(row.get(10))));
				product.setProductHeight(new BigDecimal(parseInt(row.get(11))));
				product.setProductLength(new BigDecimal(parseInt(row.get(12))));
				product.setProductWidth(new BigDecimal(parseInt(row.get(13))));

				ProductDescription prodDesc = new ProductDescription();
				prodDesc.setProductName(row.get(2));
				prodDesc.setProductDescription(row.get(3));
				prodDesc.setId(new ProductDescriptionId(0, languageId));

				Set<ProductDescription> descSet = new HashSet<ProductDescription>();
				descSet.add(prodDesc);
				product.setDescriptions(descSet);
				try {
					catalogService.saveOrUpdateProduct(product);
				} catch (CatalogException e) {
					addErrorMsg(errorMap, lineNo, UNABLE_TO_PROCESS + row);
					continue;
				}
			}
		} catch (IOException e) {
			log.error("Error occurred while uploading Categories", e);
		}
		return errorMap;
	}

	public static Integer parseInt(String val) {
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static void addErrorMsg(Map<Integer, List<String>> errorMap,
			Integer lineNo, String errorMsg) {
		if (errorMap.get(lineNo) == null) {
			errorMap.put(lineNo, new ArrayList<String>());

		}
		errorMap.get(lineNo).add(errorMsg);
	}

	public static boolean isValidLanguage(Integer langId) {
		return (RefCache.getLanguageswithindex().get(langId) != null);
	}

	public CSVFileReader getFileReader() {
		return fileReader;
	}

	public void setFileReader(CSVFileReader fileReader) {
		this.fileReader = fileReader;
	}

}

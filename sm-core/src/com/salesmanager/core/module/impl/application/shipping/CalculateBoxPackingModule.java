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
package com.salesmanager.core.module.impl.application.shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.validator.ValidationException;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.module.model.application.CalculatePackingModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class CalculateBoxPackingModule implements CalculatePackingModule {

	public String getConfigurationOptionsFileName(Locale locale)
			throws Exception {

		return "packing-box.jsp";
	}

	public PackageDetail getConfigurationOptions(MerchantConfiguration config,
			String currency) throws Exception {

		if (config == null || config.getConfigurationValue1() == null) {
			return null;
		}

		PackageDetail details = new PackageDetail();
		StringTokenizer st = new StringTokenizer(config
				.getConfigurationValue1(), "|");

		Map parseTokens = new HashMap();

		int i = 1;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (i == 1) {

				details.setShippingWidth(new BigDecimal(token).doubleValue());

			} else if (i == 2) {
				details.setShippingHeight(new BigDecimal(token).doubleValue());

			} else if (i == 3) {
				details.setShippingLength(new BigDecimal(token).doubleValue());

			} else if (i == 4) {
				details.setShippingWeight(new BigDecimal(token).doubleValue());

			} else if (i == 5) {
				details.setShippingMaxWeight(new BigDecimal(token)
						.doubleValue());

			} else if (i == 6) {
				details.setTreshold(Integer.parseInt(token));

			}
			i++;
		}
		details.setCurrency(currency);
		return details;

	}

	public Collection<PackageDetail> calculatePacking(
			Collection<OrderProduct> products, MerchantConfiguration config,
			int merchantId) throws Exception {

		if (products == null) {
			throw new Exception("Product list cannot be null !!");
		}

		double width = 0;
		double length = 0;
		double height = 0;
		double weight = 0;
		double maxweight = 0;

		int treshold = 0;

		// get box details from merchantconfiguration
		String values = config.getConfigurationValue1();
		if (!StringUtils.isBlank(values)) {
			StringTokenizer st = new StringTokenizer(config
					.getConfigurationValue1(), "|");

			Map parseTokens = new HashMap();

			int i = 1;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (i == 1) {

					width = new BigDecimal(token).doubleValue();

				} else if (i == 2) {
					height = new BigDecimal(token).doubleValue();

				} else if (i == 3) {
					length = new BigDecimal(token).doubleValue();

				} else if (i == 4) {
					weight = new BigDecimal(token).doubleValue();

				} else if (i == 5) {

					maxweight = new BigDecimal(token).doubleValue();

				} else if (i == 6) {

					treshold = Integer.parseInt(token);

				}
				i++;
			}

		} else {
			LogMerchantUtil.log(merchantId,
					"Shipping Box information is not configured adequatly");
			throw new Exception("Cannot determine box size");
		}

		List boxes = new ArrayList();

		// maximum number of boxes
		int maxBox = 100;
		int iterCount = 0;

		Collection leftProducts = new ArrayList();

		// need to put items individualy
		Iterator prodIter = products.iterator();
		while (prodIter.hasNext()) {
			OrderProduct op = (OrderProduct) prodIter.next();

			if (!op.isShipping()) {
				continue;
			}

			int qty = op.getProductQuantity();

			Set attrs = op.getOrderattributes();

			// set attributes values
			BigDecimal w = op.getProductWeight();
			if (attrs != null && attrs.size() > 0) {
				Iterator attributesIterator = attrs.iterator();
				OrderProductAttribute opa = (OrderProductAttribute) attributesIterator
						.next();
				w = w.add(opa.getProductAttributeWeight());
			}

			if (qty > 1) {

				for (int i = 1; i <= qty; i++) {
					OrderProduct tempop = new OrderProduct();
					tempop.setProductHeight(op.getProductHeight());
					tempop.setProductLength(op.getProductLength());
					tempop.setProductWidth(op.getProductWidth());
					tempop.setProductWeight(w);
					tempop.setProductQuantity(1);
					tempop.setOrderattributes(attrs);
					leftProducts.add(tempop);
				}
			} else {
				op.setProductWeight(w);
				leftProducts.add(op);
			}
			iterCount++;
		}

		if (iterCount == 0) {
			return null;
		}

		int productCount = leftProducts.size();

		if (productCount < treshold) {
			throw new Exception("Number of item smaller than treshold");
		}

		List usedBoxesList = new ArrayList();

		PackingBox b = new PackingBox();
		// set box max volume
		double maxVolume = width * length * height;

		if (maxVolume == 0 || maxweight == 0) {
			LogMerchantUtil.log(merchantId,
					"Check shipping box configuration, it has a volume of "
							+ maxVolume + " and a maximum weight of "
							+ maxweight
							+ ". Those values must be greater than 0.");
		}
		b.setVolumeLeft(maxVolume);
		b.setWeightLeft(maxweight);

		usedBoxesList.add(b);

		int boxCount = 1;
		Collection assignedProducts = new ArrayList();

		// calculate the volume for the next object
		if (assignedProducts.size() > 0) {
			leftProducts.removeAll(assignedProducts);
			assignedProducts = new ArrayList();
		}
		Iterator prodIterator = leftProducts.iterator();

		boolean productAssigned = false;

		while (prodIterator.hasNext()) {
			OrderProduct op = (OrderProduct) prodIterator.next();
			Collection attributes = op.getOrderattributes();
			productAssigned = false;

			double productWeight = op.getProductWeight().doubleValue();


			// validate if product fits in the box
			if (op.getProductWidth().doubleValue() > width
					|| op.getProductHeight().doubleValue() > height
					|| op.getProductLength().doubleValue() > length) {
				// log message to customer
				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has a demension larger than the box size specified. Will use per item calculation.");
				// exit this process and let shipping calculator calculate
				// individual items
				throw new Exception(
						"Product configuration exceeds box configuraton");
			}

			if (productWeight > maxweight) {
				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has a weight larger than the box maximum weight specified. Will use per item calculation.");
				throw new Exception("Product weight exceeds box maximum weight");
			}

			double productVolume = (op.getProductWidth().doubleValue()
					* op.getProductHeight().doubleValue() * op
					.getProductLength().doubleValue());

			if (productVolume == 0) {
				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has one of the dimension set to 0 and therefore cannot calculate the volume");
				throw new Exception("Cannot calculate volume");
			}

			List boxesList = usedBoxesList;

			// try each box
			Iterator boxIter = boxesList.iterator();
			while (boxIter.hasNext()) {
				PackingBox pb = (PackingBox) boxIter.next();
				double volumeLeft = pb.getVolumeLeft();
				double weightLeft = pb.getWeightLeft();

				if (pb.getVolumeLeft() >= productVolume
						&& pb.getWeightLeft() >= productWeight) {// fit the item
																	// in this
																	// box
					// fit in the current box
					volumeLeft = volumeLeft - productVolume;
					pb.setVolumeLeft(volumeLeft);
					weightLeft = weightLeft - productWeight;
					pb.setWeightLeft(weightLeft);

					assignedProducts.add(op);
					productCount--;

					double w = pb.getWeight();
					w = w + productWeight;
					pb.setWeight(w);
					productAssigned = true;
					maxBox--;
					break;

				}

			}

			if (!productAssigned) {// create a new box

				b = new PackingBox();
				// set box max volume
				b.setVolumeLeft(maxVolume);
				b.setWeightLeft(maxweight);

				usedBoxesList.add(b);

				double volumeLeft = b.getVolumeLeft() - productVolume;
				b.setVolumeLeft(volumeLeft);
				double weightLeft = b.getWeightLeft() - productWeight;
				b.setWeightLeft(weightLeft);
				assignedProducts.add(op);
				productCount--;
				double w = b.getWeight();
				w = w + productWeight;
				b.setWeight(w);
				maxBox--;
			}

		}

		// now prepare the shipping info

		// number of boxes

		Iterator ubIt = usedBoxesList.iterator();

		System.out.println("###################################");
		System.out.println("Number of boxex " + usedBoxesList.size());
		System.out.println("###################################");

		while (ubIt.hasNext()) {
			PackingBox box = (PackingBox) ubIt.next();
			PackageDetail details = new PackageDetail();
			details.setShippingHeight(height);
			details.setShippingLength(length);
			details.setShippingWeight(weight + box.getWeight());
			details.setShippingWidth(width);
			boxes.add(details);
		}

		return boxes;

	}

	public void storeConfiguration(int merchantId, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {

		// get the store information
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(merchantId);

		// id - merchantId - SHP_PACK - packing-item/packing/box - [values] -
		// null - packing-item/packing/box

		// validate submited values
		// box_maxweight
		// box_weight
		// box_length
		// box_height
		// box_width
		
		Locale locale = LocaleUtil.getLocale(request);

		StringBuffer buf = new StringBuffer();

		try {
			BigDecimal width = CurrencyUtil.validateMeasure(request
					.getParameter("box_width"), store.getCurrency());
			// int width = Integer.parseInt();
			buf.append(width.toString()).append("|");
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.width"));
		}

		try {
			BigDecimal height = CurrencyUtil.validateMeasure(request
					.getParameter("box_height"), store.getCurrency());
			// int height =
			// Integer.parseInt(request.getParameter("box_height"));
			buf.append(height.toString()).append("|");
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.height"));
		}

		try {
			BigDecimal length = CurrencyUtil.validateMeasure(request
					.getParameter("box_length"), store.getCurrency());
			// int length =
			// Integer.parseInt(request.getParameter("box_length"));
			buf.append(length.toString()).append("|");
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.length"));
		}

		try {
			BigDecimal weight = CurrencyUtil.validateMeasure(request
					.getParameter("box_weight"), store.getCurrency());

			buf.append(weight.toString()).append("|");
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.weight"));
		}

		try {
			BigDecimal maxweight = CurrencyUtil.validateMeasure(request
					.getParameter("box_maxweight"), store.getCurrency());

			buf.append(maxweight.toString()).append("|");
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.maxweight"));
		}

		try {
			int treshold = Integer.parseInt(request
					.getParameter("box_treshold"));
			buf.append(treshold);
		} catch (Exception e) {
			throw new ValidationException(LabelUtil.getInstance().getText(
					locale, "module.box.invalid.treshold"));
		}

		ConfigurationRequest vr = new ConfigurationRequest(merchantId,
				"SHP_PACK");
		ConfigurationResponse resp = mservice.getConfiguration(vr);

		MerchantConfiguration conf = null;
		if (resp == null || resp.getMerchantConfiguration("SHP_PACK") == null) {

			conf = new MerchantConfiguration();

		} else {
			conf = resp.getMerchantConfiguration("SHP_PACK");
		}

		conf.setMerchantId(merchantId);
		conf.setConfigurationKey("SHP_PACK");
		conf.setConfigurationValue("packing-box");
		conf.setConfigurationValue1(buf.toString());

		mservice.saveOrUpdateMerchantConfiguration(conf);

	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo) {

		// nothing specific

		return vo;
	}

}

class PackingBox {

	private double volumeLeft;
	private double weightLeft;
	private double weight;

	public double getVolumeLeft() {
		return volumeLeft;
	}

	public void setVolumeLeft(double volumeLeft) {
		this.volumeLeft = volumeLeft;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeightLeft() {
		return weightLeft;
	}

	public void setWeightLeft(double weightLeft) {
		this.weightLeft = weightLeft;
	}

}

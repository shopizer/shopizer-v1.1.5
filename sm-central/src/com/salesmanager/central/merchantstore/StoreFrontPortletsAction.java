/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.merchantstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MerchantConfigurationUtil;

public class StoreFrontPortletsAction extends BaseAction {

	private static Logger log = Logger
			.getLogger(StoreFrontPortletsAction.class);

	Collection<CoreModuleService> portlets;
	Map<String, String> selectedPortlets;
	Map<String, String> configuredPortlets;

	Collection customPortlets;

	String[] selection;// selected portlets
	String[] selectionCustomPortlets;// selection custom portlets

	private List<String> descriptions = new ArrayList<String>();

	private DynamicLabel label = null;

	private Map portletsPositions = null;

	private MerchantConfiguration mc = null;

	public MerchantConfiguration getMc() {
		return mc;
	}

	public void setMc(MerchantConfiguration mc) {
		this.mc = mc;
	}

	public String[] getSelection() {
		return selection;
	}

	public void setSelection(String[] selection) {
		this.selection = selection;
	}

	public Map<String, String> getSelectedPortlets() {
		return selectedPortlets;
	}

	public void setSelectedPortlets(Map<String, String> selectedPortlets) {
		this.selectedPortlets = selectedPortlets;
	}

	public Collection<CoreModuleService> getPortlets() {
		return portlets;
	}

	public void setPortlets(Collection<CoreModuleService> portlets) {
		this.portlets = portlets;
	}

	public String display() {

		try {
			
			super.setPageTitle("label.storefront.portletsconfig");

			// get portlets
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			portlets = rservice.getCoreModules(
					CatalogConstants.STORE_FRONT_PORTLETS_CODE, "XX");
			Collections.reverse((List) portlets);

			// get selection
			ConfigurationRequest request = new ConfigurationRequest(super
					.getContext().getMerchantid(), true,
					ConfigurationConstants.STORE_PORTLETS_);
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse vo = mservice.getConfiguration(request);

			if (vo != null) {
				List configurations = vo.getMerchantConfigurationList();

				if (configurations != null && configurations.size() > 0) {

					Iterator i = configurations.iterator();

					while (i.hasNext()) {

						MerchantConfiguration conf = (MerchantConfiguration) i
								.next();

						if (conf.getConfigurationKey().equals(
								ConfigurationConstants.STORE_PORTLETS_)) {
							mc = conf;
							Collection portletsList = MerchantConfigurationUtil
									.getConfigurationList(mc
											.getConfigurationValue(), ";");
							if (portletsList != null && portletsList.size() > 0) {
								Map returnMap = new HashMap();
								Iterator ii = portletsList.iterator();
								while (ii.hasNext()) {
									String p = (String) ii.next();
									returnMap.put(p, p);
								}
								selectedPortlets = returnMap;
							}
							continue;
						}

						if (conf.getConfigurationModule() != null) {
							this.configuredPortlets.put(conf
									.getConfigurationModule(), conf
									.getConfigurationModule());
						}
					}

				}
			}

			// get custom portlets
			customPortlets = rservice.getDynamicLabels(super.getContext()
					.getMerchantid(),
					LabelConstants.STORE_FRONT_CUSTOM_PORTLETS, super
							.getLocale());

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String customPortletsDetails() {
		
		super.setPageTitle("label.storefront.portletsconfig");

		try {

			super.prepareLanguages();

			this.preparePortletsPositions();
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			if (label != null) {

				// get label

				label = rservice.getDynamicLabel(label.getDynamicLabelId());

				Set descriptionsSet = label.getDescriptions();

				Map descriptionsMap = new HashMap();

				if (descriptionsSet != null) {

					for (Object desc : descriptionsSet) {
						DynamicLabelDescription description = (DynamicLabelDescription) desc;
						descriptionsMap.put(
								description.getId().getLanguageId(),
								description);
					}

					// iterate through languages for appropriate order
					for (int count = 0; count < reflanguages.size(); count++) {
						int langid = (Integer) reflanguages.get(count);
						DynamicLabelDescription description = (DynamicLabelDescription) descriptionsMap
								.get(langid);
						if (description != null) {
							descriptions.add(description
									.getDynamicLabelDescription());
						}
					}
				}

			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	private void preparePortletsPositions() {
		

		LabelUtil l = LabelUtil.getInstance();

		portletsPositions = new HashMap();
		portletsPositions.put(LabelConstants.LABEL_POSITION_LEFT, l.getText(
				super.getLocale(), "label.generic.position.left"));
		portletsPositions.put(LabelConstants.LABEL_POSITION_RIGHT, l.getText(
				super.getLocale(), "label.generic.position.right"));
		portletsPositions.put(LabelConstants.LABEL_POSITION_BOTTOM_LANDING, l
				.getText(super.getLocale(),
						"label.merchantstore.position.bottom.landing"));


	}

	public String savePortlet() {

		try {

			super.setPageTitle("label.storefront.portletsconfig");
			
			boolean hasError = false;

			super.prepareLanguages();

			this.preparePortletsPositions();

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			if (label == null) {
				label = new DynamicLabel();
			}
			
			if (this.label.getSortOrder()==null) {
				super.addFieldError("label.sortOrder",
						getText("invalid.fieldvalue.sortorder"));
				hasError = true;
			}

			if (StringUtils.isBlank(this.label.getTitle())) {
				super.addFieldError("label.title",
						getText("error.message.storefront.portletidrequired"));
				hasError = true;
			}

			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();

				String description = (String) this.getDescriptions().get(
						langcount);

				int submitedlangid = (Integer) reflanguages.get(langcount);
				// create
				DynamicLabelDescriptionId id = new DynamicLabelDescriptionId();
				id.setLanguageId(submitedlangid);
				if (label != null) {
					id.setDynamicLabelId(label.getDynamicLabelId());
				}

				DynamicLabelDescription dldescription = new DynamicLabelDescription();
				dldescription.setId(id);
				dldescription.setDynamicLabelDescription(description);
				dldescription.setDynamicLabelTitle("-");

				Set descs = label.getDescriptions();
				if (descs == null) {
					descs = new HashSet();
				}

				descs.add(dldescription);

				label.setMerchantId(super.getContext().getMerchantid());
				label.setSectionId(LabelConstants.STORE_FRONT_CUSTOM_PORTLETS);
				label.setDescriptions(descs);

			}

			if (hasError) {
				return INPUT;
			}

			rservice.saveOrUpdateDynamicLabel(label);

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String saveCustomPortlets() {

		try {
			
			super.setPageTitle("label.storefront.portletsconfig");

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Collection<DynamicLabel> labels = rservice.getDynamicLabels(super
					.getContext().getMerchantid(),
					LabelConstants.STORE_FRONT_CUSTOM_PORTLETS);

			if (labels != null && labels.size()>0) {

				for (Object o : labels) {

					DynamicLabel dl = (DynamicLabel) o;
					String[] labelIds = this.getSelectionCustomPortlets();

					if (labelIds != null && labelIds.length > 0) {

						boolean found = false;
						for (int i = 0; i < labelIds.length; i++) {
							String sId = labelIds[i];
							try {
								long id = Long.parseLong(sId);
								if (dl.getDynamicLabelId() == id) {
									found = true;
								}

							} catch (Exception e) {
								log.error("Wrong id " + sId);
								if (sId.equals("false")) {
									dl.setVisible(false);
								} else {
									dl.setVisible(true);
								}
							}

						}
						if (found == true) {
							dl.setVisible(true);
						} else {
							dl.setVisible(false);
						}

					} else {
						dl.setVisible(false);
					}
				}

				rservice.saveDynamicLabel(labels);
				super.setSuccessMessage();
			}

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}
		this.display();// prepare display elements
		return SUCCESS;

	}

	public String deleteCustomPortlet() {

		try {
			
			super.setPageTitle("label.storefront.portletsconfig");

			super.prepareLanguages();
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			DynamicLabel l = rservice.getDynamicLabel(this.getLabel()
					.getDynamicLabelId());
			if (l != null) {
				if (l.getMerchantId() == super.getContext().getMerchantid()) {
					rservice.deleteDynamicLabel(l);
				}
			}

			this.display();
			super.setSuccessMessage();

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			this.display();
		}

		return SUCCESS;

	}

	public String save() {

		try {
			
			super.setPageTitle("label.storefront.portletsconfig");

			this.display();
			// save selected protlets

			// get selection first
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			if (selection == null) {

				if (mc != null) {
					mservice.deleteMerchantConfiguration(mc);
					this.selectedPortlets = null;
					super.setSuccessMessage();
					return SUCCESS;
				} else {
					return SUCCESS;
				}
			}

			List l = Arrays.asList(selection);

			String line = MerchantConfigurationUtil.buildConfigurationLine(l,
					";");

			Collection portletsList = MerchantConfigurationUtil
					.getConfigurationList(line, ";");
			if (portletsList != null && portletsList.size() > 0) {
				Map returnMap = new HashMap();
				Iterator i = portletsList.iterator();
				while (i.hasNext()) {
					String p = (String) i.next();
					returnMap.put(p, p);
				}
				selectedPortlets = returnMap;
			}

			if (mc == null) {
				mc = new MerchantConfiguration();
				mc.setConfigurationKey(ConfigurationConstants.STORE_PORTLETS_);
				mc.setDateAdded(new Date());
				mc.setLastModified(new Date());
				mc.setMerchantId(super.getContext().getMerchantid());
			}

			mc.setConfigurationValue(line);
			mservice.saveOrUpdateMerchantConfiguration(mc);

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;

	}

	public Map<String, String> getConfiguredPortlets() {
		return configuredPortlets;
	}

	public void setConfiguredPortlets(Map<String, String> configuredPortlets) {
		this.configuredPortlets = configuredPortlets;
	}

	public Collection getCustomPortlets() {
		return customPortlets;
	}

	public void setCustomPortlets(Collection customPortlets) {
		this.customPortlets = customPortlets;
	}

	public String[] getSelectionCustomPortlets() {
		return selectionCustomPortlets;
	}

	public void setSelectionCustomPortlets(String[] selectionCustomPortlets) {
		this.selectionCustomPortlets = selectionCustomPortlets;
	}

	public DynamicLabel getLabel() {
		return label;
	}

	public void setLabel(DynamicLabel label) {
		this.label = label;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public Map getPortletsPositions() {
		return portletsPositions;
	}

	public void setPortletsPositions(Map portletsPositions) {
		this.portletsPositions = portletsPositions;
	}

}

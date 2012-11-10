/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Dec 7, 2010 Consultation CS-TI inc. 
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.entity.system.FieldOption;



/**
 * JSON to String helper class
 * @author Carl Samson
 *
 */
public class ConfigurationFieldUtil {
	
	private static Logger log = Logger.getLogger(ConfigurationFieldUtil.class);
	
	
	
	public static String getMerchantConfigurationKey(String page, String module) {
		
		return new StringBuilder().append(ConfigurationConstants.PAGE_PORTLET_PREFIX).append(page).append("_").append(module).toString();
	}
	
	public static String getMerchantConfigurationKeyLike(String page) {
		
		return new StringBuilder().append(ConfigurationConstants.PAGE_PORTLET_PREFIX).append(page).append("_").toString();
	}
	
	/**
	 * This method de-serializes fields instructions formated as JSON objects
	 * Multiple fields instructions can be de-serialized using this class
	 * The string must be formated as an array of fields
	 * {"fields":[field,field]}
	 * The system currently supports Text, Select, Radio and checkbox fields
	 * Text field:
	 * {"field":{"type":"text","name":"nameOfTheField","label":"Field Label"}}
	 * Select field
	 * {"field":{"type":"select","name":"option",,"label":"Field Label","values":[{"name":"option label","value":"option value","isDefault":"false"},{"name":"option label2","value":"option value2","isDefault":"false"}]}}
	 * Radio field
	 * {"field":{"type":"radio","name":"option","label":"Field Label","values":[{"name":"option label","value":"option value","isDefault":"false"},{"name":"option label2","value":"option value2","isDefault":"false"}]}}
	 * Returns a Map of String[fieldName] and Field field content
	 * 
	 * Full example
	 * {"fields":[{"field":{"type":"text","label":"Invitation type text","name":"invitationType"}},{"field":{"type":"text","label":"Action text","name":"actionText"}},{"field":{"type":"select","label":"Select Action text","name":"selectActionText","values":[{"name":"select1","value":"option value1","isDefault":"false"},{"name":"select2","value":"option value2","isDefault":"false"}]}},{"field":{"type":"radio","label":"Radio Action text","name":"radioActionText","values":[{"name":"radioActionText","value":"yes","isDefault":"false"},{"name":"radioActionText","value":"no","isDefault":"true"}]}},{"field":{"type":"checkbox","label":"Checkbox Action text","name":"checkboxActionText"}}]}
	 * 
	 * Returns Map<String->fieldName,Field>
	 * @author Carl Samson
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Field> parseFields(String fields) throws Exception {
		
		Map returnMap = new HashMap();

			Map<String, String> data = new ObjectMapper().readValue(fields, HashMap.class);
			
			if(data!=null) {
				for(Object o: data.keySet()) {

					if(o instanceof String && ((String)o).equals("fields")) {
						// can parse
						
						Object oo = data.get(o);
						if(oo instanceof List) {//List 
							
							for(Object ooo:(List)oo) {

								if(ooo instanceof LinkedHashMap) {
									
									Map m = (Map)ooo;
									
									//get each fields
									Map field = (Map)m.get("field");
									
									//get field type
									String type = (String)field.get("type");
									String name = (String)field.get("name");
									String label = (String)field.get("label");
									
									//System.out.println("This field is of type [" + type + "] and name [" + name + "]");
									
									
									Field f = new Field();
									f.setName(name);
									f.setType(type);
									f.setLabel(label);
									returnMap.put(name, f);
									
									
									List valuesList = (List)field.get("values");
									
									if(valuesList!=null) {
										
										for(Object oooo:valuesList) {
											
											FieldOption fo = new FieldOption();
											
											
											
											Map values = (Map)oooo;
											String valueName = (String)values.get("name");
											String valueValue = (String)values.get("value");
											String defaultOption = (String)values.get("isDefault");
											
											boolean def = false;
											
											try {
												def = new Boolean(defaultOption).booleanValue();
											} catch (Exception e) {
												log.error("Invalid value for isDefault " + name);
											}
											
											//System.out.println("This field is of type [" + type + "] has option [" + valueName + "] and value [" + valueValue + "]");
											
											fo.setName(valueName);
											fo.setValue(valueValue);
											fo.setDefaultOption(def);
											
											
											
											f.addFieldOption(fo);
											
										}
										
									}
									
								}
							}
						}
					}
						
				}

			}
			
			return returnMap;
	}
	
	/**
	 * Accepts a list of json string and creates a full json object, then
	 * returns a Map<String->module,List<Field>)
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public static Map<String,List<Field>> parseFieldsValues(List<String> stringFields) throws Exception {
		
		
		Map returnMap = new HashMap();
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"page\":\"p\",\"modules\":[");
		int i = 0;
		for(Object o:stringFields) {
			
			String s = (String)o;
			sb.append(s);
			if(i<stringFields.size()-1) {
				sb.append(",");
			}
			i++;
			
		}
		sb.append("]}");

		Map<String, String> data = new ObjectMapper().readValue(sb.toString(), HashMap.class);
		
		
		if(data!=null) {
			for(Object o: data.keySet()) {

				if(o instanceof String && ((String)o).equals("modules")) {
					// can parse
					
					Object oo = data.get(o);
					if(oo instanceof List) {//List 
						
						for(Object ooo:(List)oo) {

							if(ooo instanceof LinkedHashMap) {
								
								Map m = (Map)ooo;
								
								String module = (String)m.get("module");
							
								List valuesList = (List)m.get("values");
								
								List returnList = new ArrayList();
								
								if(valuesList!=null) {
									
									for(Object oooo:valuesList) {
										
										Field f = new Field();
										
										
										Map values = (Map)oooo;
										String valueName = (String)values.get("name");
										String valueValue = (String)values.get("value");
										
										f.setName(valueName);
										f.setFieldValue(valueValue);

										returnList.add(f);
									}
								}
								
								returnMap.put(module, returnList);
							}
						}
					}
				}
			}
		}
		
		
		
		return returnMap;
	}
	
	/**
	 * {"fields":[{"module":"moduleName","values":[{"name":"fieldName","value":"fieldValue"}...]}...]}
	 * @param fields
	 * @return String->module, List<Field>
	 * @throws Exception
	 */
	public static Map<String,List<Field>> parseFieldsValues(String fields) throws Exception {
		
		Map returnMap = new HashMap();

		Map<String, String> data = new ObjectMapper().readValue(fields, HashMap.class);
		
		if(data!=null) {
			for(Object o: data.keySet()) {

				if(o instanceof String && ((String)o).equals("fields")) {
					// can parse
					
					Object oo = data.get(o);
					if(oo instanceof List) {//List 
						
						for(Object ooo:(List)oo) {

							if(ooo instanceof LinkedHashMap) {
								
								Map m = (Map)ooo;
								
								String module = (String)m.get("module");
							
								List valuesList = (List)m.get("values");
								
								List returnList = new ArrayList();
								
								if(valuesList!=null) {
									
									for(Object oooo:valuesList) {
										
										Field f = new Field();
										
										
										Map values = (Map)oooo;
										String valueName = (String)values.get("name");
										String valueValue = (String)values.get("value");
										
										f.setName(valueName);
										f.setFieldValue(valueValue);

										returnList.add(f);
									}
								}
								
								returnMap.put(module, returnList);
							}
						}
					}
				}
			}
		}
			return returnMap;
	}
	
	/**
	 * Builds a JSON string with all fields values for a given module
	 * @param fieldValues
	 * @return
	 */
	public static String buildFieldValuesString(Map<String,List<Field>> fieldValues) {
		
		
		if(fieldValues==null) 
			return null;
		
		if(fieldValues.size()==0) 
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		
		for(Object o: fieldValues.keySet()){
			
			String module = (String)o;
			
			//{"fields":[{"module":"moduleName","values":[{"name":"fieldName","value":"fieldValue"}...]}...]}
			sb.append("\"module\":\"").append(module).append("\",");
			List fv = fieldValues.get(module);
			int i = 0;
			if(fv!=null && fv.size()>0) {
				sb.append("\"values\":[");
				for(Object v:fv) {
					
					Field f = (Field)v;
					sb.append("{\"name\":\"").append(f.getName()).append("\",\"value\":\"").append(f.getFieldValue()).append("\"}");
					if(i<fv.size()-1) {
						sb.append(",");
					}
					i++;
				}
				sb.append("]");
			}
		}
		sb.append("}");
		
		return sb.toString();
		
	}

}

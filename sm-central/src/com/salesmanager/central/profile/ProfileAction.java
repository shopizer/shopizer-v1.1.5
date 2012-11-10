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
package com.salesmanager.central.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.exception.ConstraintViolationException;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.CountrySelectBaseAction;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.IMerchant;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.merchant.MerchantUserRole;
import com.salesmanager.core.entity.merchant.MerchantUserRoleDef;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.module.model.application.PasswordGeneratorModule;
import com.salesmanager.core.module.model.application.SecurityQuestionsModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;

/**
 * Manage User Profile
 * 
 * @author Carl Samson
 * 
 */
public class ProfileAction extends CountrySelectBaseAction {

	private Logger log = Logger.getLogger(ProfileAction.class);

	private MerchantUserInformation merchantProfile;
	private Integer countryCode;
	private String ccMonth;
	private String ccYear;
	private String securityCode;

	private String newPassword;
	private String repeatNewPassword;
	
	//when submiting reset password
	private String merchantId;
	private String adminName;
	
	private boolean resetPasswordResponse;
	
	private Map securityQuestions = new HashMap();
	private List<Integer> answers = new ArrayList<Integer>();
	private List<String> answersText = new ArrayList<String>();
	private List<String> questionsText = new ArrayList<String>();
	private boolean passwordResetSuccess = false;
	private String merchantPassword = "";
	
	Collection<MerchantUserInformation> merchantUserInformations;
	
	//security
	private Map<String,String> roles = new TreeMap();
	private Map<String,String> masterRoles = new TreeMap();
	private Collection<String> userRoles = new ArrayList();
	
	private String submitMasterRole = null;
	private List<String> submitRoles = new ArrayList();
	
	
	
	public String resetPassword() {
		
		
		try {
			
			int merchantId = Integer.parseInt(this.getMerchantId());
			

			//log attempt
			LogMerchantUtil.log(merchantId, "Attempt to change password for user name " + this.getAdminName());
			
			//validate merchant id admin
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantUserInformation merchantUserInformation = mservice.getMerchantUserInformation(this.getAdminName());
			MerchantStore store = mservice.getMerchantStore(merchantId);
			
			if(merchantUserInformation!=null) {
				if(merchantUserInformation.getMerchantId()!=merchantId) {
					LogMerchantUtil.log(merchantId, "Attempt to change password for user name " + this.getAdminName() + " and inputed wrong merchantId " + merchantId);
				}
			}
			
			//create email link
			Configuration config = PropertiesUtil.getConfiguration();
			String l = config.getString("core.system.defaultlanguage", "en");
			if (!StringUtils.isBlank(merchantUserInformation.getUserlang())) {
				l = merchantUserInformation.getUserlang();
			}

			LabelUtil lhelper = LabelUtil.getInstance();
			lhelper.setLocale(super.getLocale());
			String subject = lhelper.getText(l, "label.admin.resetpassword.subject");
			String message = lhelper.getText(l, "label.admin.resetpassword");

			String centralUri = ReferenceUtil.buildCentralUri(store);

			String link = FileUtil.getAdminPasswordResetUrl(merchantUserInformation,store);
			
			String url = "<a href=\""
					+ link
					+ "\">"
					+ link
					+ "</a>";

			Map emailctx = new HashMap();
			emailctx.put("EMAIL_PASSWORD_TEXT", message);
			emailctx.put("EMAIL_PASSWORD_LINK", url);

			CommonService cservice = new CommonService();
			cservice.sendHtmlEmail(merchantUserInformation.getAdminEmail(), subject,
					store, emailctx,
					"email_template_user_password_link.ftl",merchantUserInformation.getAdminEmail());
			
			//send email
			
		} catch (Exception e) {
			log.error(e);
			resetPasswordResponse = false;
			return SUCCESS;
		}

		resetPasswordResponse = true;
		return SUCCESS;

	}
	
	public String displayResetPassword() {
		

		try {
			
			super.setPageTitle("security.question.title");
			
			//parse tokens
			String fileId = getServletRequest().getParameter("urlId");
			String lang = getServletRequest().getParameter("lang");
			
			Locale l = LocaleUtil.getDefaultLocale();
			try {
				l = new Locale(Constants.CA_ISOCODE,lang);
			} catch (Exception e) {
				log.warn("Wrong locale language " + lang);
			}
			
			
			super.setLocale(l);

			if (StringUtils.isBlank(fileId)) {
				List msg = new ArrayList();
				msg.add(getText("error.invalid.url"));
				super.setActionErrors(msg);
				return "ANONYMOUSGENERICERROR";
			}
			
			Map fileInfo = FileUtil.getUrlTokens(fileId);

			String id = (String) fileInfo.get("ID");
			String date = (String) fileInfo.get("DATE");
			
			if(StringUtils.isBlank(id) || StringUtils.isBlank(date)) {
				List msg = new ArrayList();
				msg.add(getText("error.invalid.url"));
				super.setActionErrors(msg);
				return "ANONYMOUSGENERICERROR";
			}
			
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantUserInformation merchantUserInformation = mservice.getMerchantUserInformation(new Long(id));
			this.setMerchantProfile(merchantUserInformation);
			
			if(merchantUserInformation==null) {
				log.error("merchantUserInformation " + id + " is null");
				List msg = new ArrayList();
				msg.add(getText("error.invalid.url"));
				super.setActionErrors(msg);
				return "ANONYMOUSGENERICERROR";
			}
			
			//display security questions
			SecurityQuestionsModule module = (SecurityQuestionsModule)SpringUtil.getBean("securityQuestions");
			Map questions = module.getSecurityQuestions(super.getLocale());
			this.setSecurityQuestions(questions);
			
			questionsText.add(0, module.getQuestionText(1,super.getLocale()));
			questionsText.add(1, module.getQuestionText(2,super.getLocale()));
			questionsText.add(2, module.getQuestionText(3,super.getLocale()));
			

			//set merchantUserId in http session
			HttpSession session = super.getServletRequest().getSession(true);
			session.setAttribute("merchantUserId", merchantUserInformation.getMerchantUserId());

			
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "ANONYMOUSGENERICERROR";
		}
		
		return SUCCESS;
		
	}
	
	public String saveSecurityQuestions() {
		
		if(this.getMerchantProfile()==null) {
			log.error("MerchantUserId not submited");
			super.setTechnicalMessage();
			return INPUT;
		}
		
		try {
			//this will submit the questions and the answers
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantUserInformation merchantUserInformation = mservice.getMerchantUserInformation(this.getMerchantProfile().getMerchantUserId());
			
			if(merchantUserInformation==null) {
				log.error("MerchantUserInformation is null");
				super.setTechnicalMessage();
				return INPUT;
			}
			
			int i = 0;
			for(int j = 0; j < answers.size(); j++) {
				
				Integer questionId = answers.get(j);
				String answer = answersText.get(j);
				
				//only 3 are stored in merchant user
				if(i==0) {
					merchantUserInformation.setSecurityQuestion1(String.valueOf(questionId));
					merchantUserInformation.setSecurityAnswer1(answer.trim().toLowerCase());
				} else if(i==1) {
					merchantUserInformation.setSecurityQuestion2(String.valueOf(questionId));
					merchantUserInformation.setSecurityAnswer2(answer.trim().toLowerCase());
				} else if(i==2) {
					merchantUserInformation.setSecurityQuestion3(String.valueOf(questionId));
					merchantUserInformation.setSecurityAnswer3(answer.trim().toLowerCase());
				} else {
					break;
				}
				
				i++;
			}
			
			if(
			
				!StringUtils.isBlank(merchantUserInformation.getSecurityQuestion1())
					
				&& !StringUtils.isBlank(merchantUserInformation.getSecurityQuestion2())
				
						&& !StringUtils.isBlank(merchantUserInformation.getSecurityQuestion3())
								
							&&	(merchantUserInformation.getSecurityQuestion1().equals(merchantUserInformation.getSecurityQuestion2()) || merchantUserInformation.getSecurityQuestion2().equals(merchantUserInformation.getSecurityQuestion3()) || merchantUserInformation.getSecurityQuestion1().equals(merchantUserInformation.getSecurityQuestion3())))

			{
				
				super.setErrorMessage("security.questions.differentmessages");
				return INPUT;
			}


			mservice.saveOrUpdateMerchantUserInformation(merchantUserInformation);
			this.setMerchantProfile(merchantUserInformation);
			
			super.setSuccessMessage();
			
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String answerQuestions() {
		
		super.setPageTitle("security.question.title");
		
		SecurityQuestionsModule module = (SecurityQuestionsModule)SpringUtil.getBean("securityQuestions");
		Map questions = module.getSecurityQuestions(super.getLocale());
		this.setSecurityQuestions(questions);
		
		questionsText.add(0, module.getQuestionText(1,super.getLocale()));
		questionsText.add(1, module.getQuestionText(2,super.getLocale()));
		questionsText.add(2, module.getQuestionText(3,super.getLocale()));
		
		

		
		try {
			
			HttpSession session = super.getServletRequest().getSession(true);
			Long merchantUserId = (Long)session.getAttribute("merchantUserId");

			
			if(merchantUserId==null) {
				log.error("MerchantUserInformationId is null");
				super.setTechnicalMessage();
				return INPUT;
			}
			
			//this will submit the questions and the answers
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			MerchantUserInformation merchantUserInformation = mservice.getMerchantUserInformation(merchantUserId);
			
			if(merchantUserInformation==null) {
				log.error("MerchantUserInformation is null");
				super.setTechnicalMessage();
				return INPUT;
			}
			
			int i = 0;
			for(int j = 0; j < answersText.size(); j++) {
				

				String answer = answersText.get(j);
				
				//only 3 are stored in merchant user
				if(i==0) {
					if(!answer.trim().toLowerCase().equals(merchantUserInformation.getSecurityAnswer1())) {
						super.setErrorMessage("message.invalid.security.answers");
						return INPUT;
					}
				} else if(i==1) {
					if(!answer.trim().toLowerCase().equals(merchantUserInformation.getSecurityAnswer2())) {
						super.setErrorMessage("message.invalid.security.answers");
						return INPUT;
					}
				} else if(i==2) {
					if(!answer.trim().toLowerCase().equals(merchantUserInformation.getSecurityAnswer3())) {
						super.setErrorMessage("message.invalid.security.answers");
						return INPUT;
					}
				} else {
					break;
				}
				
				i++;
			}
			

			
			//generate new password
			
			PasswordGeneratorModule passwordGenerator = (PasswordGeneratorModule) SpringUtil
			.getBean("passwordgenerator");

			// encrypt
			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			boolean found = true;

			String password = null;
			String encrypted = null;
			// validate if already exist
			while (found) {

				password = passwordGenerator.generatePassword();
				encrypted = EncryptionUtil.encrypt(key, password);
				MerchantUserInformation info =mservice.getMerchantInformationByUserNameAndPassword(
						merchantUserInformation.getAdminName(), encrypted);
				if (info == null) {
					found = false;
				}
			}
			
			merchantPassword = password;
			
			
			merchantUserInformation.setAdminPass(encrypted);
			mservice.saveOrUpdateMerchantUserInformation(merchantUserInformation);
			this.setMerchantProfile(merchantUserInformation);
			
			passwordResetSuccess = true;
			
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}
		
		return SUCCESS;
		
	}
	

	public String changeLanguage() {

		Context context = super.getContext();

		String language = super.getServletRequest().getParameter("lang");
		if (!StringUtils.isBlank(language)) {

			RefCache cache = RefCache.getInstance();

			Map countries = cache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(context.getLang()));
			Country c = (Country) countries.get(context.getCountryid());

			Locale locale = new Locale(language, c.getCountryIsoCode2());
			super.setLocale(locale);
			
			Context ctx = super.getContext();
			ctx.setLang(locale.getLanguage());
			
		}

		return SUCCESS;

	}

	public String displayPassword() {

		try {
			
			super.setPageTitle("label.changepassword");

			int merchantId = super.getContext().getMerchantid();
			String user = super.getPrincipal().getRemoteUser();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			merchantProfile = mservice.getMerchantUserInformation(user);

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public String changePassword() {

		try {
			
			super.setPageTitle("label.changepassword");

			if (merchantProfile == null) {
				log.error("merchantProfileIsNull");
				super.setTechnicalMessage();
				return INPUT;
			}

			int merchantId = super.getContext().getMerchantid();
			String user = super.getPrincipal().getRemoteUser();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			MerchantUserInformation profile = mservice
					.getMerchantUserInformation(user);

			// new paswords match
			if (StringUtils.isBlank(merchantProfile.getAdminPass())) {
				super.setErrorMessage("messages.required.currentpassword");
				return INPUT;
			}

			if (StringUtils.isBlank(this.getNewPassword())) {
				super.setErrorMessage("messages.required.newpassword");
				return INPUT;
			}

			if (StringUtils.isBlank(this.getRepeatNewPassword())) {
				super.setErrorMessage("messages.required.repeatnewpassword");
				return INPUT;
			}

			if (!this.getNewPassword().equals(this.getRepeatNewPassword())) {
				super.setErrorMessage("messages.password.match");
				return INPUT;
			}

			// 6 to 8 characters
			if (this.getNewPassword().length() < 6
					|| this.getNewPassword().length() > 8) {
				super.setErrorMessage("messages.password.length");
				return INPUT;
			}

			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			String enc = EncryptionUtil.encrypt(key, this.getNewPassword());

			profile.setAdminPass(enc);

			mservice.saveOrUpdateMerchantUserInformation(profile);

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;

	}

	public String display() {


		try {
			
			//@TODO set security questions and answers
			
			super.setPageTitle("label.menu.group.profile");
			
			SecurityQuestionsModule module = (SecurityQuestionsModule)SpringUtil.getBean("securityQuestions");
			Map questions = module.getSecurityQuestions(super.getLocale());
			this.setSecurityQuestions(questions);

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			merchantProfile = mservice.getMerchantUserInformation(super
					.getPrincipal().getUserPrincipal().getName());
			if (merchantProfile == null) {// should be created from the original
											// subscribtion process
				log.error("Profile does not exist for merchantid "
						+ super.getPrincipal().getRemoteUser());
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText("errors.technical"));
				return ERROR;
			}

			// NEED TO SET COUNTRY ID IN THE SESSION IN ORDER TO RETREIVE
			// ASSOCIATE PROVINCES
			if (merchantProfile.getUsercountrycode() == 0) {// original default
															// country code
				Configuration conf = PropertiesUtil.getConfiguration();
				int defaultCountry = conf
						.getInt("core.system.defaultcountryid",
								Constants.US_COUNTRY_ID);
				MessageUtil.addNoticeMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(
								"messages.updateinformation"));
				merchantProfile.setUsercountrycode(defaultCountry);
				super.prepareSelections(defaultCountry);

			} else {

				super.prepareSelections(merchantProfile.getUsercountrycode());
			}
			
			if(!StringUtils.isBlank(merchantProfile.getSecurityQuestion1())
					
				|| !StringUtils.isBlank(merchantProfile.getSecurityQuestion2())
				
				|| !StringUtils.isBlank(merchantProfile.getSecurityQuestion3())
				) {
				
				
			
			answers.add(0,Integer.parseInt(merchantProfile.getSecurityQuestion1()));
			answers.add(1,Integer.parseInt(merchantProfile.getSecurityQuestion2()));
			answers.add(2,Integer.parseInt(merchantProfile.getSecurityQuestion3()));
			
			answersText.add(0, merchantProfile.getSecurityAnswer1());
			answersText.add(1, merchantProfile.getSecurityAnswer2());
			answersText.add(2, merchantProfile.getSecurityAnswer3());
			
		}

			/**
			 * //@todo get credit card //parse expiration date to mm yy format
			 * String ccDate = profile.getCcExpires(); if(ccDate!=null &&
			 * !ccDate.equals("")) { int length = ccDate.length(); String ccm =
			 * null; String ccy = null; if(length==4) { ccm =
			 * ccDate.substring(0,2); ccy = ccDate.substring(3); } else { ccm =
			 * "0" + ccDate.substring(0,1); ccy = ccDate.substring(2); }
			 * this.setCcYear(ccy); this.setCcMonth(ccm);
			 * 
			 * this.setSecurityCode(new String(profile.getCcCvv())); } else {
			 * java.util.Calendar cal = new java.util.GregorianCalendar();
			 * this.setCcYear
			 * (String.valueOf((cal.get(java.util.Calendar.YEAR))));
			 * this.setCcMonth
			 * (String.valueOf((cal.get(java.util.Calendar.MONTH))));
			 * this.setSecurityCode(""); }
			 * 
			 * 
			 * if(profile.getCcNumber()!=null &&
			 * !profile.getCcNumber().trim().equals("")) { //decrypt credit card
			 * String decryptedvalue =
			 * EncryptionUtil.decrypt(EncryptionUtil.generatekey
			 * (String.valueOf(merchantid.intValue())), profile.getCcNumber());
			 * //mask value CreditCardUtil util = new CreditCardUtil(); String
			 * card = util.maskCardNumber(decryptedvalue);
			 * profile.setCcNumber(card); }
			 **/

			// }

		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
		}

		return SUCCESS;

	}

	/**
	 * saveProfile
	 * 
	 * @return "SUCCESS" or "ERROR"
	 * @throws Exception
	 */
	public String saveProfile() throws Exception {


		try {
			
			super.setPageTitle("label.menu.group.profile");
			
			SecurityQuestionsModule module = (SecurityQuestionsModule)SpringUtil.getBean("securityQuestions");
			Map questions = module.getSecurityQuestions(super.getLocale());
			this.setSecurityQuestions(questions);


			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantUserInformation mUserInfo = mservice
					.getMerchantUserInformation(super.getPrincipal()
							.getRemoteUser());

			if (mUserInfo == null) {
				mUserInfo = new MerchantUserInformation();
			}



			java.util.Date dt = new java.util.Date();

			mUserInfo.setAdminEmail(this.merchantProfile.getAdminEmail());
			mUserInfo.setUserfname(this.merchantProfile.getUserfname());
			mUserInfo.setUserlname(this.merchantProfile.getUserlname());
			mUserInfo.setUseraddress(this.merchantProfile.getUseraddress());
			mUserInfo.setUsercity(this.merchantProfile.getUsercity());
			mUserInfo.setUserphone(this.merchantProfile.getUserphone());
			mUserInfo.setUserpostalcode(this.merchantProfile
					.getUserpostalcode());
			mUserInfo.setUserstate(this.merchantProfile.getUserstate());
			mUserInfo.setUsercountrycode(this.merchantProfile
					.getUsercountrycode());
			mUserInfo.setUserlang(this.merchantProfile.getUserlang());

			super.prepareSelections(mUserInfo.getUsercountrycode());

			mUserInfo.setLastModified(new java.util.Date(dt.getTime()));

			mservice.saveOrUpdateMerchantUserInformation(mUserInfo);


			ctx.setLang(mUserInfo.getUserlang());


			super.setSuccessMessage();

		} catch (Exception e) {

			if (e instanceof ConstraintViolationException) {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(
								"messages.emailalreadyexist"));
			} else {
				log.error(e);
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText("errors.technical"));
			}
		}

		return SUCCESS;

	}
	
	public String viewUser() throws Exception {
		
		super.setPageTitle("label.admin.security.manageuser");
		
		//get roles
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());
		
		
		
		//label.admin.security.admin
		//label.admin.security.store
		//label.admin.security.catalog
		//label.admin.security.checkout
		//label.admin.security.order
		
		masterRoles.put("admin", label.getText("label.admin.security.admin"));
		masterRoles.put("user", label.getText("label.admin.security.user"));
		
		//get roles from def
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);

		
		Collection<MerchantUserRoleDef> defs = mservice.getMerchantUserRoleDef();
		for(Object o : defs) {
			
			MerchantUserRoleDef def = (MerchantUserRoleDef)o;
			
			if(!def.getRoleCode().equals("admin") && !def.getRoleCode().equals("superuser")) {
				roles.put(def.getRoleCode(),label.getText("label.admin.security." + def.getRoleCode()));
			}
		}
		
		//see if user exist
		
		MerchantUserInformation userInformation = this.getMerchantProfile();
		
		if(userInformation!=null && userInformation.getMerchantUserId()!=null) {
			

			merchantProfile = mservice.getMerchantUserInformation(userInformation.getMerchantUserId());
			
			super.authorize((IMerchant)merchantProfile);
			

			
			Collection rollColl = mservice.getUserRoles(merchantProfile.getAdminName());
			for(Object o : rollColl) {
				MerchantUserRole r = (MerchantUserRole)o;
				userRoles.add(r.getRoleCode());
			}
		} else {
			
			if(!userRoles.contains("superuser") || !userRoles.contains("admin") || !userRoles.contains("user")) {
				userRoles.add("admin");
			}
			
		}
		
		
		
		//if user == superuser -> admin if user = admin -> admin else -> user and populate checkboxes
		

		
		return SUCCESS;
		
	}
	
	public String saveUser() throws Exception {

		
		if(this.getMerchantProfile()==null) {
			log.error("MerchantUserInformation should not be null");
			super.setTechnicalMessage();
			return INPUT;
		}
		
		this.viewUser();
		
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		if(StringUtils.isBlank(this.getSubmitMasterRole())) {
			//set to admin by default
			this.setSubmitMasterRole("admin");
		}
		
		//see if user exist
		
		if(this.getMerchantProfile()!=null && this.getMerchantProfile().getMerchantUserId()!=null && this.getMerchantProfile().getMerchantUserId().longValue() >0) {
			
			//Collection rollColl = mservice.getUserRoles(merchantProfile.getAdminName());

			mservice.deleteUserRoles(merchantProfile.getAdminName());
			
		} 
		
		if(this.getMerchantProfile()!=null && this.getMerchantProfile().getMerchantUserId()==null) {
		
			//check if email already exist
			MerchantUserInformation user = mservice.getMerchantUserInformationByAdminEmail(this.getMerchantProfile().getAdminEmail());
			if(user!=null) {
				super.setErrorMessage("messages.emailalreadyexist");
				return INPUT;
			}
		
		}
			
		List<MerchantUserRole> newRoles = new ArrayList();
		
		if(this.getMerchantProfile()!=null && this.getMerchantProfile().getMerchantUserId()==null) {
			this.getMerchantProfile().setAdminName(this.getAdminName());
		}
		
		
		if(!StringUtils.isBlank(this.getSubmitMasterRole()) && !this.getSubmitMasterRole().equals("admin")) {
			for(Object o: submitRoles) {
				
				String role = (String)o;
				
				MerchantUserRole mur = new MerchantUserRole();
				mur.setAdminName(this.getMerchantProfile().getAdminName());
				mur.setRoleCode(role);
				
				newRoles.add(mur);
				
			}
		}
		

		
		//add master role
		MerchantUserRole mur = new MerchantUserRole();
		mur.setAdminName(this.getMerchantProfile().getAdminName());
		mur.setRoleCode(this.getSubmitMasterRole());
		newRoles.add(mur);
		
		if(this.getMerchantProfile()!=null && this.getMerchantProfile().getMerchantUserId()==null) {
			mservice.createMerchantUserInformation(super.getContext().getMerchantid(),this.getMerchantProfile(),newRoles,super.getLocale());
			
		} else {
			
			mservice.saveOrUpdateRoles(newRoles);
		}
		
		super.setSuccessMessage();
		
		return SUCCESS;
		
	}
	
	/**
	 * Get a user list for a given store
	 * @return
	 * @throws Exception
	 */
	public String editUserList() throws Exception {
		
		super.setPageTitle("label.user.userlist");
		
		//view user list
		
		//get users from merchantId
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		merchantUserInformations = mservice.getMerchantUserInfo(super.getContext().getMerchantid());
		
		return SUCCESS;
		
	}
	
	public String deleteUser() throws Exception {
		
		if(this.getMerchantProfile()==null || this.getMerchantProfile().getMerchantUserId()==null) {
			log.error("Should receive MerchantUserInformation id");
			super.setTechnicalMessage();
			return INPUT;
		}
		
		//get users from merchantId
		MerchantService mservice = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		
		
		MerchantUserInformation user = mservice.getMerchantUserInformation(this.getMerchantProfile().getMerchantUserId());
		
		mservice.deleteMerchantUserInformation(user);
		
		super.setSuccessMessage();
		

		return SUCCESS;
		
	}

	public MerchantUserInformation getMerchantProfile() {
		return merchantProfile;
	}

	public void setMerchantProfile(MerchantUserInformation merchantProfile) {
		this.merchantProfile = merchantProfile;
	}

	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

	public String getCcMonth() {
		return ccMonth;
	}

	public void setCcMonth(String ccMonth) {
		this.ccMonth = ccMonth;
	}

	public String getCcYear() {
		return ccYear;
	}

	public void setCcYear(String ccYear) {
		this.ccYear = ccYear;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}



	public boolean isResetPasswordResponse() {
		return resetPasswordResponse;
	}

	public void setResetPasswordResponse(boolean resetPasswordResponse) {
		this.resetPasswordResponse = resetPasswordResponse;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public Map getSecurityQuestions() {
		return securityQuestions;
	}

	public void setSecurityQuestions(Map securityQuestions) {
		this.securityQuestions = securityQuestions;
	}

	public List<Integer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}

	public List<String> getAnswersText() {
		return answersText;
	}

	public void setAnswersText(List<String> answersText) {
		this.answersText = answersText;
	}

	public List<String> getQuestionsText() {
		return questionsText;
	}

	public void setQuestionsText(List<String> questionsText) {
		this.questionsText = questionsText;
	}
	
	@JSON(name="passwordResetSuccess")
	public boolean isPasswordResetSuccess() {
		return passwordResetSuccess;
	}

	public void setPasswordResetSuccess(boolean passwordResetSuccess) {
		this.passwordResetSuccess = passwordResetSuccess;
	}

	public String getMerchantPassword() {
		return merchantPassword;
	}

	public void setMerchantPassword(String merchantPassword) {
		this.merchantPassword = merchantPassword;
	}

	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

	public List<String> getSubmitRoles() {
		return submitRoles;
	}

	public void setSubmitRoles(List<String> submitRoles) {
		this.submitRoles = submitRoles;
	}

	public Map<String, String> getMasterRoles() {
		return masterRoles;
	}

	public void setMasterRoles(Map<String, String> masterRoles) {
		this.masterRoles = masterRoles;
	}

	public Collection<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<String> userRoles) {
		this.userRoles = userRoles;
	}

	public String getSubmitMasterRole() {
		return submitMasterRole;
	}

	public void setSubmitMasterRole(String submitMasterRole) {
		this.submitMasterRole = submitMasterRole;
	}

	public Collection<MerchantUserInformation> getMerchantUserInformations() {
		return merchantUserInformations;
	}

	public void setMerchantUserInformations(
			Collection<MerchantUserInformation> merchantUserInformations) {
		this.merchantUserInformations = merchantUserInformations;
	}

	//public String getMasterRole() {
	//	return masterRole;
	//}

	//public void setMasterRole(String masterRole) {
	//	this.masterRole = masterRole;
	//}



}

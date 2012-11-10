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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Prepares HTML emails to be sent by the system
 * 
 * @author Carl Samson
 * 
 */

public abstract class EmailUtilImpl implements EmailUtil {

	private static org.apache.commons.configuration.Configuration config = PropertiesUtil
			.getConfiguration();

	private String chkpath = config.getString("core.store.mediaurl");

	private Configuration configuration;

	private String freemarkerTemplate;
	private JavaMailSender mailSender;

	private String fromEmail = null;

	private String fromAddress = null;

	private Logger log = Logger.getLogger(EmailUtilImpl.class);

	public Map prepareEmailContext(
			MerchantStore profile, String lang) throws Exception {

		Map emailcontext = new HashMap();

		String domain = ReferenceUtil.getUnSecureDomain(profile);

		if (profile == null) {
			throw new Exception("Profile is null");
		}

		String disclaim = LabelUtil.getInstance().getText(lang,
				"email.disclaimer");
		String spam = LabelUtil.getInstance().getText(lang,
				"email.spam.disclaimer");
		String footer = LabelUtil.getInstance().getText(lang,
				"footer.copywright");
		String msgfrom = LabelUtil.getInstance().getText(lang,
				"email.message.from");

		emailcontext.put("EMAIL_STORE_NAME", msgfrom + " "
				+ profile.getStorename());

		if (profile.getStorelogo() != null
				&& !profile.getStorelogo().equals("")) {
			StringBuffer logopath = new StringBuffer();
			logopath.append("<div class=\"header\">").append("<img src=\"");
			logopath.append(ReferenceUtil.getUnSecureDomain(profile)).append(
					"/").append(chkpath).append("/images/brandings/").append(
					profile.getMerchantId()).append("/header/").append(
					profile.getStorelogo()).append("\"");
			logopath.append(" alt=\"logo\" /></div>");
			emailcontext.put("LOGOPATH", logopath.toString());
		} else {
			emailcontext.put("LOGOPATH", "");
		}

		fromEmail = profile.getStorename();
		fromAddress = profile.getStoreemailaddress();

		StringBuffer disclaimbuffer = new StringBuffer();
		disclaimbuffer.append(disclaim).append(" ").append("<a href=\"mailto:")
				.append(profile.getStoreemailaddress()).append("\">").append(
						profile.getStoreemailaddress()).append("</a>");
		emailcontext.put("EMAIL_DISCLAIMER", disclaimbuffer.toString());

		emailcontext.put("EMAIL_SPAM_DISCLAIMER", spam);

		StringBuffer footerbuffer = new StringBuffer();
		footerbuffer.append(footer).append(" ").append(
				DateUtil.getPresentYear());
		footerbuffer.append(" ").append("<a href=\"").append(domain).append(
				"\">").append(profile.getStorename()).append("</a>");
		emailcontext.put("EMAIL_FOOTER_COPYRIGHT", footerbuffer.toString());

		return emailcontext;
	}



	public abstract void send(final String email, final String subject,
			final Map entries) throws Exception;

	public void setEmailTemplate(String template) {
		this.setFreemarkerTemplate(template);
	}

	public void setFreemarkerMailConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setFreemarkerTemplate(String freemarkerTemplate) {
		this.freemarkerTemplate = freemarkerTemplate;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getFreemarkerTemplate() {
		return freemarkerTemplate;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

}

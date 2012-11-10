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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.mail.MailPreparationException;

import com.salesmanager.core.util.EmailUtilImpl;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SimpleSmtpMailModule extends EmailUtilImpl {

	public void send(final String email, final String subject, final Map entries)
			throws Exception {

		Session session = Session.getDefaultInstance(new Properties());

		MimeMessage mimeMessage = new MimeMessage(session);

		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(
				email));

		InternetAddress inetAddress = new InternetAddress();

		inetAddress.setPersonal(super.getFromEmail());
		inetAddress.setAddress(super.getFromAddress());

		mimeMessage.setFrom(inetAddress);
		mimeMessage.setSubject(subject);

		Multipart mp = new MimeMultipart("alternative");

		// Create a "text" Multipart message
		BodyPart textPart = new MimeBodyPart();
		Template textTemplate = super.getConfiguration().getTemplate(
				super.getFreemarkerTemplate());
		final StringWriter textWriter = new StringWriter();
		try {
			textTemplate.process(entries, textWriter);
		} catch (TemplateException e) {
			throw new MailPreparationException("Can't generate text mail", e);
		}
		textPart.setDataHandler(new javax.activation.DataHandler(
				new javax.activation.DataSource() {
					public InputStream getInputStream() throws IOException {
						return new StringBufferInputStream(textWriter
								.toString());
					}

					public OutputStream getOutputStream() throws IOException {
						throw new IOException("Read-only data");
					}

					public String getContentType() {
						return "text/plain";
					}

					public String getName() {
						return "main";
					}
				}));
		mp.addBodyPart(textPart);

		// Create a "HTML" Multipart message
		Multipart htmlContent = new MimeMultipart("related");
		BodyPart htmlPage = new MimeBodyPart();
		Template htmlTemplate = super.getConfiguration().getTemplate(
				super.getFreemarkerTemplate());
		final StringWriter htmlWriter = new StringWriter();
		try {
			htmlTemplate.process(entries, htmlWriter);
		} catch (TemplateException e) {
			throw new MailPreparationException("Can't generate HTML mail", e);
		}
		htmlPage.setDataHandler(new javax.activation.DataHandler(
				new javax.activation.DataSource() {
					public InputStream getInputStream() throws IOException {
						return new StringBufferInputStream(htmlWriter
								.toString());
					}

					public OutputStream getOutputStream() throws IOException {
						throw new IOException("Read-only data");
					}

					public String getContentType() {
						return "text/html";
					}

					public String getName() {
						return "main";
					}
				}));
		htmlContent.addBodyPart(htmlPage);
		BodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(htmlContent);
		mp.addBodyPart(htmlPart);

		mimeMessage.setContent(mp);

		// if(attachment!=null) {
		// MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
		// true);
		// messageHelper.addAttachment(attachmentFileName, attachment);
		// }

		throw new Exception(
				"Not Implemented, needs to connect to an implementation");
		// simple http server sends emails on port 25, no configuration required
		// https://aspirin.dev.java.net/ (2 jars are required dnsjava and
		// aspirin)
		// org.masukomi.aspirin.core.MailQue.queMail(mimeMessage);

	}

}

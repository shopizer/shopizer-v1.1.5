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
package com.salesmanager.central.catalog;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.ServletContextAware;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttributeDownload;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class EditProductUploadAction extends BaseAction implements
		ServletContextAware {

	private String uploadfilefilename;
	private File uploadfile;
	private String uploadfilecontenttype;

	private ServletContext servletContext;

	private Product product;

	private static Logger log = Logger.getLogger(EditProductUploadAction.class);

	private static Long MAXFILESIZE = null;

	private static Configuration conf = PropertiesUtil.getConfiguration();

	static {
		try {
			Long newmaxfilesize = conf.getLong("core.product.file.maxfilesize");
			if (newmaxfilesize != null) {
				MAXFILESIZE = newmaxfilesize;
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public String showUploadForm() {
		
		super.setPageTitle("label.generic.uploadfile");

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);

		if (this.getProduct() != null && this.getProduct().getProductId() > 0) {

			long lproductid = this.getProduct().getProductId();

			Product product;
			try {
				CatalogService catalogservice = (CatalogService) ServiceFactory
						.getService(ServiceFactory.CatalogService);
				product = catalogservice.getProduct(lproductid);

				this.setProduct(product);
				super.getServletRequest().setAttribute("product.productId",
						this.getProduct().getProductId());

				ProductAttributeDownload pda = catalogservice
						.getProductDownload(lproductid);

				if (pda != null) {
					super.getServletRequest().setAttribute("uploadfilename",
							pda.getProductAttributeFilename());
				}

			} catch (Exception e) {
				log.error(e);
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),"errors.technical"));
			}

			return SUCCESS;

		} else {
			return "unauthorized";
		}

	}

	public String uploadProduct() {
		
		super.setPageTitle("label.generic.uploadfile");

		try {

			if (this.getUploadfile() == null
					|| this.getUploadfileFileName() == null) {
				this.showUploadForm();
				return SUCCESS;
			}

			if (this.getUploadfile() != null
					&& !StringUtils.isBlank(this.getUploadfileFileName())
					&& this.MAXFILESIZE != null) {
				java.io.File f = this.getUploadfile();

				if (f.length() > this.MAXFILESIZE) {

					super.addFieldError("uploadfile",
							getText("error.message.product.file.file") + " "
									+ getText("label.product.uploadfile"));
					return SUCCESS;
				}
			}

			CatalogService catalogservice = null;
			if (this.getProduct() != null
					&& this.getProduct().getProductId() > 0) {

				long lproductid = this.getProduct().getProductId();

				Product product;
				try {
					catalogservice = (CatalogService) ServiceFactory
							.getService(ServiceFactory.CatalogService);
					product = catalogservice.getProduct(lproductid);
					this.setProduct(product);

					super.getServletRequest().setAttribute("product.productId",
							this.getProduct().getProductId());

					catalogservice.persistUploadProduct(this.getProduct(), this
							.getUploadfile(), this.getUploadfileFileName(),
							this.getUploadfileContentType());

					super.getServletRequest().setAttribute("uploadfilename",
							this.getUploadfileFileName());

				} catch (Exception e) {
					log.error(e);
					MessageUtil
							.addErrorMessage(super.getServletRequest(),
									LabelUtil.getInstance().getText(super.getLocale(),
											"errors.technical"));
					return SUCCESS;

				}

			} else {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),"errors.technical"));
				return SUCCESS;
			}

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
		}

		return SUCCESS;

	}

	public File getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getUploadfileContentType() {
		return uploadfilecontenttype;
	}

	public void setUploadfileContentType(String uploadfilecontenttype) {
		this.uploadfilecontenttype = uploadfilecontenttype;
	}

	public String getUploadfileFileName() {
		return uploadfilefilename;
	}

	public void setUploadfileFileName(String uploadfilefilename) {
		this.uploadfilefilename = uploadfilefilename;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}

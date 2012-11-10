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
package com.salesmanager.core.module.impl.application.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.module.model.application.FileModule;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.PropertiesUtil;

public abstract class CoreFileImpl implements FileModule {

	private Configuration conf = PropertiesUtil.getConfiguration();
	private static Logger log = Logger.getLogger(CoreFileImpl.class);

	public String uploadFile(int merchantid, String config, File file,
			String fileName, String contentType) throws FileException {

		/** Check content type **/
		String imgct = conf.getString(config + ".contenttypes");
		if (imgct != null) {

			List ct = new ArrayList();
			StringTokenizer st = new StringTokenizer(imgct, ";");
			while (st.hasMoreTokens()) {
				ct.add(st.nextToken());
			}

			// check content type
			if (!ct.contains(contentType)) {
				throw new FileException(LabelUtil.getInstance().getText(
						"errors.unsupported.file ")
						+ contentType);
			}
		}

		/** if an image check size **/
		String imgwsz = conf.getString(config + ".maxwidth");
		String imghsz = conf.getString(config + ".maxheight");
		
		
		
		if (imgwsz != null && imghsz != null) {

			int wseize = 0;
			int hseize = 0;

			BufferedImage originalImage = null;

			try {
				wseize = Integer.parseInt(imgwsz);
				hseize = Integer.parseInt(imghsz);

			} catch (Exception e) {
				throw new FileException(e);
			}

			try {

				originalImage = ImageIO.read(file);
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();

				if (width > wseize || height > hseize) {
					throw new FileException(LabelUtil.getInstance().getText(
							"errors.filedimensiontoolarge"));
				}

			} catch (FileException fe) {
				throw fe;
			} catch (Exception e) {
				throw new FileException(e);
			}

		}

		// Check file size
		long fsize = file.length();
		String smaxfsize = conf.getString(config + ".maxfilesize");
		if(StringUtils.isBlank(smaxfsize)) {
			smaxfsize = conf.getString("core.branding.cart.maxfilesize");
		}
		if (smaxfsize == null) {
			throw new FileException(FileException.ERROR, "Properties " + config
					+ ".maxfilesize not defined");
		}
		long maxsize = 0;
		try {
			maxsize = Long.parseLong(smaxfsize);

		} catch (Exception e) {
			throw new FileException(e);
		}

		if (fsize > maxsize) {
			throw new FileException(LabelUtil.getInstance().getText(
					"errors.filetoolarge"));
		}

		return copyFile(merchantid, config, file, fileName, contentType);

	}

}

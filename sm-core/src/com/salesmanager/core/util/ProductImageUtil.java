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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.Configuration;

import sun.awt.image.BufferedImageGraphicsConfig;

import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.module.model.application.FileModule;

public class ProductImageUtil {

	private boolean cropeable = true;
	private int cropeBaseline = 0;// o is width, 1 is height

	public int getCropeBaseline() {
		return cropeBaseline;
	}

	public void setCropeBaseline(int cropeBaseline) {
		this.cropeBaseline = cropeBaseline;
	}

	private int cropAreaWidth;
	private int cropAreaHeight;

	public int getCropAreaWidth() {
		return cropAreaWidth;
	}

	public void setCropAreaWidth(int cropAreaWidth) {
		this.cropAreaWidth = cropAreaWidth;
	}

	public int getCropAreaHeight() {
		return cropAreaHeight;
	}

	public void setCropAreaHeight(int cropAreaHeight) {
		this.cropAreaHeight = cropAreaHeight;
	}

	public void initCropImage(Product product,
			Map<String, String> moduleConfigMap) throws Exception {

		Configuration conf = PropertiesUtil.getConfiguration();
		//String folder = conf.getString("core.product.image.filefolder") 
		String folder = FileUtil.getProductFilePath()
				+ "/"
				+ product.getMerchantId() + "/";
		File image = new File(folder + product.getProductImage());

		Map<String, String> defaultConfigMap = getDefaultConfigMap();
		// Save the Large Image
		// get specifications
		int largeImageHeight = getValue("largeimageheight", moduleConfigMap,
				defaultConfigMap);
		int largeImageWidth = getValue("largeimagewidth", moduleConfigMap,
				defaultConfigMap);

		/** Original Image **/
		// get original image size
		BufferedImage originalImage = ImageIO.read(image);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		/*** determine if image can be cropped ***/
		determineCropeable(width, largeImageWidth, height, largeImageHeight);

		/*** determine crop area calculation baseline ***/
		this.determineBaseline(width, height);

		determineCropArea(width, largeImageWidth, height, largeImageHeight);
	}

	public void uploadProductImages(File image, String imageName,
			String imageContentType, Product product,
			Map<String, String> moduleConfigMap) throws Exception {
		FileModule fh = (FileModule) SpringUtil.getBean("localfile");
		Configuration conf = PropertiesUtil.getConfiguration();
		Map<String, String> defaultConfigMap = getDefaultConfigMap();
		// Save the Large Image
		// get specifications
		int largeImageHeight = getValue("largeimageheight", moduleConfigMap,
				defaultConfigMap);
		int largeImageWidth = getValue("largeimagewidth", moduleConfigMap,
				defaultConfigMap);

		/** Original Image **/
		// get original image size
		BufferedImage originalImage = ImageIO.read(image);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// original image
		StringBuffer imgName = new StringBuffer();
		imgName.append(product.getProductId()).append("-").append(imageName);
		// store renamed image in products_image column
		product.setProductImage(imgName.toString());

		// upload anyway
		fh.uploadFile(product.getMerchantId(), "core.product.image", image,
				imgName.toString(), imageContentType);

		/*** determine if image can be cropped ***/
		determineCropeable(width, largeImageWidth, height, largeImageHeight);

		product.setProductImageCrop(this.isCropeable());

		/*** determine crop area calculation baseline ***/
		this.determineBaseline(width, height);

		// Save the small Image
		int smallImageHeight = getValue("smallimageheight", moduleConfigMap,
				defaultConfigMap);
		int smallImageWidth = getValue("smallimagewidth", moduleConfigMap,
				defaultConfigMap);
		File resizedSmallImage = resizeImage(originalImage, smallImageWidth,
				smallImageHeight);
		StringBuffer smallImgName = new StringBuffer();
		smallImgName.append(conf.getString("core.product.image.small.prefix"))
				.append("-").append(imgName.toString());
		fh.uploadFile(product.getMerchantId(), "core.product.image",
				resizedSmallImage, smallImgName.toString(), imageContentType);
		resizedSmallImage.delete();

		// Save large Image
		int listingImageHeight = getValue("largeimageheight", moduleConfigMap,
				defaultConfigMap);
		int listingImageWidth = getValue("largeimagewidth", moduleConfigMap,
				defaultConfigMap);
		File resizedListingImage = resizeImage(originalImage,
				listingImageWidth, listingImageHeight);
		StringBuffer largeImgName = new StringBuffer();
		largeImgName.append(conf.getString("core.product.image.large.prefix"))
				.append("-").append(imgName.toString());

		fh.uploadFile(product.getMerchantId(), "core.product.image",
				resizedListingImage, largeImgName.toString(), imageContentType);
		resizedListingImage.delete();

		determineCropArea(width, largeImageWidth, height, largeImageHeight);

	}

	public void uploadCropedProductImages(File image, String imageName,
			String imageContentType, Product product,
			Map<String, String> moduleConfigMap) throws Exception {
		FileModule fh = (FileModule) SpringUtil.getBean("localfile");
		Configuration conf = PropertiesUtil.getConfiguration();
		Map<String, String> defaultConfigMap = getDefaultConfigMap();

		/** Original Image **/
		// get original image size
		BufferedImage originalImage = ImageIO.read(image);

		// Save the small Image
		int smallImageHeight = getValue("smallimageheight", moduleConfigMap,
				defaultConfigMap);
		int smallImageWidth = getValue("smallimagewidth", moduleConfigMap,
				defaultConfigMap);
		File resizedSmallImage = resizeImage(originalImage, smallImageWidth,
				smallImageHeight);
		StringBuffer smallImgName = new StringBuffer();
		smallImgName.append(conf.getString("core.product.image.small.prefix"))
				.append("-").append(product.getProductImage());
		fh.uploadFile(product.getMerchantId(), "core.product.image",
				resizedSmallImage, smallImgName.toString(), imageContentType);
		resizedSmallImage.delete();

		// Save large Image
		int listingImageHeight = getValue("largeimageheight", moduleConfigMap,
				defaultConfigMap);
		int listingImageWidth = getValue("largeimagewidth", moduleConfigMap,
				defaultConfigMap);
		File resizedListingImage = resizeImage(originalImage,
				listingImageWidth, listingImageHeight);
		StringBuffer largeImgName = new StringBuffer();
		largeImgName.append(conf.getString("core.product.image.large.prefix"))
				.append("-").append(product.getProductImage());

		fh.uploadFile(product.getMerchantId(), "core.product.image",
				resizedListingImage, largeImgName.toString(), imageContentType);
		resizedListingImage.delete();

	}

	private void determineCropeable(int width, int specificationsWidth,
			int height, int specificationsHeight) {
		/*** determine if image can be cropped ***/
		// height
		int y = height - specificationsHeight;
		// width
		int x = width - specificationsWidth;

		if (x < 0 || y < 0) {
			cropeable = false;
		}

		if (x == 0 && y == 0) {
			cropeable = false;
		}
	}

	private void determineBaseline(int width, int height) {
		/*** determine crop area calculation baseline ***/
		if (width < height) {
			this.setCropeBaseline(0);// width
		}
		if (height < width) {
			this.setCropeBaseline(1);// height
		}
		if (width == height) {
			this.setCropeBaseline(0);
		}
	}

	private void determineCropArea(int width, int specificationsWidth,
			int height, int specificationsHeight) {

		cropAreaWidth = specificationsWidth;
		cropAreaHeight = specificationsHeight;

		// crop factor
		double factor = 1;
		if (this.getCropeBaseline() == 0) {// width
			factor = new Integer(width).doubleValue()
					/ new Integer(specificationsWidth).doubleValue();
		} else {// height
			factor = new Integer(height).doubleValue()
					/ new Integer(specificationsHeight).doubleValue();
		}

		double w = factor * specificationsWidth;
		double h = factor * specificationsHeight;

		cropAreaWidth = (int) w;
		cropAreaHeight = (int) h;

		/*
		 * if(factor>1) { //determine croping section for(double
		 * i=factor;i>1;i--) { //multiply specifications by factor int newWidth
		 * = (int)(i * specificationsWidth); int newHeight = (int)(i *
		 * specificationsHeight); //check if new size >= original image
		 * if(width>=newWidth && height>=newHeight) { cropAreaWidth = newWidth;
		 * cropAreaHeight = newHeight; break; } } }
		 */

	}

	public File getCroppedImage(File originalFile, int x1, int y1, int width,
			int height) throws Exception {

		BufferedImage image = ImageIO.read(originalFile);
		BufferedImage out = image.getSubimage(x1, y1, width, height);
		File tempFile = File.createTempFile("temp", ".jpg");
		tempFile.deleteOnExit();
		ImageIO.write(out, "jpg", tempFile);
		return tempFile;
	}

	public Integer getValue(String key, Map<String, String> moduleConfigMap,
			Map<String, String> defaultConfigMap) {
		if (moduleConfigMap.get(key) != null) {
			return Integer.valueOf(moduleConfigMap.get(key));
		} else {
			return Integer.valueOf(defaultConfigMap.get(key));
		}
	}

	public Map<String, String> getDefaultConfigMap() {
		Configuration conf = PropertiesUtil.getConfiguration();
		Map<String, String> defaultConfigMap = new HashMap<String, String>();
		defaultConfigMap.put("largeimageheight", conf
				.getString("core.product.config.large.image.height"));
		defaultConfigMap.put("largeimagewidth", conf
				.getString("core.product.config.large.image.width"));
		defaultConfigMap.put("smallimageheight", conf
				.getString("core.product.config.small.image.height"));
		defaultConfigMap.put("smallimagewidth", conf
				.getString("core.product.config.small.image.width"));
		defaultConfigMap.put("listingimageheight", conf
				.getString("core.product.config.large.image.height"));
		defaultConfigMap.put("listingimagewidth", conf
				.getString("core.product.config.large.image.width"));
		return defaultConfigMap;
	}

	public BufferedImage resize(BufferedImage image, int width, int height) {
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image
				.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	public BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f / 9.0f;
		float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth,
				ninth, ninth };
		Map<Key, Object> map = new HashMap<Key, Object>();
		map.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel),
				ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);
	}

	private BufferedImage createCompatibleImage(BufferedImage image) {
		GraphicsConfiguration gc = BufferedImageGraphicsConfig.getConfig(image);
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage result = gc.createCompatibleImage(w, h,
				Transparency.TRANSLUCENT);
		Graphics2D g2 = result.createGraphics();
		g2.drawRenderedImage(image, null);
		g2.dispose();
		return result;
	}

	// To Shrink
	/*
	 * private BufferedImage shrinkResize(BufferedImage image, int width,int
	 * height) { image = createCompatibleImage(image); image = resize(image,
	 * 100, 100); image = blurImage(image); image = resize(image, width,
	 * height); return image; }
	 */

	public File resizeImage(BufferedImage image, int width, int height)
			throws Exception {
		// BufferedImage readImage = ImageIO.read(image);

		BufferedImage resizedImage = resize(image, width, height);
		File temp = File.createTempFile("temp", ".png");
		ImageIO.write(resizedImage, "png", temp);
		return temp;
	}

	public boolean isCropeable() {
		return cropeable;
	}

	public void setCropeable(boolean cropeable) {
		this.cropeable = cropeable;
	}

}

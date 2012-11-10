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

import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletRequest;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.salesmanager.core.module.model.application.CaptchaModule;

public class SimpleCaptchaModule implements CaptchaModule {

	private static SimpleCaptchaModule innerInstance = null;

	private static ImageCaptchaService instance;

	public static SimpleCaptchaModule getInstance() {
		if (innerInstance == null) {
			innerInstance = new SimpleCaptchaModule();
			instance = new DefaultManageableImageCaptchaService(
					new FastHashMapCaptchaStore(),
					new CustomImageCaptchaEngine(), 180, 100000, 75000);

		}
		return innerInstance;
	}

	private SimpleCaptchaModule() {
	}

	public BufferedImage getImageForSessionId(String sessionId,
			HttpServletRequest request) {


		return instance.getImageChallengeForID(sessionId, request.getLocale());

	}

	public boolean validateResponseForSessonId(String sessionId,
			String captchaParameter) {
		// TODO Auto-generated method stub

		if (captchaParameter != null) {
			captchaParameter.toUpperCase();
		}

		boolean response = instance.validateResponseForID(sessionId,
				captchaParameter);

		return response;

	}

}

class CustomImageCaptchaEngine extends ListImageCaptchaEngine {
	protected void buildInitialFactories() {
		WordGenerator wgen = new RandomWordGenerator(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789");
		RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(
				new int[] { 0, 100 }, new int[] { 0, 100 },
				new int[] { 0, 100 });
		TextPaster textPaster = new RandomTextPaster(new Integer(5),
				new Integer(5), cgen, true);// 7 7

		BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(
				new Integer(100), new Integer(50));// 200 100

		Font[] fontsList = new Font[] { new Font("Arial", 0, 10),
				new Font("Tahoma", 0, 10), new Font("Verdana", 0, 10), };

		FontGenerator fontGenerator = new RandomFontGenerator(new Integer(20),
				new Integer(32), fontsList);// 20 35

		WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
				backgroundGenerator, textPaster);
		this.addFactory(new GimpyFactory(wgen, wordToImage));
	}
}

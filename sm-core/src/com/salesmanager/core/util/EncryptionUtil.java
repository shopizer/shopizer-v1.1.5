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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * Can be used to encrypt/decrypt from java-php
 * 
 * @author Administrator
 * 
 */
public class EncryptionUtil {

	private EncryptionUtil() {
	}

	public static String generatekey(String value) {
		String key = StringUtils.rightPad(value, 16, "0");
		return key;
	}

	public static String decryptFromExternal(String key, String value)
			throws Exception {

		if (value == null || value.equals(""))
			return "";

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
				.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] outText;
		outText = cipher.doFinal(hexToBytes(value));
		return new String(outText);

	}

	public static String decrypt(String key, String value) throws Exception {

		if (value == null || value.equals(""))
			return "";

		// NEED TO UNDERSTAND WHY PKCS5Padding DOES NOT WORK
		// Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
				.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] outText;
		outText = cipher.doFinal(hexToBytes(value));
		return new String(outText);

	}

	public static String encrypt(String key, String value) throws Exception {

		// value = StringUtils.rightPad(value, 16,"*");
		// Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		// NEED TO UNDERSTAND WHY PKCS5Padding DOES NOT WORK
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
				.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] inpbytes = value.getBytes();
		byte[] encrypted = cipher.doFinal(inpbytes);
		return new String(bytesToHex(encrypted));

	}

	public static String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		} else {
			int len = data.length;
			String str = "";
			for (int i = 0; i < len; i++) {
				if ((data[i] & 0xFF) < 16) {
					str = str + "0"
							+ java.lang.Integer.toHexString(data[i] & 0xFF);
				} else {
					str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
				}

			}
			return str;
		}
	}

	private static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(str.substring(i * 2,
						i * 2 + 2), 16);
			}
			return buffer;
		}
	}

}

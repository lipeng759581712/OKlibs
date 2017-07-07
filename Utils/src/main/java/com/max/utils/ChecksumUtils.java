package com.max.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 校验和工具，提供了计算文件内容和字节数组散列等方法
 */
public class ChecksumUtils {

	/**
	 * 采用MD5算法计算文件内容的散列
	 */
	public static byte[] calculateFileMd5(String filePath) {
		return calculateFileHash(filePath, "MD5");
	}

	/**
	 * 采用SHA1算法计算文件内容的散列
	 */
	public static byte[] calculateFileSha1(String filePath) {
		return calculateFileHash(filePath, "SHA1");
	}

	/**
	 * 采用MD5算法计算字节数组的散列
	 */
	public static byte[] calculateStreamMd5(byte[] stream) {
		return calculateStreamHash(stream, "MD5");
	}

	/**
	 * 采用SHA1算法计算字节数组的散列
	 */
	public static byte[] calculateStreamSha1(byte[] steam) {
		return calculateStreamHash(steam, "SHA1");
	}

	/**
	 * 采用指定算法计算文件内容的散列
	 *
	 * @param algorithm 算法名，如"MD5"、"SHA1"
	 */
	public static byte[] calculateStreamHash(byte[] steam, String algorithm) {
		if (steam == null || steam.length == 0) return steam;

		try {
			MessageDigest m = MessageDigest.getInstance(algorithm);
			m.reset();
			m.update(steam);
			byte[] digest = m.digest();
			return digest;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 采用指定算法计算字节数组的散列
	 *
	 * @param algorithm 算法名，如"MD5"、"SHA1"
	 */
	public static byte[] calculateFileHash(String filePath, String algorithm) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			MessageDigest md = MessageDigest.getInstance(algorithm);

			byte[] buffer = new byte[8192];
			int numOfBytesRead;
			while((numOfBytesRead = fis.read(buffer)) > 0){
				md.update(buffer, 0, numOfBytesRead);
			}

			byte[] hash = md.digest();
			return hash;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}

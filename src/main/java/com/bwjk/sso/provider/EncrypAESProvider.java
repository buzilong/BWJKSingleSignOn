package com.bwjk.sso.provider;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.springframework.util.Base64Utils;

import com.bwjk.common.util.Base64Util;

import io.jsonwebtoken.impl.Base64UrlCodec;

public class EncrypAESProvider {
	// KeyGenerator 提供对称密钥生成器的功能，支持各种算法
	private KeyGenerator keygen ;
	// SecretKey 负责保存对称密钥
	private SecretKey deskey;
	// Cipher负责完成加密或解密工作
	private Cipher c  ;
	// 该字节数组负责保存加密的结果
	private byte[] cipherByte;

	public EncrypAESProvider() throws NoSuchAlgorithmException, NoSuchPaddingException {
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		keygen = KeyGenerator.getInstance("AES");
		// 生成密钥
		deskey = keygen.generateKey();
		// 生成Cipher对象,指定其支持的DES算法
		c = Cipher.getInstance("AES");
	}

	public EncrypAESProvider(String secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException {
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		keygen = KeyGenerator.getInstance("AES");
		keygen.init(128, new SecureRandom(secretKey.getBytes()));
		// 生成密钥
		deskey = keygen.generateKey();
		// 生成Cipher对象,指定其支持的DES算法
		c = Cipher.getInstance("AES");
	}

	/**
	 * 对字符串加密
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public String Encrytor(String str)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes("UTF-8");
		// 加密，结果保存进cipherByte
		cipherByte = c.doFinal(src);
		return Base64Utils.encodeToUrlSafeString(cipherByte);
	}

	/**
	 * 对字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public String Decryptor(String str)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] decBytes = Base64Utils.decodeFromUrlSafeString(str);
		// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
		c.init(Cipher.DECRYPT_MODE, deskey);
		cipherByte = c.doFinal(decBytes);
		return new String(cipherByte, "UTF-8");
	}

}
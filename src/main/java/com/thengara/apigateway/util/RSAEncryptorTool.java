package com.thengara.apigateway.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.RSAKeyProvider;

@Service
public class RSAEncryptorTool {

	private final RSAKeyPairGenerator rsaKeyPairGenerator;
	private final EnvironmentWrapper environment;

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private volatile RSAKeyProvider rsaKeyProvider;

	public RSAEncryptorTool(RSAKeyPairGenerator rsaKeyPairGenerator, EnvironmentWrapper environment) {
		this.rsaKeyPairGenerator = rsaKeyPairGenerator;
		this.environment = environment;
	}

	@PostConstruct
	private void postLoad() {
		boolean loadOnStartup = Boolean.valueOf(System.getProperty("load.cache.onstartup", "false"));
		if (loadOnStartup) {
			initializeRSAKeyCache();
		}
	}

	public String decrypt(String data) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
	        NoSuchPaddingException, IOException {
		return decrypt(Base64.getDecoder().decode(data.getBytes()));
	}

	public String decrypt(byte[] data)
	        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		try {
			lock.readLock().lock();
			if (rsaKeyProvider == null) {
				lock.readLock().unlock();
				initializeRSAKeyCache();
				lock.readLock().lock();
			}
			cipher.init(Cipher.DECRYPT_MODE, rsaKeyProvider.getPrivateKey());
			return new String(cipher.doFinal(data));
		} finally {
			lock.readLock().unlock();
		}

	}

	public byte[] encrypt(String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException,
	        NoSuchAlgorithmException, IOException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		try {
			lock.readLock().lock();
			if (rsaKeyProvider == null) {
				lock.readLock().unlock();
				initializeRSAKeyCache();
				lock.readLock().lock();
			}
			cipher.init(Cipher.ENCRYPT_MODE, rsaKeyProvider.getPublicKeyById(null));
			return cipher.doFinal(data.getBytes());
		} finally {
			lock.readLock().unlock();
		}

	}
	private void initializeRSAKeyCache() {
		lock.writeLock().lock();
		rsaKeyProvider = rsaKeyPairGenerator.rsaKeyProvider(environment.getRsaPublicKeyForEncryption(), environment.getRsaPrivateKeyForDecryption());
		lock.writeLock().unlock();
	}
}
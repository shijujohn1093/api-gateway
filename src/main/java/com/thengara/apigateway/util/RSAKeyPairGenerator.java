package com.thengara.apigateway.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.RSAKeyProvider;

@Service
public class RSAKeyPairGenerator {

	public static final String PUBLIC = "PUBLIC";
	public static final String PRIVATE = "PRIVATE";

	private final int keySize = 2048;
	private final String RSA_ALGORITHM = "RSA";

	private final KeyFactory rsaKeyFactory;

	public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
		rsaKeyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
	}

	public RSAKeyProvider rsaKeyProvider(String rsaPublicFile, String rsaPrivateKey) {
		try {
			return this.rsaKeyProvider(this.readPublicKeyFromFile(rsaPublicFile), this.readPrivateKeyFromFile(rsaPrivateKey));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeKeyInFile(File file, byte[] keyEncodedBytes) throws IOException {
		FileOutputStream stream;
		stream = new FileOutputStream(file);
		stream.write(keyEncodedBytes);
		stream.close();
	}

	public byte[] fileDataByteArrayFromResource(String fileName) throws IOException {
		final ClassLoader classloader = this.getClass().getClassLoader();
		System.out.println(fileName);
		URL url = classloader.getResource(fileName);
		System.out.println(url.getPath());
		File file = new File(classloader.getResource(fileName).getFile());

		FileInputStream fis;
		fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		final byte[] keyBytes = new byte[(int) file.length()];
		dis.readFully(keyBytes);
		dis.close();
		return keyBytes;

	}

	public RSAPublicKey readPublicKeyFromFile(String fileName) throws IOException {
		try {
			final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileDataByteArrayFromResource(fileName));
			return (RSAPublicKey) rsaKeyFactory.generatePublic(x509EncodedKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public RSAPrivateKey readPrivateKeyFromFile(String fileName) throws IOException {
		try {
			final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(fileDataByteArrayFromResource(fileName));
			return (RSAPrivateKey) rsaKeyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	private RSAKeyProvider rsaKeyProvider(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		if (publicKey == null && privateKey == null) {
			throw new IllegalArgumentException("Both provided keys cannot be null");
		}
		return new RSAKeyProvider() {

			@Override
			public RSAPublicKey getPublicKeyById(String id) {
				return publicKey;
			}

			@Override
			public String getPrivateKeyId() {
				return null;
			}

			@Override
			public RSAPrivateKey getPrivateKey() {
				return privateKey;
			}
		};
	}

	public Map<String, Object> getRsaKeys() {
		KeyPairGenerator generator;
		try {
			Map<String, Object> map = new HashMap<>();

			generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			generator.initialize(keySize);
			final KeyPair keyPair = generator.generateKeyPair();

			RSAPublicKeySpec rsaPublicKeySpec = rsaKeyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec rsaPrivateKeySpec = rsaKeyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);

			RSAPublicKey publicKey = (RSAPublicKey) rsaKeyFactory.generatePublic(rsaPublicKeySpec);
			RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeyFactory.generatePrivate(rsaPrivateKeySpec);

			map.put(PUBLIC, publicKey);
			map.put(PRIVATE, privateKey);
			return map;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void generateKeysInFile(String filePath) throws IOException {
		Map<String, Object> rsaKeys = getRsaKeys();
		RSAPublicKey publicKey = (RSAPublicKey) rsaKeys.get(PUBLIC);
		RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeys.get(PRIVATE);

		this.writeKeyInFile(new File(filePath + "publickey.file"), publicKey.getEncoded());
		this.writeKeyInFile(new File(filePath + "privatekey.file"), privateKey.getEncoded());
	}
}

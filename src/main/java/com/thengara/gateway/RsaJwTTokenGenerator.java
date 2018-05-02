package com.thengara.gateway;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.apache.commons.io.input.ReaderInputStream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;

public class RsaJwTTokenGenerator {

	public static final String PUBLIC_KEY_FILE_PATH = "public.key.file";
	public static final String PRIVATE_KEY_FILE_PATH = "private.key.file";

	enum KeyConstant {

		PUBLIC("publickey.file"), PRIVATE("privatekey.file");

		private final String fileParameter;

		KeyConstant(String fileParameter) {
			this.fileParameter = fileParameter;
		}

		public String getFileParameter() {
			return fileParameter;
		}

		public String getFileToRead() {
			return "security/" + fileParameter;
		}

		public String getFileToWrite() {
			return fileParameter;
		}
	}

	public Map<KeyConstant, Object> getRsaKeys() {
		Map<KeyConstant, Object> map = new HashMap<>();

		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance("RSA");

			generator.initialize(2048);
			final KeyPair keyPair = generator.generateKeyPair();

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);

			RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
			RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivateKeySpec);

			map.put(KeyConstant.PUBLIC, publicKey);
			map.put(KeyConstant.PRIVATE, privateKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return map;
	}

	public RSAKeyProvider rsaKeyProvider(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
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

	public String getJwtToken(RSAKeyProvider rsaKeyProvider) {
		final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
		final String token = JWT.create().withIssuer("auth0").sign(algorithm);
		return token;
	}

	public void writeKeyInFile(String fileName, byte[] keyEncodedBytes) {
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(fileName);
			stream.write(keyEncodedBytes);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] fileDataByteArray(String fileName) {
		final ClassLoader classloader = this.getClass().getClassLoader();
		System.out.println(fileName);
		URL url = classloader.getResource(fileName);
		System.out.println(url.getPath());
		File file = new File(classloader.getResource(fileName).getFile());

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			final byte[] keyBytes = new byte[(int) file.length()];
			dis.readFully(keyBytes);
			dis.close();
			return keyBytes;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public RSAPublicKey readPublicKeyFromFile(String fileName)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileDataByteArray(fileName));
		return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);
	}

	public RSAPrivateKey readPrivateKeyFromFile(String fileName)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(fileDataByteArray(fileName));
		return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
	}

	public void generateKeysInFile() {
		Map<KeyConstant, Object> rsaKeys = getRsaKeys();
		RSAPublicKey publicKey = (RSAPublicKey) rsaKeys.get(KeyConstant.PUBLIC);
		RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeys.get(KeyConstant.PRIVATE);

		writeKeyInFile(KeyConstant.PUBLIC.getFileToWrite(), publicKey.getEncoded());
		writeKeyInFile(KeyConstant.PRIVATE.getFileToWrite(), privateKey.getEncoded());
	}

	public RSAPrivateKey readPrivateKeyFromFile() throws InvalidKeySpecException, NoSuchAlgorithmException {
		return readPrivateKeyFromFile(KeyConstant.PRIVATE.getFileToRead());
	}

	public RSAPublicKey readPublicKeyFromFile() throws InvalidKeySpecException, NoSuchAlgorithmException {
		return readPublicKeyFromFile(KeyConstant.PUBLIC.getFileToRead());
	}

	public String jwtTokwn() throws InvalidKeySpecException, NoSuchAlgorithmException {
		RSAKeyProvider rsaKeyProvider = rsaKeyProvider(null, readPrivateKeyFromFile());
		return getJwtToken(rsaKeyProvider);
	}

	public DecodedJWT decodeJwtTokenString(String tokenString) {
		return JWT.decode(tokenString);
	}

	public DecodedJWT verify(String jwtToken) {
		RSAKeyProvider rsaKeyProvider;
		try {
			rsaKeyProvider = rsaKeyProvider(readPublicKeyFromFile(), null);
			final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
			final JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("auth0").build();
			return jwtVerifier.verify(jwtToken);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
		RsaJwTTokenGenerator gen = new RsaJwTTokenGenerator();
		// gen.generateKeysInFile();
		String token = gen.jwtTokwn();
		System.out.println(gen.decodeJwtTokenString(token));
		System.out.println(token);
		System.out.println("---------------");
		DecodedJWT decodedJWT = gen.verify(token);
		System.out.println(decodedJWT.getSignature());
	}

}

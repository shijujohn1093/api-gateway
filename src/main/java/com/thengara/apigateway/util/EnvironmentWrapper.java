package com.thengara.apigateway.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentWrapper {

	public static final String ENCRYPT_RSA_PUBLICKEY_PATH = "enc.rsa.publickey.path";
	public static final String DECRYPT_RSA_PRIVATEKEY_PATH = "dec.rsa.publickey.path";
	public static final String AUTH_PROVIDER_KEY = "auth.provider";
	public static final String AUTH_ACCESS_ROLES_KEY = "auth.access.roles";
	public static final String JWT_RSA_PUBLICKEY_PATH = "jwt.rsa.publickey.path";
	public static final String JWT_RSA_PRIVATEKEY_PATH = "jwt.rsa.privatekey.path";
	public static final String APPLICATION_NAME = "spring.application.name";
	public static final String APPLICATION_PREFIX = "applicaiton.prefix"; // finapps_

	private final Environment environment;

	@Autowired
	public EnvironmentWrapper(Environment environment) {
		this.environment = environment;
	}

	@PostConstruct
	private void performConfiguraitonCheck() {
		authProvider();
		getSupportedAccessRole();
	}

	public Environment getEnvironment() {
		return environment;
	}

	private String getManadatoryFieldValue(String keyName) {
		String propertyValue = getEnvironment().getProperty(keyName);
		if (propertyValue == null) {
			throw new ConfigurationException(keyName + " is not configured in property file.");
		}
		return propertyValue;
	}

	public String authProvider() {
		return getManadatoryFieldValue(AUTH_PROVIDER_KEY);
	}

	public String applicationName() {
		return getManadatoryFieldValue(APPLICATION_NAME);
	}

	public String getAppPrefix() {
		return getManadatoryFieldValue(APPLICATION_PREFIX);
	}

	public String[] getSupportedAccessRole() {
		String roleString = getManadatoryFieldValue(AUTH_ACCESS_ROLES_KEY);
		if (roleString != null) {
			System.out.println("No supported role configured");
			String[] roles = roleString.split(",");
			return roles;
		}
		return null;
	}

	public String getRsaPublicKeyFilePathForJWTSign() {
		return getManadatoryFieldValue(JWT_RSA_PUBLICKEY_PATH);
	}

	public String getRsaPrivateKeyFilePathForJWTSign() {
		return getManadatoryFieldValue(JWT_RSA_PRIVATEKEY_PATH);
	}

	public String getRsaPublicKeyForEncryption() {
		return getManadatoryFieldValue(ENCRYPT_RSA_PUBLICKEY_PATH);
	}

	public String getRsaPrivateKeyForDecryption() {
		return getManadatoryFieldValue(DECRYPT_RSA_PRIVATEKEY_PATH);
	}

}

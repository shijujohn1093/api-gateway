package com.thengara.apigateway.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentWrapper {

	private final String AUTH_PROVIDER_KEY = "auth.provider";
	private final String AUTH_ACCESS_ROLES_KEY = "auth.access.roles";
	private final String RSA_PUBLIC_RESOURCE_FILE = "rsa.public.resource.path";
	private final String RSA_PRIVATE_RESOURCE_FILE = "rsa.private.resource.path";
	private final String APPLICATION_NAME = "spring.application.name";
	private final String APPLICATION_PREFIX = "applicaiton.prefix"; // finapps_

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

	public String getRsaPublicKeyFilePath() {
		return getManadatoryFieldValue(RSA_PUBLIC_RESOURCE_FILE);
	}

	public String getRsaPrivateKeyFilePath() {
		return getManadatoryFieldValue(RSA_PRIVATE_RESOURCE_FILE);
	}

}

package com.thengara.apigateway.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentWrapper {

	String AUTH_PROVIDER_KEY = "auth.provider";
	String AUTH_ACCESS_ROLES_KEY = "auth.access.roles";

	private final Environment environment;

	@Autowired
	public EnvironmentWrapper(Environment environment) {
		this.environment = environment;
	}

	@PostConstruct
	private void performConfiguraitonCheck() {
		getProvider();
		getSupportedAccessRole();
	}

	public Environment getEnvironment() {
		return environment;
	}

	public String getProvider() {
		String provider = getEnvironment().getProperty(AUTH_PROVIDER_KEY);
		if (provider == null) {
			throw new ConfigurationException(AUTH_PROVIDER_KEY + " configuration is not available");
		}
		return provider;
	}

	public String[] getSupportedAccessRole() {
		String roleString = getEnvironment().getProperty(AUTH_ACCESS_ROLES_KEY);
		if (roleString != null) {
			System.out.println("No supported role configured");
			String[] roles = roleString.split(",");
			return roles;
		}
		return null;
	}

}

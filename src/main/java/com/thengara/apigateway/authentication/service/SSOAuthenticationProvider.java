package com.thengara.apigateway.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SSOAuthenticationProvider implements AuthenticationServiceProvider {

	@Override
	public List<String> authenticate(String username, String password, String role) {
		List<String> permissions = new ArrayList<>();
		/**
		 * TODO : Write SSO implementation
		 */
		return permissions;
	}

	@Override
	public boolean isSupported(String supportedAuthenticaitonProviderName) {
		return this.getClass().getName().equals(supportedAuthenticaitonProviderName);
	}

}

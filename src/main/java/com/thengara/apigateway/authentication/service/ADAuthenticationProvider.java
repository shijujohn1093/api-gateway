package com.thengara.apigateway.authentication.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ADAuthenticationProvider implements AuthenticationServiceProvider {

	@Override
	public List<String> authenticate(String username, String password, String role) {
		/**
		 * TODO : Write SSO implementation
		 */
		return null;
	}

	@Override
	public boolean isSupported(String supportedAuthenticaitonProviderName) {
		return false;
	}
}

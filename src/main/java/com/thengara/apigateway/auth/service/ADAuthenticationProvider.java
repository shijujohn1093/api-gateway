package com.thengara.apigateway.auth.service;

import java.util.List;

public class ADAuthenticationProvider implements AuthenticationServiceProvider {

	@Override
	public List<String> authenticate(String username, String password, String role) {
		return null;
	}

	@Override
	public boolean isSupported(String supportedAuthenticaitonProviderName) {
		return false;
	}
}

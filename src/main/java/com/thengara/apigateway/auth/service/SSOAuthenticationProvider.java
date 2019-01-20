package com.thengara.apigateway.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SSOAuthenticationProvider implements AuthenticationServiceProvider {

	@Override
	public List<String> authenticate(String username, String password, String role) {
		List<String> permissions = new ArrayList<>();
		return permissions;
	}

	@Override
	public boolean isSupported(String supportedAuthenticaitonProviderName) {
		return true;
	}

}

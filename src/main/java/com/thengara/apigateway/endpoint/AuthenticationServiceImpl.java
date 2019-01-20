package com.thengara.apigateway.endpoint;

import com.thengara.apigateway.auth.model.UserAuthority;
import com.thengara.apigateway.auth.service.AuthenticationServiceProviderManager;

public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationServiceProviderManager providerManager;

	public AuthenticationServiceImpl(AuthenticationServiceProviderManager providerManager) {
		this.providerManager = providerManager;
	}

	@Override
	public UserAuthority login(String userName, String password) {
s		return providerManager.authenticate(username, password);
	}

	@Override
	public String refresh(String jwtToken) {
		// TODO Auto-generated method stub
		return null;
	}

}

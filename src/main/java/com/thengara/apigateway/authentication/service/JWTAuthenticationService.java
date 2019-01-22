package com.thengara.apigateway.authentication.service;

import org.springframework.stereotype.Service;

import com.thengara.apigateway.authentication.model.UserAuthority;
import com.thengara.apigateway.util.JWTTokenUtil;

@Service
public class JWTAuthenticationService implements AuthenticationService {

	private final AuthenticationServiceProviderManager providerManager;
	private final JWTTokenUtil jwtUtil;

	public JWTAuthenticationService(AuthenticationServiceProviderManager providerManager, JWTTokenUtil jwtUtil) {
		this.providerManager = providerManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public String login(String userName, String password) {
		UserAuthority userAuthority = providerManager.authenticate(userName, password);
		return jwtUtil.generateToken(userAuthority);
	}

	@Override
	public String refresh(String jwtToken) {
		return jwtUtil.refreshToken(jwtToken);
	}

}

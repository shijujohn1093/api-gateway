package com.thengara.apigateway.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.thengara.apigateway.authentication.model.UserAuthority;
import com.thengara.apigateway.util.EnvironmentWrapper;

@Service
public class AuthenticationServiceProviderManagerImpl implements AuthenticationServiceProviderManager {

	private final AuthenticationServiceProvider[] authenticationServiceProvider;
	private final EnvironmentWrapper environment;

	public AuthenticationServiceProviderManagerImpl(AuthenticationServiceProvider[] authenticationServiceProvider, EnvironmentWrapper environment) {
		this.authenticationServiceProvider = authenticationServiceProvider;
		this.environment = environment;
	}

	@Override
	public UserAuthority authenticate(String userName, String password) {
		UserAuthority userAuthority = new UserAuthority(userName, new ArrayList<>());
		for (AuthenticationServiceProvider authenticationServiceProvider : authenticationServiceProvider) {
			if (authenticationServiceProvider.isSupported(environment.authProvider())) {
				for (String role : environment.getSupportedAccessRole()) {
					List<String> permissions = authenticationServiceProvider.authenticate(userName, password, role);
					if (permissions != null && permissions.size() > 0) {
						userAuthority.getRoles().add(role);
					}
				}
			}
		}
		return userAuthority;
	}
}

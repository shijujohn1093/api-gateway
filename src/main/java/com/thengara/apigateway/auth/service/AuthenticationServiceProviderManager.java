package com.thengara.apigateway.auth.service;

import com.thengara.apigateway.auth.model.UserAuthority;

public interface AuthenticationServiceProviderManager {
	String AUTH_PROVIDER_KEY = "auth.provider";
	String AUTH_ACCESS_ROLES_KEY = "auth.access.roles";

	UserAuthority authenticate(String userName, String password);
}

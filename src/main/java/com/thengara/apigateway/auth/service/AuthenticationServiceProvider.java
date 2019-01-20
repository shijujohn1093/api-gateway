package com.thengara.apigateway.auth.service;

import java.util.List;

public interface AuthenticationServiceProvider {

	List<String> authenticate(String username, String password, String role);

	boolean isSupported(String supportedAuthenticaitonProviderName);

}

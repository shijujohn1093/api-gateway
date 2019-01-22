package com.thengara.apigateway.authentication.service;

import java.util.List;

public interface AuthenticationServiceProvider {

	List<String> authenticate(String username, String password, String role);

	boolean isSupported(String supportedAuthenticaitonProviderName);

}

package com.thengara.apigateway.authentication.service;

import com.thengara.apigateway.authentication.model.UserAuthority;

public interface AuthenticationServiceProviderManager {

	UserAuthority authenticate(String userName, String password);
}

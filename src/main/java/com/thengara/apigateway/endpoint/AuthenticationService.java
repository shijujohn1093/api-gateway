package com.thengara.apigateway.endpoint;

import com.thengara.apigateway.auth.model.UserAuthority;

public interface AuthenticationService {

	public UserAuthority login(String userName, String password);
	public String refresh(String jwtToken);

}

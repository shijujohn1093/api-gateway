package com.thengara.apigateway.authentication.service;

public interface AuthenticationService {

	public String login(String userName, String password);
	public String refresh(String jwtToken);

}

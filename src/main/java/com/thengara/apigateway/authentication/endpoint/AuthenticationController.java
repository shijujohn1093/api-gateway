package com.thengara.apigateway.authentication.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thengara.apigateway.authentication.model.Login;
import com.thengara.apigateway.authentication.model.Response;

public interface AuthenticationController {

	public Response<?> login(Login login, HttpServletRequest request, HttpServletResponse response);

	public Response<?> refresh(HttpServletRequest request, HttpServletResponse response);
}

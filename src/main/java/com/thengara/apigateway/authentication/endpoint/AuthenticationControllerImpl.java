package com.thengara.apigateway.authentication.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thengara.apigateway.authentication.model.Login;
import com.thengara.apigateway.authentication.model.Response;
import com.thengara.apigateway.authentication.service.AuthenticationService;

@RestController
public class AuthenticationControllerImpl implements AuthenticationController {

	private final String HEADER_STRING = "Authorization";

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationControllerImpl(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response<Object> login(@RequestBody Login loginInfo, HttpServletRequest request, HttpServletResponse response) {
		String jwtToken = authenticationService.login(loginInfo.getUser(), loginInfo.getPassword());
		response.setHeader(HEADER_STRING, jwtToken);
		return new Response<Object>(new Object(), Response.SUCCESS);
	}

	@Override
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public Response<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(HEADER_STRING);
		token = authenticationService.refresh(token);
		response.setHeader(HEADER_STRING, token);
		return new Response<Object>(new Object(), Response.SUCCESS);
	}

}

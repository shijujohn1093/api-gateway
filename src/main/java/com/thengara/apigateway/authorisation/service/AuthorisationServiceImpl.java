package com.thengara.apigateway.authorisation.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.thengara.apigateway.authorisation.repository.AuthReadRepository;
import com.thengara.apigateway.util.JWTTokenUtil;

@Service
public class AuthorisationServiceImpl implements AuthorisationService {

	private final AuthReadRepository authReadRepository;
	private final JWTTokenUtil jwtUtil;

	AuthorisationServiceImpl(AuthReadRepository authReadRepository, JWTTokenUtil jwtUtil) {
		this.authReadRepository = authReadRepository;
		this.jwtUtil = jwtUtil;
	}

	public void validateAuthorisation(HttpServletRequest request, HttpServletResponse response) {

		jwtUtil.verify(request);
		jwtUtil.refreshToken(request);

	}

}

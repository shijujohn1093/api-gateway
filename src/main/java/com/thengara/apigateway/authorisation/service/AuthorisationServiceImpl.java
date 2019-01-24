package com.thengara.apigateway.authorisation.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.thengara.apigateway.authentication.model.Request;
import com.thengara.apigateway.authorisation.entity.Permission;
import com.thengara.apigateway.authorisation.repository.AuthReadRepository;
import com.thengara.apigateway.util.JWTTokenGenerator;

@Service
public class AuthorisationServiceImpl implements AuthorisationService {

	private final AuthReadRepository authReadRepository;
	private final JWTTokenGenerator jwtUtil;

	AuthorisationServiceImpl(AuthReadRepository authReadRepository, JWTTokenGenerator jwtUtil) {
		this.authReadRepository = authReadRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void validateRequest(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(Request.HEADER_STRING);
		jwtUtil.verify(token);
		if (isAuthorisedRequest(request, response)) {
			String newToken = jwtUtil.refreshToken(request);
			response.setHeader(Request.HEADER_STRING, newToken);
		}

	}

	private boolean isAuthorisedRequest(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Map<String, List<Permission>>> permissions = authReadRepository.getPermissions();

		return Boolean.FALSE;

	}

}

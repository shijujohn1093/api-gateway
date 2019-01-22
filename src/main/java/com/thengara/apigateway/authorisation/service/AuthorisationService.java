package com.thengara.apigateway.authorisation.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthorisationService {

	public void validateRequest(HttpServletRequest request, HttpServletResponse response);

}

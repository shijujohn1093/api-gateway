package com.thengara.apigateway.authorisation.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.thengara.apigateway.authorisation.service.AuthorisationService;

@Component
public class AuthorisationFilter extends ZuulFilter {

	AuthorisationService authorisationService;

	public AuthorisationFilter(AuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response = ctx.getResponse();
		authorisationService.validateRequest(request, response);
		System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
		return null;
	}

}

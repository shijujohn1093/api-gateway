package com.thengara.apigateway.authorisation.entity;

public class Permission {

	private final String service;
	private final String role;
	private final String urlpattern;
	private final boolean excludeAuth;

	public Permission(String service, String role, String urlpattern, boolean excludeAuth) {
		super();
		this.service = service;
		this.role = role;
		this.urlpattern = urlpattern;
		this.excludeAuth = excludeAuth;
	}

	public String getService() {
		return service;
	}

	public String getRole() {
		return role;
	}

	public String getUrlpattern() {
		return urlpattern;
	}

	public boolean isExcludeAuth() {
		return excludeAuth;
	}

}

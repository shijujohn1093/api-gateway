package com.thengara.apigateway.authentication.model;

import java.util.List;
import java.util.UUID;

public class UserAuthority {

	private final String id;
	private final String userName;
	private final List<String> roles;

	public UserAuthority(String userName, List<String> roles) {
		super();
		id = UUID.randomUUID().toString();
		this.userName = userName;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "UserAuthority [id=" + id + ", userName=" + userName + ", roles=" + roles + "]";
	}

}

package com.thengara.apigateway.auth.model;

import java.util.List;

public class UserAuthority {
	private final String userName;
	private final List<String> permissions;

	public UserAuthority(String userName, List<String> permissions) {
		super();
		this.userName = userName;
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "UserAuthority [userName=" + userName + ", permissions=" + permissions + "]";
	}

	public String getUserName() {
		return userName;
	}

	public List<String> getPermissions() {
		return permissions;
	}

}

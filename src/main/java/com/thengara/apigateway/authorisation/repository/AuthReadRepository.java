package com.thengara.apigateway.authorisation.repository;

import java.util.List;
import java.util.Map;

import com.thengara.apigateway.authorisation.entity.Permission;

public interface AuthReadRepository {

	public Map<String, Map<String, List<Permission>>> getPermissions();

}

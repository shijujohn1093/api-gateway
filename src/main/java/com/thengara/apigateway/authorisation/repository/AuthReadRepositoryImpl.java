package com.thengara.apigateway.authorisation.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.thengara.apigateway.authorisation.entity.Permission;

@Repository
public class AuthReadRepositoryImpl implements AuthReadRepository {

	private final JdbcTemplate JdbcTemplate;
	private String selectSql = "SELECT SERVICE, ROLE, URL_PATTERN, EXCLUDE_AUTH FROM PERMISSION";

	@Autowired
	AuthReadRepositoryImpl(DataSource datasource) {
		JdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Map<String, Map<String, List<Permission>>> getPermissions() {
		return JdbcTemplate.query(selectSql, (ResultSetExtractor<Map<String, Map<String, List<Permission>>>>) rs -> {
			Map<String, Map<String, List<Permission>>> cache = new HashMap<>();
			while (rs.next()) {
				Permission permission = new Permission(rs.getString("SERVICE"), rs.getString("ROLE"), rs.getString("URL_PATTERN"),
				        rs.getBoolean("EXCLUDE_AUTH"));

				Map<String, List<Permission>> rolePermissionMap = cache.get(permission.getService());
				if (rolePermissionMap == null) {
					rolePermissionMap = new HashMap<>();
					rolePermissionMap.put(permission.getRole(), new ArrayList<Permission>());
					cache.put(permission.getService(), rolePermissionMap);
				}
				rolePermissionMap.get(rs.getString("ROLE")).add(permission);
			}
			return cache;
		});
	}

}
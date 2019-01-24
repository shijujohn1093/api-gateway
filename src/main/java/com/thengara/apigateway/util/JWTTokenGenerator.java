package com.thengara.apigateway.util;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.thengara.apigateway.authentication.model.UserAuthority;

@Service
public class JWTTokenGenerator {

	private final long EXPIRATIONTIME = 864_000_000; // 10 days
	private final String TOKEN_PREFIX = "Bearer";
	private final String HEADER_STRING = "Authorization";

	private final String JWT_KEY_USER;
	private final String JWT_KEY_ROLES;
	private final String JWT_SESSION_ID;
	private final String JWT_SESSION_CREATED;

	private final RSAKeyPairGenerator rsaFileUtil;
	private final EnvironmentWrapper environment;

	@Autowired
	public JWTTokenGenerator(RSAKeyPairGenerator rsaFileUtil, EnvironmentWrapper environment) {
		this.rsaFileUtil = rsaFileUtil;
		this.environment = environment;
		JWT_KEY_ROLES = environment.getAppPrefix() + "role";
		JWT_SESSION_ID = environment.getAppPrefix() + "sessionId";
		JWT_SESSION_CREATED = environment.getAppPrefix() + "sessCreated";
		JWT_KEY_USER = environment.getAppPrefix() + "user";
	}

	public DecodedJWT verify(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		return verify(token);
	}

	public DecodedJWT verify(String jwtToken) {
		final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider());
		final JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(environment.applicationName()).build();
		return jwtVerifier.verify(jwtToken.replace(TOKEN_PREFIX + " ", ""));
	}

	public DecodedJWT decode(String tokenString) {
		return JWT.decode(tokenString);
	}

	public String generateToken(UserAuthority userAuth) {
		return generateToken(userAuth, System.currentTimeMillis());
	}
	public String generateToken(UserAuthority userAuth, long inception) {
		return generateToken(userAuth.getUserName(), userAuth.getId(), userAuth.getRoles().toString(), inception);
	}

	public String generateToken(String userName, String id, String roles, long inception) {
		if (userName != null && id != null && roles != null && inception > 0) {
			return TOKEN_PREFIX + " " + getJWTToken(userName, id, roles, inception);
		}
		return null;
	}

	private String getJWTToken(String userName, String id, String roles, long sessionCreatedOn) {

		if (userName != null && id != null && roles != null && sessionCreatedOn > 0) {
			Algorithm algorithm = getSignAlgorithn();
			return JWT.create().withIssuer(environment.applicationName()).withIssuedAt(new Date()).withJWTId(UUID.randomUUID().toString())
			        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATIONTIME)).withClaim(JWT_KEY_USER, userName)
			        .withClaim(JWT_SESSION_ID, id).withClaim(JWT_KEY_ROLES, roles).withClaim(JWT_SESSION_CREATED, sessionCreatedOn).sign(algorithm);
		}
		return null;
	}

	public String refreshToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		return refreshToken(token);
	}

	public String refreshToken(String token) {
		DecodedJWT decodeJwt = decode(token.replace(TOKEN_PREFIX + " ", ""));
		return generateToken(decodeJwt.getClaims().get(JWT_KEY_USER).asString(), decodeJwt.getClaims().get(JWT_SESSION_ID).asString(),
		        decodeJwt.getClaims().get(JWT_KEY_ROLES).asString(), Long.parseLong(decodeJwt.getClaims().get(JWT_SESSION_CREATED).asString()));

	}

	private Algorithm getSignAlgorithn() {
		final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider());
		return algorithm;
	}

	public RSAKeyProvider rsaKeyProvider() {
		return rsaFileUtil.rsaKeyProvider(environment.getRsaPublicKeyFilePathForJWTSign(), environment.getRsaPrivateKeyFilePathForJWTSign());
	}

}

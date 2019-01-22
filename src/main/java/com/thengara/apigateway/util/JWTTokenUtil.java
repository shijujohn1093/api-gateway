package com.thengara.apigateway.util;

import java.util.Date;

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
public class JWTTokenUtil {

	private final long EXPIRATIONTIME = 864_000_000; // 10 days
	private final String TOKEN_PREFIX = "Bearer";
	private final String HEADER_STRING = "Authorization";

	private final String JWT_KEY_USER;
	private final String JWT_KEY_ROLES;
	private final String JWT_KEY_ID;
	private final String JWT_KEY_INCEPTION;

	private final RSAFileUtil rsaFileUtil;
	private final EnvironmentWrapper environment;

	@Autowired
	public JWTTokenUtil(RSAFileUtil rsaFileUtil, EnvironmentWrapper environment) {
		this.rsaFileUtil = rsaFileUtil;
		this.environment = environment;
		JWT_KEY_ROLES = environment.getAppPrefix() + "role";
		JWT_KEY_ID = environment.getAppPrefix() + "id";
		JWT_KEY_INCEPTION = environment.getAppPrefix() + "inception";
		JWT_KEY_USER = environment.getAppPrefix() + "user";
	}

	public DecodedJWT verify(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		return verify(token);
	}

	public DecodedJWT verify(String jwtToken) {
		RSAKeyProvider rsaKeyProvider = rsaFileUtil.rsaKeyProvider();
		final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
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
		if (userName != null && id != null && roles != null && inception < 1) {
			Algorithm algorithm = getSignAlgorithn();
			return TOKEN_PREFIX + " "
			        + JWT.create().withIssuer(environment.applicationName()).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
			                .withClaim(JWT_KEY_USER, userName).withClaim(JWT_KEY_ID, id).withClaim(JWT_KEY_ROLES, roles)
			                .withClaim(JWT_KEY_INCEPTION, inception).sign(algorithm);
		}
		return null;
	}

	public String refreshToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		return refreshToken(token);
	}

	public String refreshToken(String token) {
		DecodedJWT decodeJwt = decode(token.replace(TOKEN_PREFIX + " ", ""));
		return generateToken(decodeJwt.getClaims().get(JWT_KEY_USER).asString(), decodeJwt.getClaims().get(JWT_KEY_ID).asString(),
		        decodeJwt.getClaims().get(JWT_KEY_ROLES).asString(), Long.parseLong(decodeJwt.getClaims().get(JWT_KEY_INCEPTION).asString()));

	}

	private Algorithm getSignAlgorithn() {
		RSAKeyProvider rsaKeyProvider = rsaFileUtil.rsaKeyProvider();
		final Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
		return algorithm;
	}

}

package com.thengara.apigateway.util;

import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JWTTokenUtilTest {

	private JWTTokenGenerator jwtTokenUtil;

	@Mock
	private EnvironmentWrapper environment;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		when(environment.getRsaPrivateKeyFilePathForJWTSign()).thenReturn("security/jwt/sign/privatekey.file");
		when(environment.getRsaPublicKeyFilePathForJWTSign()).thenReturn("security/jwt/sign/publickey.file");
		when(environment.getAppPrefix()).thenReturn("myapp_");

		RSAKeyPairGenerator rsaFileUtil = null;
		try {
			rsaFileUtil = new RSAKeyPairGenerator();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jwtTokenUtil = new JWTTokenGenerator(rsaFileUtil, environment);
	}

	@Test
	public void shouldGenerateTokenWith() {
		String token = jwtTokenUtil.generateToken("shiju", "id1", "rol1,rol2", System.currentTimeMillis());
		System.out.println(token);
	}

}

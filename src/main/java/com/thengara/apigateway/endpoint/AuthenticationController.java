package com.thengara.apigateway.endpoint;

import java.util.List;

public interface AuthenticationController {

	List<String> login(String username, String password);
	List<String> refresh();

}

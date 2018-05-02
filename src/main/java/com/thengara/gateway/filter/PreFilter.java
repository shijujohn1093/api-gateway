package com.thengara.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class PreFilter extends ZuulFilter {



	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		System.out.println(
				"Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
		return null;
	}
	
	// @Override
	// public Object run() {
	// RequestContext ctx = RequestContext.getCurrentContext();
	// HttpServletRequest request = ctx.getRequest();
	// String header = request.getHeader("Authorization");
	// if (header == null || header.isEmpty() || !header.startsWith("Bearer "))
	// {
	// ctx.setResponseStatusCode(401);
	// ctx.setSendZuulResponse(false);
	// } else {
	// header = header.replace("Bearer ", "");
	// System.out.println("Token is '" + header + "'");
	// ResponseEntity<String> responseToken =
	// authenticationServiceClient.validateToken(header);
	// String jwtToken = responseToken.getBody();
	//
	// // request.setAttribute("jwt", jwtToken);
	// HttpServletRequestWrapper wrappedRequest = modifyRequest(request,
	// jwtToken);
	// /* wrappedRequest.setAttribute("jwt", jwtToken); */
	// ctx.setRequest(wrappedRequest);
	//
	// if (responseToken == null) {
	// ctx.setResponseStatusCode(500);
	// ctx.setResponseBody("AuthenticationService Not Available");
	// ctx.setSendZuulResponse(false);
	// } else {
	// System.out.println(responseToken.getStatusCode().name());
	// System.out.println(responseToken.getBody().toString());
	//
	// }
	// }
	// System.out.println(String.format("%s request to %s", request.getMethod(),
	// request.getRequestURL().toString()));
	// return null;
	//
	// System.out.println(
	// "Request Method : " + request.getMethod() + " Request URL : " +
	// request.getRequestURL().toString());
	// return null;
	// }
	//
	// // private void saveTokenInRedis() {
	// // long currentDate = System.currentTimeMillis();
	// // long expireTime = currentDate + 1000 *
	// // (Integer.parseInt(String.valueOf(result.get("expires_in"))));
	// // TokenDetails tokenDetails = new TokenDetails(null, username, token,
	// // currentDate, expireTime);
	// // String jsonTokenDetails = gson.toJson(tokenDetails,
	// TokenDetails.class);
	// // tokenRedisMap.put(token, jsonTokenDetails);
	// // }
	//
	// public String generateToken() {
	//
	// long expireyTime = 5L; // 5minutes
	// String key = authenticationServiceClient.SSO.get("cuckoo");
	// long issueDate = System.currentTimeMillis();
	// long expirationDate = issueDate + (expireyTime * 60 * 1000);
	//
	// String jws = Jwts.builder().setIssuer("RBSSSO").setSubject("SSO
	// AUTHENTICATION").claim("NAME", "SHIJU JOHN")
	// .claim("scope", "admins").setIssuedAt(new
	// Date(issueDate)).setExpiration(new Date(expirationDate))
	// .signWith(SignatureAlgorithm.HS512,
	// TextCodec.BASE64.decode(key)).compact();
	//
	// return jws;
	//
	// }

}
package com.thengara.apigateway.authentication.model;

import java.io.Serializable;

public class Request<T> implements Serializable {

	public static final String HEADER_STRING = "Authorization";

	private static final long serialVersionUID = 1L;

	private T payload;
	private String command;

	public Request() {
		super();
	}

	public Request(T payload, String command) {
		super();
		this.payload = payload;
		this.command = command;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}

package com.thengara.apigateway.authentication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";

	private final T payload;
	private final String status;
	private List<String> messages = new ArrayList<String>();

	public Response(T payload, String status) {
		super();
		this.payload = payload;
		this.status = status;
	}
	public Response(T payload, String status, List<String> messages) {
		super();
		this.payload = payload;
		this.status = status;
		this.messages = messages;
	}

	public T getPayload() {
		return payload;
	}
	public String getStatus() {
		return status;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	public String[] getMessages() {
		return (String[]) messages.toArray();
	}

}

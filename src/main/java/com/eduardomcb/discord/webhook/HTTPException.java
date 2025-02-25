package com.eduardomcb.discord.webhook;

public class HTTPException extends Exception {

	private static final long serialVersionUID = 1L;
	private int responseCode;
	private String message;

	public HTTPException(int responseCode, String message) {
		this.responseCode = responseCode;
		this.message = message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getMessage() {
		return message;
	}

}

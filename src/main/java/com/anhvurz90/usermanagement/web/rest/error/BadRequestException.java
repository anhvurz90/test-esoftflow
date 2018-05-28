package com.anhvurz90.usermanagement.web.rest.error;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 5151131129861419321L;

	public BadRequestException(String msg) {
		super(msg);
	}
}

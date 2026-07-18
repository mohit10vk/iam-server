package com.security.iam_server.exception;

public class BadRequestException extends RuntimeException {

	public BadRequestException(String massage) {
		super(massage);
	}
}

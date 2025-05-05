package com.mytask.MyTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	// Recebe a mensagem de erro e passa para a classe pai.
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
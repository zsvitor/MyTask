package com.mytask.MyTask.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	@NotBlank(message = "O e-mail é obrigatório")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	private String password;

	private boolean rememberMe;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
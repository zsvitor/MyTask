package com.mytask.MyTask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
	private String name;

	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

}
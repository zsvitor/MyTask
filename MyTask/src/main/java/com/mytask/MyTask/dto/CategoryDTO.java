package com.mytask.MyTask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
	private String name;

	private String description;

}
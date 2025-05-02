package com.mytask.MyTask.dto;

import com.mytask.MyTask.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

	private Long id;

	@NotBlank(message = "O título é obrigatório")
	@Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
	private String title;

	private String description;

	@NotNull(message = "A data de vencimento é obrigatória")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate;

	@NotNull(message = "A prioridade é obrigatória")
	private Task.Priority priority;

	private Task.Status status = Task.Status.PENDENTE;

	private Set<Long> categoryIds = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Task.Priority getPriority() {
		return priority;
	}

	public void setPriority(Task.Priority priority) {
		this.priority = priority;
	}

	public Task.Status getStatus() {
		return status;
	}

	public void setStatus(Task.Status status) {
		this.status = status;
	}

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Set<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

}
package com.mytask.MyTask.dto;

import com.mytask.MyTask.model.Task;
import java.time.LocalDate;

public class TaskFilterDTO {

	private Task.Status status;
	private Task.Priority priority;
	private Long categoryId;
	private LocalDate dueDateBefore;
	private LocalDate dueDateAfter;
	private String searchTerm;

	public Task.Status getStatus() {
		return status;
	}

	public void setStatus(Task.Status status) {
		this.status = status;
	}

	public Task.Priority getPriority() {
		return priority;
	}

	public void setPriority(Task.Priority priority) {
		this.priority = priority;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public LocalDate getDueDateBefore() {
		return dueDateBefore;
	}

	public void setDueDateBefore(LocalDate dueDateBefore) {
		this.dueDateBefore = dueDateBefore;
	}

	public LocalDate getDueDateAfter() {
		return dueDateAfter;
	}

	public void setDueDateAfter(LocalDate dueDateAfter) {
		this.dueDateAfter = dueDateAfter;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

}
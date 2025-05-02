package com.mytask.MyTask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O título é obrigatório")
	@Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@NotNull(message = "A data de vencimento é obrigatória")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dueDate")
	private LocalDate dueDate;

	@NotNull(message = "A prioridade é obrigatória")
	@Enumerated(EnumType.STRING)
	private Priority priority;

	@NotNull(message = "O status é obrigatório")
	@Enumerated(EnumType.STRING)
	private Status status;

	@CreationTimestamp
	@Column(name = "creationDate", updatable = false)
	private LocalDateTime creationDate;

	@UpdateTimestamp
	@Column(name = "updateDate")
	private LocalDateTime updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany
	@JoinTable(name = "task_category", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))

	private Set<Category> categories = new HashSet<>();

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<HistoryTask> history = new HashSet<>();

	public enum Priority {
		BAIXA, MEDIA, ALTA
	}

	public enum Status {
		PENDENTE, CONCLUIDO
	}

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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<HistoryTask> getHistory() {
		return history;
	}

	public void setHistory(Set<HistoryTask> history) {
		this.history = history;
	}

}
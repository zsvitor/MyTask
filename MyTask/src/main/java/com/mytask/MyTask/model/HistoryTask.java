package com.mytask.MyTask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_history")
@NoArgsConstructor
@AllArgsConstructor
public class HistoryTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;

	@Column(name = "old_status")
	@Enumerated(EnumType.STRING)
	private Task.Status oldStatus;

	@Column(name = "new_status")
	@Enumerated(EnumType.STRING)
	private Task.Status newStatus;

	@CreationTimestamp
	@Column(name = "change_date")
	private LocalDateTime changeDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Task.Status getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Task.Status oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Task.Status getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(Task.Status newStatus) {
		this.newStatus = newStatus;
	}

	public LocalDateTime getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(LocalDateTime changeDate) {
		this.changeDate = changeDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static HistoryTask createRecord(Task task, Task.Status oldStatus, Task.Status newStatus, User user) {
		HistoryTask history = new HistoryTask();
		history.setTask(task);
		history.setOldStatus(oldStatus);
		history.setNewStatus(newStatus);
		history.setUser(user);
		return history;
	}

}
package com.mytask.MyTask.service;

import com.mytask.MyTask.dto.TaskDTO;
import com.mytask.MyTask.dto.TaskFilterDTO;
import com.mytask.MyTask.model.HistoryTask;
import com.mytask.MyTask.model.Task;
import com.mytask.MyTask.model.User;

import java.util.List;

// Interface para o TaskService (implementação do design pattern Proxy).
public interface ITaskService {

	public Task createTask(TaskDTO taskDTO, User user);

	public Task getTaskById(Long id, User user);

	public List<Task> getTasksByUser(User user);

	public List<Task> filterTasks(User user, TaskFilterDTO filterDTO);

	public Task updateTask(Long id, TaskDTO taskDTO, User user);

	public void deleteTask(Long id, User user);

	public Task updateTaskStatus(Long id, Task.Status newStatus, User user);

	public List<HistoryTask> getTaskHistory(Long taskId, User user);

}
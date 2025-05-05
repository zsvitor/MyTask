package com.mytask.MyTask.service;

import com.mytask.MyTask.dto.TaskDTO;
import com.mytask.MyTask.dto.TaskFilterDTO;
import com.mytask.MyTask.model.HistoryTask;
import com.mytask.MyTask.model.Task;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.util.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementa o padrão Proxy para adicionar logging e segurança antes de repassar chamadas ao TaskService.
@Service
public class TaskServiceProxy implements ITaskService {

	@Autowired
	private TaskService realTaskService;

	private final LoggerManager logger = LoggerManager.getInstance();

	@Override
	public Task createTask(TaskDTO taskDTO, User user) {
		logger.info("Usuário " + user.getEmail() + " criando nova tarefa: " + taskDTO.getTitle());
		Task task = realTaskService.createTask(taskDTO, user);
		logger.info("Tarefa criada com sucesso. ID: " + task.getId());
		return task;
	}

	@Override
	public Task getTaskById(Long id, User user) {
		logger.info("Usuário " + user.getEmail() + " consultando tarefa ID: " + id);
		return realTaskService.getTaskById(id, user);
	}

	@Override
	public List<Task> getTasksByUser(User user) {
		logger.info("Listando todas as tarefas do usuário: " + user.getEmail());
		return realTaskService.getTasksByUser(user);
	}

	@Override
	public List<Task> filterTasks(User user, TaskFilterDTO filterDTO) {
		logger.info("Filtrando tarefas para o usuário: " + user.getEmail());
		return realTaskService.filterTasks(user, filterDTO);
	}

	@Override
	public Task updateTask(Long id, TaskDTO taskDTO, User user) {
		logger.info("Usuário " + user.getEmail() + " atualizando tarefa ID: " + id);
		Task task = realTaskService.updateTask(id, taskDTO, user);
		logger.info("Tarefa atualizada com sucesso. ID: " + task.getId());
		return task;
	}

	@Override
	public void deleteTask(Long id, User user) {
		logger.info("Usuário " + user.getEmail() + " excluindo tarefa ID: " + id);
		Task task = realTaskService.getTaskById(id, user);
		if (task.getStatus() == Task.Status.CONCLUIDO) {
			logger.warn("Tentativa de excluir uma tarefa concluída. Usuário: " + user.getEmail());
		}
		realTaskService.deleteTask(id, user);
		logger.info("Tarefa excluída com sucesso. ID: " + id);
	}

	@Override
	public Task updateTaskStatus(Long id, Task.Status newStatus, User user) {
		logger.info("Usuário " + user.getEmail() + " alterando status da tarefa ID: " + id + " para " + newStatus);
		Task task = realTaskService.updateTaskStatus(id, newStatus, user);
		logger.info("Status da tarefa atualizado com sucesso. ID: " + task.getId());
		return task;
	}

	@Override
	public List<HistoryTask> getTaskHistory(Long taskId, User user) {
		logger.info("Usuário " + user.getEmail() + " consultando histórico da tarefa ID: " + taskId);
		return realTaskService.getTaskHistory(taskId, user);
	}

}
package com.mytask.MyTask.service;

import com.mytask.MyTask.dto.TaskDTO;
import com.mytask.MyTask.dto.TaskFilterDTO;
import com.mytask.MyTask.exception.ResourceNotFoundException;
import com.mytask.MyTask.model.Category;
import com.mytask.MyTask.model.HistoryTask;
import com.mytask.MyTask.model.Task;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.repository.CategoryRepository;
import com.mytask.MyTask.repository.HistoryTaskRepository;
import com.mytask.MyTask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService implements ITaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private HistoryTaskRepository historyTaskRepository;

	// Método para criar uma nova tarefa de um usuário específico.
	@Override
	@Transactional
	public Task createTask(TaskDTO taskDTO, User user) {
		Task task = new Task();
		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());
		task.setDueDate(taskDTO.getDueDate());
		task.setPriority(taskDTO.getPriority());
		task.setStatus(Task.Status.PENDENTE);
		task.setUser(user);
		if (taskDTO.getCategoryIds() != null && !taskDTO.getCategoryIds().isEmpty()) {
			Set<Category> categories = taskDTO.getCategoryIds().stream()
					.map(categoryId -> categoryRepository.findById(categoryId).orElseThrow(
							() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId)))
					.collect(Collectors.toSet());
			task.setCategories(categories);
		}
		Task savedTask = taskRepository.save(task);
		HistoryTask history = HistoryTask.createRecord(savedTask, null, Task.Status.PENDENTE, user); // Cria um registro.																								// histórico.
		historyTaskRepository.save(history);
		return savedTask;
	}

	// Método para buscar uma tarefa pelo ID de um usuário específico.
	@Override
	public Task getTaskById(Long id, User user) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o ID: " + id));
		if (!task.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("Acesso negado a esta tarefa");
		}
		return task;
	}

	// Método para buscar a tarefa de um usuário específico.
	@Override
	public List<Task> getTasksByUser(User user) {
		return taskRepository.findByUser(user);
	}

	// Método para filtrar as tarefas de um usuário específico.
	@Override
	public List<Task> filterTasks(User user, TaskFilterDTO filterDTO) {
		if (filterDTO == null) {
			return taskRepository.findByUser(user);
		}
		if (filterDTO.getStatus() != null) {
			return taskRepository.findByUserAndStatus(user, filterDTO.getStatus());
		}
		if (filterDTO.getPriority() != null) {
			return taskRepository.findByUserAndPriority(user, filterDTO.getPriority());
		}
		if (filterDTO.getCategoryId() != null) {
			return taskRepository.findByUserAndCategory(user, filterDTO.getCategoryId());
		}
		if (filterDTO.getDueDateBefore() != null) {
			return taskRepository.findByUserAndDueDateBefore(user, filterDTO.getDueDateBefore());
		}
		return taskRepository.findByUser(user);
	}

	// Método para atualizar a tarefa de um usuário específico.
	@Override
	@Transactional
	public Task updateTask(Long id, TaskDTO taskDTO, User user) {
		Task task = getTaskById(id, user);
		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());
		task.setDueDate(taskDTO.getDueDate());
		task.setPriority(taskDTO.getPriority());
		if (taskDTO.getStatus() != task.getStatus()) {
			Task.Status oldStatus = task.getStatus();
			task.setStatus(taskDTO.getStatus());
			HistoryTask history = HistoryTask.createRecord(task, oldStatus, taskDTO.getStatus(), user);
			historyTaskRepository.save(history);
		}
		if (taskDTO.getCategoryIds() != null) {
			Set<Category> categories = new HashSet<>();
			for (Long categoryId : taskDTO.getCategoryIds()) {
				Category category = categoryRepository.findById(categoryId).orElseThrow(
						() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId));
				if (!category.getUser().getId().equals(user.getId())) {
					throw new IllegalArgumentException("Acesso negado a esta categoria");
				}
				categories.add(category);
			}
			task.setCategories(categories);
		}
		return taskRepository.save(task);
	}

	// Método para remover a tarefa de um usuário específico.
	@Override
	@Transactional
	public void deleteTask(Long id, User user) {
		Task task = getTaskById(id, user);
		taskRepository.delete(task);
	}

	// Método para atualizar o status da tarefa de um usuário específico.
	@Override
	@Transactional
	public Task updateTaskStatus(Long id, Task.Status newStatus, User user) {
		Task task = getTaskById(id, user);
		Task.Status oldStatus = task.getStatus();
		if (oldStatus != newStatus) {
			task.setStatus(newStatus);
			HistoryTask history = HistoryTask.createRecord(task, oldStatus, newStatus, user);
			historyTaskRepository.save(history);
			taskRepository.save(task);
		}
		return task;
	}

	// Método para buscar o histórico da tarefa de um usuário específico.
	@Override
	public List<HistoryTask> getTaskHistory(Long taskId, User user) {
		Task task = getTaskById(taskId, user);
		return historyTaskRepository.findByTaskOrderByChangeDateDesc(task);
	}

}
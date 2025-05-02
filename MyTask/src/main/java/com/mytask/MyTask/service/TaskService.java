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
public class TaskService {
    
	@Autowired
    private TaskRepository taskRepository;
	
	@Autowired
    private CategoryRepository categoryRepository;
	
	@Autowired
    private HistoryTaskRepository historyTaskRepository;
    
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
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId)))
                    .collect(Collectors.toSet());
            task.setCategories(categories);
        }
        
        Task savedTask = taskRepository.save(task);
        
        // Criar registro histórico inicial
        HistoryTask history = HistoryTask.createRecord(savedTask, null, Task.Status.PENDENTE, user);
        historyTaskRepository.save(history);
        
        return savedTask;
    }
    
    public Task getTaskById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o ID: " + id));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Acesso negado a esta tarefa");
        }
        
        return task;
    }
    
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }
    
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
    
    @Transactional
    public Task updateTask(Long id, TaskDTO taskDTO, User user) {
        Task task = getTaskById(id, user);
        
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setPriority(taskDTO.getPriority());
        
        // Verificar se houve mudança de status
        if (taskDTO.getStatus() != task.getStatus()) {
            Task.Status oldStatus = task.getStatus();
            task.setStatus(taskDTO.getStatus());
            
            // Criar registro histórico
            HistoryTask history = HistoryTask.createRecord(task, oldStatus, taskDTO.getStatus(), user);
            historyTaskRepository.save(history);
        }
        
        // Atualizar categorias
        if (taskDTO.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : taskDTO.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId));
                
                if (!category.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("Acesso negado a esta categoria");
                }
                
                categories.add(category);
            }
            task.setCategories(categories);
        }
        
        return taskRepository.save(task);
    }
    
    @Transactional
    public void deleteTask(Long id, User user) {
        Task task = getTaskById(id, user);
        taskRepository.delete(task);
    }
    
    @Transactional
    public Task updateTaskStatus(Long id, Task.Status newStatus, User user) {
        Task task = getTaskById(id, user);
        Task.Status oldStatus = task.getStatus();
        
        if (oldStatus != newStatus) {
            task.setStatus(newStatus);
            
            // Criar registro histórico
            HistoryTask history = HistoryTask.createRecord(task, oldStatus, newStatus, user);
            historyTaskRepository.save(history);
            
            taskRepository.save(task);
        }
        
        return task;
    }
    
    public List<HistoryTask> getTaskHistory(Long taskId, User user) {
        Task task = getTaskById(taskId, user);
        return historyTaskRepository.findByTaskOrderByChangeDateDesc(task);
    }
}
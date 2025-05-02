package com.mytask.MyTask.controller;

import com.mytask.MyTask.dto.TaskDTO;
import com.mytask.MyTask.dto.TaskFilterDTO;
import com.mytask.MyTask.model.Category;
import com.mytask.MyTask.model.HistoryTask;
import com.mytask.MyTask.model.Task;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.service.CategoryService;
import com.mytask.MyTask.service.TaskService;
import com.mytask.MyTask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;

	@GetMapping
	public String listTasks(Model model, TaskFilterDTO filterDTO) {
		User user = getCurrentUser();
		List<Task> tasks = taskService.filterTasks(user, filterDTO);
		List<Category> categories = categoryService.getCategoriesByUser(user);
		model.addAttribute("tasks", tasks);
		model.addAttribute("categories", categories);
		model.addAttribute("filterDTO", filterDTO != null ? filterDTO : new TaskFilterDTO());
		model.addAttribute("priorities", Task.Priority.values());
		model.addAttribute("statuses", Task.Status.values());
		return "tasks/list";
	}

	@GetMapping("/new")
	public String createTaskForm(Model model) {
		User user = getCurrentUser();
		List<Category> categories = categoryService.getCategoriesByUser(user);
		model.addAttribute("taskDTO", new TaskDTO());
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", Task.Priority.values());
		return "tasks/form";
	}

	@PostMapping("/new")
	public String createTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			User user = getCurrentUser();
			model.addAttribute("categories", categoryService.getCategoriesByUser(user));
			model.addAttribute("priorities", Task.Priority.values());
			return "tasks/form";
		}
		try {
			User user = getCurrentUser();
			taskService.createTask(taskDTO, user);
			redirectAttributes.addFlashAttribute("successMessage", "Tarefa criada com sucesso!");
			return "redirect:/tasks";
		} catch (Exception e) {
			User user = getCurrentUser();
			model.addAttribute("categories", categoryService.getCategoriesByUser(user));
			model.addAttribute("priorities", Task.Priority.values());
			model.addAttribute("errorMessage", e.getMessage());
			return "tasks/form";
		}
	}

	@GetMapping("/{id}/edit")
	public String editTaskForm(@PathVariable Long id, Model model) {
		User user = getCurrentUser();
		Task task = taskService.getTaskById(id, user);
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(task.getId());
		taskDTO.setTitle(task.getTitle());
		taskDTO.setDescription(task.getDescription());
		taskDTO.setDueDate(task.getDueDate());
		taskDTO.setPriority(task.getPriority());
		taskDTO.setStatus(task.getStatus());
		Set<Long> categoryIds = task.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
		taskDTO.setCategoryIds(categoryIds);
		model.addAttribute("taskDTO", taskDTO);
		model.addAttribute("categories", categoryService.getCategoriesByUser(user));
		model.addAttribute("priorities", Task.Priority.values());
		model.addAttribute("statuses", Task.Status.values());
		return "tasks/form";
	}

	@PostMapping("/{id}/edit")
	public String updateTask(@PathVariable Long id, @Valid @ModelAttribute("taskDTO") TaskDTO taskDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			User user = getCurrentUser();
			model.addAttribute("categories", categoryService.getCategoriesByUser(user));
			model.addAttribute("priorities", Task.Priority.values());
			model.addAttribute("statuses", Task.Status.values());
			return "tasks/form";
		}
		try {
			User user = getCurrentUser();
			taskService.updateTask(id, taskDTO, user);
			redirectAttributes.addFlashAttribute("successMessage", "Tarefa atualizada com sucesso!");
			return "redirect:/tasks";
		} catch (Exception e) {
			User user = getCurrentUser();
			model.addAttribute("categories", categoryService.getCategoriesByUser(user));
			model.addAttribute("priorities", Task.Priority.values());
			model.addAttribute("statuses", Task.Status.values());
			model.addAttribute("errorMessage", e.getMessage());
			return "tasks/form";
		}
	}

	@GetMapping("/{id}/delete")
	public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User user = getCurrentUser();
			taskService.deleteTask(id, user);
			redirectAttributes.addFlashAttribute("successMessage", "Tarefa excluída com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/tasks";
	}

	@GetMapping("/{id}/complete")
	public String completeTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User user = getCurrentUser();
			taskService.updateTaskStatus(id, Task.Status.CONCLUIDO, user);
			redirectAttributes.addFlashAttribute("successMessage", "Tarefa concluída com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/tasks";
	}

	@GetMapping("/{id}/reopen")
	public String reopenTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User user = getCurrentUser();
			taskService.updateTaskStatus(id, Task.Status.PENDENTE, user);
			redirectAttributes.addFlashAttribute("successMessage", "Tarefa reaberta com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/tasks";
	}

	@GetMapping("/{id}/history")
	public String taskHistory(@PathVariable Long id, Model model) {
		User user = getCurrentUser();
		Task task = taskService.getTaskById(id, user);
		List<HistoryTask> history = taskService.getTaskHistory(id, user);
		model.addAttribute("task", task);
		model.addAttribute("history", history);
		return "tasks/history";
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUserByEmail(auth.getName())
				.orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));
	}

}
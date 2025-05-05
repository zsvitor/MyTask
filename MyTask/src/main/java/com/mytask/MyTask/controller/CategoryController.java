package com.mytask.MyTask.controller;

import com.mytask.MyTask.dto.CategoryDTO;
import com.mytask.MyTask.model.Category;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.service.CategoryService;
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

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@GetMapping
	public String listCategories(Model model) {
		User user = getCurrentUser();
		List<Category> categories = categoryService.getCategoriesByUser(user);
		model.addAttribute("categories", categories);
		return "categories/list";
	}

	@GetMapping("/new")
	public String createCategoryForm(Model model) {
		model.addAttribute("categoryDTO", new CategoryDTO());
		return "categories/form";
	}

	@PostMapping("/new")
	public String createCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "categories/form";
		}
		try {
			User user = getCurrentUser();
			categoryService.createCategory(categoryDTO, user);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria criada com sucesso!");
			return "redirect:/categories";
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "categories/form";
		}
	}

	@GetMapping("/{id}/edit")
	public String editCategoryForm(@PathVariable Long id, Model model) {
		User user = getCurrentUser();
		Category category = categoryService.getCategoryById(id, user);
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(category.getId());
		categoryDTO.setName(category.getName());
		categoryDTO.setDescription(category.getDescription());
		model.addAttribute("categoryDTO", categoryDTO);
		return "categories/form";
	}

	@PostMapping("/{id}/edit")
	public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "categories/form";
		}
		try {
			User user = getCurrentUser();
			categoryService.updateCategory(id, categoryDTO, user);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria atualizada com sucesso!");
			return "redirect:/categories";
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "categories/form";
		}
	}

	@GetMapping("/{id}/delete")
	public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User user = getCurrentUser();
			categoryService.deleteCategory(id, user);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria excluída com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/categories";
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUserByEmail(auth.getName())
				.orElseThrow(() -> new IllegalStateException("Usuário não autenticado."));
	}

}
package com.mytask.MyTask.service;

import com.mytask.MyTask.dto.CategoryDTO;
import com.mytask.MyTask.exception.ResourceNotFoundException;
import com.mytask.MyTask.model.Category;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category createCategory(CategoryDTO categoryDTO, User user) {
		if (categoryRepository.existsByNameAndUser(categoryDTO.getName(), user)) {
			throw new IllegalArgumentException("Já existe uma categoria com este nome");
		}
		Category category = new Category();
		category.setName(categoryDTO.getName());
		category.setDescription(categoryDTO.getDescription());
		category.setUser(user);
		return categoryRepository.save(category);
	}

	public Category getCategoryById(Long id, User user) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
		if (!category.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("Acesso negado a esta categoria");
		}
		return category;
	}

	public List<Category> getCategoriesByUser(User user) {
		return categoryRepository.findByUser(user);
	}

	public Category updateCategory(Long id, CategoryDTO categoryDTO, User user) {
		Category category = getCategoryById(id, user);
		if (!category.getName().equals(categoryDTO.getName())
				&& categoryRepository.existsByNameAndUser(categoryDTO.getName(), user)) {
			throw new IllegalArgumentException("Já existe uma categoria com este nome");
		}
		category.setName(categoryDTO.getName());
		category.setDescription(categoryDTO.getDescription());
		return categoryRepository.save(category);
	}

	public void deleteCategory(Long id, User user) {
		Category category = getCategoryById(id, user);
		if (!category.getTasks().isEmpty()) {
			throw new IllegalArgumentException("Não é possível excluir uma categoria que possui tarefas associadas");
		}
		categoryRepository.delete(category);
	}

}
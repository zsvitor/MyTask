package com.mytask.MyTask.repository;

import com.mytask.MyTask.model.Category;
import com.mytask.MyTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	// Busca as categorias pertencentes a um usuário específico.
	public List<Category> findByUser(User user);

	// Verifica se já existe uma categoria com o mesmo nome para o usuário específico.
	public boolean existsByNameAndUser(String name, User user);
	
}
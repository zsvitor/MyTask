package com.mytask.MyTask.repository;

import com.mytask.MyTask.model.Task;
import com.mytask.MyTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	// Busca todas as tarefas pertencentes a um usuário específico.
	List<Task> findByUser(User user);

	// Busca tarefas de um usuário com um status específico.
	List<Task> findByUserAndStatus(User user, Task.Status status);

	// Busca tarefas de um usuário com uma prioridade específica.
	List<Task> findByUserAndPriority(User user, Task.Priority priority);

	// Busca tarefas de um usuário com data de vencimento anterior/igual à data especificada.
	@Query("SELECT t FROM Task t WHERE t.user = :user AND t.dueDate <= :date")
	List<Task> findByUserAndDueDateBefore(@Param("user") User user, @Param("date") LocalDate date);

	// Busca tarefas de um usuário que pertencem a uma categoria específica.
	@Query("SELECT t FROM Task t JOIN t.categories c WHERE t.user = :user AND c.id = :categoryId")
	List<Task> findByUserAndCategory(@Param("user") User user, @Param("categoryId") Long categoryId);
	
}
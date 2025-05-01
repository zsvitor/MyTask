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
	
	List<Task> findByUserOrderByDueDateAsc(User user);

	List<Task> findByUserAndStatusOrderByDueDateAsc(User user, Task.Status status);

	List<Task> findByUserAndPriorityOrderByDueDateAsc(User user, Task.Priority priority);

	@Query("SELECT t FROM Task t JOIN t.categories c WHERE t.user = :user AND c.id = :categoryId ORDER BY t.dueDate ASC")
	List<Task> findByUserAndCategoryOrderByDueDateAsc(@Param("user") User user, @Param("categoryId") Long categoryId);

	@Query("SELECT t FROM Task t WHERE t.user = :user AND t.dueDate BETWEEN :startDate AND :endDate ORDER BY t.dueDate ASC")
	List<Task> findByUserAndDueDateBetweenOrderByDueDateAsc(@Param("user") User user,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	List<Task> findByUserAndDueDateLessThanEqualAndStatusOrderByDueDateAsc(User user, LocalDate date,
			Task.Status status);

}
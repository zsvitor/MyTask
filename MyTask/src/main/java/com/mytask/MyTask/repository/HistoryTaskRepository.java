package com.mytask.MyTask.repository;

import com.mytask.MyTask.model.HistoryTask;
import com.mytask.MyTask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryTaskRepository extends JpaRepository<HistoryTask, Long> {
	
    List<HistoryTask> findByTaskOrderByChangeDateDesc(Task task);
    
}

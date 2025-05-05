package com.mytask.MyTask.config;

import com.mytask.MyTask.service.ITaskService;
import com.mytask.MyTask.service.TaskService;
import com.mytask.MyTask.service.TaskServiceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceConfig {

	// Garante que, ao injetar ITaskService, será usada a implementação Proxy em vez do TaskService.
	@Bean
	@Primary
	public ITaskService taskServiceProxy(TaskService taskService) {
		return new TaskServiceProxy();
	}

}
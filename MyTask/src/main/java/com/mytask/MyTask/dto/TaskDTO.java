package com.mytask.MyTask.dto;

import com.mytask.MyTask.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String title;

    private String description;

    @NotNull(message = "A data de vencimento é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull(message = "A prioridade é obrigatória")
    private Task.Priority priority;

    private Task.Status status = Task.Status.PENDENTE;

    private Set<Long> categoryIds = new HashSet<>();
    
}
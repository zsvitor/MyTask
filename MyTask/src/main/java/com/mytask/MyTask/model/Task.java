package com.mytask.MyTask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "A data de vencimento é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dueDate")
    private LocalDate dueDate;

    @NotNull(message = "A prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(name = "creationDate", updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
        name = "task_category",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HistoryTask> history = new HashSet<>();

    public enum Priority {
        BAIXA, MEDIA, ALTA
    }

    public enum Status {
        PENDENTE, CONCLUIDO
    }
    
}
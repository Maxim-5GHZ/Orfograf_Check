package com.example.errorfreetext.controller;

import com.example.errorfreetext.dto.CreateTaskRequest;
import com.example.errorfreetext.dto.CreateTaskResponse;
import com.example.errorfreetext.dto.TaskResponse;
import com.example.errorfreetext.models.Task;
import com.example.errorfreetext.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


// тут только http: забрали json, отдали json. никакой бизнес-логики
@Tag(name = "tasks")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Создать задачу на исправление текста")
    @PostMapping
    public ResponseEntity<CreateTaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        UUID id = taskService.create(request.getText(), request.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTaskResponse(id));
    }

    @Operation(summary = "Получить задачу по id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable UUID id) {
        Task task = taskService.getById(id);
        TaskResponse body = new TaskResponse(
                task.getId(),
                task.getStatus().name(),
                task.getLanguage(),
                task.getOriginalText(),
                task.getCorrectedText(),
                task.getErrorMessage(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
        return ResponseEntity.ok(body);
    }
}

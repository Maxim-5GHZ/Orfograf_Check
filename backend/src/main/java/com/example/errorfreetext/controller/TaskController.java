package com.example.errorfreetext.controller;

import com.example.errorfreetext.dto.CreateTaskRequest;
import com.example.errorfreetext.dto.CreateTaskResponse;
import com.example.errorfreetext.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<CreateTaskResponse> create(@RequestBody CreateTaskRequest request) {
        UUID id = taskService.create(request.getText(), request.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTaskResponse(id));
    }
}

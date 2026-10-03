package com.example.errorfreetext.service;

import com.example.errorfreetext.exception.NotFoundException;
import com.example.errorfreetext.models.Task;
import com.example.errorfreetext.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    
    @Transactional
    public UUID create(String text, String language) {
        // язык приводим к нижнему регистру, чтобы в базу не лезло RU и ru разными строками
        Task task = new Task(text, language.toLowerCase());
        task = taskRepository.save(task);
        log.info("created task {}", task.getId());
        return task.getId();
    }

    @Transactional(readOnly = true)
    public Task getById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
    }
}

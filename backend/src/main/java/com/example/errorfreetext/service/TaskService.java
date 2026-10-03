package com.example.errorfreetext.service;

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
        Task task = new Task(text, language);
        task = taskRepository.save(task);
        log.info("created task {}", task.getId());
        return task.getId();
    }
}

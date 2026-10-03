package com.example.errorfreetext.service;

import com.example.errorfreetext.exception.NotFoundException;
import com.example.errorfreetext.models.Task;
import com.example.errorfreetext.models.TaskStatus;
import com.example.errorfreetext.repository.TaskRepository;
import com.example.errorfreetext.service.client.SpellerClient;
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
    private final SpellerClient spellerClient;

    
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

    // обработка одной задачи из шедулера.
    // без общего @Transactional: статус PROCESSING должен упасть в базу
    // до похода в яндекс, а итог записываем отдельным save
    public void process(Task task) {
        task.setStatus(TaskStatus.PROCESSING);
        taskRepository.save(task);
        log.info("processing task {}", task.getId());

        try {
            String corrected = spellerClient.correct(task.getOriginalText(), task.getLanguage());
            task.setCorrectedText(corrected);
            task.setStatus(TaskStatus.COMPLETED);
            task.setErrorMessage(null);
            log.info("task {} completed", task.getId());
        } catch (Exception e) {
            // яндекс упал или что-то еще, задачу больше не трогаем
            task.setStatus(TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
            log.error("task {} failed: {}", task.getId(), e.getMessage());
        }
        taskRepository.save(task);
    }
}

package com.example.errorfreetext.service.scheduler;

import com.example.errorfreetext.models.Task;
import com.example.errorfreetext.models.TaskStatus;
import com.example.errorfreetext.repository.TaskRepository;
import com.example.errorfreetext.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// забирает из базы новые задачи и гоняет их через яндекс
@Slf4j
@Component
@RequiredArgsConstructor
public class CorrectionScheduler {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Scheduled(fixedDelayString = "${scheduler.interval-ms:5000}")
    public void run() {
        List<Task> tasks = taskRepository.findTop5ByStatusOrderByCreatedAtAsc(TaskStatus.NEW);
        if (tasks.isEmpty()) {
            return;
        }
        log.debug("found {} new tasks", tasks.size());
        for (Task task : tasks) {
            taskService.process(task);
        }
    }
}

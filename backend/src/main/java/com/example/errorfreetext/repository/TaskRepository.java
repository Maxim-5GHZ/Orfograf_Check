package com.example.errorfreetext.repository;

import com.example.errorfreetext.models.Task;
import com.example.errorfreetext.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {


    List<Task> findTop5ByStatusOrderByCreatedAtAsc(TaskStatus status);
}

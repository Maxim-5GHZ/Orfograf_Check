package com.example.errorfreetext.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

// то что отдаем по GET, correctedText пока null пока задача не готова
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private UUID id;
    private String status;
    private String language;
    private String originalText;
    private String correctedText;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

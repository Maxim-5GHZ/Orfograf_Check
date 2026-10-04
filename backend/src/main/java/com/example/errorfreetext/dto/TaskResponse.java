package com.example.errorfreetext.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

// то что отдаем по GET, строго по ТЗ:
// COMPLETED - статус + скорректированный текст,
// FAILED - статус + описание ошибки,
// NEW/PROCESSING - только статус (null-поля не сериализуются)
@JsonInclude(JsonInclude.Include.NON_NULL)
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



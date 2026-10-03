package com.example.errorfreetext.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// пока без валидации, просто принимаем что прислали
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    private String text;
    private String language;
}

package com.example.errorfreetext.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String errorMessage;
    private int errorCode;
    private LocalDateTime timestamp;
    private String path;
}

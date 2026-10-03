package com.example.errorfreetext.dto;

import java.util.UUID;

public class CreateTaskResponse {

    private UUID id;

    public CreateTaskResponse() {
    }

    public CreateTaskResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

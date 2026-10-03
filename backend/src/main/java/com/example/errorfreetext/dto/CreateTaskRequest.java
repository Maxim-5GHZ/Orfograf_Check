package com.example.errorfreetext.dto;

// то что присылает пользователь, пока без аннотаций - валидацию добавим следующим коммитом
public class CreateTaskRequest {

    private String text;
    private String language;

    public CreateTaskRequest() {
    }

    public CreateTaskRequest(String text, String language) {
        this.text = text;
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

package com.example.errorfreetext.controller;

import com.example.errorfreetext.exception.NotFoundException;
import com.example.errorfreetext.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private static final String VALID_BODY =
            "{\"text\":\"обычный текст\",\"language\":\"ru\"}";

    @Test
    void createReturns201WithId() throws Exception {
        UUID id = UUID.randomUUID();
        when(taskService.create(anyString(), anyString())).thenReturn(id);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void textTooShortRejected() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"аб\",\"language\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40001))
                .andExpect(jsonPath("$.path").value("/api/v1/tasks"))
                .andExpect(jsonPath("$.errorMessage").value("text: text must be at least 3 characters"));
    }

    @Test
    void digitsAndSymbolsOnlyRejected() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"123!!\",\"language\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40001))
                .andExpect(jsonPath("$.errorMessage").value("text: text must contain letters"));
    }

    @Test
    void wrongLanguageRejected() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"обычный текст\",\"language\":\"de\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40001))
                .andExpect(jsonPath("$.errorMessage").value("language: language must be ru or en"));
    }

    @Test
    void blankTextRejected() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"   \",\"language\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40001));
    }

    @Test
    void getUnknownTaskReturns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(taskService.getById(id)).thenThrow(new NotFoundException("Task with id: " + id + " not found"));

        mockMvc.perform(get("/api/v1/tasks/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(40401))
                .andExpect(jsonPath("$.errorMessage").value("Task with id: " + id + " not found"))
                .andExpect(jsonPath("$.path").value("/api/v1/tasks/" + id));
    }
}

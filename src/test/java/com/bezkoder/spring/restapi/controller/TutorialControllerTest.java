package com.bezkoder.spring.restapi.controller;

import com.bezkoder.spring.restapi.model.Tutorial;
import com.bezkoder.spring.restapi.service.TutorialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TutorialController.class)
class TutorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TutorialService tutorialService;

    @Test
    void shouldReturnEmptyList() throws Exception {
        when(tutorialService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateTutorial() throws Exception {
        Tutorial mockTutorial = new Tutorial("Spring Boot", "REST API Example", false);
        when(tutorialService.save(org.mockito.ArgumentMatchers.any(Tutorial.class)))
                .thenReturn(mockTutorial);

        String newTutorial = """
            {
              "title": "Spring Boot",
              "description": "REST API Example",
              "published": false
            }
            """;

        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTutorial))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Spring Boot"))
                .andExpect(jsonPath("$.description").value("REST API Example"))
                .andExpect(jsonPath("$.published").value(false));
    }
}

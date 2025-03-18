package me.dio.bootcamp.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.TextController;
import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.service.TextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextController.class)
public class TextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextService textService;

    @Autowired
    private ObjectMapper objectMapper;

    private TextRequest validTextRequest;
    private Text validText;
    private Text validTextWithReduced;

    @BeforeEach
    void setUp() {
        // Setup valid text request
        validTextRequest = TextRequest.builder()
                .text("Este é um texto de teste válido")
                .build();

        // Setup valid text entity
        validText = Text.builder()
                .id(1L)
                .text("Este é um texto de teste válido")
                .build();

        // Setup valid text with reduced version
        validTextWithReduced = Text.builder()
                .id(1L)
                .text("Este é um texto de teste válido")
                .textReduced("Este é um")
                .build();
    }

    @Test
    void saveText_WithValidRequest_ShouldReturnOk() throws Exception {
        // Given
        when(textService.existsByText(anyString())).thenReturn(false);
        when(textService.saveText(anyString(), anyInt())).thenReturn(validTextWithReduced);

        // When & Then
        mockMvc.perform(post("/text/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest))
                        .param("lines", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$.textReduced", is("Este é um")));

        verify(textService).saveText(validTextRequest.text(), 2);
    }

    @Test
    void saveText_WithValidationErrors_ShouldReturnBadRequest() throws Exception {
        // Given
        TextRequest invalidRequest = TextRequest.builder()
                .text("")
                .build();

        // When & Then
        mockMvc.perform(post("/text/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(textService, never()).saveText(anyString(), anyInt());
    }

    @Test
    void saveText_WithExistingText_ShouldReturnBadRequest() throws Exception {
        // Given
        when(textService.existsByText(anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/text/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Texto já existe no banco de dados."));

        verify(textService, never()).saveText(anyString(), anyInt());
    }

    @Test
    void findTextByContent_ShouldReturnMatchingTexts() throws Exception {
        // Given
        List<Text> texts = Arrays.asList(validText, validTextWithReduced);
        when(textService.findByContent(anyString())).thenReturn(texts);

        // When & Then
        mockMvc.perform(get("/text/find/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[1].text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$[1].textReduced", is("Este é um")));

        verify(textService).findByContent(validTextRequest.text());
    }

    @Test
    void findTextByContent_WhenNoTextsFound_ShouldReturnEmptyList() throws Exception {
        // Given
        when(textService.findByContent(anyString())).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/text/find/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(textService).findByContent(validTextRequest.text());
    }

    @Test
    void findTextByContent_WithException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(textService.findByContent(anyString())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/text/find/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Erro ao buscar textos: Database error")));
    }

    @Test
    void findAllTexts_ShouldReturnAllTexts() throws Exception {
        // Given
        List<Text> texts = Arrays.asList(validText, validTextWithReduced);
        when(textService.findAll()).thenReturn(texts);

        // When & Then
        mockMvc.perform(get("/text/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[1].text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$[1].textReduced", is("Este é um")));

        verify(textService).findAll();
    }

    @Test
    void findAllTexts_WhenNoTextsExist_ShouldReturnEmptyList() throws Exception {
        // Given
        when(textService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/text/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(textService).findAll();
    }

    @Test
    void findAllTexts_WithException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(textService.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/text/find"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Erro ao buscar todos os textos: Database error")));
    }

    @Test
    void findTextById_WithExistingId_ShouldReturnText() throws Exception {
        // Given
        when(textService.findById(1L)).thenReturn(Optional.of(validTextWithReduced));

        // When & Then
        mockMvc.perform(get("/text/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$.textReduced", is("Este é um")));

        verify(textService).findById(1L);
    }

    @Test
    void findTextById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        when(textService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/text/find/999"))
                .andExpect(status().isNotFound());

        verify(textService).findById(999L);
    }

    @Test
    void findTextById_WithException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(textService.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/text/find/1"))
                .andExpect(status().isInternalServerError());

        verify(textService).findById(1L);
    }

    @Test
    void updateText_WithValidRequest_ShouldReturnUpdatedText() throws Exception {
        // Given
        when(textService.updateText(eq(1L), anyString(), anyInt())).thenReturn(validTextWithReduced);

        // When & Then
        mockMvc.perform(put("/text/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest))
                        .param("lines", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Este é um texto de teste válido")))
                .andExpect(jsonPath("$.textReduced", is("Este é um")));

        verify(textService).updateText(eq(1L), eq(validTextRequest.text()), eq(2));
    }

    @Test
    void updateText_WithInvalidLines_ShouldReturnBadRequest() throws Exception {
        // Given
        when(textService.updateText(eq(1L), anyString(), eq(11)))
                .thenThrow(new IllegalArgumentException("O número de linhas deve estar entre 1 e 10."));

        // When & Then
        mockMvc.perform(put("/text/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest))
                        .param("lines", "11"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O número de linhas deve estar entre 1 e 10."));
    }

    @Test
    void updateText_WithGeneralException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(textService.updateText(eq(1L), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(put("/text/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTextRequest))
                        .param("lines", "2"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Erro ao atualizar o texto: Database error")));
    }

    @Test
    void updateText_WithValidationErrors_ShouldReturnBadRequest() throws Exception {
        // Given
        TextRequest invalidRequest = TextRequest.builder()
                .text("")
                .build();

        // When & Then
        mockMvc.perform(put("/text/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .param("lines", "2"))
                .andExpect(status().isBadRequest());

        verify(textService, never()).updateText(anyLong(), anyString(), anyInt());
    }

    @Test
    void deleteText_WithExistingId_ShouldReturnSuccess() throws Exception {
        // Given
        when(textService.findById(1L)).thenReturn(Optional.of(validText));
        doNothing().when(textService).deleteText(1L);

        // When & Then
        mockMvc.perform(delete("/text/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Texto deletado com sucesso."));

        verify(textService).deleteText(1L);
    }

    @Test
    void deleteText_WithNonExistingId_ShouldReturnBadRequest() throws Exception {
        // Given
        when(textService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/text/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Texto não encontrado com o ID: 999"));

        verify(textService, never()).deleteText(999L);
    }

    @Test
    void deleteText_WithException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(textService.findById(1L)).thenReturn(Optional.of(validText));
        doThrow(new RuntimeException("Database error")).when(textService).deleteText(1L);

        // When & Then
        mockMvc.perform(delete("/text/delete/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Erro ao deletar o texto: Database error")));

        verify(textService).deleteText(1L);
    }
}
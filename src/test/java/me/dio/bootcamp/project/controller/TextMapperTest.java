package me.dio.bootcamp.project.controller;

import me.dio.bootcamp.project.Controller.Mapper.TextMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextMapperTest {

    @Test
    void toText_ShouldMapCorrectly() {
        // Given
        TextRequest textRequest = TextRequest.builder()
                .text("Texto de teste")
                .build();

        // When
        Text text = TextMapper.toText(textRequest);

        // Then
        assertNotNull(text);
        assertEquals("Texto de teste", text.getText());
        assertNull(text.getId());
        assertNull(text.getTextReduced());
    }

    @Test
    void toTextResponse_ShouldMapCorrectly() {
        // Given
        Text text = Text.builder()
                .id(1L)
                .text("Texto de teste")
                .textReduced("Texto")
                .build();

        // When
        TextResponse response = TextMapper.toTextResponse(text);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Texto de teste", response.text());
        assertEquals("Texto", response.textReduced());
    }

    @Test
    void toTextResponse_WithNullValues_ShouldMapCorrectly() {
        // Given
        Text text = Text.builder()
                .id(1L)
                .text("Texto de teste")
                .textReduced(null)
                .build();

        // When
        TextResponse response = TextMapper.toTextResponse(text);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Texto de teste", response.text());
        assertNull(response.textReduced());
    }
}
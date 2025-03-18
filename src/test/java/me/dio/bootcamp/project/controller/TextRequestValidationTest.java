package me.dio.bootcamp.project.controller;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TextRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenTextIsValid_thenNoViolations() {
        TextRequest textRequest = TextRequest.builder()
                .text("Este é um texto válido")
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTextIsBlank_thenViolation() {
        TextRequest textRequest = TextRequest.builder()
                .text("")
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertEquals(1, violations.size());
        assertEquals("Texto não pode ser vazio", violations.iterator().next().getMessage());
    }

    @Test
    void whenTextIsNull_thenViolation() {
        TextRequest textRequest = TextRequest.builder()
                .text(null)
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertEquals(1, violations.size());
        assertEquals("Texto não pode ser vazio", violations.iterator().next().getMessage());
    }

    @Test
    void whenTextExceedsMaxSize_thenViolation() {
        // Criando texto com mais de 1500 caracteres
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1501; i++) {
            sb.append("a");
        }

        TextRequest textRequest = TextRequest.builder()
                .text(sb.toString())
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertEquals(1, violations.size());
        assertEquals("O texto não pode ter mais de 1500 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void whenTextContainsInvalidCharacters_thenViolation() {
        TextRequest textRequest = TextRequest.builder()
                .text("Este texto contém caracteres inválidos como <>@#$%^&*")
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertEquals(1, violations.size());
        assertEquals("O texto contém caracteres inválidos", violations.iterator().next().getMessage());
    }

    @Test
    void whenTextContainsOnlyValidCharacters_thenNoViolation() {
        TextRequest textRequest = TextRequest.builder()
                .text("Este texto é válido, contém acentos e pontuação! Números 12345 também são permitidos.")
                .build();

        Set<ConstraintViolation<TextRequest>> violations = validator.validate(textRequest);
        assertTrue(violations.isEmpty());
    }
}
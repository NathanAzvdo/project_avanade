package me.dio.bootcamp.project.service;

import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.repository.TextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextServiceIntegrationTest {

    private TextService textService;

    @Mock
    private TextRepository textRepository;

    @Mock
    private TextSummarizer textSummarizer;

    @BeforeEach
    void setUp() {
        // Inicializa o serviço com os mocks
        textService = new TextService(textRepository, textSummarizer);
    }

    @Test
    void testSaveTextIntegration() {
        // Arrange
        String originalText = "Esta é a primeira frase do texto. Esta é a segunda frase.";
        String summarizedText = "Esta é a primeira frase do texto.";
        int lines = 1;

        // Configure o comportamento do mock do summarizer
        when(textSummarizer.summarize(originalText, lines)).thenReturn(summarizedText);

        // Configure o comportamento do repository mock
        when(textRepository.save(any(Text.class))).thenAnswer(invocation -> {
            Text text = invocation.getArgument(0);
            text.setId(1L);
            return text;
        });

        // Act
        Text result = textService.saveText(originalText, lines);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(originalText, result.getText());
        assertEquals(summarizedText, result.getTextReduced());

        verify(textSummarizer).summarize(originalText, lines);
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void testUpdateTextIntegration() {
        // Arrange
        Long id = 1L;
        String originalText = "Texto original. Segunda frase.";
        String updatedText = "Texto atualizado. Segunda frase nova.";
        String summarizedText = "Texto atualizado.";
        int lines = 1;

        Text existingText = new Text();
        existingText.setId(id);
        existingText.setText(originalText);
        existingText.setTextReduced("Texto original.");

        when(textRepository.findById(id)).thenReturn(Optional.of(existingText));
        when(textSummarizer.summarize(updatedText, lines)).thenReturn(summarizedText);
        when(textRepository.save(any(Text.class))).thenReturn(existingText);

        // Act
        Text result = textService.updateText(id, updatedText, lines);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedText, result.getText());
        assertEquals(summarizedText, result.getTextReduced());

        verify(textRepository).findById(id);
        verify(textSummarizer).summarize(updatedText, lines);
        verify(textRepository).save(any(Text.class));
    }
}
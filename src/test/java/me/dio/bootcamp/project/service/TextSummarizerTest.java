package me.dio.bootcamp.project.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextSummarizerTest {

    @Mock
    private SentenceDetectorME sentenceDetector;

    private TextSummarizer textSummarizer;

    @BeforeEach
    void setUp() throws IOException {
        // Cria uma subclasse anônima de TextSummarizer para evitar carregar o modelo real
        textSummarizer = new TextSummarizer() {
            // Sobrescreve o construtor para não carregar o modelo
            {
                // Substitui o detector de sentenças pelo mock
                this.sentenceDetector = TextSummarizerTest.this.sentenceDetector;
            }
        };
    }

    @Test
    void testSummarizeFullText() {
        // Arrange
        String text = "Esta é a primeira frase. Esta é a segunda frase. Esta é a terceira frase.";
        String[] sentences = {
                "Esta é a primeira frase.",
                "Esta é a segunda frase.",
                "Esta é a terceira frase."
        };

        when(sentenceDetector.sentDetect(text)).thenReturn(sentences);

        // Act
        String result = textSummarizer.summarize(text, 3);

        // Assert
        assertEquals("Esta é a primeira frase. Esta é a segunda frase. Esta é a terceira frase.", result);
        verify(sentenceDetector).sentDetect(text);
    }

    @Test
    void testSummarizePartialText() {
        // Arrange
        String text = "Esta é a primeira frase. Esta é a segunda frase. Esta é a terceira frase.";
        String[] sentences = {
                "Esta é a primeira frase.",
                "Esta é a segunda frase.",
                "Esta é a terceira frase."
        };

        when(sentenceDetector.sentDetect(text)).thenReturn(sentences);

        // Act
        String result = textSummarizer.summarize(text, 2);

        // Assert
        assertEquals("Esta é a primeira frase. Esta é a segunda frase.", result);
        verify(sentenceDetector).sentDetect(text);
    }

    @Test
    void testSummarizeWithMoreLinesThanText() {
        // Arrange
        String text = "Esta é a única frase.";
        String[] sentences = {"Esta é a única frase."};

        when(sentenceDetector.sentDetect(text)).thenReturn(sentences);

        // Act
        String result = textSummarizer.summarize(text, 3);

        // Assert
        assertEquals("Esta é a única frase.", result);
        verify(sentenceDetector).sentDetect(text);
    }

    @Test
    void testSummarizeEmptyText() {
        // Arrange
        String text = "";
        String[] sentences = {};

        when(sentenceDetector.sentDetect(text)).thenReturn(sentences);

        // Act
        String result = textSummarizer.summarize(text, 1);

        // Assert
        assertEquals("", result);
        verify(sentenceDetector).sentDetect(text);
    }
}
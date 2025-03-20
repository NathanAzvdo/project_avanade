package me.dio.bootcamp.project.service;

import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.repository.TextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextServiceTest {

    @Mock
    private TextRepository textRepository;

    @Mock
    private TextSummarizer summarizer;

    @InjectMocks
    private TextService textService;

    private Text sampleText;

    @BeforeEach
    void setUp() {
        sampleText = new Text();
        sampleText.setId(1L);
        sampleText.setText("Este é um texto de exemplo completo. Ele tem várias frases para testar.");
        sampleText.setTextReduced("Este é um texto de exemplo completo.");
    }

    @Test
    void testSaveText() {
        // Arrange
        String originalText = "Este é um texto de exemplo completo. Ele tem várias frases para testar.";
        String summarizedText = "Este é um texto de exemplo completo.";
        int lines = 1;

        when(summarizer.summarize(originalText, lines)).thenReturn(summarizedText);
        when(textRepository.save(any(Text.class))).thenAnswer(invocation -> {
            Text textToSave = invocation.getArgument(0);
            textToSave.setId(1L);
            return textToSave;
        });

        // Act
        Text result = textService.saveText(originalText, lines);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(originalText, result.getText());
        assertEquals(summarizedText, result.getTextReduced());

        verify(summarizer).summarize(originalText, lines);
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void testFindByContent() {
        // Arrange
        String searchText = "exemplo";
        List<Text> expectedTexts = Collections.singletonList(sampleText);

        when(textRepository.findByTextContainingIgnoreCase(searchText)).thenReturn(expectedTexts);

        // Act
        List<Text> result = textService.findByContent(searchText);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleText.getId(), result.get(0).getId());
        assertEquals(sampleText.getText(), result.get(0).getText());

        verify(textRepository).findByTextContainingIgnoreCase(searchText);
    }

    @Test
    void testFindAll() {
        // Arrange
        Text anotherText = new Text();
        anotherText.setId(2L);
        anotherText.setText("Outro texto para testar");
        anotherText.setTextReduced("Outro texto para testar");

        List<Text> expectedTexts = Arrays.asList(sampleText, anotherText);

        when(textRepository.findAll()).thenReturn(expectedTexts);

        // Act
        List<Text> result = textService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(textRepository).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(textRepository.findById(1L)).thenReturn(Optional.of(sampleText));

        // Act
        Optional<Text> result = textService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(sampleText.getText(), result.get().getText());

        verify(textRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(textRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Text> result = textService.findById(999L);

        // Assert
        assertFalse(result.isPresent());

        verify(textRepository).findById(999L);
    }

    @Test
    void testUpdateText() {
        // Arrange
        Long id = 1L;
        String newText = "Texto atualizado para teste. Segunda frase do texto.";
        String summarizedNewText = "Texto atualizado para teste.";
        int lines = 1;

        when(textRepository.findById(id)).thenReturn(Optional.of(sampleText));
        when(summarizer.summarize(newText, lines)).thenReturn(summarizedNewText);
        when(textRepository.save(any(Text.class))).thenReturn(sampleText);

        // Act
        Text result = textService.updateText(id, newText, lines);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(newText, result.getText());
        assertEquals(summarizedNewText, result.getTextReduced());

        verify(textRepository).findById(id);
        verify(summarizer).summarize(newText, lines);
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void testUpdateTextNotFound() {
        // Arrange
        Long id = 999L;
        String newText = "Texto atualizado para teste";
        int lines = 1;

        when(textRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            textService.updateText(id, newText, lines);
        });

        assertEquals("Texto não encontrado com o ID: " + id, exception.getMessage());

        verify(textRepository).findById(id);
        verify(summarizer, never()).summarize(anyString(), anyInt());
        verify(textRepository, never()).save(any(Text.class));
    }

    @Test
    void testDeleteText() {
        // Arrange
        Long id = 1L;
        when(textRepository.existsById(id)).thenReturn(true);
        doNothing().when(textRepository).deleteById(id);

        // Act
        textService.deleteText(id);

        // Assert
        verify(textRepository).existsById(id);
        verify(textRepository).deleteById(id);
    }

    @Test
    void testDeleteTextNotFound() {
        // Arrange
        Long id = 999L;
        when(textRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            textService.deleteText(id);
        });

        assertEquals("Texto não encontrado com o ID: " + id, exception.getMessage());

        verify(textRepository).existsById(id);
        verify(textRepository, never()).deleteById(any());
    }

    @Test
    void testExistsByText() {
        // Arrange
        String text = "exemplo";
        when(textRepository.findByTextContainingIgnoreCase(text))
                .thenReturn(Collections.singletonList(sampleText));

        // Act
        boolean result = textService.existsByText(text);

        // Assert
        assertTrue(result);
        verify(textRepository).findByTextContainingIgnoreCase(text);
    }

    @Test
    void testExistsByTextNotFound() {
        // Arrange
        String text = "inexistente";
        when(textRepository.findByTextContainingIgnoreCase(text))
                .thenReturn(Collections.emptyList());

        // Act
        boolean result = textService.existsByText(text);

        // Assert
        assertFalse(result);
        verify(textRepository).findByTextContainingIgnoreCase(text);
    }
}
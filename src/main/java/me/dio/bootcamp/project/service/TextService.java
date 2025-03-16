package me.dio.bootcamp.project.service;

import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.repository.TextRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    private final TextRepository textRepository;
    private final TextSummarizer summarizer;

    public TextService(TextRepository textRepository, TextSummarizer summarizer) {
        this.textRepository = textRepository;
        this.summarizer = summarizer;
    }

    public Text saveText(String originalText, int lines) {
        String summarizedText = summarizer.summarize(originalText, lines);

        Text text = new Text();
        text.setText(originalText);
        text.setTextReduced(summarizedText);

        return textRepository.save(text);
    }
    public List<Text> findByContent(String text) {
        return textRepository.findByTextContainingIgnoreCase(text);
    }

    public List<Text> findAll() {
        return textRepository.findAll();
    }

    public Optional<Text> findById(Long id) {
        return textRepository.findById(id);
    }

    public Text updateText(Long id, String newText, int lines) {
        Optional<Text> optionalText = textRepository.findById(id);

        if (optionalText.isPresent()) {
            Text text = optionalText.get();
            String summarizedText = summarizer.summarize(newText, lines);

            text.setText(newText);
            text.setTextReduced(summarizedText);

            return textRepository.save(text);
        } else {
            throw new RuntimeException("Texto não encontrado com o ID: " + id);
        }
    }

    public void deleteText(Long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
        } else {
            throw new RuntimeException("Texto não encontrado com o ID: " + id);
        }
    }

    public boolean existsByText(String text) {
        return !textRepository.findByTextContainingIgnoreCase(text).isEmpty();
    }



}

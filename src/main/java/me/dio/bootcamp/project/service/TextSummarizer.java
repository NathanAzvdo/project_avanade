package me.dio.bootcamp.project.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TextSummarizer {

    private final SentenceDetectorME sentenceDetector;

    public TextSummarizer() throws IOException {
        try (InputStream modelIn = getClass().getResourceAsStream("/models/pt-sent.bin")) {
            if (modelIn == null) {
                throw new IOException("Modelo de detecção de sentenças não encontrado!");
            }
            SentenceModel model = new SentenceModel(modelIn);
            this.sentenceDetector = new SentenceDetectorME(model);
        }
    }

    public String summarize(String text, int lines) {
        String[] sentences = sentenceDetector.sentDetect(text);
        int maxSentences = Math.min(sentences.length, lines);
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < maxSentences; i++) {
            summary.append(sentences[i]).append(" ");
        }
        return summary.toString().trim();
    }
}

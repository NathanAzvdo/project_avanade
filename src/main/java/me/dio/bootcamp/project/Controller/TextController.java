package me.dio.bootcamp.project.Controller;

import me.dio.bootcamp.project.Controller.Mapper.TextMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.exception.TextNullException;
import me.dio.bootcamp.project.service.TextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping("/save")
    public ResponseEntity<TextResponse> saveText(@RequestBody TextRequest textRequest, @RequestParam(defaultValue = "2") int lines) {
        if(textRequest.text() == null || textRequest.text().isEmpty()) {
            throw new TextNullException("Texto não pode ser vazio");
        }
        Text toText = TextMapper.toText(textRequest);
        Text savedText = textService.saveText(toText.getText(), lines);
        return ResponseEntity.ok().body(TextMapper.toTextResponse(savedText));
    }

    @GetMapping("/find/content")
    public ResponseEntity<List<TextResponse>> findTextByContent(@RequestBody TextRequest textRequest) {
        Text toText = TextMapper.toText(textRequest);
        List<Text> texts = textService.findByContent(toText.getText());
        return ResponseEntity.ok()
                .body(texts.stream()
                .map(TextMapper::toTextResponse)
                .collect(Collectors.toList())
                );
    }

    @GetMapping("/find")
    public ResponseEntity<List<TextResponse>> findAllTexts() {
        List<Text> texts = textService.findAll();
        return ResponseEntity.ok()
                .body(texts.stream()
                .map(TextMapper::toTextResponse)
                .collect(Collectors.toList())
                );
    }

    @GetMapping("/find/{id}")
    public TextResponse findTextById(@PathVariable Long id) {
        Optional<Text> text = textService.findById(id);
        if (text.isPresent()) {
            return TextMapper.toTextResponse(text.get());
        } else {
            throw new RuntimeException("Texto não encontrado com o ID: " + id);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TextResponse> updateText(@PathVariable Long id, @RequestBody TextRequest textRequest, @RequestParam(defaultValue = "2") int lines) {
        Text toText = TextMapper.toText(textRequest);
        Text updatedText = textService.updateText(id, toText.getText(), lines);
        return ResponseEntity.ok()
                .body(TextMapper.toTextResponse(updatedText));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteText(@PathVariable Long id) {
        textService.deleteText(id);
    }
}
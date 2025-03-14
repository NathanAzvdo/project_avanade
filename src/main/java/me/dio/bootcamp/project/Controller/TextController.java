package me.dio.bootcamp.project.Controller;

import jakarta.validation.Valid;
import me.dio.bootcamp.project.Controller.Mapper.TextMapper;
import me.dio.bootcamp.project.Controller.Request.TextRequest;
import me.dio.bootcamp.project.Controller.Response.TextResponse;
import me.dio.bootcamp.project.entity.Text;
import me.dio.bootcamp.project.service.TextService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> saveText(@Valid @RequestBody TextRequest textRequest,
                                                 @RequestParam(defaultValue = "2") int lines) {

        if (textService.existsByText(textRequest.text())) {
            return ResponseEntity.badRequest().body("Texto já existe no banco de dados.");
        }

        Text toText = TextMapper.toText(textRequest);
        Text savedText = textService.saveText(toText.getText(), lines);
        return ResponseEntity.ok().body(TextMapper.toTextResponse(savedText));
    }

    @GetMapping("/find/content")
    public ResponseEntity<?> findTextByContent(@RequestBody TextRequest textRequest) {
        try {
            Text toText = TextMapper.toText(textRequest);
            List<Text> texts = textService.findByContent(toText.getText());
            return ResponseEntity.ok(
                    texts.stream().map(TextMapper::toTextResponse).collect(Collectors.toList())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar textos: " + e.getMessage());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<?> findAllTexts() {
        try {
            List<Text> texts = textService.findAll();
            return ResponseEntity.ok(
                    texts.stream().map(TextMapper::toTextResponse).collect(Collectors.toList())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar todos os textos: " + e.getMessage());
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TextResponse> findTextById(@PathVariable Long id) {
        try {
            Optional<Text> text = textService.findById(id);
            return text.map(value -> ResponseEntity.ok(TextMapper.toTextResponse(value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateText(@Valid @PathVariable Long id, @Valid @RequestBody TextRequest textRequest, @RequestParam(defaultValue = "2") int lines) {
        try {
            if (lines < 1 || lines > 10) {
                throw new IllegalArgumentException("O número de linhas deve estar entre 1 e 10.");
            }
            Text toText = TextMapper.toText(textRequest);
            Text updatedText = textService.updateText(id, toText.getText(), lines);
            return ResponseEntity.ok(TextMapper.toTextResponse(updatedText));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar o texto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteText(@PathVariable Long id) {
        try {
            if(textService.findById(id).isPresent()) {
                textService.deleteText(id);
                return ResponseEntity.ok("Texto deletado com sucesso.");
            }
            else{
                return ResponseEntity.badRequest().body("Texto não encontrado com o ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar o texto: " + e.getMessage());
        }
    }
}

package me.dio.bootcamp.project.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Text Controller", description = "Endpoints para gerenciar textos")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping("/save")
    @Operation(summary = "Salvar um novo texto", description = "Salva um novo texto no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Texto salvo com sucesso", content = @Content(schema = @Schema(implementation = TextResponse.class))),
            @ApiResponse(responseCode = "400", description = "Texto já existe no banco de dados", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar o texto", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> saveText(
            @Parameter(description = "Dados do texto a ser salvo", required = true) @Valid @RequestBody TextRequest textRequest,
            @Parameter(description = "Número de linhas", example = "2") @RequestParam(defaultValue = "2") int lines) {

        if (textService.existsByText(textRequest.text())) {
            return ResponseEntity.badRequest().body("Texto já existe no banco de dados.");
        }

        Text toText = TextMapper.toText(textRequest);
        Text savedText = textService.saveText(toText.getText(), lines);
        return ResponseEntity.ok().body(TextMapper.toTextResponse(savedText));
    }

    @GetMapping("/find/content")
    @Operation(summary = "Buscar textos por conteúdo", description = "Busca textos que contenham o conteúdo especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Textos encontrados", content = @Content(schema = @Schema(implementation = TextResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar textos", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> findTextByContent(
            @Parameter(description = "Conteúdo do texto a ser buscado", required = true) @RequestBody TextRequest textRequest) {
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
    @Operation(summary = "Buscar todos os textos", description = "Busca todos os textos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Textos encontrados", content = @Content(schema = @Schema(implementation = TextResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar textos", content = @Content(schema = @Schema(implementation = String.class)))
    })
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
    @Operation(summary = "Buscar texto por ID", description = "Busca um texto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Texto encontrado", content = @Content(schema = @Schema(implementation = TextResponse.class))),
            @ApiResponse(responseCode = "404", description = "Texto não encontrado", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar o texto", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<TextResponse> findTextById(
            @Parameter(description = "ID do texto a ser buscado", required = true) @PathVariable Long id) {
        try {
            Optional<Text> text = textService.findById(id);
            return text.map(value -> ResponseEntity.ok(TextMapper.toTextResponse(value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Atualizar texto", description = "Atualiza um texto existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Texto atualizado com sucesso", content = @Content(schema = @Schema(implementation = TextResponse.class))),
            @ApiResponse(responseCode = "400", description = "Número de linhas inválido", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar o texto", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateText(
            @Parameter(description = "ID do texto a ser atualizado", required = true) @PathVariable Long id,
            @Parameter(description = "Dados do texto a ser atualizado", required = true) @Valid @RequestBody TextRequest textRequest,
            @Parameter(description = "Número de linhas", example = "2") @RequestParam(defaultValue = "2") int lines) {
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
    @Operation(summary = "Deletar texto", description = "Deleta um texto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Texto deletado com sucesso", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Texto não encontrado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao deletar o texto", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deleteText(
            @Parameter(description = "ID do texto a ser deletado", required = true) @PathVariable Long id) {
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
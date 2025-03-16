package me.dio.bootcamp.project.Controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Objeto de requisição para enviar um texto")
public record TextRequest(
        @Schema(
                description = "Conteúdo do texto",
                example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec mollis odio, et" +
                        " imperdiet leo. Curabitur vitae leo quis est efficitur tincidunt. Curabitur quis finibus " +
                        "metus. Sed posuere ullamcorper metus vitae fringilla. Nunc convallis viverra urna, non accumsan" +
                        " lectus mattis a. Curabitur cursus nisi at arcu malesuada ultricies. Maecenas sed suscipit" +
                        " purus. Fusce venenatis ultricies sollicitudin. Vestibulum nec accumsan ipsum." +
                        " Etiam vel tristique tortor. Aliquam vel est quis quam aliquam condimentum bibendum" +
                        " sed enim. Mauris euismod blandit sagittis. ",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Texto não pode ser vazio")
        @Size(max = 1500, message = "O texto não pode ter mais de 1500 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s.,!?]*$", message = "O texto contém caracteres inválidos")
        String text
) {}
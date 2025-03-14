package me.dio.bootcamp.project.Controller.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TextRequest(
        @NotBlank(message = "Texto não pode ser vazio")
        @Size(max = 500, message = "O texto não pode ter mais de 500 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s.,!?]*$", message = "O texto contém caracteres inválidos")
        String text
) {}

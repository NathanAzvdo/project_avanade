package me.dio.bootcamp.project.Controller.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Objeto de resposta contendo informações sobre o texto processado")
public record TextResponse(
        @Schema(
                description = "ID único do texto",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Conteúdo completo do texto",
                example = "Este é um exemplo de texto completo."
        )
        String text,

        @Schema(
                description = "Versão reduzida do texto (se aplicável)",
                example = "Este é um exemplo reduzido."
        )
        String textReduced
) {}
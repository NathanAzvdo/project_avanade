package me.dio.bootcamp.project.Controller.Response;

import lombok.Builder;

@Builder
public record TextResponse(Long id, String text, String textReduced) {
}

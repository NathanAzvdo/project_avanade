package me.dio.bootcamp.project.Controller.Request;

import lombok.Builder;

@Builder
public record TextRequest(String text) {
}

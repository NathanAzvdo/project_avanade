package me.dio.bootcamp.project.Controller;

import me.dio.bootcamp.project.exception.TextNullException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class TextControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TextNullException.class)
    public ResponseEntity<Object> capturaErroNull(){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Texto não pode ser vazio");
        return ResponseEntity.badRequest().body(body);
    }
}

package com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.exception;

import com.hexagonale.carrefour.delivery.delivery_service.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex, ServerWebExchange exchange) {
        log.warn("404 Not Found at {}: {}", exchange.getRequest().getPath(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                buildBody(HttpStatus.NOT_FOUND, ex.getMessage(), exchange)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex, ServerWebExchange exchange) {
        log.warn("400 Bad Request at {}: {}", exchange.getRequest().getPath(), ex.getMessage());
        return ResponseEntity.badRequest().body(
                buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>  handleGeneric(Exception ex, ServerWebExchange exchange) {
        log.error("500 Internal Server Error at {}: {}", exchange.getRequest().getPath(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), exchange)
        );
    }

    private Map<String, Object> buildBody(HttpStatus status, String message, ServerWebExchange exchange) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "path", exchange.getRequest().getPath().value()
        );
    }
}

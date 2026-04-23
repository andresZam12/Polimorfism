package edu.universidad.notificaciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/** Manejador global de excepciones: convierte errores en respuestas JSON claras. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificacionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(NotificacionNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CodigoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> conflict(CodigoDuplicadoException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "Error de validación");
        Map<String, String> campos = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                campos.put(fe.getField(), fe.getDefaultMessage()));
        body.put("campos", campos);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> generic(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // ------------- helpers -------------

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String msg) {
        return ResponseEntity.status(status).body(baseBody(status, msg));
    }

    private Map<String, Object> baseBody(HttpStatus status, String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", msg);
        return body;
    }
}

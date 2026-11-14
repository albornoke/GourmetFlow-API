package gourmetflow_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de errores de validación de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Error de Validación");
        respuesta.put("detalles", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // Manejo de conflictos de negocio (ej. duplicados, reglas de negocio)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return construirRespuestaError(HttpStatus.CONFLICT, "Conflicto de Negocio", ex.getMessage());
    }

    // Manejo de estados ilegales o restricciones (ej. regla anti-bloqueo)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return construirRespuestaError(HttpStatus.FORBIDDEN, "Acción Denegada", ex.getMessage());
    }

    // Manejo genérico para recursos no encontrados o fallos generales
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        HttpStatus status = ex.getMessage() != null && ex.getMessage().contains("no encontrado") 
                ? HttpStatus.NOT_FOUND 
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return construirRespuestaError(status, status == HttpStatus.NOT_FOUND ? "No Encontrado" : "Error Interno del Servidor", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirRespuestaError(HttpStatus status, String error, String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", status.value());
        respuesta.put("error", error);
        respuesta.put("mensaje", mensaje);

        return ResponseEntity.status(status).body(respuesta);
    }
}
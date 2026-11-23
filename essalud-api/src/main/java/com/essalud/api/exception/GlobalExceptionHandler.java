package com.essalud.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================
    // AUTENTICACIÓN
    // =====================================================

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> manejarAutenticacion(
            AuthenticationException exception) {

        return construirRespuesta(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas o cuenta no autorizada."
        );
    }

    // =====================================================
    // VALIDACIONES
    // =====================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(
            MethodArgumentNotValidException exception) {

        String errores = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                errores
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException exception) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarFormatoJSON(
            HttpMessageNotReadableException exception) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "El formato de los datos enviados no es válido."
        );
    }

    // =====================================================
    // REGISTROS DUPLICADOS O RESTRICCIONES DE BASE DE DATOS
    // =====================================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarConflictoDatos(
            DataIntegrityViolationException exception) {

        return construirRespuesta(
                HttpStatus.CONFLICT,
                "La operación genera un conflicto con los datos existentes."
        );
    }

    // =====================================================
    // RECURSOS NO ENCONTRADOS Y OTROS ESTADOS HTTP
    // =====================================================

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoHTTP(
            ResponseStatusException exception) {

        HttpStatus estado = HttpStatus.valueOf(
                exception.getStatusCode().value()
        );

        String mensaje = exception.getReason() != null
                ? exception.getReason()
                : estado.getReasonPhrase();

        return construirRespuesta(estado, mensaje);
    }

    // =====================================================
    // ERRORES GENERALES
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception exception) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Se produjo un error interno en el servidor."
        );
    }

    // =====================================================
    // CONSTRUIR RESPUESTA JSON
    // =====================================================

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus estado,
            String mensaje) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        respuesta.put("mensaje", mensaje);

        return ResponseEntity.status(estado).body(respuesta);
    }
}
package com.banquito.sucursal.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        log.error("Error de validación en la petición: {}", errores);

        ErrorResponse response = ErrorResponse.builder()
                .codigo("VALIDATION_ERROR")
                .mensaje("La petición contiene datos inválidos")
                .detalles(errores)
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio detectado: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .codigo("BUSINESS_RULE_VIOLATION")
                .mensaje(ex.getMessage())
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Error interno no controlado: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String codigo = "INTERNAL_SERVER_ERROR";
        
        if (ex.getMessage() != null && ex.getMessage().contains("no encontrada")) {
            status = HttpStatus.NOT_FOUND;
            codigo = "NOT_FOUND";
        }

        ErrorResponse response = ErrorResponse.builder()
                .codigo(codigo)
                .mensaje(ex.getMessage())
                .fecha(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, status);
    }
}
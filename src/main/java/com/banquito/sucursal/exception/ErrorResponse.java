package com.banquito.sucursal.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String codigo;
    private String mensaje;
    private List<String> detalles; 
    private LocalDateTime fecha;
}